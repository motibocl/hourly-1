package com.elector.Controllers;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


//

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;
import java.util.zip.ZipEntry;

@Controller
@EnableAutoConfiguration

public class SampleController {
    private static Connection myConn;
    private static boolean loggedIn;//if logged in
    private static boolean flag;//if logged in
    private static int counter = 0;
    private static String name = null;
    private static int employeeId;
    private static float enter;
    private static float exit;
    private static float total;
    Date now = new Date();
    java.sql.Date today = new java.sql.Date(now.getTime());
    @PostConstruct
    public void init() throws Exception {
        myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "tuRgmhuI1");
    }

    @RequestMapping(value = "/getAlldata", method = RequestMethod.POST)
    public String getdata(@RequestParam String login, @RequestParam String pass) throws SQLException {
        if (checkCredentials(login, pass)) {//if pass and phone correct
            loggedIn = true;
            return "redirect:/main";
        } else
            return "Landing_page";
    }


    @RequestMapping(value = "/getAlltext", method = RequestMethod.GET)
    public String getText(@RequestParam String text, @RequestParam String hoursWorked, @RequestParam String theDay) throws SQLException {

        // Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "tuRgmhuI1");
        String sql = "insert into reason (employeeId,howmanyHours,reasonText,date) values (?,?,?,?)";
        PreparedStatement preparedStmt = myConn.prepareStatement(sql);
        preparedStmt.setInt(1, employeeId);
        preparedStmt.setString(2, hoursWorked);
        preparedStmt.setString(3, text);
        preparedStmt.setString(4, theDay);
        preparedStmt.execute();

        return "redirect:/main";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainp(Model model) throws Exception {
        try {
            model.addAttribute("name", "Welcome back " + name);

                PreparedStatement Stmt = myConn.prepareStatement("select * from test2.worktime WHERE test2.worktime.employeeId=? and test2.worktime.date=?");
                Stmt .setInt(1, employeeId);
                Stmt.setDate(2, today);
                ResultSet rs2 = Stmt.executeQuery();
                float workedToday = 0;
                // model.addAttribute("lengthList",commentList.size() );
                while (rs2.next()) {
                    workedToday+=rs2.getFloat("totalhoursWorked");
                }
                String time="time you worked today:  "+(int)(workedToday / 60)+"hr"+":"+(int)(workedToday % 60)+"min";
                model.addAttribute("workedToday", time);


            if (!loggedIn)
                return "Landing_page";
            return "main";
        } catch (Exception exc) {
            return "error";
        }
    }

    @RequestMapping("/home")
    public String home(Model model) throws Exception {
        if (loggedIn)
            return "redirect:/main";
        return "Landing_page";
    }
    @RequestMapping("/result")
    public String resultTime(Model model,  @RequestParam ("enterTime") float enterTime, @RequestParam ("exitTime") float exitTime) throws Exception {
        if (loggedIn) {
            flag=true;
            enter=enterTime;
            exit=exitTime;
            total=exitTime-enterTime;
            String sql = "insert into worktime (employeeId,enterTime,exitTime,totalhoursWorked,date) values (?,?,?,?,?)";
            PreparedStatement preparedStmt = myConn.prepareStatement(sql);
            preparedStmt.setInt(1, employeeId);
            preparedStmt.setFloat(2, enter);
            preparedStmt.setFloat(3, exit);
            preparedStmt.setFloat(4, total);
            preparedStmt.setDate(5,today);

            preparedStmt.execute();

            return "redirect:/main";
        }
        return "Landing_page";
    }
    @RequestMapping("/logout")
    public String logout(Model model) throws Exception {
        loggedIn = false;
        return "Landing_page";
    }

    @RequestMapping("/reports")
    public String reports(Model model) throws Exception {
        try {
            if (!loggedIn)
                return "Landing_page";
            PreparedStatement myStmt = myConn.prepareStatement("select * from test2.comments WHERE test2.comments.employeeId=?  ");
            myStmt.setInt(1, employeeId);
            ResultSet rs = myStmt.executeQuery();
            ArrayList<String> commentList = new ArrayList<String>();
            while (rs.next()) {
                commentList.add(rs.getString("comments"));
            }


            model.addAttribute("comment", commentList);

            return "reports";
        } catch (Exception exc) {
            return "error";

        }
    }


    @RequestMapping("/special_report")
    public String special_reports(Model model) throws Exception {
        if (!loggedIn)
            return "Landing_page";
        return "special_report";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
        delete(2);
    }
    public  static void delete(int employeeId)throws Exception{
        String sqlDel ="delete from test2.comments where employeeId=?";
        PreparedStatement preparedStmt = myConn.prepareStatement(sqlDel);
        preparedStmt.setInt(1, employeeId);
        preparedStmt.executeQuery();

        sqlDel ="delete from test2.employee where employeeId=?";
        preparedStmt = myConn.prepareStatement(sqlDel);
        preparedStmt.setInt(1, employeeId);
        preparedStmt.executeQuery();

    }
    //iliya said to add try catch final.
    private static boolean checkCredentials(String login, String pass) throws SQLException {


        PreparedStatement myStmt = myConn.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  and test2.employee.employeePassword=? ");
        try {
            myStmt.setInt(1, Integer.parseInt(login));
            myStmt.setString(2, pass);
        }catch (Exception exc){
            return false;
        }
        ResultSet rs = myStmt.executeQuery();

        if (rs.next()) {
            name = rs.getString("employeeName");
            employeeId = rs.getInt("employeeId");
            return true;
        }

        return false;

    }



}

