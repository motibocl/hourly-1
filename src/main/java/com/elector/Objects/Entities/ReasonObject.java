package com.elector.Objects.Entities;

import java.sql.Date;

public class ReasonObject {
    private float exitTime;
    private float enterTime;
    private int howManyHours;
    private int reasonId;
    private String reasonText;
    private Date date;
    private EmployeeObject employeeObject;
public ReasonObject(){}
    public ReasonObject(float exitTime,float enterTime,String reasonText,Date date,EmployeeObject employeeObject,int howManyHours){
    this.enterTime=enterTime;
    this.exitTime=exitTime;
    this.date=date;
    this.howManyHours=howManyHours;
    this.employeeObject=employeeObject;
    this.reasonText=reasonText;
    }

    public float getExitTime() {
        return exitTime;
    }

    public void setExitTime(float exitTime) {
        this.exitTime = exitTime;
    }

    public float getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(float enterTime) {
        this.enterTime = enterTime;
    }

    public int getHowManyHours() {
        return howManyHours;
    }

    public void setHowManyHours(int howManyHours) {
        this.howManyHours = howManyHours;
    }

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EmployeeObject getEmployeeObject() {
        return employeeObject;
    }

    public void setEmployeeObject(EmployeeObject employeeObject) {
        this.employeeObject = employeeObject;
    }
}
