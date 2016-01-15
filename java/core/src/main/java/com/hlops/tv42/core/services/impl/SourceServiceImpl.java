package com.hlops.tv42.core.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hlops.tv42.core.bean.Source;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.M3uService;
import com.hlops.tv42.core.services.SourceService;

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
    public boolean loadIfModified(@NotNull Source source) throws IOException {
        URLConnection connection = source.getUrl().openConnection();
        connection.setIfModifiedSince(source.getLastModified());

        if (connection instanceof HttpURLConnection) {
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(30000);
            connection.setRequestProperty("If-Modified-Since", HTTP_DATE_FORMAT.format(new Date(source.getLastModified())));

            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            try {
                int responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
                    log.info("url not modified");
                } else if (responseCode == HttpURLConnection.HTTP_OK) {
                    loadSource(source, connection);
                } else {
                    throw new UnsupportedOperationException("Unexpected response code: " + responseCode + " at " + source.getUrl());
                }
            } finally {
                httpConnection.disconnect();
            }

        } else {
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(3000);

            if (connection.getLastModified() > source.getLastModified()) {
                loadSource(source, connection);
            } else {
                log.info("not modified");
            }
        }

        return false;
    }

    private void loadSource(@NotNull Source source, @NotNull URLConnection connection) throws IOException {
        if (source.getType() instanceof Source.ChannelSourceType) {
            m3uService.actualize(
                    m3uService.load(source.getId(), new BufferedReader(new InputStreamReader(connection.getInputStream())))
            );
        }
    }

}
