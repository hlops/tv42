package com.hlops.tv42.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.services.M3uChannelService;
import com.hlops.tv42.core.services.SourceService;
import com.hlops.tv42.webService.bean.ChannelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

/**
 * Created by tom on 1/29/16.
 */
@RestController
@RequestMapping("/channels")
public class ChannelResource {

    static GsonBuilder gsonBuilder = new GsonBuilder();

    @Autowired
    private M3uChannelService m3UChannelService;

    @Autowired
    private SourceService sourceService;

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void list(Writer responseWriter) throws IOException {

        Gson gson = gsonBuilder.create();
        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();

        for (M3uChannel channel : m3UChannelService.getChannels()) {
            gson.toJson(new ChannelVO(channel, sourceService.getSource(channel.getSource()).getName()), ChannelVO.class, jsonWriter);
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

    @RequestMapping(path = "names", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void listNames(Writer responseWriter) throws IOException {

        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();

        for (M3uChannel channel : m3UChannelService.getChannels()) {
            jsonWriter.value(channel.getId());
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

    @RequestMapping(path = "m3u", produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void m3u(Writer responseWriter) throws IOException {
        Collection<M3uChannel> channels = m3UChannelService.getOrderedChannels();
        m3UChannelService.writeChannels(responseWriter, channels);
    }

}
