package com.hlops.tv42.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.services.SourceService;
import com.hlops.tv42.core.services.XmltvService;
import com.hlops.tv42.webService.bean.TvShowChannelVO;
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
@RequestMapping("/groups")
public class GroupResource {

    static GsonBuilder gsonBuilder = new GsonBuilder();

    @Autowired
    private XmltvService xmltvService;

    @Autowired
    private SourceService sourceService;

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void list(Writer responseWriter) throws IOException {

        Gson gson = gsonBuilder.create();
        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();

        for (TvShowChannel channel : xmltvService.getChannels()) {
            gson.toJson(new TvShowChannelVO(channel, sourceService.getSource(channel.getSource()).getName()), TvShowChannelVO.class, jsonWriter);
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

}
