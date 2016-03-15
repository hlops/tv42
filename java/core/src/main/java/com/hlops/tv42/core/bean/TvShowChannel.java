package com.hlops.tv42.core.bean;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by tom on 1/31/16.
 */
public class TvShowChannel implements Identifiable<TvShowChannel> {

    private final String channelId;
    private String name;
    private String icon;
    private final Map<String, List<TvShowItem>> sources = new HashMap<>();

    public TvShowChannel(@NotNull String source, @NotNull String channelId) {
        this.channelId = channelId;
        sources.computeIfAbsent(source, s -> new ArrayList<>());
    }

    @NotNull
    @Override
    public String getId() {
        return channelId;
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

    public Map<String, List<TvShowItem>> getSources() {
        return sources;
    }

    public List<TvShowItem> getItems(String source) {
        return sources.get(source);
    }

    @SuppressWarnings("unchecked")
    @Override
    public TvShowChannel clone() throws CloneNotSupportedException {
        return (TvShowChannel) super.clone();
    }

}
