package com.hlops.tv42.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.hlops.tv42.core.bean.Link;
import com.hlops.tv42.core.services.LinkService;
import com.hlops.tv42.webService.bean.LinkVO;
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
 * Date: 2/2/16
 * Time: 4:19 PM
 */
@RestController
@RequestMapping("/links")
public class LinksResource {

    static GsonBuilder gsonBuilder = new GsonBuilder();

    @Autowired
    private LinkService linkService;

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void list(Writer responseWriter) throws IOException {

        Gson gson = gsonBuilder.create();
        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();
        for (Link link : linkService.getLinks()) {
            gson.toJson(new LinkVO(link), LinkVO.class, jsonWriter);
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

}
