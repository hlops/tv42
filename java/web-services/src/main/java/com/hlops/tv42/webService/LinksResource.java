package com.hlops.tv42.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.hlops.tv42.core.bean.Link;
import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.services.LinkService;
import com.hlops.tv42.core.services.M3uChannelService;
import com.hlops.tv42.core.services.XmltvService;
import com.hlops.tv42.webService.bean.LinkVO;
import com.hlops.tv42.webService.bean.TvShowChannelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 2/2/16
 * Time: 4:19 PM
 */
@RestController
@RequestMapping("/links")
public class LinksResource {

    static GsonBuilder gsonBuilder = new GsonBuilder();

    @Autowired
    private M3uChannelService m3uChannelService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private XmltvService xmltvService;

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void list(Writer responseWriter) throws IOException {

        Gson gson = gsonBuilder.create();
        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();
        for (Link link : linkService.getLinks()) {
            // todo: source is null
            LinkVO src = new LinkVO(link, null);
            if (src.getGroup() == null) {
                src.setGroup(m3uChannelService.getChannelById(link.getM3uChannel()).getGroup());
            }
            gson.toJson(src, LinkVO.class, jsonWriter);
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

    @RequestMapping(path = "raw", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void listRaw(Writer responseWriter) throws IOException {

        Gson gson = gsonBuilder.create();
        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();
        for (Link link : linkService.getLinks()) {
            gson.toJson(link, Link.class, jsonWriter);
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public void save(@RequestBody LinkVO link) throws IOException {
        linkService.actualize(Collections.singletonList(link.toLink()));
    }

    @RequestMapping(value = "channels", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void listChannels(Writer responseWriter) throws IOException {
        Gson gson = gsonBuilder.create();
        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();

        for (TvShowChannel channel : xmltvService.getChannels()) {
            // todo: source is null
            TvShowChannelVO tvShow = new TvShowChannelVO(channel, null);
            gson.toJson(tvShow, TvShowChannelVO.class, jsonWriter);
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }
}
