package com.hlops.tv42.webService.bean;

import com.hlops.tv42.core.bean.Link;
import com.hlops.tv42.core.bean.TvShowChannel;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by tom on 1/29/16.
 */
public class LinkVO implements Serializable {

    private String channel;
    private String tvShowId;
    private String tvShowName;
    private String group;
    private Short shift;

    public LinkVO() {
    }

    public LinkVO(@NotNull Link link, TvShowChannel tvShowChannel) {
        this.channel = link.getM3uChannel();
        this.tvShowId = tvShowChannel.getId();
        this.tvShowName = tvShowChannel.getName();
        this.shift = link.getTimeshift();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTvShowId() {
        return tvShowId;
    }

    public void setTvShowId(String tvShowId) {
        this.tvShowId = tvShowId;
    }

    public String getTvShowName() {
        return tvShowName;
    }

    public void setTvShowName(String tvShowName) {
        this.tvShowName = tvShowName;
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
