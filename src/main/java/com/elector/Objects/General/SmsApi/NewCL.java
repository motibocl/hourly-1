package com.elector.Objects.General.SmsApi;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NewCL {
    private User user;
    private ContactList cl;

    public User getUser() {
        return user;
    }

    @XmlElement
    public void setUser(User user) {
        this.user = user;
    }

    public ContactList getCl() {
        return cl;
    }

    @XmlElement
    public void setCl(ContactList cl) {
        this.cl = cl;
    }
}
