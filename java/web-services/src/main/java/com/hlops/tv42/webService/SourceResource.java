package com.hlops.tv42.webService;

import com.google.gson.stream.JsonWriter;
import com.hlops.tv42.core.bean.Source;
import com.hlops.tv42.core.services.SourceService;
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
 * Date: 1/15/16
 * Time: 12:53 PM
 */

@RestController
@RequestMapping("/sources")
public class SourceResource {

    @Autowired
    private SourceService sourceService;

    private void writeSource(JsonWriter jsonWriter, Source source) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name");
        jsonWriter.value(source.getName());
        jsonWriter.name("type");
        jsonWriter.value(source.getType().name());
        jsonWriter.name("url");
        jsonWriter.value(source.getUrl().toString());
        jsonWriter.name("time");
        jsonWriter.value(source.getLastModified());
        jsonWriter.endObject();
    }

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void list(Writer responseWriter) throws IOException {

        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();
        for (Source source : sourceService.getSources()) {
            writeSource(jsonWriter, source);
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

}
