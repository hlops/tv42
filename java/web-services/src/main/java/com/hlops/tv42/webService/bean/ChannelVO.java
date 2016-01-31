package com.hlops.tv42.webService.bean;

import com.hlops.tv42.core.bean.M3uChannel;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by tom on 1/29/16.
 */
public class ChannelVO implements Serializable {

    private String id;
    private String name;
    private String sourceName;
    private String url;
    private String group;

    public ChannelVO() {
    }

    public ChannelVO(@NotNull M3uChannel channel, @NotNull String sourceName) {
        this.id = channel.getId();
        this.name = channel.getName();
        this.group = channel.getGroup();
        this.url = channel.getUrl();
        this.sourceName = sourceName;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
