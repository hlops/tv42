package com.hlops.tv42.core.services;

import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.bean.TvShowItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

/**
 * Created by tom on 1/31/16.
 */
public interface XmltvService {

    List<TvShowItem> findItems(TvShowChannel tvShowChannel, long start, long stop, @Nullable String preferableSource);

    TvShowChannel getChannelById(String id);

    Collection<TvShowChannel> getChannels();

    void actualize(@NotNull Collection<TvShowChannel> channels);

    Collection<TvShowChannel> load(@NotNull String source, @NotNull InputStream stream) throws IOException;

    @Nullable
    TvShowChannel matchByName(@NotNull String name);

    void save(Collection<TvShowChannel> channels, @NotNull String source, OutputStream outputStream) throws IOException;

}
