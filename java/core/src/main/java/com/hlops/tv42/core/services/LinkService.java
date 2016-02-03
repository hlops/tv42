package com.hlops.tv42.core.services;

import com.hlops.tv42.core.bean.Link;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by tom on 2/2/16.
 */
public interface LinkService {

    Collection<Link> getLinks();

    @Nullable
    Link getLink(String channelName);

    void update(@NotNull Collection<Link> links);
}
