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
    private String show;

    public TvShowVO() {
    }

    public TvShowVO(@NotNull M3uChannel channel) {
        this.name = channel.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
