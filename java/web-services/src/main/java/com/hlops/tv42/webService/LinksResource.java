package com.hlops.tv42.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.services.M3uService;
import com.hlops.tv42.core.services.SourceService;
import com.hlops.tv42.core.services.XmltvService;
import com.hlops.tv42.webService.bean.LinkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

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
    private XmltvService xmltvService;

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

        Map<String, String> showChannels = new HashMap<>();
        for (TvShowChannel tvShowChannel : xmltvService.getChannels()) {
            String name = tvShowChannel.getName().toLowerCase();
            showChannels.put(name, tvShowChannel.getId());
            if (name.contains(" канал")) {
                showChannels.put(name.replaceAll("\\s+канал", ""), tvShowChannel.getId());
            }
            if (name.contains(" тв")) {
                showChannels.put(name.replaceAll("\\s+тв", ""), tvShowChannel.getId());
            }
        }

        for (M3uChannel channel : m3uService.getChannels()) {
            boolean exists = showChannels.containsKey(channel.getName().toLowerCase());
            if (!exists) {
                exists = showChannels.containsKey(channel.getName().toLowerCase().replaceAll("\\s+", ""));
            }
            if (!exists) {
                LinkVO link = new LinkVO(channel, sourceService.getSource(channel.getSource()).getName());
                link.setShowChannel(showChannels.get(channel.getName().toLowerCase()));
                gson.toJson(link, LinkVO.class, jsonWriter);
            }
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

}
