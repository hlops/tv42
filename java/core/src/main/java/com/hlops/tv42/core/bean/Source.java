package com.hlops.tv42.core.bean;

import java.net.URL;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/14/16
 * Time: 12:07 PM
 */
public class Source implements Identifiable {

    private final String name;
    private final SourceType type;
    private final URL url;
    private long lastModified = -1;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Source)) return false;

        Source source = (Source) o;

        if (lastModified != source.lastModified) return false;
        if (!name.equals(source.name)) return false;
        if (!type.equals(source.type)) return false;
        return url.equals(source.url);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + (int) (lastModified ^ (lastModified >>> 32));
        return result;
    }

    public enum ChannelSourceType implements SourceType {
        m3u
    }

    public enum TvShowSourceType implements SourceType {
        xmltv
    }

    public interface SourceType {
    }
}
