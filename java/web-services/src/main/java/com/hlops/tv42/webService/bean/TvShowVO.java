package com.hlops.tv42.webService.bean;

import com.hlops.tv42.core.bean.M3uChannel;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 2/3/16
 * Time: 11:33 AM
 */
public class TvShowVO implements Serializable {
    private String name;
    private String sourceName;

    public TvShowVO() {
    }

    public TvShowVO(@NotNull M3uChannel channel, @NotNull String sourceName) {
        this.name = channel.getName();
        this.sourceName = sourceName;
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

}
