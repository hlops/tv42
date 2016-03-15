package com.hlops.tv42.core.services;

import com.hlops.tv42.core.bean.Identifiable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/13/16
 * Time: 3:41 PM
 */
public interface DbService {

    boolean isInitDefaultValues();

    Map<String, ? extends Identifiable> get(@NotNull Entity entity);

    void commit();

    void drop(@NotNull Entity entity);

    enum Entity {
        sources, m3uChannels, m3uGroups, tvShowChannels, links
    }
}
