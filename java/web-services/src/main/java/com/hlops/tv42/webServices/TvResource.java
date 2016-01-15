package com.hlops.tv42.webServices;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/15/16
 * Time: 12:53 PM
 */

@RestController
public class TvResource {

    @RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "\"Hello!!\"";
    }
}
