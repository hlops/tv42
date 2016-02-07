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
public class M3uChannel implements Identifiable<M3uChannel> {

    private final String name;
    private final String source;
    private final int sourceWeight;
    private final Set<ChannelAttribute> attributes = new HashSet<>();
    private String url;
    private boolean actual = true;
    private String group;
    private String tvgName;
    private long created = System.currentTimeMillis();

    public M3uChannel(@NotNull String name, @NotNull String source, int sourceWeight) {
        this.source = source;
        this.name = name;
        this.sourceWeight = sourceWeight;
    }

    public M3uChannel(@NotNull String name, String group, @NotNull String source, int sourceWeight) {
        this(name, source, sourceWeight);
        this.group = group;
    }

    @NotNull
    @Override
    public String getId() {
        return name;
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

    public int getSourceWeight() {
        return sourceWeight;
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

    public long getCreated() {
        return created;
    }

    @SuppressWarnings("unchecked")
    @Override
    public M3uChannel clone() throws CloneNotSupportedException {
        return (M3uChannel) super.clone();
    }

    @Override
    public M3uChannel combine(M3uChannel oldValue) throws CloneNotSupportedException {
        if (oldValue.getSource().equals(getSource())) {
            return clone();
        } else {
            boolean isOldValueUsed = oldValue.getSourceWeight() > getSourceWeight();
            if (oldValue.getSourceWeight() == getSourceWeight()) {
                isOldValueUsed = oldValue.source.compareTo(source) >= 0;
            }
            if (isOldValueUsed) {
                return oldValue;
            } else {
                M3uChannel newValue = clone();
                newValue.created = oldValue.created;
                return newValue;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        M3uChannel channel = (M3uChannel) o;

        if (actual != channel.actual) return false;
        if (created != channel.created) return false;
        if (!source.equals(channel.source)) return false;
        if (!name.equals(channel.name)) return false;
        if (!attributes.equals(channel.attributes)) return false;
        if (url != null ? !url.equals(channel.url) : channel.url != null) return false;
        if (group != null ? !group.equals(channel.group) : channel.group != null) return false;
        return !(tvgName != null ? !tvgName.equals(channel.tvgName) : channel.tvgName != null);

    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + attributes.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (actual ? 1 : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (tvgName != null ? tvgName.hashCode() : 0);
        result = 31 * result + (int) (created ^ (created >>> 32));
        return result;
    }

    public enum ChannelAttribute {
        WIDE, HD
    }
}
