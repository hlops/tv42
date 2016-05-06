package com.hlops.tv42.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 4/4/16
 * Time: 5:45 PM
 */
public class TimeFormatterTest extends Assert {

    @Test
    public void testParse() throws Exception {
        assertEquals(1456392000000L, TimeFormatter.parse("20160225162000"));
        assertEquals(1456392000000L + 3 * 3600000, TimeFormatter.parse("20160225162000 +0300"));
        assertEquals(1456392000000L - 3 * 3600000, TimeFormatter.parse("20160225162000 -0300"));
    }

    @Test
    public void testFormat() throws Exception {
        assertEquals(TimeFormatter.format(1456392000000L), "20160225162000");
    }
}