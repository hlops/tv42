package com.hlops.tv42.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.hlops.tv42.core.bean.Source;
import com.hlops.tv42.core.services.SourceService;
import com.hlops.tv42.webService.bean.SourceVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/15/16
 * Time: 12:53 PM
 */

@RestController
@RequestMapping("/sources")
public class SourceResource {

    static GsonBuilder gsonBuilder = new GsonBuilder();

    @Autowired
    private SourceService sourceService;

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void list(Writer responseWriter) throws IOException {

        Gson gson = gsonBuilder.create();
        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();
        for (int i = 0; i < 10; i++) {
            for (Source source : sourceService.getSources()) {
                gson.toJson(new SourceVO(source), SourceVO.class, jsonWriter);
            }
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

    @RequestMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public void save(@RequestBody SourceVO sourceVO) throws IOException {
        if (sourceVO.getId() != null) {
            sourceService.delete(Collections.singletonList(sourceService.getSource(sourceVO.getId())));
        }
        sourceService.update(Collections.singletonList(createSource(sourceVO)));
    }

    @RequestMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void listTypes(Writer responseWriter) throws IOException {

        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();
        for (Source.SourceType type : Source.SourceType.values()) {
            jsonWriter.value(type.name());
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

    private Source createSource(@NotNull SourceVO sourceVO) throws MalformedURLException {
        return new Source(
                sourceVO.getName(), Source.SourceType.valueOf(sourceVO.getType()), new URL(sourceVO.getUrl()));
    }

}
