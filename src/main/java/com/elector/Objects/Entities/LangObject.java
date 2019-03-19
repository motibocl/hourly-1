package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

/**
 * Created by Sigal on 4/8/2017.
 */
public class LangObject extends BaseEntity{
    private int code;
    private String name;
    private boolean active;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
