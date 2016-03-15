package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.Source;
import com.hlops.tv42.core.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/14/16
 * Time: 12:26 PM
 */
@Service
public class SourceServiceImpl extends GenericServiceImpl<Source> implements SourceService {

    private static final DateFormat HTTP_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    private static Logger log = LogManager.getLogger(SourceServiceImpl.class);

    @Autowired
    private M3uChannelService m3UChannelService;

    @Autowired
    private XmltvService xmltvService;

    @Autowired
    private LinkService linkService;

    @Override
    protected DbService.Entity getEntity() {
        return DbService.Entity.sources;
    }

    @Override
    public Source getSource(String id) {
        return get(id);
    }

    @Override
    public Collection<Source> getSources() {
        return values();
    }

    @Override
    public Collection<Source> getOrderedSources(@Nullable Source.SourceType sourceType) {
        Stream<Source> stream = values().stream();
        if (sourceType != null) {
            stream = stream.filter(source -> source.getType() == sourceType);
        }
        return stream.sorted().collect(Collectors.toList());
    }

    @Override
    public void update(@NotNull Collection<Source> sources) {
        actualize(sources);
    }

    @Override
    public void delete(List<Source> sources) {
        super.delete(sources);
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
            m3UChannelService.actualize(
                    m3UChannelService.load(source.getId(), source.getWeight(), new BufferedReader(new InputStreamReader(connection.getInputStream())))
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
        actualize(source);

        linkService.actualizeLinks();
    }

    @Override
    protected void loadDefaultValues() {
        loadDefaultValues(Source[].class);
    }
}
