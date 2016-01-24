package com.hlops.tv42.webService.bean;

import com.hlops.tv42.core.bean.Source;

import java.io.Serializable;

/**
 * Created by tom on 1/24/16.
 */
public class SourceVO implements Serializable {

    private String id;
    private String name;
    private String type;
    private String url;
    private long time;

    public SourceVO() {
    }

    public SourceVO(Source source) {
        this.id = source.getId();
        this.name = source.getName();
        this.type = source.getType().name();
        this.url = source.getUrl().toString();
        this.time = source.getLastModified();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
