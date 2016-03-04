package com.hlops.tv42.core.utils;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 3/1/16
 * Time: 4:08 PM
 */
public class WellFormedXmlInputStreamTest extends Assert {

    private String replaceAmps(String text) {
        return text.replaceAll("&amp;", "&").replaceAll("&", "&amp;");
    }

    private void testReader(String text, int bufferSize) throws Exception {
        WellFormedXmlInputStream wellFormedXmlInputStream = new WellFormedXmlInputStream(
                new ByteArrayInputStream(text.getBytes("UTF-8")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copyLarge(wellFormedXmlInputStream, outputStream, new byte[bufferSize]);
        assertEquals("buffer size is " + bufferSize, replaceAmps(text), outputStream.toString("UTF-8"));
    }

    @Test
    public void testReader() throws Exception {
        for (int n = 5; n < 50; n++) {
            testReader("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&", n);
            testReader("&amp;&amp;&amp;&amp;&amp;&amp;", n);
            testReader("&&&&&&&&&&&&&&&amp;&amp;&&&&&&&&&&&&&&&&&&&", n);
            testReader("&&amp;&&amp;&&amp;&&amp;&&amp;", n);
            testReader("&&&amp;&&&amp;&&&amp;&&&amp;&&&amp;", n);
            testReader("1&23456789&0&amp;a&bcdefgh&amp;", n);
            testReader("11111111111111111111111111111111111111111111", n);
        }
    }


}