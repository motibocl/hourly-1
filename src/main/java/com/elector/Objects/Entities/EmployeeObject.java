package com.elector.Objects.Entities;

import org.hibernate.annotations.Type;

public class EmployeeObject {
    private int id;
    private String name;
    private String password;
    private String email;
    private String phone;
    private boolean enterOrExit;
    private CompanyObject companyObject;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnterOrExit() {
        return enterOrExit;
    }

    public void setEnterOrExit(boolean enterOrExit) {
        this.enterOrExit = enterOrExit;
    }

    public CompanyObject getCompanyObject() {
        return companyObject;
    }

    public void setCompanyObject(CompanyObject companyObject) {
        this.companyObject = companyObject;
    }
}
