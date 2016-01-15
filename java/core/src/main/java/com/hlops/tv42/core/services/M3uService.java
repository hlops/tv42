package com.hlops.tv42.core.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.hlops.tv42.core.bean.M3uChannel;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/12/16
 * Time: 7:33 PM
 */
public interface M3uService {

    Collection<M3uChannel> getChannels();

    void actualize(@NotNull Collection<M3uChannel> channels);

    List<M3uChannel> load(@NotNull String source, @NotNull BufferedReader reader) throws IOException;

}
