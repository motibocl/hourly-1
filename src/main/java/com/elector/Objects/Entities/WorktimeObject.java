package com.elector.Objects.Entities;

import org.hibernate.annotations.Type;

import java.sql.Date;

public class WorktimeObject {
    private float enterTime;
    private float exitTime;
    private EmployeeObject employeeObject;
    private Date date;
    private int timeId;
    private float totalHoursWorked;
    private String dayOfTheWeek;
    private String comment;

    public WorktimeObject(){

    }
    public WorktimeObject(float enterTime,float exitTime,EmployeeObject employeeObject,Date date,float totalHoursWorked,String dayOfTheWeek,String comment){
        this.enterTime=enterTime;
        this.exitTime=exitTime;
        this.employeeObject=employeeObject;
        this.date=date;
        this.totalHoursWorked=totalHoursWorked;
        this.dayOfTheWeek=dayOfTheWeek;
        this.comment=comment;

    }

    public float getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(float enterTime) {
        this.enterTime = enterTime;
    }

    public float getExitTime() {
        return exitTime;
    }

    public void setExitTime(float exitTime) {
        this.exitTime = exitTime;
    }

    public EmployeeObject getEmployeeObject() {
        return employeeObject;
    }

    public void setEmployeeObject(EmployeeObject employeeObject) {
        this.employeeObject = employeeObject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public float getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public void setTotalHoursWorked(float totalHoursWorked) {
        this.totalHoursWorked = totalHoursWorked;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
