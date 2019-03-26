package com.elector.Controllers;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


//

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;

@Controller
@EnableAutoConfiguration

public class SampleController {
    private static Connection myConn;
    private static boolean loggedIn;//if logged in
    private static int counter = 0;
    private static String name = null;
    private static int employeeId;

    @PostConstruct
    public void init() throws Exception {
        myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "RAMI2018");
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
        String sql = "insert into comments (employeeId,comments) values (?,?)";
        PreparedStatement preparedStmt = myConn.prepareStatement(sql);
        preparedStmt.setInt(1, employeeId);
        preparedStmt.setString(2, text);
        preparedStmt.execute();

        return "redirect:/main";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainp(Model model) throws Exception {
        try {
            model.addAttribute("name", "Welcome back " + name);
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

    @RequestMapping("/logout")
    public String logout(Model model) throws Exception {
        loggedIn = false;
        return "redirect:/home";
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
            // model.addAttribute("lengthList",commentList.size() );

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
    }

    //iliya said to add try catch final.
    private static boolean checkCredentials(String login, String pass) throws SQLException {


        PreparedStatement myStmt = myConn.prepareStatement("select * from test2.employee WHERE test2.employee.companyId='1234' and test2.employee.employeePhone=? and test2.employee.employeePassword=? ");
        myStmt.setString(1, login);
        myStmt.setString(2, pass);

        ResultSet rs = myStmt.executeQuery();

        if (rs.next()) {
            name = rs.getString("employeeName");
            employeeId = rs.getInt("employeeId");
            return true;
        }

        return false;

    }

}


