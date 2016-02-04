package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.Link;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.LinkService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by tom on 2/2/16.
 */
@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private DbService dbService;

    @Override
    @Nullable
    public Link getLink(String channelName) {
        return (Link) dbService.get(DbService.Entity.links).get(channelName);
    }

    @Override
    public Collection<Link> getLinks() {
        //noinspection unchecked
        return (Collection<Link>) dbService.get(DbService.Entity.links).values();
    }

    @Override
    public void update(@NotNull Collection<Link> links) {
        dbService.update(DbService.Entity.links, links);
    }

}
