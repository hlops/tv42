package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.XmltvService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tom on 1/31/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-spring-config-no-max-age.xml"})
public class XmltvServiceImplTest extends Assert {

    @Autowired
    private DbService dbService;

    @Autowired
    private XmltvService xmltvService;

    @Before
    public void setUp() throws Exception {
        dbService.drop(DbService.Entity.tvShowChannels);
    }

    @Test
    public void testLoad() throws Exception {
        // todo
        try (InputStream inputStream = getClass().getResourceAsStream("/xmltv2.xml");) {
            Collection<TvShowChannel> channels = xmltvService.load("test", inputStream);
            xmltvService.actualize(channels);
        }
    }

    @Test
    public void testMatchByName() throws Exception {
        List<TvShowChannel> channels = new ArrayList<>();
        TvShowChannel channel1 = new TvShowChannel("test1", "channel1");
        channel1.setName("2X2 КАНАЛ");
        channels.add(channel1);
        xmltvService.actualize(channels);

        assertNotNull(xmltvService.matchByName("2x2"));
        assertNotNull(xmltvService.matchByName("2x2 Канал"));
        assertNotNull(xmltvService.matchByName("2x2-Канал"));
        assertNotNull(xmltvService.matchByName("2x2(тест!)"));
        assertNotNull(xmltvService.matchByName("2x2 ТВ"));
        assertNotNull(xmltvService.matchByName("2x2+ТВ(1)"));
    }

    @Test
    public void testSave() throws Exception {

        //todo
        try (InputStream inputStream = getClass().getResourceAsStream("/xmltv1.xml");) {
            Collection<TvShowChannel> channels = xmltvService.load("test", inputStream);
            xmltvService.actualize(channels);
        }

        InputStream inputStream = getClass().getResourceAsStream("/xmltv1.xml");
        xmltvService.save(xmltvService.getChannels(), "test", System.out);
/*
        xmltvService.save(xmltvService.getChannels(), new OutputStream() {
            private int n;

            @Override
            public void write(int b) throws IOException {
                assertEquals("File position is " + n, inputStream.read(), b);
                n++;
            }
        });

        assertTrue("File length is different", inputStream.available() == 0);
*/
        inputStream.close();
    }
}