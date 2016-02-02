package com.hlops.tv42.webService.bean;

import com.hlops.tv42.core.bean.TvShowChannel;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 2/2/16
 * Time: 7:03 PM
 */
public class TvShowChannelVO implements Serializable {

    private String id;
    private String name;
    private String sourceName;
    private String showChannel;

    public TvShowChannelVO() {
    }

    public TvShowChannelVO(@NotNull TvShowChannel channel, @NotNull String sourceName) {
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
