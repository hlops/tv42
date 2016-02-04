package com.hlops.tv42.webService.bean;

import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.TvShowItem;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 2/3/16
 * Time: 11:33 AM
 */
public class TvShowVO implements Serializable {
    private String name;
    private String group;
    private List<TvShowItem> items;

    public TvShowVO(@NotNull M3uChannel channel, List<TvShowItem> items) {
        this.items = items;
        this.name = channel.getName();
        this.group = channel.getGroup();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<TvShowItem> getItems() {
        return items;
    }

    public void setItems(List<TvShowItem> items) {
        this.items = items;
    }
}
