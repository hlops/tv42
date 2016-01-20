package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.Identifiable;
import com.hlops.tv42.core.bean.Source;
import com.hlops.tv42.core.services.DbService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/18/16
 * Time: 12:28 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-spring-config.xml"})
public class DbServiceImplTest extends Assert {

    @Autowired
    private DbServiceImpl dbService;

    @Before
    public void setUp() throws Exception {
        dbService.drop(DbService.Entity.m3uChannels);
    }

    @Test
    public void testLoadDefaultValues() throws Exception {
        dbService.loadDefaultValues();

        Map<String, Identifiable> map = dbService.get(DbService.Entity.sources);
        assertEquals(2, map.size());

        Source source1 = (Source) map.get("source1");
        assertNotNull(source1);
        assertEquals("source1", source1.getName());
        assertEquals(Source.SourceType.m3u, source1.getType());
        assertEquals("file:playlist.m3u", source1.getUrl().toString());

        Source source2 = (Source) map.get("source2");
        assertNotNull(source2);
        assertEquals("source2", source2.getName());
        assertEquals(Source.SourceType.xmltv, source2.getType());
        assertEquals("file:tvShow.zip", source2.getUrl().toString());
    }
}