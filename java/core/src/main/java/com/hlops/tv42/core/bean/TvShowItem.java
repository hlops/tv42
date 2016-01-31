package com.hlops.tv42.core.bean;

import org.jetbrains.annotations.NotNull;

/**
 * Created by tom on 1/31/16.
 */
public class TvShowItem implements Identifiable {

    private final String source;
    private final String channelId;
    private final long start;
    private final long stop;
    private String title;
    private String description;
    private String category;

    public TvShowItem(@NotNull String source, @NotNull String channelId, long start, long stop) {
        this.source = source;
        this.channelId = channelId;
        this.start = start;
        this.stop = stop;
    }

    @NotNull
    public String getId() {
        return source + "_" + String.valueOf(start);
    }

    public String getSource() {
        return source;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public long getStart() {
        return start;
    }

    public long getStop() {
        return stop;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TvShowItem clone() throws CloneNotSupportedException {
        return (TvShowItem) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TvShowItem that = (TvShowItem) o;

        if (start != that.start) return false;
        if (stop != that.stop) return false;
        if (!source.equals(that.source)) return false;
        if (!channelId.equals(that.channelId)) return false;
        if (!title.equals(that.title)) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return !(category != null ? !category.equals(that.category) : that.category != null);

    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + channelId.hashCode();
        result = 31 * result + (int) (start ^ (start >>> 32));
        result = 31 * result + (int) (stop ^ (stop >>> 32));
        result = 31 * result + title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
