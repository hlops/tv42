package com.hlops.tv42.core.bean;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/14/16
 * Time: 12:07 PM
 */
public class Source implements Identifiable, Comparable<Source> {

    private final String name;
    private final SourceType type;
    private final URL url;
    private long lastModified = -1;
    private String order;

    public Source(@NotNull String name, @NotNull SourceType type, @NotNull URL url) {
        this.name = name;
        this.type = type;
        this.url = url;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public SourceType getType() {
        return type;
    }

    @NotNull
    public URL getUrl() {
        return url;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    @NotNull
    @Override
    public String getId() {
        return name;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Source clone() throws CloneNotSupportedException {
        return (Source) super.clone();
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Source)) return false;

        Source source = (Source) o;

        if (lastModified != source.lastModified) return false;
        if (!name.equals(source.name)) return false;
        if (type != source.type) return false;
        if (!url.equals(source.url)) return false;
        return !(order != null ? !order.equals(source.order) : source.order != null);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + (int) (lastModified ^ (lastModified >>> 32));
        result = 31 * result + (order != null ? order.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Source o) {
        return 0;
    }

    public enum SourceType {
        m3u, xmltv;
    }

}
