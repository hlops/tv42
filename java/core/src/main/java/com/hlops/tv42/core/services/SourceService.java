package com.hlops.tv42.core.services;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.hlops.tv42.core.bean.Source;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/14/16
 * Time: 12:26 PM
 */
public interface SourceService {

    Source getSource(String id);

    Collection<Source> getSources();

    void update(@NotNull Collection<Source> sources);

    void delete(List<Source> sources);

    boolean loadIfModified(@NotNull Source source) throws IOException;

}
