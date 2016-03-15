package com.hlops.tv42.core.services;

import com.hlops.tv42.core.bean.Link;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by tom on 2/2/16.
 */
public interface LinkService {

    @Nullable
    Link getLink(String channelName);

    Collection<Link> getLinks();

    void actualize(@NotNull Collection<Link> links);

    void actualizeLinks();
}
