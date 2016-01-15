package com.hlops.tv42.core.services.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hlops.tv42.core.bean.Source;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.SourceService;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/14/16
 * Time: 2:53 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class SourceServiceImplTest extends Assert {

    @Autowired
    private DbService dbService;

    @Autowired
    private SourceService sourceService;

    @Before
    public void setUp() throws Exception {
        dbService.drop(DbService.Entity.sources);
    }

    @Test
    public void testUpdate() throws Exception {
        Collection<Source> sources = new ArrayList<>();
        URL url = getClass().getClassLoader().getResource("playlist.m3u");
        assert (url != null);
        sources.add(new Source("test", Source.ChannelSourceType.m3u, url));
        sourceService.update(sources);

        assertEquals(1, sourceService.getSources().size());
        Source source = sourceService.getSources().iterator().next();
        assertEquals("test", source.getName());
        assertEquals(Source.ChannelSourceType.m3u, source.getType());
        assertEquals(url, source.getUrl());
    }

    @Test
    public void testUpdateIfModified() throws Exception {
        URL url = getClass().getClassLoader().getResource("playlist.m3u");
        assert (url != null);
        Source source = new Source("test", Source.ChannelSourceType.m3u, url);
        long lastModified = new File(url.getFile()).lastModified();
        source.setLastModified(lastModified + 1);
        sourceService.loadIfModified(source);
    }
}