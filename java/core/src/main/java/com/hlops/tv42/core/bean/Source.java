package com.hlops.tv42.core.bean;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/14/16
 * Time: 12:07 PM
 */
public class Source implements Identifiable<Source>, Comparable<Source> {

    private final String name;
    private final SourceType type;
    private final URL url;
    private long lastModified = -1;
    private int weight;

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Source clone() throws CloneNotSupportedException {
        return (Source) super.clone();
    }

    @Override
    public Source combine(Source oldValue) throws CloneNotSupportedException {
        return clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Source source = (Source) o;

        if (lastModified != source.lastModified) return false;
        if (weight != source.weight) return false;
        if (!name.equals(source.name)) return false;
        if (type != source.type) return false;
        return url.equals(source.url);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + (int) (lastModified ^ (lastModified >>> 32));
        result = 31 * result + weight;
        return result;
    }

    @Override
    public int compareTo(@NotNull Source o) {
        if (getType() != o.getType()) {
            return Integer.compare(getType().ordinal(), o.getType().ordinal());
        }
        if (getWeight() == o.getWeight()) {
            return getName().compareTo(o.getName());
        }
        return -Integer.compare(getWeight(), o.getWeight());
    }

    public enum SourceType {
        m3u, xmltv;
    }

}
