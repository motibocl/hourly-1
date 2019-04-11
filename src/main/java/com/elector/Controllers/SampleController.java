package com.elector.Controllers;


import com.elector.Persist;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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

public class SampleController {
    private static Connection dbConnection;
    private static final String SECRET_KEY = "fgmdfgfdke34932HASDBAbsahdbsaBHbBHJBbhb";
    private static final String SESSION = "foo";
    // private static boolean loggedIn;//if logged in
    //  private static boolean entered;//if logged in
    // private static int counter = 0;
    // private static String name = null;
    //private static int employeeId;
    //  private static float enter;
    //  private static float exit;
    //  private static float total;
    // private static float enterBpressed;qwewqsadSAsaF
    // private static int clicked;
    Date now = new Date();
    java.sql.Date today = new java.sql.Date(now.getTime());


    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() throws Exception {
        dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "RAMI2018");
    }

    @RequestMapping(value = "/getAlldata", method = RequestMethod.POST)
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

    @RequestMapping(value = "/getAlltext", method = RequestMethod.GET)
    public String getText(@RequestParam String text, @RequestParam String hoursWorked, @RequestParam String reasonDate, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
        if (cookie.equals(""))//cheack if loged in.
            return "Landing_page";
        String sql = "insert into reason (employeeId,howmanyHours,reasonText,date) values (?,?,?,?)";
        PreparedStatement preparedStmt = dbConnection.prepareStatement(sql);
        preparedStmt.setInt(1, parseInt(getEmployeeId(cookie)));
        preparedStmt.setString(2, hoursWorked);
        preparedStmt.setString(3, text);
        preparedStmt.setString(4, reasonDate);
        preparedStmt.execute();

        return "redirect:/main";
    }


    //Sending comments "What did you do today"
    @RequestMapping(value = "/sendComment", method = RequestMethod.POST)
    public String getCommentary(@RequestParam String commentary, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
        String sql = "insert into comments (employeeId,comments) values (?,?)";
        PreparedStatement preparedStmt = dbConnection.prepareStatement(sql);
        preparedStmt.setInt(1, parseInt(getEmployeeId(cookie)));
        preparedStmt.setString(2, commentary);
        preparedStmt.execute();

        return "redirect:/reports";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainp(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        // if (cookie.equals(""))//cheack if loged in.
        String phone = convertToken(cookie);
        if (!isPhoneNumberExist(phone))
            return "Landing_page";
        try {
            String name = "";
            PreparedStatement statement = dbConnection.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  ");
            statement.setInt(1, parseInt(phone));
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                name = result.getString("employeeName");
            }

            model.addAttribute("name", "ברוך הבא  " + name);//the welcome back page title.
            //getting all the working time list
            PreparedStatement Stmt = dbConnection.prepareStatement("select * from test2.worktime WHERE test2.worktime.employeeId=? and test2.worktime.date=?");
            Stmt.setInt(1, parseInt(getEmployeeId(cookie)));
            Stmt.setDate(2, today);
            ResultSet rs2 = Stmt.executeQuery();
            float workedToday = 0;
            ArrayList<String> timeWorkList = new ArrayList<String>();
            //counting how many hours the employee worked this day.
            while (rs2.next()) {
                workedToday += rs2.getFloat("totalhoursWorked");
                timeWorkList.add("זמן עבודה: " + timeString(rs2.getFloat("enterTime")) + "  ->   " + timeString(rs2.getFloat("exitTime")));
                //adding to a list of working hours.
            }
            PreparedStatement Stmt1 = dbConnection.prepareStatement("select enterOrExit from test2.employee WHERE test2.employee.employeeId=?;");
            Stmt1.setInt(1, parseInt(getEmployeeId(cookie)));
            ResultSet rs = Stmt1.executeQuery();
            boolean clicked = false;
            while (rs.next()) {
                clicked = rs.getBoolean("enterOrExit");
            }
            if (!clicked) {
                model.addAttribute("url", "css/images/enter-button2.png");
                model.addAttribute("id", "0");


            } else {
                model.addAttribute("url", "css/images/exit-button.png");
                model.addAttribute("id", "1");
                timeWorkList.remove( timeWorkList.size()-1);
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
            PreparedStatement Stmt2 = dbConnection.prepareStatement("select * from test2.worktime WHERE test2.worktime.employeeId=? and MONTH(date)=? and YEAR (date)=?");
            Stmt2.setInt(1, parseInt(getEmployeeId(cookie)));
            Stmt2.setInt(2, todayMonth + 1);//because we want the current time we add 1.
            Stmt2.setInt(3, todayYear);
            ResultSet rs3 = Stmt2.executeQuery();//excuting query.

            float workedThisMonth = 0;//count the month working time.
            while (rs3.next()) {
                workedThisMonth += rs3.getFloat("totalhoursWorked");
            }
            //the string.
            String timeMonth = "הזמן שעבדת החודש: " + timeString(workedThisMonth);
            model.addAttribute("timeWorkMonth", timeMonth);


            return "main";
        } catch (Exception exc) {
            return "error";
        }
    }
    @RequestMapping("/button")
    public @ResponseBody boolean button(@CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
        PreparedStatement Stmt = dbConnection.prepareStatement("select * from test2.employee WHERE employeeId=? ");
        Stmt.setInt(1, parseInt(getEmployeeId(cookie)));
        ResultSet rs =  Stmt.executeQuery();
        if(rs.next()) {
            boolean flagTest = rs.getBoolean("enterOrExit");
            return flagTest;
        }
        else
            return false;
    }

    @RequestMapping("/update")
    public String update(Model model, @RequestParam("button") int button, @CookieValue(value = SESSION, defaultValue = "") String cookie, @RequestParam("enterTime") Float enterTime) throws Exception {
        if (checkCookie(cookie)) {
           // JSONObject jsonObject = new JSONObject();
            //enterBpressed=enterTime;

            String sql = "update test2.employee set enterOrExit=? where employeeId=? ";
            PreparedStatement preparedStmt = dbConnection.prepareStatement(sql);
            preparedStmt.setInt(1, button);
            preparedStmt.setInt(2, parseInt(getEmployeeId(cookie)));
            preparedStmt.execute();
            Date day = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(day);
            String[] dayOfTheWeek = {"יום א", "יום ב", "יום ג", "יום ד", "יום ה", "יום ו", "יום ז"};
            String sql2 = "insert into worktime (employeeId,enterTime,exitTime,totalhoursWorked,date,dayOfTheWeek) values (?,?,?,?,?,?)";
            PreparedStatement preparedStmt2 = dbConnection.prepareStatement(sql2);
            preparedStmt2.setInt(1, parseInt(getEmployeeId(cookie)));
            preparedStmt2.setFloat(2, enterTime);
            preparedStmt2.setFloat(3, 0);
            preparedStmt2.setFloat(4, 0);
            preparedStmt2.setDate(5, today);
            preparedStmt2.setString(6, dayOfTheWeek[c.get(Calendar.DAY_OF_WEEK) - 1]);
            preparedStmt2.execute();
            model.addAttribute("url", "css/images/exit-button.png");
          //  jsonObject.put("success", "true");
           // return jsonObject.toString();

             return "redirect:/main";
        }
        return "Landing_page";
    }

    @RequestMapping("/home")
    public String home(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        if (checkCookie(cookie))
            return "redirect:/main";
        return "Landing_page";
    }


    @ResponseBody
    @RequestMapping("/result")
    public String resultTime(Model model, @RequestParam("button") int button, @RequestParam("exitTime") float exitTime, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        if (checkCookie(cookie)) {
            float exit = exitTime;
            float total = exitTime - enterTime(cookie);
            String sql = " update test2.worktime set exitTime=?,totalhoursWorked=?  where employeeId=? order by timeId DESC limit 1";
            PreparedStatement preparedStmt = dbConnection.prepareStatement(sql);
            preparedStmt.setFloat(1, exit);
            preparedStmt.setFloat(2, total);
            preparedStmt.setInt(3, parseInt(getEmployeeId(cookie)));
            preparedStmt.execute();
            String sql2 = "update test2.employee set enterOrExit=? where employeeId=? ";
            PreparedStatement preparedStmt2 = dbConnection.prepareStatement(sql2);
            preparedStmt2.setInt(1, button);
            preparedStmt2.setInt(2, parseInt(getEmployeeId(cookie)));
            preparedStmt2.execute();
            ///////////////////////////////////////////////////
            PreparedStatement Stmt = dbConnection.prepareStatement("select * from test2.worktime WHERE test2.worktime.employeeId=? and test2.worktime.date=? order by  timeId desc limit 1");
            Stmt.setInt(1, parseInt(getEmployeeId(cookie)));
            Stmt.setDate(2, today);
            ResultSet rs2 = Stmt.executeQuery();
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
        //loggedIn = false;
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
                PreparedStatement myStmt = dbConnection.prepareStatement("select * from test2.worktime WHERE test2.worktime.employeeId=? and YEAR (date)=? and MONTH (date)=?");
                myStmt.setInt(1, parseInt(getEmployeeId(cookie)));
                myStmt.setInt(2, parseInt(year));
                myStmt.setInt(3, parseInt(month));
                ResultSet rs = myStmt.executeQuery();
                ResultSet rsExitTime;
                ResultSet rsEnterTime;
                ResultSet rsSumTime;

                ArrayList<String> dayList = new ArrayList<String>();
                ArrayList<String> hoursWorked = new ArrayList<String>();
                ArrayList<String> hoursList = new ArrayList<String>();
                ArrayList<Date> dateList = new ArrayList<Date>();
                for (int i = 1; i <= 31; i++) {
                    myStmt = dbConnection.prepareStatement("SELECT exitTime FROM worktime WHERE YEAR (date)=? and MONTH(date)=? and employeeId=? and day(date)=? ORDER BY timeId DESC limit 1");
                    myStmt.setInt(1, parseInt(year));
                    myStmt.setInt(2, parseInt(month));
                    myStmt.setInt(3, parseInt(getEmployeeId(cookie)));
                    myStmt.setInt(4, i);
                    rsExitTime = myStmt.executeQuery();
                    myStmt = dbConnection.prepareStatement("SELECT * FROM worktime WHERE YEAR (date)=? and MONTH(date)=? and    day(date)=? and employeeId=?   limit 1");
                    myStmt.setInt(1, parseInt(year));
                    myStmt.setInt(2, parseInt(month));
                    myStmt.setInt(3, i);
                    myStmt.setInt(4, parseInt(getEmployeeId(cookie)));

                    rsEnterTime = myStmt.executeQuery();
                    myStmt = dbConnection.prepareStatement("SELECT SUM(worktime.totalhoursWorked) as total FROM worktime WHERE YEAR (date)=? and MONTH(date)=? and  day(date)=? and employeeId=? ");
                    myStmt.setInt(1, parseInt(year));
                    myStmt.setInt(2, parseInt(month));
                    myStmt.setInt(3, i);
                    myStmt.setInt(4, parseInt(getEmployeeId(cookie)));

//rsSumTime.getFloat("total") / 60) + ":" + (int) (rsSumTime.getFloat("total") % 60)
                    rsSumTime = myStmt.executeQuery();
                    if (rsSumTime.next() && rsEnterTime.next() && rsExitTime.next()) {
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
                myStmt = dbConnection.prepareStatement("SELECT * FROM worktime WHERE YEAR (date)=? and MONTH(date)=? and employeeId=?");
                myStmt.setInt(1, parseInt(year));
                myStmt.setInt(2, parseInt(month));
                myStmt.setInt(3, parseInt(getEmployeeId(cookie)));
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

    @RequestMapping("/special_report")
    public String special_reports(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        if (!checkCookie(cookie))
            return "Landing_page";
        return "special_report";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
        delete(2);
    }

    public static void delete(int employeeId) throws Exception {
        String sqlDel = "delete from test2.comments where employeeId=?";
        PreparedStatement preparedStmt = dbConnection.prepareStatement(sqlDel);
        preparedStmt.setInt(1, employeeId);
        preparedStmt.executeQuery();
        sqlDel = "delete from test2.employee where employeeId=?";
        preparedStmt = dbConnection.prepareStatement(sqlDel);
        preparedStmt.setInt(1, employeeId);
        preparedStmt.executeQuery();

    }

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