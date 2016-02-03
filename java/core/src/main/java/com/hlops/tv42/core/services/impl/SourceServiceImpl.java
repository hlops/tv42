package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.Link;
import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.Source;
import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/14/16
 * Time: 12:26 PM
 */
@Service
public class SourceServiceImpl implements SourceService {

    private static final DateFormat HTTP_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    private static Logger log = LogManager.getLogger(SourceServiceImpl.class);

    @Autowired
    DbService dbService;

    @Autowired
    M3uService m3uService;

    @Autowired
    XmltvService xmltvService;

    @Autowired
    LinkService linkService;

    @Override
    public Source getSource(String id) {
        return (Source) dbService.get(DbService.Entity.sources).get(id);
    }

    @Override
    public Collection<Source> getSources() {
        //noinspection unchecked
        return (Collection<Source>) dbService.get(DbService.Entity.sources).values();
    }

    @Override
    public void update(@NotNull Collection<Source> sources) {
        dbService.update(DbService.Entity.sources, sources);
    }

    @Override
    public void delete(List<Source> sources) {
        dbService.delete(DbService.Entity.sources, sources);
    }

    @Override
    public boolean load(Source source) throws IOException {
        return loadIfModified(source, 0);
    }

    @Override
    public boolean loadIfModified(@NotNull Source source) throws IOException {
        return loadIfModified(source, source.getLastModified());
    }

    public boolean loadIfModified(@NotNull Source source, long lastModified) throws IOException {
        URLConnection connection = source.getUrl().openConnection();
        connection.setIfModifiedSince(lastModified);

        if (connection instanceof HttpURLConnection) {
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(30000);
            connection.setRequestProperty("If-Modified-Since", HTTP_DATE_FORMAT.format(new Date(lastModified)));

            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            try {
                int responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
                    log.info("source not modified: " + source.getId());
                    return false;
                } else if (responseCode == HttpURLConnection.HTTP_OK) {
                    loadSource(source, connection);
                } else {
                    throw new UnsupportedOperationException("Unexpected response code: " + responseCode + " at " + source.getUrl());
                }
            } finally {
                httpConnection.disconnect();
            }

        } else {
            if (connection instanceof FileURLConnection) {
                connection = getClass().getResource(connection.getURL().getFile()).openConnection();
            }
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(3000);
            connection.connect();

            if (connection.getLastModified() >= lastModified) {
                loadSource(source, connection);
            } else {
                log.info("source not modified: " + source.getId());
                return false;
            }
        }

        return true;
    }

    private void loadSource(@NotNull Source source, @NotNull URLConnection connection) throws IOException {
        if (source.getType() == Source.SourceType.m3u) {
            m3uService.actualize(
                    m3uService.load(source.getId(), new BufferedReader(new InputStreamReader(connection.getInputStream())))
            );
        } else if (source.getType() == Source.SourceType.xmltv) {
            xmltvService.actualize(
                    xmltvService.load(source.getId(), connection.getInputStream())
            );
        } else {
            log.warn("Not supported source type: " + source.getType());
            return;
        }
        source.setLastModified(System.currentTimeMillis());
        dbService.update(DbService.Entity.sources, source);

        actualizeLinks();
    }

    private void actualizeLinks() {

        Set<Link> links = new HashSet<>();
        for (M3uChannel channel : m3uService.getChannels()) {
            // todo: check direct link
            // if (channel.tvShowChannel == null)
            if (linkService.getLink(channel.getName()) == null) {
                TvShowChannel tvShowChannel = xmltvService.findByName(channel.getName());
                if (tvShowChannel != null) {
                    links.add(new Link(channel.getName(), tvShowChannel.getName()));
                }
            }
        }

        if (!links.isEmpty()) {
            linkService.update(links);
        }
    }

}
