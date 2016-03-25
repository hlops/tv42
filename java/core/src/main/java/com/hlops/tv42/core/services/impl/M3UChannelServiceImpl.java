package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.M3uChannelService;
import com.hlops.tv42.core.services.M3uGroupService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/12/16
 * Time: 7:34 PM
 */
@Service
public class M3UChannelServiceImpl extends GenericServiceImpl<M3uChannel> implements M3uChannelService {

    @Value("${M3U-UDP-HTTP-PROXY-PREFIX:http://192.168.1.1:4000/udp}")
    private String UDP_HTTP_PROXY_PREFIX;

    @Autowired
    private M3uGroupService groupService;

    @Override
    protected DbService.Entity getEntity() {
        return DbService.Entity.m3uChannels;
    }

    @Override
    public Collection<M3uChannel> getChannels() {
        return values();
    }

    @Override
    public Collection<M3uChannel> getOrderedChannels() {
        Stream<M3uChannel> stream = values().stream();
        return stream.sorted().collect(Collectors.toList());
    }

    @Override
    public M3uChannel getChannelById(String id) {
        return get(id);
    }

    @Override
    public void actualize(@NotNull Collection<M3uChannel> channels) {
        List<String> sources = channels.stream().map(M3uChannel::getSource).collect(Collectors.toList());
        List<M3uChannel> sourceChannels = getChannels().stream().filter(p -> sources.contains(p.getSource())).collect(Collectors.toList());
        sourceChannels.forEach(p -> p.setActual(false));
        sourceChannels.addAll(channels);
        super.actualize(sourceChannels);

        groupService.actualizeGroups(channels);
        actualizeLinks(channels);
        commit();
    }

    private void actualizeLinks(@NotNull Collection<M3uChannel> channels) {
        // todo: set direct link with source
/*
        for (M3uChannel channel: channels) {

        }
*/
    }

    @Override
    public List<M3uChannel> load(@NotNull String source, int sourceWeight, @NotNull BufferedReader reader) throws IOException {
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
                M3uChannel channel = new M3uChannel(item.getName(), source, sourceWeight);
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

    @Override
    public void writeChannels(Writer responseWriter, Collection<M3uChannel> channels) throws IOException {
        BufferedWriter writer = new BufferedWriter(responseWriter);

        writer.write("#EXTM3U m3uautoload=0 deinterlace=1 aspect-ratio=4:3");
        writer.newLine();

        String currentGroup = "";
        for (M3uChannel channel : channels) {
            writer.write("#EXTINF:-1");
            if (StringUtils.isNotEmpty(channel.getTvgName())) {
                writer.write(" tvg-name=" + channel.getTvgName());
            }
            if (channel.getAttributes().contains(M3uChannel.ChannelAttribute.WIDE)) {
                writer.write(" aspect-ratio=16:9");
            }
            if (channel.getAttributes().contains(M3uChannel.ChannelAttribute.HD)) {
                writer.write(" crop=1920x1080+0+0");
            }

            if (!currentGroup.equals(channel.getGroup())) {
                currentGroup = channel.getGroup();
                writer.write(" group-title=\"" + currentGroup + "\"");
            }

            writer.write(", " + channel.getName());
            writer.newLine();

            writer.write(modifyUrl(channel.getUrl()));
            writer.newLine();
            writer.newLine();
        }
        writer.flush();
    }

    public String modifyUrl(String url) {
        if (url.startsWith("udp://")) {
            return UDP_HTTP_PROXY_PREFIX + url.substring(7);
        }
        return url;
    }

    @Override
    protected M3uChannel combine(M3uChannel value, M3uChannel oldValue) throws CloneNotSupportedException {
        if (oldValue.getSource().equals(value.getSource())) {
            return value.clone();
        } else {
            boolean isOldValueUsed = oldValue.getSourceWeight() > value.getSourceWeight();
            if (oldValue.getSourceWeight() == value.getSourceWeight()) {
                isOldValueUsed = oldValue.getSource().compareTo(value.getSource()) >= 0;
            }
            if (isOldValueUsed) {
                return oldValue;
            } else {
                M3uChannel newValue = value.clone();
                newValue.setCreated(oldValue.getCreated());
                return newValue;
            }
        }
    }

    @Override
    protected void loadDefaultValues() {
        loadDefaultValues(M3uChannel[].class);
    }
}
