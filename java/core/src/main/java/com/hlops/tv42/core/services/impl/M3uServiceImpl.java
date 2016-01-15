package com.hlops.tv42.core.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.M3uService;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/12/16
 * Time: 7:34 PM
 */
@Service
public class M3uServiceImpl implements M3uService {

    @Autowired
    DbService dbService;

    @Override
    public Collection<M3uChannel> getChannels() {
        //noinspection unchecked
        return (Collection<M3uChannel>) dbService.get(DbService.Entity.m3uChannels).values();
    }

    @Override
    public void actualize(@NotNull Collection<M3uChannel> channels) {

        Set<String> sources = channels.stream().map(M3uChannel::getSource).collect(Collectors.toSet());
        Collection<M3uChannel> allChannels = getChannels();
        Set<M3uChannel> sourceChannels = allChannels.stream().filter(p -> sources.contains(p.getSource())).collect(Collectors.toSet());
        sourceChannels.forEach(p -> p.setActual(false));
        sourceChannels.addAll(channels);
        dbService.update(DbService.Entity.m3uChannels, sourceChannels);
    }

    @Override
    public List<M3uChannel> load(@NotNull String source, @NotNull BufferedReader reader) throws IOException {
        List<M3uChannel> channels = new ArrayList<>();
        try {
            String line, lineUC;
            ExtInf extInf = null;
            String groupTitle = null;
            List<ExtInf> items = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }

                lineUC = line.toUpperCase();

                if (lineUC.startsWith("#EXTINF")) {
                    extInf = new ExtInf(line);
                    items.add(extInf);
                    if (extInf.get(ExtInf.Attribute.group_title) == null) {
                        extInf.set(ExtInf.Attribute.group_title, groupTitle);
                    } else {
                        groupTitle = extInf.get(ExtInf.Attribute.group_title);
                    }
                } else {
                    if (extInf != null) {
                        extInf.setUrl(line.trim());
                    }
                }
            }

            for (ExtInf item : items) {
                M3uChannel channel = new M3uChannel(source, item.getName());
                channels.add(channel);
                channel.setGroup(item.get(ExtInf.Attribute.group_title));
                channel.setUrl(item.getUrl());
                channel.setTvgName(item.get(ExtInf.Attribute.tvg_name));

                String aspect = item.get(ExtInf.Attribute.aspect_ratio);
                if ("16:9".equals(aspect)) {
                    channel.getAttributes().add(M3uChannel.ChannelAttribute.WIDE);
                }
                String crop = item.get(ExtInf.Attribute.crop);
                if ("1920x1080+0+0".equals(crop)) {
                    channel.getAttributes().add(M3uChannel.ChannelAttribute.HD);
                }
            }
        } finally {
            reader.close();
        }
        return channels;
    }

}