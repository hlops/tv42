package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.M3uChannelService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/13/16
 * Time: 12:50 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-spring-config.xml"})
public class M3UChannelServiceImplTest extends Assert {

    @Autowired
    private DbService dbService;

    @Autowired
    private M3uChannelService m3UChannelService;

    @Before
    public void setUp() throws Exception {
        dbService.drop(DbService.Entity.m3uChannels);
    }

    @Test
    public void testLoad() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/playlist.m3u");
        assertNotNull(inputStream);
        List<M3uChannel> channels = m3UChannelService.load("test", 101, new BufferedReader(new InputStreamReader(inputStream)));
        assertNotNull(channels);
        assertEquals(160, channels.size());

        M3uChannel channel = channels.get(0);
        assertEquals("Первый", channel.getId());
        assertEquals("test", channel.getSource());
        assertEquals(101, channel.getSourceWeight());
        assertEquals("Эфир", channel.getGroup());
        assertEquals("udp://@239.1.15.1:1234", channel.getUrl());
        assertEquals("2", channel.getTvgName());
        assertTrue(channel.getAttributes().isEmpty());

        channel = channels.get(23);
        assertEquals("Nickelodeon", channel.getId());
        assertEquals("test", channel.getSource());
        assertEquals(101, channel.getSourceWeight());
        assertEquals("Детям и мамам", channel.getGroup());
        assertEquals("udp://@239.1.3.4:1234", channel.getUrl());
        assertEquals("2230", channel.getTvgName());
        assertTrue(channel.getAttributes().contains(M3uChannel.ChannelAttribute.WIDE));
        assertFalse(channel.getAttributes().contains(M3uChannel.ChannelAttribute.HD));

        channel = channels.get(130);
        assertEquals("Eurosport 2 HD", channel.getId());
        assertEquals("Спорт HD", channel.getGroup());

        channel = channels.get(131);
        assertEquals("Кинопоказ HD 1", channel.getId());
        assertEquals("test", channel.getSource());
        assertEquals(101, channel.getSourceWeight());
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
            m3uChannels.add(new M3uChannel("channel0", "test0", 0));
            m3uChannels.add(new M3uChannel("channel1", "test1", 0));
            m3uChannels.add(new M3uChannel("channel2", "test1", 0));
            m3uChannels.add(new M3uChannel("channel3", "test2", 0));
            m3uChannels.add(new M3uChannel("channel4", "test2", 0));
            m3UChannelService.actualize(m3uChannels);

            //noinspection unchecked
            Collection<M3uChannel> channels = m3UChannelService.getChannels();
            assertEquals(5, channels.size());
            channels.stream().forEach(p -> assertEquals(true, p.isActual()));
        }

        {
            Collection<M3uChannel> m3uChannels = new ArrayList<>();
            m3uChannels.add(new M3uChannel("channel1", "test1", -1));
            m3uChannels.add(new M3uChannel("channel2", "test2", 10));
            m3uChannels.add(new M3uChannel("channel3", "test3", -1));
            m3UChannelService.actualize(m3uChannels);

            //noinspection unchecked
            Collection<M3uChannel> channels = m3UChannelService.getChannels();
            assertEquals(5, channels.size());
            channels.stream().filter(p -> p.getName().equals("channel0")).forEach(
                    p -> {
                        assertEquals(true, p.isActual());
                        assertEquals("test0", p.getSource());
                    }
            );

            channels.stream().filter(p -> p.getName().equals("channel1")).forEach(
                    p -> {
                        assertEquals(true, p.isActual());
                        assertEquals("test1", p.getSource());
                    }
            );

            channels.stream().filter(p -> p.getName().equals("channel2")).forEach(
                    p -> {
                        assertEquals(true, p.isActual());
                        assertEquals("test2", p.getSource());
                    }
            );

            channels.stream().filter(p -> p.getName().equals("channel3")).forEach(
                    p -> {
                        assertEquals(false, p.isActual());
                        assertEquals("test2", p.getSource());
                    }
            );

            channels.stream().filter(p -> p.getName().equals("channel4")).forEach(
                    p -> {
                        assertEquals(false, p.isActual());
                        assertEquals("test2", p.getSource());
                    }
            );
        }
    }

    @Test
    public void testLoadDefaultValues() throws Exception {
        ((M3UChannelServiceImpl) m3UChannelService).loadDefaultValues();
        Collection<M3uChannel> channels = m3UChannelService.getChannels();
        assertEquals(1, channels.size());

        M3uChannel channel = channels.iterator().next();
        assertEquals("channel1", channel.getName());
        assertEquals("source1", channel.getSource());
        assertEquals(20, channel.getSourceWeight());
        assertEquals("udp://@239.1.15.1:1234", channel.getUrl());
        assertEquals("group1", channel.getGroup());
        assertEquals("100", channel.getTvgName());
    }

}