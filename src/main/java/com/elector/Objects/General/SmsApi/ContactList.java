package com.elector.Objects.General.SmsApi;

import javax.xml.bind.annotation.XmlElement;

public class ContactList {

    private String name;

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }
}
