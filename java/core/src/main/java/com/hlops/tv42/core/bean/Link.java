package com.hlops.tv42.core.bean;

import org.jetbrains.annotations.NotNull;

/**
 * Created by tom on 2/2/16.
 */
public class Link implements Identifiable {

    private final String m3uChannel;
    private final String tvShowChannel;
    private Byte timeshift;

    public Link(@NotNull String m3uChannel, @NotNull String tvShowChannel) {
        this.m3uChannel = m3uChannel;
        this.tvShowChannel = tvShowChannel;
    }

    @NotNull
    @Override
    public String getId() {
        return m3uChannel;
    }

    @NotNull
    public String getM3uChannel() {
        return m3uChannel;
    }

    @NotNull
    public String getTvShowChannel() {
        return tvShowChannel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        if (!m3uChannel.equals(link.m3uChannel)) return false;
        if (!tvShowChannel.equals(link.tvShowChannel)) return false;
        return !(timeshift != null ? !timeshift.equals(link.timeshift) : link.timeshift != null);

    }

    @Override
    public int hashCode() {
        int result = m3uChannel.hashCode();
        result = 31 * result + tvShowChannel.hashCode();
        result = 31 * result + (timeshift != null ? timeshift.hashCode() : 0);
        return result;
    }

    public Byte getTimeshift() {

        return timeshift;
    }

    public void setTimeshift(Byte timeshift) {
        this.timeshift = timeshift;
    }

    @SuppressWarnings("unchecked")
    @Override
    public M3uChannel clone() throws CloneNotSupportedException {
        return (M3uChannel) super.clone();
    }

}
