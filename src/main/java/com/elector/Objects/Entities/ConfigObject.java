package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

/**
 * Created by Sigal on 6/16/2016.
 */
public class ConfigObject extends BaseEntity {
    private int oid;
    private String configKey;
    private String configValue;

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

}
