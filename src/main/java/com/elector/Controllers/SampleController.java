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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

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
        myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "tuRgmhuI1");
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
    public String getText(@RequestParam String text, @RequestParam String hoursWorked, @RequestParam String reasonDate,@CookieValue(value = "foo", defaultValue = "") String cookie) throws SQLException {
        if (cookie.equals(""))//cheack if loged in.
            return "Landing_page";
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
            model.addAttribute("name", "ברוך הבא  " + name);//the welcome back page title.
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
                    timeWorkList.add("זמן עבודה: "+(int)(rs2.getFloat("enterTime") / 60)+":"+(int)(rs2.getFloat("enterTime")%60) +"  ->   "+(int)(rs2.getFloat("exitTime") / 60)+":"+(int)(rs2.getFloat("exitTime")%60)) ;
                //adding to a list of working hours.
                }


                String time="הזמן שעבדת היום: "+(int)(workedToday / 60)+"hr"+":"+(int)(workedToday % 60)+"min";//all the time that the employee worked.
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
                 String timeMonth="הזמן שעבדת החודש: "+(int)(workedThisMonth / 60)+"hr"+":"+(int)(workedThisMonth % 60)+"min";
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
            Date day = new Date();
            LocalDate localDate = day.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String[] dayOfTheWeek={"יום א","יום ב","יום ג","יום ד","יום ה","יום ו","יום ז"};
            String sql = "insert into worktime (employeeId,enterTime,exitTime,totalhoursWorked,date,dayOfTheWeek) values (?,?,?,?,?,?)";
            PreparedStatement preparedStmt = myConn.prepareStatement(sql);
            preparedStmt.setInt(1, employeeId);
            preparedStmt.setFloat(2, enter);
            preparedStmt.setFloat(3, exit);
            preparedStmt.setFloat(4, total);
            preparedStmt.setDate(5,today);
            preparedStmt.setString(6,dayOfTheWeek[localDate.getDayOfMonth()]);

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

       // if (!cookie.equals(""))
           // return "redirect:/main";
        return "Landing_page";
    }

    @RequestMapping("/reports")
    public String reports(Model model,@CookieValue(value = "foo", defaultValue = "") String cookie,@RequestParam(value = "month",defaultValue = "")String month,@RequestParam(value = "year",defaultValue = "")String year) throws Exception {

            if (cookie.equals(""))
                return "Landing_page";
        try {
            if(!month.equals("")&&!year.equals("")) {
                PreparedStatement myStmt = myConn.prepareStatement("select * from test2.worktime WHERE test2.worktime.employeeId=? and YEAR (date)=? and MONTH (date)=?");
                myStmt.setInt(1, employeeId);
                myStmt.setInt(2, parseInt(year));
                myStmt.setInt(3,  parseInt(month) );
                ResultSet rs = myStmt.executeQuery();
                ResultSet rsExitTime;
                ResultSet rsEnterTime;
                ResultSet rsSumTime;

                ArrayList<String> dayList = new ArrayList<String>();
                ArrayList<String> hoursWorked = new ArrayList<String>();
                ArrayList<String> hoursList = new ArrayList<String>();
                ArrayList<Date> dateList = new ArrayList<Date>();
                for(int i=1;i<=31;i++){
                    myStmt =myConn.prepareStatement("SELECT exitTime FROM worktime WHERE YEAR (date)=? and MONTH(date)=? and    day(date)=? ORDER BY timeId DESC limit 1");
                    myStmt.setInt(1, parseInt(year));
                    myStmt.setInt(2,  parseInt(month) );
                    myStmt.setInt(3,  i );
                    rsExitTime = myStmt.executeQuery();
                    myStmt =myConn.prepareStatement("SELECT * FROM worktime WHERE YEAR (date)=? and MONTH(date)=? and    day(date)=?  limit 1");
                    myStmt.setInt(1, parseInt(year));
                    myStmt.setInt(2,  parseInt(month) );
                    myStmt.setInt(3, i );
                    rsEnterTime = myStmt.executeQuery();
                    myStmt =myConn.prepareStatement("SELECT SUM(worktime.totalhoursWorked) as total FROM worktime WHERE YEAR (date)=? and MONTH(date)=? and  day(date)=?");
                    myStmt.setInt(1, parseInt(year));
                    myStmt.setInt(2,  parseInt(month) );
                    myStmt.setInt(3, i );

                    rsSumTime = myStmt.executeQuery();
                    if(rsSumTime.next()&&rsEnterTime.next()&&rsExitTime.next()) {
                        dayList.add(rsEnterTime.getString("dayOfTheWeek"));
                        hoursWorked.add((int) (rsSumTime.getFloat("total") / 60) + ":" + (int) (rsSumTime.getFloat("total") % 60));
                        hoursList.add((int) (rsEnterTime.getFloat("enterTime") / 60) + ":" + (int) (rsEnterTime.getFloat("enterTime") % 60) + "  ->   " + (int) (rsExitTime.getFloat("exitTime") / 60) + ":" + (int) (rsExitTime.getFloat("exitTime") % 60));
                        dateList.add(rsEnterTime.getDate("date"));
                    }
                }
                model.addAttribute("days", dayList);
                model.addAttribute("hours", hoursList);
                model.addAttribute("hoursWorked", hoursWorked);
                model.addAttribute("dateWorked", dateList);
                /*this is queris for the details modal*/
                ArrayList<String> dayListDetails = new ArrayList<String>();
                ArrayList<String> hoursWorkedDetails = new ArrayList<String>();
                ArrayList<String> hoursListDetails = new ArrayList<String>();
                ArrayList<Date> dateListDetails = new ArrayList<Date>();
                myStmt =myConn.prepareStatement("SELECT * FROM worktime WHERE YEAR (date)=? and MONTH(date)=? ");
                myStmt.setInt(1, parseInt(year));
                myStmt.setInt(2,  parseInt(month) );
                rs = myStmt.executeQuery();
                while (rs.next()) {
                    dayListDetails.add(rs.getString("dayOfTheWeek"));
                    hoursListDetails.add((int) (rs.getFloat("enterTime") / 60) + ":" + (int) (rs.getFloat("enterTime") % 60) + "  ->   " + (int) (rs.getFloat("exitTime") / 60) + ":" + (int) (rs.getFloat("exitTime") % 60));
                    hoursWorkedDetails.add((int) (rs.getFloat("totalhoursWorked") / 60) + ":" + (int) (rs.getFloat("totalhoursWorked") % 60));
                    dateListDetails.add(rs.getDate("date"));
                }

                //for the details
                model.addAttribute("daysDetails", dayListDetails);
                model.addAttribute("hoursDetails", hoursListDetails);
                model.addAttribute("hoursWorkedDetails", hoursWorkedDetails);
                model.addAttribute("dateWorkedDetails", dateListDetails);
            }
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
            myStmt.setInt(1, parseInt(login));
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

