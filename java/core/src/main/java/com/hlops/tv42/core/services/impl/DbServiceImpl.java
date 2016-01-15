package com.hlops.tv42.core.services.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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

import com.hlops.tv42.core.bean.Identifiable;
import com.hlops.tv42.core.services.DbService;

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
                .closeOnJvmShutdown()
                .make();

        for (Entity entity : Entity.values()) {
            db.treeMapCreate(entity.name())
                    .keySerializer(Serializer.STRING)
                    .makeOrGet();
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
        return db.treeMap(entity.name());
    }

    @Override
    public void update(@NotNull Entity entity, @NotNull Collection<? extends Identifiable> values) {
        Map<String, Identifiable> map = get(entity);
        for (Identifiable value : values) {
            try {
                if (!value.equals(map.get(value.getId()))) {
                    map.put(value.getId(), value.clone());
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
    public void drop(@NotNull Entity entity) {
        db.delete(entity.name());
        db.commit();
    }
}
