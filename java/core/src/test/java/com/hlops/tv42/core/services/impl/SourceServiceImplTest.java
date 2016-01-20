package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.Source;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.M3uService;
import com.hlops.tv42.core.services.SourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/14/16
 * Time: 2:53 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-spring-config.xml"})
public class SourceServiceImplTest extends Assert {

    @Autowired
    private DbService dbService;

    @Autowired
    private SourceService sourceService;

    @Before
    public void setUp() throws Exception {
        dbService.drop(DbService.Entity.m3uChannels);
        dbService.drop(DbService.Entity.sources);
    }

    @Autowired
    private M3uService m3uService;

    @Test
    public void testUpdate() throws Exception {
        Collection<Source> sources = new ArrayList<>();
        URL url = getClass().getClassLoader().getResource("playlist.m3u");
        assert (url != null);
        sources.add(new Source("test", Source.SourceType.m3u, url));
        sourceService.update(sources);

        assertEquals(1, sourceService.getSources().size());
        Source source = sourceService.getSources().iterator().next();
        assertEquals("test", source.getName());
        assertEquals(Source.SourceType.m3u, source.getType());
        assertEquals(url, source.getUrl());
    }

    @Test
    public void testUpdatedIfModified() throws Exception {
        URL url = getClass().getClassLoader().getResource("playlist.m3u");
        assert (url != null);
        Source source = new Source("test", Source.SourceType.m3u, url);
        long lastModified = new File(url.getFile()).lastModified();
        source.setLastModified(lastModified - 1);
        assertTrue(sourceService.loadIfModified(source));
        assertTrue(lastModified - 1 < source.getLastModified());

        Collection<M3uChannel> channels = m3uService.getChannels();
        assertEquals(159, channels.size());
    }

    @Test
    public void testNoUpdateIfNotModified() throws Exception {
        URL url = getClass().getClassLoader().getResource("playlist.m3u");
        assert (url != null);
        Source source = new Source("test", Source.SourceType.m3u, url);
        long lastModified = new File(url.getFile()).lastModified();
        source.setLastModified(lastModified + 1);
        assertFalse(sourceService.loadIfModified(source));
        assertEquals(lastModified + 1, source.getLastModified());

        Collection<M3uChannel> channels = m3uService.getChannels();
        assertEquals(0, channels.size());
    }

}