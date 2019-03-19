package com.elector.Objects.General;

import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Field;

public abstract class BaseEntity {
    protected int oid;
    protected boolean deleted;

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setObject(BaseEntity other){
        try {
            Field[] fields = new Field[0];
            Class clazz = this.getClass();
            do {
                fields =(Field[]) ArrayUtils.addAll(fields, clazz.getDeclaredFields());
                clazz = clazz.getSuperclass();

            } while (!clazz.equals(BaseEntity.class));
            for(Field f : fields){
                f.setAccessible(true);
                Object value = f.get(other);
                if (value != null) {
                    f.set(this, value) ;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDifferentData (BaseEntity other) {
        boolean differentData = false;
        try {
            Field[] fields = (Field[]) ArrayUtils.addAll(this.getClass().getDeclaredFields(), this.getClass().getSuperclass().getDeclaredFields()) ;
            for(Field f : fields){
                f.setAccessible(true);
                Object value = f.get(other);
                if (value != null && !value.equals(f.get(this))) {
                    differentData = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return differentData;

    }


    @Override
    public boolean equals(Object obj) {
        return getClass().equals(obj.getClass()) && this.oid == ((BaseEntity)obj).getOid();
    }
}
