package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.services.XmltvService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.util.Collection;

/**
 * Created by tom on 1/31/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-spring-config.xml"})
public class XmltvServiceImplTest extends Assert {

    @Autowired
    private XmltvService xmltvService;

    @Test
    public void testLoad() throws Exception {

        try (InputStream inputStream = getClass().getResourceAsStream("/xmltv1.xml.gz");) {
            Collection<TvShowChannel> channels = xmltvService.load("test", inputStream);
            xmltvService.actualize(channels);
        }
    }
}