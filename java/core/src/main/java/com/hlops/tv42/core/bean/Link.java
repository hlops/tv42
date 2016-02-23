package com.hlops.tv42.core.bean;

import org.jetbrains.annotations.NotNull;

/**
 * Created by tom on 2/2/16.
 */
public class Link implements Identifiable<Link> {

    private final String m3uChannel;
    private final String tvShowChannel;
    private String source;
    private String group;
    private Short timeshift;
    private Boolean active;

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

    public String getSource() {

        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Short getTimeshift() {

        return timeshift;
    }

    public void setTimeshift(Short timeshift) {
        this.timeshift = timeshift;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Link clone() throws CloneNotSupportedException {
        return (Link) super.clone();
    }

    @Override
    public Link combine(Link oldValue) throws CloneNotSupportedException {
        return clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        if (!m3uChannel.equals(link.m3uChannel)) return false;
        if (!tvShowChannel.equals(link.tvShowChannel)) return false;
        if (source != null ? !source.equals(link.source) : link.source != null) return false;
        if (group != null ? !group.equals(link.group) : link.group != null) return false;
        if (timeshift != null ? !timeshift.equals(link.timeshift) : link.timeshift != null) return false;
        return !(active != null ? !active.equals(link.active) : link.active != null);

    }

    @Override
    public int hashCode() {
        int result = m3uChannel.hashCode();
        result = 31 * result + tvShowChannel.hashCode();
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (timeshift != null ? timeshift.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }
}
