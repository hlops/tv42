package com.hlops.tv42.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.services.M3uService;
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

/**
 * Created by tom on 1/29/16.
 */
@RestController
@RequestMapping("/channels")
public class ChannelResource {

    static GsonBuilder gsonBuilder = new GsonBuilder();

    @Autowired
    private M3uService m3uService;

    @Autowired
    private SourceService sourceService;

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void list(Writer responseWriter) throws IOException {

        Gson gson = gsonBuilder.create();
        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();

        for (M3uChannel channel : m3uService.getChannels()) {
            gson.toJson(new ChannelVO(channel, sourceService.getSource(channel.getSource()).getName()), ChannelVO.class, jsonWriter);
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

}
