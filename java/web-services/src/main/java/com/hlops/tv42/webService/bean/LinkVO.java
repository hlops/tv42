package com.hlops.tv42.webService.bean;

import com.hlops.tv42.core.bean.Link;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by tom on 1/29/16.
 */
public class LinkVO implements Serializable {

    private String channel;
    private String tvShow;
    private String group;
    private Short timeshift;

    public LinkVO() {
    }

    public LinkVO(@NotNull Link link) {
        this.channel = link.getM3uChannel();
        this.tvShow = link.getTvShowChannel();
        this.timeshift = link.getTimeshift();
    }

    public String getChannel() {
        return channel;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Short getTimeshift() {
        return timeshift;
    }

    public void setTimeshift(Short timeshift) {
        this.timeshift = timeshift;
    }

    public String getTvShow() {
        return tvShow;
    }
}
