package com.elector.Services.impl;

import com.elector.Objects.Entities.*;
import com.elector.Objects.General.*;
import com.elector.Persist;
import com.elector.Services.GeneralManager;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

import static com.elector.Utils.Definitions.*;

/**
 * Created by Sigal on 5/21/2016.
 */

@Service
@DependsOn(value = {"persist"})
public class GeneralManagerImpl implements GeneralManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralManagerImpl.class);

    @Autowired
    private Persist persist;

    public <T> void updateObjects(List<T> list) {
        try {
            for (T item : list) {
                BaseEntity old = (BaseEntity) persist.loadObject(item.getClass(), ((BaseEntity) item).getOid());
                if (old != null) {
                    old.setObject((BaseEntity) item);
                    persist.save(old);
                } else {
                    persist.save(item);
                }
            }
        } catch (Exception e) {
            LOGGER.error("updateObjects", e);
        }
    }



    public <T> T loadObject(Class<T> clazz, int oid) {
        return persist.loadObject(clazz, oid);
    }

    public <T> void updateObject(T object) {
        persist.save(object);
    }

    public <T> List<T> getList(Class<T> clazz) {
        return persist.getList(clazz);
    }

    public <T> void delete(Class<T> clazz) {
        persist.delete(clazz);
    }

    public <T> void deleteByAdminOid(Class<T> clazz, int adminOid) {
        persist.deleteByAdminOid(clazz, adminOid);
    }


    public <T> List<T> loadList(Class<T> clazz) {
        return persist.loadList(clazz);
    }





}
