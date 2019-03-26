package com.elector.Controllers;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


//

import java.sql.*;

@Controller
@EnableAutoConfiguration

public class SampleController {
    static Connection myConn;
    static int counter=0;
     static String name=null;
     static int employeeId;
    @RequestMapping(value = "/getAlldata", method = RequestMethod.GET)
    String getdata(@RequestParam String login, @RequestParam String pass) throws SQLException {
        if(getAllData(login, pass))//if pass and phone correct
        return "redirect:/main";
        else
            return "Landing_page";
    }
       @RequestMapping(value = "/getAlltext", method = RequestMethod.GET)
    String getText(@RequestParam String text, @RequestParam String hoursWorked,@RequestParam String theDay) throws SQLException {

        Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "tuRgmhuI1");
        String sql ="insert into comments "+"(employeeId,comments)"+"values (?,?)";
        PreparedStatement preparedStmt = myConn.prepareStatement(sql);
        preparedStmt.setInt (1, employeeId);
        preparedStmt.setString (2, text);
        preparedStmt.execute();

        return "redirect:/main";
    }
    @RequestMapping(value="/main" ,method = RequestMethod.GET)
    public String mainp(Model model) throws Exception {

        model.addAttribute("name", "Welcome back "+name);
        return "main";
    }
    @RequestMapping("/home" )
    public String home(Model model) throws Exception {
        return "Landing_page";
    }


    @RequestMapping("/reports" )
    public String reports(Model model) throws Exception {
        return "reports";
    }



    @RequestMapping("/special_report" )
    public String special_reports(Model model) throws Exception {
        return "special_report";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }


    private static boolean getAllData(String login, String pass) throws SQLException {

        myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "tuRgmhuI1");

        PreparedStatement myStmt = myConn.prepareStatement("select * from test2.employee WHERE test2.employee.companyId='1' and test2.employee.employeePhone=? and test2.employee.employeePassword=? ");
        myStmt.setString(1, login);
        myStmt.setString(2, pass);

        ResultSet  rs =  myStmt.executeQuery();

        if (rs.next()==true) {
            name = rs.getString("employeeName");
            employeeId = rs.getInt("employeeId");
            return true;
        }

            return false;

    }

}


