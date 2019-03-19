package com.elector.Services;

import com.elector.Objects.Entities.*;
import com.elector.Objects.General.*;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Sigal on 5/21/2016.
 */

public interface GeneralManager {

    public <T> void updateObjects(List<T> list);

    public <T> T loadObject(Class<T> clazz, int oid);

    public <T> void updateObject(T object);

    public <T> List<T> getList(Class<T> clazz);

    public <T> void delete(Class<T> clazz);

    public <T> void deleteByAdminOid(Class<T> clazz, int adminOid);

    public <T> List<T> loadList(Class<T> clazz);


}