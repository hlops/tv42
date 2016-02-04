package com.hlops.tv42.core.services;

import com.hlops.tv42.core.bean.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/14/16
 * Time: 12:26 PM
 */
public interface SourceService {

    Source getSource(String id);

    Collection<Source> getSources();

    Collection<Source> getOrderedSources(@Nullable Source.SourceType sourceType);

    void update(@NotNull Collection<Source> sources);

    void delete(List<Source> sources);

    boolean load(Source source) throws IOException;

    boolean loadIfModified(@NotNull Source source) throws IOException;

}
