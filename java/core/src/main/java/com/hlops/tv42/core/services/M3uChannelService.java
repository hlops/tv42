package com.hlops.tv42.core.services;

import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.M3uGroup;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/12/16
 * Time: 7:33 PM
 */
public interface M3uChannelService {

    Collection<M3uChannel> getChannels();

    Collection<M3uChannel> getOrderedChannels();

    M3uChannel getChannelById(String id);

    void actualize(@NotNull Collection<M3uChannel> channels);

    List<M3uChannel> load(@NotNull String source, int sourceWeight, @NotNull BufferedReader reader) throws IOException;

    void writeChannels(Writer responseWriter, Collection<M3uChannel> channels) throws IOException;

}
