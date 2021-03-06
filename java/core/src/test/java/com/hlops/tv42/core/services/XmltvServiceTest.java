package com.hlops.tv42.core.services;

import com.hlops.tv42.core.bean.TvShowChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 2/2/16
 * Time: 1:27 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-spring-config.xml"})
public class XmltvServiceTest {

    @Autowired
    private XmltvService xmltvService;

    @Test
    @SuppressWarnings("Duplicates")
    public void testLoadXmltv() throws Exception {
        Collection<TvShowChannel> channels = xmltvService.load("test", getClass().getResourceAsStream("/xmltv1.xml"));

        assertEquals(110, channels.size());
        //assertEquals(26397, pack.getItems().size());
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testLoadXmltvGz() throws Exception {
        Collection<TvShowChannel> channels = xmltvService.load("test", getClass().getResourceAsStream("/xmltv1.xml.gz"));

        assertEquals(110, channels.size());
        //assertEquals(26397, pack.getItems().size());
    }

}