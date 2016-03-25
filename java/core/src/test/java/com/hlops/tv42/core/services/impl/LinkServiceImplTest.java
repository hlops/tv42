package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.Link;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.LinkService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 3/24/16
 * Time: 5:09 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-spring-config.xml"})
public class LinkServiceImplTest {

    @Autowired
    private DbService dbService;

    @Autowired
    private LinkService linkService;

    @Before
    public void setUp() throws Exception {
        dbService.drop(DbService.Entity.links);
    }

    @Test
    public void testLoadDefaultValues() throws Exception {
        ((LinkServiceImpl) linkService).loadDefaultValues();

        Collection<Link> links = linkService.getLinks();
        assertEquals(1, links.size());

        Iterator<Link> it = links.iterator();

        Link link1 = it.next();
        assertNotNull(link1);
        assertEquals("погода", link1.getM3uChannel());
        assertEquals("100", link1.getTvShowChannel());
        assertEquals("ngs", link1.getSource());
        assertEquals("Погода", link1.getGroup());
        assertEquals(-3, (int) link1.getTimeshift());
        assertEquals(false, link1.getActive());
    }
}