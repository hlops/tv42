package com.hlops.tv42.webService.bean;

import com.hlops.tv42.core.bean.TvShowChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 2/2/16
 * Time: 7:03 PM
 */
public class TvShowChannelVO implements Serializable {

    private final String id;
    private final String name;
    private final List<String> sources;

    public TvShowChannelVO(@NotNull TvShowChannel channel, @Nullable List<String> sources) {
        this.id = channel.getId();
        this.name = channel.getName();
        this.sources = sources;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getSources() {
        return sources;
    }
}
