package com.hlops.tv42.core.services.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.hlops.tv42.core.bean.Source;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.M3uService;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/13/16
 * Time: 12:50 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-spring-config.xml" })
public class M3uServiceImplTest extends Assert {

    @Autowired
    private DbService dbService;

    @Autowired
    private M3uService m3uService;

    @Before
    public void setUp() throws Exception {
        dbService.drop(DbService.Entity.m3uChannels);
    }

    @Test
    public void testLoad() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/playlist.m3u");
        assertNotNull(inputStream);
        List<M3uChannel> channels = m3uService.load("test", new BufferedReader(new InputStreamReader(inputStream)));
        assertNotNull(channels);
        assertEquals(160, channels.size());

        M3uChannel channel = channels.get(0);
        assertEquals("test_Первый", channel.getId());
        assertEquals("Эфир", channel.getGroup());
        assertEquals("udp://@239.1.15.1:1234", channel.getUrl());
        assertEquals("2", channel.getTvgName());
        assertTrue(channel.getAttributes().isEmpty());

        channel = channels.get(23);
        assertEquals("test_Nickelodeon", channel.getId());
        assertEquals("Детям и мамам", channel.getGroup());
        assertEquals("udp://@239.1.3.4:1234", channel.getUrl());
        assertEquals("2230", channel.getTvgName());
        assertTrue(channel.getAttributes().contains(M3uChannel.ChannelAttribute.WIDE));
        assertFalse(channel.getAttributes().contains(M3uChannel.ChannelAttribute.HD));

        channel = channels.get(130);
        assertEquals("test_Eurosport 2 HD", channel.getId());
        assertEquals("Спорт HD", channel.getGroup());

        channel = channels.get(131);
        assertEquals("test_Кинопоказ HD 1", channel.getId());
        assertEquals("Клуб HD", channel.getGroup());
        assertEquals("udp://@239.1.17.2:1234", channel.getUrl());
        assertEquals("603", channel.getTvgName());
        assertTrue(channel.getAttributes().contains(M3uChannel.ChannelAttribute.WIDE));
        assertTrue(channel.getAttributes().contains(M3uChannel.ChannelAttribute.HD));
    }

    @Test
    public void testActualize() throws Exception {
        {
            Collection<M3uChannel> m3uChannels = new ArrayList<>();
            m3uChannels.add(new M3uChannel("test1", "channel1"));
            m3uChannels.add(new M3uChannel("test1", "channel2"));
            m3uChannels.add(new M3uChannel("test2", "channel1"));
            m3uChannels.add(new M3uChannel("test2", "channel2"));
            m3uService.actualize(m3uChannels);

            //noinspection unchecked
            Collection<M3uChannel> channels = m3uService.getChannels();
            assertEquals(4, channels.size());
            channels.stream().forEach(p -> assertEquals(true, p.isActual()));
        }

        {
            Collection<M3uChannel> m3uChannels = new ArrayList<>();
            m3uChannels.add(new M3uChannel("test2", "channel2"));
            m3uChannels.add(new M3uChannel("test3", "channel1"));
            m3uService.actualize(m3uChannels);

            //noinspection unchecked
            Collection<M3uChannel> channels = m3uService.getChannels();
            assertEquals(5, channels.size());
            channels.stream().filter(p -> p.getSource().equals("test1")).forEach(
                    p -> assertEquals(true, p.isActual()));
            channels.stream().filter(p -> p.getSource().equals("test2")).forEach(
                    p -> assertEquals(p.getName().equals("channel2"), p.isActual()));
            channels.stream().filter(p -> p.getSource().equals("test3")).forEach(
                    p -> assertEquals(true, p.isActual()));
        }
    }

}