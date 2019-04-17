package com.elector.Controllers;


import com.elector.Persist;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

//

@Controller
@EnableAutoConfiguration
@Configuration
@ComponentScan("com.elector")

public class SampleController {
    private static Connection dbConnection;
    private static final String SECRET_KEY = "fgmdfgfdke34932HASDBAbsahdbsaBHbBHJBbhb";
    private static final String SESSION = "foo";

    Date now = new Date();
    java.sql.Date today = new java.sql.Date(now.getTime());
//

    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() throws Exception {
        dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "Elector2019");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String getdata(@RequestParam String login, @RequestParam String pass, HttpServletResponse response) throws SQLException {
        if (checkCredentials(login, pass)) {//if pass and phone correct
            response.addCookie(new Cookie(SESSION, generateToken(login)));//making a cookie
            return "redirect:/main";
        } else
            return "Landing_page";
    }

    @RequestMapping("/admin")
    public String admin(Model model) throws SQLException {

        return "admin";

    }
     //adding reason if the employee forgot to press the enter and exit button.
    @RequestMapping(value = "/addReason", method = RequestMethod.GET)
    public String getText(@RequestParam String text, @RequestParam String hoursWorked, @RequestParam String reasonDate, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
            persist.sendReason(parseInt(getEmployeeId(cookie)),hoursWorked, text, reasonDate);//adding reason to dataBase
        return "redirect:/main";
    }


    //Sending comments "What did you do today"
    @RequestMapping(value = "/sendComment", method = RequestMethod.POST)
    public String getCommentary(@RequestParam String commentary, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
        persist.sendComment(parseInt(getEmployeeId(cookie)),commentary);//sendin the comment

        return "redirect:/reports";
    }
    //our home page.
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainp(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        // if (cookie.equals(""))//check if logged in.
        String phone = convertToken(cookie);
        if (!isPhoneNumberExist(phone))
            return "Landing_page";
        try {
            String name = "";
            ResultSet result = persist.selectEmployeeByPhone(phone);//getting employee by phone.
            while (result.next()) {
                name = result.getString("employeeName");
            }

            model.addAttribute("name", "ברוך הבא  " + name);//the welcome back page title.
            //getting all the working time list
            ResultSet rs2 = persist.selectWorkTimeByDay(parseInt(getEmployeeId(cookie)),today);
            float workedToday = 0;
            ArrayList<String> timeWorkList = new ArrayList<String>();
            //counting how many hours the employee worked this day.
            while (rs2.next()) {
                workedToday += rs2.getFloat("totalhoursWorked");
                timeWorkList.add("זמן עבודה: " + timeString(rs2.getFloat("enterTime")) + "  ->   " + timeString(rs2.getFloat("exitTime")));
                //adding to a list of working hours.
            }
            ResultSet rs = persist.selectEmployeeById(parseInt(getEmployeeId(cookie)));
            boolean clicked = false;
            while (rs.next()) {
                clicked = rs.getBoolean("enterOrExit");
            }
            //change button image if clicked or not.
            if (!clicked) {
                model.addAttribute("url", "css/images/enter-button2.png");
                model.addAttribute("id", "0");
            } else {
                model.addAttribute("url", "css/images/exit-button.png");
                model.addAttribute("id", "1");
                if(timeWorkList.size()>0)
                    timeWorkList.remove( timeWorkList.size()-1);
               // else
                   // timeWorkList.remove( timeWorkList.size());
            }

            String time = "הזמן שעבדת היום: " + timeString(workedToday);//all the time that the employee worked.
            model.addAttribute("workedToday", time);
            model.addAttribute("timeWorkList", timeWorkList);
            //getting the month and the year from calendar object.
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int todayMonth = calendar.get(Calendar.MONTH);//month 0-11
            int todayYear = calendar.get(Calendar.YEAR);//year current
            ResultSet rs3 = persist.selectWorkTimeMonth(todayMonth+1,todayYear ,parseInt(getEmployeeId(cookie)) );//getting resultset for all worked time in a month.
            float workedThisMonth = 0;//count the month working time.
            while (rs3.next()) {
                workedThisMonth += rs3.getFloat("totalhoursWorked");
            }
            //the string .
            String timeMonth = "הזמן שעבדת החודש: " + timeString(workedThisMonth);
            model.addAttribute("timeWorkMonth", timeMonth);


            return "main";
        } catch (Exception exc) {
            return "error";
        }
    }
    @ResponseBody
    @RequestMapping("/buttonStatus")
    public  boolean button(@CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
        ResultSet rs =  persist.selectEmployeeById(parseInt(getEmployeeId(cookie)));//getting buton status
        if(rs.next()) {
            boolean flagTest = rs.getBoolean("enterOrExit");
            return flagTest;
        }
        else
            return false;
    }

    @RequestMapping("/addWorkTime")
    public String addWorkTime(Model model, @RequestParam("button") int button, @CookieValue(value = SESSION, defaultValue = "") String cookie, @RequestParam("enterTime") Float enterTime) throws Exception {
        if (checkCookie(cookie)) {
            persist.updateButtonStatus(button,parseInt(getEmployeeId(cookie)));
            Date day = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(day);
            String[] dayOfTheWeek = {"יום א", "יום ב", "יום ג", "יום ד", "יום ה", "יום ו", "יום ז"};
            persist.addWorktime(parseInt(getEmployeeId(cookie)),enterTime,today,dayOfTheWeek[c.get(Calendar.DAY_OF_WEEK) - 1]);
            model.addAttribute("url", "css/images/exit-button.png");

             return "redirect:/main";
        }
        return "Landing_page";
    }

    @RequestMapping("/loginPage")
    public String loginPage(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        if (checkCookie(cookie))
            return "redirect:/main";
        return "Landing_page";
    }


    @ResponseBody
    @RequestMapping("/updateWorkTime")
    public String updateWorkTime(Model model, @RequestParam("button") int button, @RequestParam("exitTime") float exitTime, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        if (checkCookie(cookie)) {
            float exit = exitTime;
            float total = exitTime - enterTime(cookie);
            persist.updateWorktime(exit,total,parseInt(getEmployeeId(cookie)));//updating total and exitTime.
            persist.updateButtonStatus(button,parseInt(getEmployeeId(cookie)));//updating button status.
            ResultSet rs2 = persist.selectLastWorktime(parseInt(getEmployeeId(cookie)),today);//getting the last working time registered in the db by the user.
            float workedToday = 0;
            String workingTime="" ;
            //counting how many hours the employee worked this day.
            while (rs2.next()) {
                workingTime+=("זמן עבודה: " + timeString(rs2.getFloat("enterTime")) + "  ->   " + timeString(rs2.getFloat("exitTime"))+"</br>");
                //adding to a list of working hours.
            }
            return workingTime;
        }
        return "Landing_page";
    }

    @RequestMapping("/logout")
    public String logout(Model model, HttpServletResponse response) throws Exception {
        Cookie cookie = new Cookie(SESSION, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        //for enter and exit.

        return "Landing_page";
    }

    @RequestMapping("/reports")
    public String reports(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie, @RequestParam(value = "month", defaultValue = "") String month, @RequestParam(value = "year", defaultValue = "") String year) throws Exception {

        if (!checkCookie(cookie))
            return "Landing_page";
        try {
            if (!month.equals("") && !year.equals("")) {
                ResultSet rs = persist.selectWorkTimeMonth(parseInt(month),parseInt(year),parseInt(getEmployeeId(cookie)));
                ResultSet rsExitTime;
                ResultSet rsEnterTime;
                ResultSet rsSumTime;

                ArrayList<String> dayList = new ArrayList<String>();
                ArrayList<String> hoursWorked = new ArrayList<String>();
                ArrayList<String> hoursList = new ArrayList<String>();
                ArrayList<Date> dateList = new ArrayList<Date>();
                for (int i = 1; i <= 31; i++) {
                    rsExitTime = persist.selectLastWorktimeDay(parseInt(year),parseInt(month),parseInt(getEmployeeId(cookie)),i);//get the last worktime in a day.
                    rsEnterTime = persist.selectFirstWorktimeDay(parseInt(year),parseInt(month),parseInt(getEmployeeId(cookie)),i);//get the first worktime in a day.;
                    rsSumTime = persist.totalHoursWorkedInDay(parseInt(year),parseInt(month),parseInt(getEmployeeId(cookie)),i);//get the total time worked in a day.

                    if (rsSumTime.next() && rsEnterTime.next()&& rsExitTime.next()) {
                        dayList.add(rsEnterTime.getString("dayOfTheWeek"));
                        hoursWorked.add(timeString(rsSumTime.getFloat("total")));
                        hoursList.add(timeString(rsEnterTime.getFloat("enterTime")) + "   ->   " + timeString(rsExitTime.getFloat("exitTime")));
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

                rs = persist.selectWorkTimeMonth(parseInt(month),parseInt(year),parseInt(getEmployeeId(cookie)));
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

    @RequestMapping("/special_report")
    public String special_reports(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        if (!checkCookie(cookie))
            return "Landing_page";
        return "special_report";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
       // delete(2);//delete all comment of employee test
    }
//test
  /*  public static void delete(int employeeId) throws Exception {
        String sqlDel = "delete from test2.comments where employeeId=?";
        PreparedStatement preparedStmt = dbConnection.prepareStatement(sqlDel);
        preparedStmt.setInt(1, employeeId);
        preparedStmt.executeQuery();
        sqlDel = "delete from test2.employee where employeeId=?";
        preparedStmt = dbConnection.prepareStatement(sqlDel);
        preparedStmt.setInt(1, employeeId);
        preparedStmt.executeQuery();

    }
*/
    //iliya said to add try catch final.
    private static boolean checkCredentials(String login, String pass) throws SQLException {
        PreparedStatement myStmt = dbConnection.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  and test2.employee.employeePassword=? ");
        try {
            myStmt.setInt(1, parseInt(login));
            myStmt.setString(2, pass);
        } catch (Exception exc) {
            return false;
        }
        ResultSet rs = myStmt.executeQuery();

        if (rs.next()) {
            return true;
        }
        return false;
    }

    private String generateToken(String login) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < login.length(); i++) {
            if (login.charAt(i) == '0')
                text.append("a");
            if (login.charAt(i) == '1')
                text.append("b");
            if (login.charAt(i) == '2')
                text.append("c");
            if (login.charAt(i) == '3')
                text.append("d");
            if (login.charAt(i) == '4')
                text.append("e");
            if (login.charAt(i) == '5')
                text.append("f");
            if (login.charAt(i) == '6')
                text.append("g");
            if (login.charAt(i) == '7')
                text.append("h");
            if (login.charAt(i) == '8')
                text.append("i");
            if (login.charAt(i) == '9')
                text.append("j");
        }
        text.append(SECRET_KEY);
        return text.toString();
    }

    private String convertToken(String token) {
        StringBuilder text = new StringBuilder();
        if (!token.equals("")) {
            for (int i = 0; i < token.length() - SECRET_KEY.length(); i++) {
                if (token.charAt(i) == 'a')
                    text.append("0");
                if (token.charAt(i) == 'b')
                    text.append("1");
                if (token.charAt(i) == 'c')
                    text.append("2");
                if (token.charAt(i) == 'd')
                    text.append("3");
                if (token.charAt(i) == 'e')
                    text.append("4");
                if (token.charAt(i) == 'f')
                    text.append("5");
                if (token.charAt(i) == 'g')
                    text.append("6");
                if (token.charAt(i) == 'h')
                    text.append("7");
                if (token.charAt(i) == 'i')
                    text.append("8");
                if (token.charAt(i) == 'j')
                    text.append("9");
            }
        } else
            text.append("0");
        return text.toString();
    }

    private boolean checkCookie(String cookie) throws SQLException {
        PreparedStatement statement = dbConnection.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  ");
        statement.setInt(1, parseInt(convertToken(cookie)));
        ResultSet result = statement.executeQuery();
        if (result.next())
            return true;
        else
            return false;

    }

    private String timeString(float minutes) {
        int hoursTime = (int) (minutes / 60);
        int minutesTime = (int) (minutes % 60);
        if (minutesTime < 10)
            return "" + hoursTime + ":" + "0" + minutesTime;
        else
            return "" + hoursTime + ":" + minutesTime;
    }

    private String getEmployeeId(String phone) throws SQLException {
        String id = "";
        String employeePhone = convertToken(phone);
        PreparedStatement statement = dbConnection.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  ");
        statement.setInt(1, parseInt(employeePhone));
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            id = result.getString("employeeId");
            //name=result.getString("employeeName");
        }
        return id;
    }

    private int enterTime(String phone) throws SQLException {
        int timeEnter = 0;
        PreparedStatement statement = dbConnection.prepareStatement("select * from test2.worktime WHERE  employeeId=? order by timeId desc limit 1; ");
        statement.setInt(1, parseInt(getEmployeeId(phone)));
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            timeEnter = (int) result.getFloat("enterTime");
            //name=result.getString("employeeName");
        }
        return timeEnter;
    }

    private boolean isPhoneNumberExist (String phone) throws SQLException {
        PreparedStatement statement = dbConnection.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  ");
        statement.setInt(1, Integer.valueOf(phone));
        ResultSet result = statement.executeQuery();
        return result.next();
    }

}