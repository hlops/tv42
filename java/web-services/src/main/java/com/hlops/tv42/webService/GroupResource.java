package com.hlops.tv42.webService;

import com.google.gson.stream.JsonWriter;
import com.hlops.tv42.core.bean.M3uGroup;
import com.hlops.tv42.core.services.M3uGroupService;
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

    @Autowired
    private M3uGroupService m3uGroupService;

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void list(Writer responseWriter) throws IOException {

        JsonWriter jsonWriter = new JsonWriter(responseWriter);
        jsonWriter.beginArray();

        for (M3uGroup group : m3uGroupService.getGroups()) {
            jsonWriter.value(group.getName());
        }
        jsonWriter.endArray();
        jsonWriter.close();
    }

}
