package com.elector.Controllers;


import com.elector.Persist;
import com.elector.Utils.sendSMS;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.SmsSubmissionResponseMessage;
import com.nexmo.client.sms.messages.TextMessage;
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
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
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
    java.sql.Date today = new java.sql.Date(now.getTime());//להגדיר בכל פונקציה כי יכול להיות שמשתמש לא יכבה מחשב והתאריך יהיה התאריך האחרון שהוצב במשתנה
//

    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() throws Exception {
        dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "RAMI2018");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String getdata(@RequestParam String login, @RequestParam String pass, HttpServletResponse response) throws SQLException {
        if (checkCredentials(login, pass)) {//if pass and phone correct
            response.addCookie(new Cookie(SESSION, generateToken(login)));//making a cookie
            return "redirect:/main";
        } else
            return "Landing_page";
    }

    @RequestMapping("/administration")
    public String admin(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
        String phone = convertToken(cookie);
        if (!isPhoneNumberExist(phone))
            return "Landing_page";
        model.addAttribute("admin",isAdmin(getEmployeeId(cookie)));
        return "administration";

    }
     //adding reason if the employee forgot to press the enter and exit button.
    @RequestMapping(value = "/addReason", method = RequestMethod.GET)
    public String getText(@RequestParam String text, @RequestParam String hoursWorked, @RequestParam String reasonDate, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
            persist.sendReason(parseInt(getEmployeeId(cookie)),hoursWorked, text, reasonDate);//adding reason to dataBase
        return "redirect:/main";
    }

    @RequestMapping("/add-employee")
    public String addEmployee(@RequestParam(value = "id", defaultValue = "") String id,@RequestParam(value = "name", defaultValue = "") String name,@RequestParam(value = "empPhone", defaultValue = "") String empPhone,@RequestParam(value = "password", defaultValue = "") String password)  throws SQLException {
        persist.addEmployee(parseInt(id),name,parseInt(empPhone),password);

        return "redirect:/administration";
    }
    @RequestMapping("/remove-employee")
    public String removeEmployee(@RequestParam(value = "id", defaultValue = "") String id)  throws SQLException {
        persist.removeEmployee(parseInt(id));
        return "administration";
    }

    //Sending comments "What did you do today"
    @RequestMapping(value = "/sendComment", method = RequestMethod.POST)
    public String getCommentary(@RequestParam String commentary, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
        persist.sendComment(parseInt(getEmployeeId(cookie)),commentary);//sendin the comment

        return "reports";
    }
    //our home page.
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainp(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        //check if logged in.
        String phone = convertToken(cookie);
        if (!isPhoneNumberExist(phone))
            return "Landing_page";
        try {
            model.addAttribute("admin",isAdmin(getEmployeeId(cookie)));

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
        boolean enterOrExit = false;
        if(rs.next()) {
            enterOrExit = rs.getBoolean("enterOrExit");
        }
        return enterOrExit;
    }

    @RequestMapping("/addWorkTime")
    public String addWorkTime(Model model, @RequestParam("button") int button, @CookieValue(value = SESSION, defaultValue = "") String cookie, @RequestParam("enterTime") Float enterTime) throws Exception {
        if (checkCookie(cookie)) {//לשנות לא צריך
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

    @RequestMapping("/request")
    public String req(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        model.addAttribute("admin",isAdmin(getEmployeeId(cookie)));
        String  days[]={ "יום ראשון", "יום שני" ,"יום שלישי" ,"יום רביעי" ,"יום חמישי" ,"יום שישי", "יום שבת"};
            ArrayList<String> reasons=new ArrayList<>();
            ArrayList<String> employeeId=new ArrayList<>();
            ArrayList<Date> date=new ArrayList<>();
            ArrayList<Float> hours=new ArrayList<Float>();
            ArrayList<String> names=new ArrayList<>();
            ArrayList<String> dayOfWeek=new ArrayList<>();
            ResultSet rs= persist.showRequests();
            Calendar cal = Calendar.getInstance();
            while(rs.next()){
                cal.setTime(rs.getDate("date"));
                int day = cal.get(Calendar.DAY_OF_WEEK);
                dayOfWeek.add(days[day-1]);
                names.add(rs.getString("employeeName"));
                hours.add(rs.getFloat("howmanyHours"));
                employeeId.add(rs.getString("employeeId"));
                date.add(rs.getDate("date"));
                reasons.add(rs.getString("reasonText"));
            }

            model.addAttribute("names",names);
            model.addAttribute("hours",hours);
            model.addAttribute("dayOfWeek",dayOfWeek);
            model.addAttribute("emplId",employeeId);
            model.addAttribute("reasonsList",reasons);
            model.addAttribute("dateList1",date);

      return "request";
    }

    @RequestMapping("/sendSms")
    public String sendSms(Model model, @RequestParam("login")String phone) throws Exception {
        ResultSet rs=persist.selectEmployeeByPhone(phone);
        String pass="";
        while (rs.next()){
            pass=rs.getString("employeePassword");
            sendSMS sms=new  sendSMS ();
            sms.sendSms(pass);
        }

        /*  NexmoClient client = new NexmoClient.Builder()
                .apiKey("6e230eed")
                .apiSecret("Cq0K3qn70SjHI2pi")
                .build();

        String messageText = "Hello from Nexmo";
        TextMessage message = new TextMessage("Nexmo", "972524704502", messageText);

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        for (SmsSubmissionResponseMessage responseMessage : response.getMessages()) {
            System.out.println(responseMessage);
        }*/
        return "Landing_page";
    }

    @RequestMapping("/forgotPass")
    public String forgotPass(Model model) throws Exception {

        return "forgotPass";
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
            StringBuilder workingTime= new StringBuilder();
            //counting how many hours the employee worked this day.
            while (rs2.next()) {
                workingTime.append("זמן עבודה: ").append(timeString(rs2.getFloat("enterTime"))).append("  ->   ").append(timeString(rs2.getFloat("exitTime"))).append("</br>");
                //adding to a list of working hours.
            }
            return workingTime.toString();
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
    //@RequestMapping("/header")
   // public String header(Model model,@CookieValue(value = SESSION, defaultValue = "")String cookie) throws Exception {
        //model.addAttribute("admin",isAdmin(getEmployeeId(cookie)));
       // return "header"  ;
   // }
    //this function is gettin the date throw js and returnning list with all the info about all the clickes in the same day.
    @ResponseBody
    @RequestMapping("/workTimeDetails")
    public ArrayList<ArrayList<String>> workTimeDetails(Model model, @RequestParam("date")String date, @CookieValue(value = SESSION, defaultValue = "")String cookie,@RequestParam(value ="id",defaultValue = "")String id) throws Exception{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = date;
        Date dateChoosed = formatter.parse(dateInString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateChoosed);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        ArrayList<String> dayListDetails = new ArrayList<String>();
        ArrayList<String> hoursWorkedDetails = new ArrayList<String>();
        ArrayList<String> hoursListDetails = new ArrayList<String>();
        ArrayList<String> dateListDetails = new ArrayList<>();
        ResultSet rs;
            rs=persist.selectWorktimeDay(year,month+1,parseInt(id),day);
        while (rs.next()) {
            dayListDetails.add(rs.getString("dayOfTheWeek"));
            hoursListDetails.add((int) (rs.getFloat("enterTime") / 60) + ":" + (int) (rs.getFloat("enterTime") % 60) + "  ->   " + (int) (rs.getFloat("exitTime") / 60) + ":" + (int) (rs.getFloat("exitTime") % 60));
            hoursWorkedDetails.add((int) (rs.getFloat("totalhoursWorked") / 60) + ":" + (int) (rs.getFloat("totalhoursWorked") % 60));
            dateListDetails.add(formatter.format(rs.getDate("date")));
        }
        ArrayList<ArrayList<String> > worktimeList = new ArrayList<ArrayList<String>>();
        worktimeList.add(dayListDetails);
        worktimeList.add(hoursWorkedDetails);
        worktimeList.add(hoursListDetails);
        worktimeList.add(dateListDetails);
        return  worktimeList;
    }

    @RequestMapping("/reports")
    public String reports(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie, @RequestParam(value = "month", defaultValue = "") String month, @RequestParam(value = "year", defaultValue = "") String year,@RequestParam(value = "id", defaultValue = "") String id) throws Exception {

        if (!checkCookie(cookie))
            return "Landing_page";
        try {

                model.addAttribute("admin",isAdmin(getEmployeeId(cookie)));
              if(!id.equals("")&&!month.equals("") && !year.equals("")) {
                  model.addAttribute("empId",id);
                  ResultSet rs = persist.selectWorkTimeMonth(parseInt(month),parseInt(year),parseInt(id));
                  ResultSet rsExitTime;
                  ResultSet rsEnterTime;
                  ResultSet rsSumTime;

                  ArrayList<String> dayList = new ArrayList<String>();
                  ArrayList<String> hoursWorked = new ArrayList<String>();
                  ArrayList<String> hoursList = new ArrayList<String>();
                  ArrayList<Date> dateList = new ArrayList<Date>();

                  for (int i = 1; i <= 31; i++) {
                      rsExitTime = persist.selectLastWorktimeDay(parseInt(year),parseInt(month),parseInt(id),i);//get the last worktime in a day.
                      rsEnterTime = persist.selectFirstWorktimeDay(parseInt(year),parseInt(month),parseInt(id),i);//get the first worktime in a day.;
                      rsSumTime = persist.totalHoursWorkedInDay(parseInt(year),parseInt(month),parseInt(id),i);//get the total time worked in a day.

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
              }
            else if (!month.equals("") && !year.equals("")) {
                model.addAttribute("empId",getEmployeeId(cookie));
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
             /*   ArrayList<String> dayListDetails = new ArrayList<String>();
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
*/
                //for the details

              // model.addAttribute("daysDetails",daysDetails);
              // model.addAttribute("hoursDetails", hoursDetails);
              // model.addAttribute("hoursWorkedDetails", hoursWorkedDetails);
              // model.addAttribute("dateWorkedDetails", dateWorkedDetails);
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
        model.addAttribute("admin",isAdmin(getEmployeeId(cookie)));

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
    private static boolean checkCredentials(String login, String pass) throws SQLException {
        PreparedStatement myStmt = dbConnection.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  and test2.employee.employeePassword=? ");
        try {
            myStmt.setInt(1, parseInt(login));
            myStmt.setString(2, pass);
        } catch (Exception exc) {
            return false;
        }
        ResultSet rs = myStmt.executeQuery();
        return rs.next() ;
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
        return result.next();
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
//Integer.valueOf(phone)
    private boolean isPhoneNumberExist (String phone) throws SQLException {
        PreparedStatement statement = dbConnection.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  ");
        statement.setInt(1, Integer.valueOf(phone));
        ResultSet result = statement.executeQuery();
        return result.next();
    }
    private boolean isAdmin(String id) throws SQLException {
        ResultSet result=persist.selectEmployee(parseInt(id));
        if(result.next())
        return result.getBoolean("isAdmin");
        else
            return false;
    }
}