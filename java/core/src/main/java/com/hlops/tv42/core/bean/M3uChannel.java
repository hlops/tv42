package com.hlops.tv42.core.bean;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/13/16
 * Time: 11:35 AM
 */
public class M3uChannel implements Identifiable {

    private final String source;
    private final String name;
    private final Set<ChannelAttribute> attributes = new HashSet<>();
    private String url;
    private boolean actual = true;
    private String group;
    private String tvgName;

    public M3uChannel(@NotNull String source, @NotNull String name) {
        this.source = source;
        this.name = name;
    }

    public M3uChannel(@NotNull String source, @NotNull String name, String group) {
        this(source, name);
        this.group = group;
    }

    @NotNull
    @Override
    public String getId() {
        return source + "_" + name;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getSource() {
        return source;
    }

    public String getTvgName() {
        return tvgName;
    }

    public void setTvgName(String tvgName) {
        this.tvgName = tvgName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<ChannelAttribute> getAttributes() {
        return attributes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public M3uChannel clone() throws CloneNotSupportedException {
        return (M3uChannel) super.clone();
    }

    @Override
    public String toString() {
        return "M3uChannel{" +
                "actual=" + actual +
                ", source='" + source + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", group='" + group + '\'' +
                ", tvgName='" + tvgName + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof M3uChannel)) return false;

        M3uChannel channel = (M3uChannel) o;

        if (actual != channel.actual) return false;
        if (source != null ? !source.equals(channel.source) : channel.source != null) return false;
        if (name != null ? !name.equals(channel.name) : channel.name != null) return false;
        if (url != null ? !url.equals(channel.url) : channel.url != null) return false;
        if (group != null ? !group.equals(channel.group) : channel.group != null) return false;
        if (tvgName != null ? !tvgName.equals(channel.tvgName) : channel.tvgName != null) return false;
        return attributes.equals(channel.attributes);

    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (actual ? 1 : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (tvgName != null ? tvgName.hashCode() : 0);
        result = 31 * result + attributes.hashCode();
        return result;
    }

    public enum ChannelAttribute {
        WIDE, HD
    }
}
