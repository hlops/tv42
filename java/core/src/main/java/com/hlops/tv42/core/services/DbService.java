package com.hlops.tv42.core.services;

import com.hlops.tv42.core.bean.Identifiable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    void delete(@NotNull Entity entity, @NotNull List<? extends Identifiable> sources);

    enum Entity {
        sources, m3uChannels, tvShowChannels, tvShowItems, mapping
    }
}
