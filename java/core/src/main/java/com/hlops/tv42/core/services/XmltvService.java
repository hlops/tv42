package com.hlops.tv42.core.services;

import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.bean.TvShowItem;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tom on 1/31/16.
 */
public interface XmltvService {

    class XmltvPack {
        List<TvShowItem> items = new ArrayList<>();
        List<TvShowChannel> channels = new ArrayList<>();

        public List<TvShowItem> getItems() {
            return items;
        }

        public List<TvShowChannel> getChannels() {
            return channels;
        }
    }

    Collection<TvShowItem> getItems();

    Collection<TvShowChannel> getChannels();

    void actualize(@NotNull XmltvPack xmltvPack);

    XmltvPack load(@NotNull String source, @NotNull Reader reader) throws IOException;

}
