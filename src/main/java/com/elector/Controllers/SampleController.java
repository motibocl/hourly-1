package com.elector.Controllers;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//

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
        myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "Elector2019");
    }

    @RequestMapping(value = "/getAlldata", method = RequestMethod.POST)
    public String getdata(@RequestParam String login, @RequestParam String pass, HttpServletResponse response) throws SQLException {
        if (checkCredentials(login, pass)) {//if pass and phone correct
            response.addCookie(new Cookie("foo", generateToken(35)));//making a cookie
            //loggedIn = true;
            return "redirect:/main";
        } else
            return "Landing_page";
    }


    @RequestMapping(value = "/getAlltext", method = RequestMethod.GET)
    public String getText(@RequestParam String text, @RequestParam String hoursWorked, @RequestParam String reasonDate) throws SQLException {

        // Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "tuRgmhuI1");
        String sql = "insert into reason (employeeId,howmanyHours,reasonText,date) values (?,?,?,?)";
        PreparedStatement preparedStmt = myConn.prepareStatement(sql);
        preparedStmt.setInt(1, employeeId);
        preparedStmt.setString(2, hoursWorked);
        preparedStmt.setString(3, text);
        preparedStmt.setString(4,  reasonDate);
        preparedStmt.execute();

        return "redirect:/main";
    }


    //Sending comments "What did you do today"
    @RequestMapping(value = "/sendComment", method = RequestMethod.GET)
    public String getCommentary(@RequestParam String commentary) throws SQLException {

        // Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "tuRgmhuI1");
        String sql = "insert into comments (employeeId,comments) values (?,?)";
        PreparedStatement preparedStmt = myConn.prepareStatement(sql);
        preparedStmt.setInt(1, employeeId);
        preparedStmt.setString(2, commentary);
        preparedStmt.execute();

        return "redirect:/reports";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainp(Model model,@CookieValue(value = "foo", defaultValue = "") String cookie) throws Exception {
        if (cookie.equals(""))//cheack if loged in.
            return "Landing_page";
        try {
            model.addAttribute("name", "Welcome back " + name);//the welcome back page title.
                //getting all the working time list
                PreparedStatement Stmt = myConn.prepareStatement("select * from test2.worktime WHERE test2.worktime.employeeId=? and test2.worktime.date=?");
                Stmt .setInt(1, employeeId);
                Stmt.setDate(2, today);
                ResultSet rs2 = Stmt.executeQuery();
                float workedToday = 0;
                ArrayList<String> timeWorkList = new ArrayList<String>();
                //couting how many hours the employee worked this day.
                while (rs2.next()) {
                    workedToday+=rs2.getFloat("totalhoursWorked");
                    timeWorkList.add("working time:  "+(int)(rs2.getFloat("enterTime") / 60)+":"+(int)(rs2.getFloat("enterTime")%60) +"  ->   "+(int)(rs2.getFloat("exitTime") / 60)+":"+(int)(rs2.getFloat("exitTime")%60)) ;
                //adding to a list of working hours.
                }


                String time="time you worked today:  "+(int)(workedToday / 60)+"hr"+":"+(int)(workedToday % 60)+"min";//all the time that the employee worked.
                model.addAttribute("workedToday", time);
                model.addAttribute("timeWorkList", timeWorkList);
               //getting the month and the year from calendar object.
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int todayMonth = calendar.get(Calendar.MONTH);//month 0-11
                int todayYear= calendar.get(Calendar.YEAR);//year current
                PreparedStatement Stmt2 = myConn.prepareStatement("select * from test2.worktime WHERE test2.worktime.employeeId=? and MONTH(date)=? and YEAR (date)=?");
                Stmt2 .setInt(1, employeeId);
                Stmt2 .setInt(2, todayMonth+1);//because we want the current time we add 1.
                Stmt2 .setInt(3, todayYear);
                ResultSet rs3 = Stmt2.executeQuery();//excuting query.

                float workedThisMonth = 0;//count the month working time.
                 while (rs3.next()) {
                     workedThisMonth+=rs3.getFloat("totalhoursWorked");
                 }
                 //the string.
                 String timeMonth="time you worked this month:  "+(int)(workedThisMonth / 60)+"hr"+":"+(int)(workedThisMonth % 60)+"min";
                 model.addAttribute("timeWorkMonth", timeMonth);



            return "main";
        } catch (Exception exc) {
            return "error";
        }
    }

    @RequestMapping("/home")
    public String home(Model model,@CookieValue(value = "foo", defaultValue = "") String cookie) throws Exception {
        if (!cookie.equals(""))
            return "redirect:/main";
        return "Landing_page";
    }
    @RequestMapping("/result")
    public String resultTime(Model model,  @RequestParam ("enterTime") float enterTime, @RequestParam ("exitTime") float exitTime,@CookieValue(value = "foo", defaultValue = "") String cookie) throws Exception {
        if (!cookie.equals("")) {
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
    public String logout(Model model,HttpServletResponse response) throws Exception {
        //loggedIn = false;
        Cookie cookie = new Cookie("foo", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        if (!cookie.equals(""))
            return "redirect:/main";
        return "Landing_page";
    }

    @RequestMapping("/reports")
    public String reports(Model model,@CookieValue(value = "foo", defaultValue = "") String cookie) throws Exception {

            if (cookie.equals(""))
                return "Landing_page";
        try {
            PreparedStatement myStmt = myConn.prepareStatement("select * from test2.comments WHERE test2.comments.employeeId=?  ");
            myStmt.setInt(1, employeeId);
            ResultSet rs = myStmt.executeQuery();
            ArrayList<String> commentList = new ArrayList<String>();
            while (rs.next()) {
                commentList.add(rs.getString("comments"));
            }
            // model.addAttribute("lengthList",commentList.size() );

            model.addAttribute("comment", commentList);

            return "reports";
        } catch (Exception exc) {
            return "error";

        }
    }
//elector8089

    @RequestMapping("/special_report")
    public String special_reports(Model model,@CookieValue(value = "foo", defaultValue = "") String cookie) throws Exception {
        if (cookie.equals(""))
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

 private String generateToken(int length){
     String text = "";
     String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

     for (int i = 0; i < length; i++)
         text += possible.charAt((int)(Math.floor(Math.random() * possible.length())));

     return text;
 }

}

