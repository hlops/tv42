package com.hlops.tv42.core.bean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 1/31/16.
 */
public class TvShowChannel implements Identifiable<TvShowChannel> {

    private final String source;
    private final String channelId;
    private String name;
    private String icon;
    private List<TvShowItem> items = new ArrayList<>();

    public TvShowChannel(@NotNull String source, @NotNull String channelId) {
        this.source = source;
        this.channelId = channelId;
    }

    @NotNull
    @Override
    public String getId() {
        return source + "_" + channelId;
    }

    public String getSource() {
        return source;
    }

    public String getChannelId() {
        return channelId;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TvShowChannel clone() throws CloneNotSupportedException {
        return (TvShowChannel) super.clone();
    }

    @Override
    public TvShowChannel combine(TvShowChannel oldValue) throws CloneNotSupportedException {
        return clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TvShowChannel channel = (TvShowChannel) o;

        if (!source.equals(channel.source)) return false;
        if (!channelId.equals(channel.channelId)) return false;
        if (!name.equals(channel.name)) return false;
        return !(icon != null ? !icon.equals(channel.icon) : channel.icon != null);

    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + channelId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }

    public List<TvShowItem> getItems() {
        return items;
    }
}
