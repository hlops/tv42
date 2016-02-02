package com.hlops.tv42.webService.bean;

import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.webService.LinksResource;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by tom on 1/29/16.
 */
public class LinkVO implements Serializable {

    private String id;
    private String name;
    private String sourceName;
    private String showChannel;

    public LinkVO() {
    }

    public LinkVO(@NotNull M3uChannel channel, @NotNull String sourceName) {
        this.id = channel.getId();
        this.name = channel.getName();
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

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }


    public void setShowChannel(String showChannel) {
        this.showChannel = showChannel;
    }

    public String getShowChannel() {
        return showChannel;
    }
}
