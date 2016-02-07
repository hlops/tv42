package com.hlops.tv42.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlops.tv42.core.bean.Identifiable;
import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.Source;
import com.hlops.tv42.core.services.DbService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/13/16
 * Time: 3:41 PM
 */
@Service
public class DbServiceImpl implements DbService {

    private static Logger log = LogManager.getLogger(DbServiceImpl.class);

    @Value("${tv42-db-model}")
    private String dbModel;

    @Value("${tv42-db-file-name:}")
    private String dbFileName;

    @Value("${tv42-db-delete-files-on-close:false}")
    private boolean dbDeleteFilesOnClose;

    private DB db;
    private boolean initDefaultValues;

    @PostConstruct
    private void init() {
        DBMaker.Maker dbMaker;
        if ("memory".equals(dbModel)) {

            log.debug("init db in memory");
            dbMaker = DBMaker
                    .memoryDB();
        } else if ("disk".equals(dbModel)) {
            if (StringUtils.isEmpty(dbFileName)) {
                throw new IllegalArgumentException("tv42-db-file-name is not specified");
            }
            File file = new File(dbFileName);
            if (!file.isAbsolute()) {
                file = new File(SystemUtils.getUserHome(), dbFileName);
            }
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();

            if (!file.exists()) {
                initDefaultValues = true;
            }

            log.debug("init db from file: " + file.getAbsolutePath());
            dbMaker = DBMaker
                    .fileDB(file)
                    .fileChannelEnable();

            if (dbDeleteFilesOnClose) {
                dbMaker.deleteFilesAfterClose();
            }
        } else {
            throw new IllegalArgumentException("tv42-db-model can be set to [memory|disk] only");
        }

        db = dbMaker
                .asyncWriteEnable()
                .closeOnJvmShutdown()
                .make();

        for (Entity entity : Entity.values()) {
            db.hashMapCreate(entity.name())
                    .keySerializer(Serializer.STRING)
                    .makeOrGet();
        }

        if (initDefaultValues) {
            loadDefaultValues();
        }
    }

    @PreDestroy
    private void destroy() {
        if (db != null) {
            db.commit();
            db.close();
        }
    }

    @Override
    public Map<String, Identifiable> get(@NotNull Entity entity) {
        return db.hashMap(entity.name());
    }

    @Override
    public void update(@NotNull Entity entity, @NotNull Collection<? extends Identifiable> values) {
        Map<String, Identifiable> map = get(entity);
        for (Identifiable value : values) {
            try {
                Identifiable oldValue = map.get(value.getId());
                if (oldValue == null) {
                    map.put(value.getId(), value);
                } else if (!value.equals(oldValue)) {
                    //noinspection unchecked
                    map.put(value.getId(), (Identifiable) value.combine(oldValue));
                }
            } catch (CloneNotSupportedException e) {
                log.error(e.getMessage() + " for " + value.getClass(), e);
            }
        }
        db.commit();
    }

    public void update(@NotNull Entity entity, Identifiable... values) {
        if (values != null) {
            update(entity, Arrays.asList(values));
        }
    }

    @Override
    public void delete(@NotNull Entity entity, @NotNull List<? extends Identifiable> list) {
        Map<String, Identifiable> map = get(entity);
        for (Identifiable value : list) {
            if (value != null) {
                map.remove(value.getId());
            }
        }
        db.commit();
    }

    @Override
    public void drop(@NotNull Entity entity) {
        db.hashMap(entity.name()).clear();
        db.commit();
    }

    public void loadDefaultValues() {
        // todo
        loadDefaultValues(Entity.sources, Source[].class);
        loadDefaultValues(Entity.m3uChannels, M3uChannel[].class);
    }

    protected void loadDefaultValues(Entity entity, Class<? extends Identifiable[]> clz) {
        InputStream resourceStream = getClass().getResourceAsStream("/default." + entity.name() + ".json");
        if (resourceStream != null) {
            try (BufferedReader resourceReader = new BufferedReader(new InputStreamReader(resourceStream))) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                update(entity, gson.fromJson(resourceReader, clz));
            } catch (IOException e) {
                log.error("Unable to load default values for " + entity.name(), e);
            }
        }
    }
}
