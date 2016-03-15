package com.hlops.tv42.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlops.tv42.core.bean.Identifiable;
import com.hlops.tv42.core.services.DbService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 3/10/16
 * Time: 12:48 PM
 */
public abstract class GenericServiceImpl<T extends Identifiable> {

    private static Logger log = LogManager.getLogger(GenericServiceImpl.class);

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private DbService dbService;

    protected abstract DbService.Entity getEntity();

    @PostConstruct
    private void init() {
        if (dbService.isInitDefaultValues()) {
            loadDefaultValues();
        }
    }

    protected Map<String, T> map() {
        //noinspection unchecked
        return (Map<String, T>) dbService.get(getEntity());
    }

    protected T get(String key) {
        return map().get(key);
    }

    protected Collection<T> values() {
        return map().values();
    }

    protected void commit() {
        dbService.commit();
    }

    protected void actualize(@NotNull Collection<T> values) {
        Map<String, T> map = map();
        for (T value : values) {
            try {
                T oldValue = map.get(value.getId());
                if (oldValue == null) {
                    map.put(value.getId(), value);
                } else if (!value.equals(oldValue)) {
                    map.put(value.getId(), combine(value, oldValue));
                }
            } catch (CloneNotSupportedException e) {
                log.error(e.getMessage() + " for " + value.getClass(), e);
            }
        }
        commit();
    }

    protected void actualize(T... values) {
        if (values != null) {
            actualize(Arrays.asList(values));
        }
    }

    protected void delete(@NotNull Collection<T> values) {
        Map<String, T> map = map();
        for (T value : values) {
            if (value != null) {
                map.remove(value.getId());
            }
        }
        commit();
    }

    protected void delete(T... values) {
        if (values != null) {
            delete(Arrays.asList(values));
        }
    }

    protected void loadDefaultValues() {
    }

    protected final void loadDefaultValues(Class<T[]> clz) {
        InputStream resourceStream = getClass().getResourceAsStream("/default." + getEntity().name() + ".json");
        if (resourceStream != null) {
            try (BufferedReader resourceReader = new BufferedReader(new InputStreamReader(resourceStream))) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                actualize(gson.fromJson(resourceReader, clz));
            } catch (IOException e) {
                log.error("Unable to load default values for " + getEntity().name(), e);
            }
        }
    }

    protected T combine(T value, @SuppressWarnings("UnusedParameters") T oldValue) throws CloneNotSupportedException {
        //noinspection unchecked
        return (T) value.clone();
    }

}
