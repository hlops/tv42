package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.Source;
import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.bean.TvShowItem;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.M3uChannelService;
import com.hlops.tv42.core.services.SourceService;
import com.hlops.tv42.core.services.XmltvService;
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
import java.util.Iterator;

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

    @Autowired
    private M3uChannelService m3UChannelService;

    @Autowired
    private XmltvService xmltvService;

    @Before
    public void setUp() throws Exception {
        dbService.drop(DbService.Entity.m3uChannels);
        dbService.drop(DbService.Entity.sources);
    }

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
        URL url = new URL("file:/playlist.m3u");
        Source source = new Source("test", Source.SourceType.m3u, url);
        long lastModified = new File(url.getFile()).lastModified();
        source.setLastModified(lastModified - 1);
        assertTrue(sourceService.loadIfModified(source));
        assertTrue(lastModified - 1 < source.getLastModified());

        Collection<M3uChannel> channels = m3UChannelService.getChannels();
        assertEquals(159, channels.size());
    }

    @Test
    public void testNoUpdateIfNotModified() throws Exception {
        URL url = new URL("file:/playlist.m3u");
        Source source = new Source("test", Source.SourceType.m3u, url);
        //noinspection ConstantConditions
        long lastModified = new File(getClass().getClassLoader().getResource("playlist.m3u").getFile()).lastModified();
        source.setLastModified(lastModified + 1);
        assertFalse(sourceService.loadIfModified(source));
        assertEquals(lastModified + 1, source.getLastModified());

        Collection<M3uChannel> channels = m3UChannelService.getChannels();
        assertEquals(0, channels.size());
    }

    @Test
    public void testLoadMp3() throws Exception {
        URL url = new URL("file:/playlist.m3u");
        Source source = new Source("test", Source.SourceType.m3u, url);
        assertTrue(sourceService.load(source));

        Collection<M3uChannel> channels = m3UChannelService.getChannels();
        assertEquals(159, channels.size());
    }

    @Test
    public void testLoadXml() throws Exception {
        URL url = new URL("file:/xmltv1.xml.gz");
        Source source = new Source("test", Source.SourceType.xmltv, url);
        assertTrue(sourceService.load(source));

        Collection<TvShowChannel> channels = xmltvService.getChannels();
        assertEquals(110, channels.size());

        for (TvShowChannel channel : channels) {
            Collection<TvShowItem> items = channel.getItems(source.getId());
            assertEquals(0, items.size());
        }
    }

    @Test
    public void testLoadDefaultValues() throws Exception {

        ((SourceServiceImpl) sourceService).loadDefaultValues();

        Collection<Source> sources = sourceService.getOrderedSources(null);
        assertEquals(2, sources.size());

        Iterator<Source> it = sources.iterator();

        Source source1 = it.next();
        assertNotNull(source1);
        assertEquals("source1", source1.getName());
        assertEquals(Source.SourceType.m3u, source1.getType());
        assertEquals("file:/playlist.m3u", source1.getUrl().toString());

        Source source2 = it.next();
        assertNotNull(source2);
        assertEquals("source2", source2.getName());
        assertEquals(Source.SourceType.xmltv, source2.getType());
        assertEquals("file:/tvShow.zip", source2.getUrl().toString());
    }
}