package com.hlops.tv42.core.services;

import java.util.Collection;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.hlops.tv42.core.bean.Identifiable;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/13/16
 * Time: 3:41 PM
 */
public interface DbService {

    Map<String, ? extends Identifiable> get(@NotNull Entity entity);

    void update(@NotNull Entity entity, @NotNull Collection<? extends Identifiable> values);

    void update(@NotNull Entity entity, Identifiable... values);

    void drop(@NotNull Entity entity);

    enum Entity {
        sources, m3uChannels, tvShow, mapping
    }
}
