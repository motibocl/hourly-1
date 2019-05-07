package com.elector.Objects.Entities;

public class AdminObject {
    private String name;
    private String password;
    private String email;
    private int id ;
    private String phone ;
    private CompanyObject companyObject;


    public AdminObject(){}
    public AdminObject(String name, String password,String email,int id,String phone,CompanyObject companyObject){
        this.id=id;
        this.email=email;
        this.name=name;
        this.password=password;
        this.phone=phone;
        this.companyObject=companyObject;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public CompanyObject getCompanyObject() {
        return companyObject;
    }

    public void setCompanyObject(CompanyObject companyObject) {
        this.companyObject = companyObject;
    }
}
