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
    private Short shift;

    public LinkVO() {
    }

    public LinkVO(@NotNull Link link) {
        this.channel = link.getM3uChannel();
        this.tvShow = link.getTvShowChannel();
        this.shift = link.getTimeshift();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTvShow() {
        return tvShow;
    }

    public void setTvShow(String tvShow) {
        this.tvShow = tvShow;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Short getShift() {
        return shift;
    }

    public void setShift(Short shift) {
        this.shift = shift;
    }
}
