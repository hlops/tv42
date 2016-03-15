package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.Link;
import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.LinkService;
import com.hlops.tv42.core.services.M3uChannelService;
import com.hlops.tv42.core.services.XmltvService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 2/2/16.
 */
@Service
public class LinkServiceImpl extends GenericServiceImpl<Link> implements LinkService {

    @Autowired
    private M3uChannelService m3UChannelService;

    @Autowired
    private XmltvService xmltvService;

    @Override
    protected DbService.Entity getEntity() {
        return DbService.Entity.links;
    }

    @Override
    @Nullable
    public Link getLink(String channelName) {
        return map().get(channelName);
    }

    @Override
    public Collection<Link> getLinks() {
        return values();
    }

    @Override
    public void actualize(@NotNull Collection<Link> values) {
        super.actualize(values);
    }

    public void actualizeLinks() {

        Map<String, Link> links = new HashMap<>();
        for (M3uChannel channel : m3UChannelService.getChannels()) {
            // todo: check direct link
            // if (channel.tvShowChannel == null)
            if (getLink(channel.getName()) == null) {
                TvShowChannel tvShowChannel = xmltvService.matchByName(channel.getName());
                if (tvShowChannel != null) {
                    Link link = new Link(channel.getName(), tvShowChannel.getChannelId());
                    links.put(link.getId(), link);
                }
            }
        }

        if (!links.isEmpty()) {
            actualize(links.values());
        }
    }

}
