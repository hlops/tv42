package com.hlops.tv42.webService.bean;

import com.hlops.tv42.core.bean.Link;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by tom on 1/29/16.
 */
public class LinkVO implements Serializable {

    private final String channel;
    private final String tvShow;
    private final Short shift;
    private final Boolean active;
    private final String source;
    private String group;

    public LinkVO(@NotNull Link link, String source) {
        this.source = source;
        this.channel = link.getM3uChannel();
        this.tvShow = link.getTvShowChannel();
        this.group = link.getGroup();
        this.active = link.getActive();
        this.shift = link.getTimeshift();
    }

    public String getChannel() {
        return channel;
    }

    public String getTvShow() {
        return tvShow;
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

    public String getSource() {
        return source;
    }

    public Boolean getActive() {
        return active;
    }

    public Link toLink() {
        Link link = new Link(this.channel, this.tvShow);
        link.setSource(this.getSource());
        link.setGroup(this.getGroup());
        link.setTimeshift(this.getShift());
        link.setActive(this.getActive());
        return link;
    }
}
