package com.hlops.tv42.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.hlops.tv42.core.bean.Link;
import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.services.LinkService;
import com.hlops.tv42.core.services.M3uChannelService;
import com.hlops.tv42.core.services.SourceService;
import com.hlops.tv42.core.services.XmltvService;
import com.hlops.tv42.webService.bean.TvShowVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 2/3/16
 * Time: 11:32 AM
 */
@RestController
@RequestMapping("/tvShow")
public class TvShowResource {

    static GsonBuilder gsonBuilder = new GsonBuilder();

    @Autowired
    private M3uChannelService m3UChannelService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private XmltvService xmltvService;

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void list(Writer responseWriter) throws IOException {

        Gson gson = gsonBuilder.create();
        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();

        for (M3uChannel channel : m3UChannelService.getChannels()) {
            Link link = linkService.getLink(channel.getName());
            if (link != null) {
                TvShowChannel tvShowChannel = xmltvService.getChannelById(link.getTvShowChannel());
                if (tvShowChannel != null) {
                    TvShowVO tvShow = new TvShowVO(channel, xmltvService.findItems(tvShowChannel, System.currentTimeMillis(), System.currentTimeMillis() + 4 * 3600000, null));
                    gson.toJson(tvShow, TvShowVO.class, jsonWriter);
                }
            }
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

}
