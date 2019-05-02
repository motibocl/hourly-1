package com.elector.Controllers;


import com.elector.Objects.Entities.AdminObject;
import com.elector.Objects.Entities.CompanyObject;
import com.elector.Objects.Entities.EmployeeObject;
import com.elector.Persist;
import com.elector.Sms.model.Phone;
import com.elector.Utils.sendSMS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

//

@Controller
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.elector")
public class SampleController {
    private static Connection dbConnection;
    private static final String SECRET_KEY = "fgmdfgfdke34932HASDBAbsahdbsaBHbBHJBbhb";
    private static final String SESSION = "foo";
    //private static String jsonString="{id : \"\", month : \"\",year : \"\"}";
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
     public String getText(@RequestParam String text,@RequestParam String enterHours,@RequestParam String enterMinutes,@RequestParam String exitHours,@RequestParam String exitMinutes, @RequestParam String reasonDate, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
         float enterTime=parseInt(enterHours)*60+parseInt(enterMinutes);
         float exitTime=parseInt(exitHours)*60+parseInt(exitMinutes);

         persist.sendReason(getEmployeeId(cookie), text, reasonDate,enterTime,exitTime);//adding reason to dataBase
         return "redirect:/main";
     }

    @RequestMapping("/add-employee")
    public String addEmployee(@RequestParam(value = "id", defaultValue = "") String id,@RequestParam(value = "name", defaultValue = "") String name,@RequestParam(value = "empPhone", defaultValue = "") String empPhone,@RequestParam(value = "password", defaultValue = "") String password)  throws SQLException {
        CompanyObject companyObject= persist.loadObject(CompanyObject.class, 1);
        EmployeeObject employeeObject=new EmployeeObject();
        employeeObject.setId(parseInt(id));
        employeeObject.setCompanyObject(companyObject);
        employeeObject.setName(name);
        employeeObject.setPassword(password);
        employeeObject.setPhone(empPhone);
        persist.save(employeeObject);



        // persist.addEmployee(parseInt(id),name,parseInt(empPhone),password);

        return "redirect:/administration";
    }
    @RequestMapping("/confirmAndAdd")
    public String confirmAndAdd(@RequestParam (value = "emplid", defaultValue = "")int emplid,@RequestParam(value = "enterTime", defaultValue = "")Float enterTime,@RequestParam(value = "exitTime", defaultValue = "")Float exitTime,@RequestParam(value = "date", defaultValue = "")String date,@RequestParam(value = "day", defaultValue = "")String day,@RequestParam(value = "reason", defaultValue = "")String comment) throws SQLException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = date;
        Date dateChoosed = formatter.parse(dateInString);
        java.sql.Date sqldate = new java.sql.Date(dateChoosed.getTime());
        persist.addAfterConfirm(emplid,enterTime,exitTime,sqldate,day,comment);

        return "request";
    }
/*--------------------------------------------------------------------------------------------------------------------------------------*/
@RequestMapping(value = "test" , method = RequestMethod.POST)
public @ResponseBody ResponseEntity test(@RequestBody String jsonString) {
    System.out.print(jsonString);
   // JSONObject json1=new JSONObject(jsonString);
    JSONObject json=new JSONObject();
   // System.out.print(json1.getString("name"));
    json.put("name","moti");
    json.put("age",25);

    return new ResponseEntity<String>(json.toString(), HttpStatus.OK);

}




 /*--------------------------------------------------------------------------------------------------------------------------------------*/
    @RequestMapping("/remove-employee")
    public String removeEmployee(@RequestParam(value = "id", defaultValue = "") String id)  throws SQLException {
        persist.removeEmployee(parseInt(id));
        return "redirect:/administration";
    }

    //Sending comments "What did you do today"
    @RequestMapping(value = "/sendComment", method = RequestMethod.POST)
    public String getCommentary(@RequestParam String commentary, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
        persist.sendComment(getEmployeeId(cookie),commentary);//sendin the comment

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
            AdminObject adminObject=persist.getAdminByPhone(phone);
          //  String name = "";
            //ResultSet result = persist.selectEmployeeByPhone(phone);//getting employee by phone.
            EmployeeObject employeeObject=persist. getEmployeeByPhone(phone);

            //  while (result.next()) {
           //     name = result.getString("employeeName");
          //  }
            if (adminObject!=null)
                model.addAttribute("name", "ברוך הבא  " + adminObject.getName());//the welcome back page title.
            else
            model.addAttribute("name", "ברוך הבא  " + employeeObject.getName());//the welcome back page title.
            //getting all the working time list
            if (employeeObject!=null) {
            ResultSet rs2 = persist.selectWorkTimeByDay(getEmployeeId(cookie),today);
            float workedToday = 0;
            ArrayList<String> timeWorkList = new ArrayList<String>();
            //counting how many hours the employee worked this day.
            while (rs2.next()) {
                workedToday += rs2.getFloat("totalhoursWorked");
                timeWorkList.add("זמן עבודה: " + timeString(rs2.getFloat("enterTime")) + "  ->   " + timeString(rs2.getFloat("exitTime")));
                //adding to a list of working hours.
            }
            //ResultSet rs = persist.selectEmployeeById(parseInt(getEmployeeId(cookie)));
         //   boolean clicked = false;
           // while (rs.next()) {

                boolean clicked = employeeObject.isEnterOrExit();
                //}
                //change button image if clicked or not.
                if (!clicked) {
                    model.addAttribute("url", "css/images/enter-button2.png");
                    // model.addAttribute("id", "0");
                } else {
                    model.addAttribute("url", "css/images/exit-button.png");
                    //  model.addAttribute("id", "1");
                    if (timeWorkList.size() > 0)
                        timeWorkList.remove(timeWorkList.size() - 1);

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
                ResultSet rs3 = persist.selectWorkTimeMonth(todayMonth + 1, todayYear, getEmployeeId(cookie));//getting resultset for all worked time in a month.
                float workedThisMonth = 0;//count the month working time.
                while (rs3.next()) {
                    workedThisMonth += rs3.getFloat("totalhoursWorked");
                }
                //the string .
                String timeMonth = "הזמן שעבדת החודש: " + timeString(workedThisMonth);
                model.addAttribute("timeWorkMonth", timeMonth);

            }
            return "main";
        } catch (Exception exc) {
            return "error";
        }
    }
    @ResponseBody
    @RequestMapping("/buttonStatus")
    public  boolean button(@CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
        EmployeeObject employeeObject=persist. getEmployeeById(getEmployeeId(cookie));

       // ResultSet rs =  persist.selectEmployeeById(parseInt(getEmployeeId(cookie)));//getting buton status
       // boolean enterOrExit = false;
       // if(rs.next()) {
        boolean  enterOrExit = employeeObject.isEnterOrExit();
       // }
        return enterOrExit;
    }
    @RequestMapping("/addWorkTime")
    public String addWorkTime(Model model, @RequestParam("button") boolean button, @CookieValue(value = SESSION, defaultValue = "") String cookie, @RequestParam("enterTime") Float enterTime) throws Exception {
        if (checkCookie(cookie)) {//לשנות לא צריך
            EmployeeObject employeeObject=persist. getEmployeeById(getEmployeeId(cookie));
            employeeObject.setEnterOrExit(button);
            persist.save(employeeObject);//for saving updates.
            //  persist.updateButtonStatus(button,parseInt(getEmployeeId(cookie)));
            Date day = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(day);
            String[] dayOfTheWeek = {"יום א", "יום ב", "יום ג", "יום ד", "יום ה", "יום ו", "יום ז"};
            persist.addWorktime(getEmployeeId(cookie),enterTime,today,dayOfTheWeek[c.get(Calendar.DAY_OF_WEEK) - 1]);
            model.addAttribute("url", "css/images/exit-button.png");

             return "redirect:/main";
        }
        return "Landing_page";
    }

    @RequestMapping("/request")
    public String req(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        model.addAttribute("admin",isAdmin(getEmployeeId(cookie)));
        String  days[]={ "יום א", "יום ב" ,"יום ג" ,"יום ד" ,"יום ה" ,"יום ו", "יום ז"};
        ArrayList<String> reasons=new ArrayList<>();
        ArrayList<String> employeeId=new ArrayList<>();
        ArrayList<Date> date=new ArrayList<>();
        ArrayList<Float> hours=new ArrayList<Float>();
        ArrayList<String> names=new ArrayList<>();
        ArrayList<String> dayOfWeek=new ArrayList<>();
        ArrayList<String> enterTime=new ArrayList<>();
        ArrayList<String> exitTime=new ArrayList<>();
        ResultSet rs= persist.showRequests();
        Calendar cal = Calendar.getInstance();
        int hour,minutes;
        while(rs.next()){
            hour=(int)(rs.getFloat("exitTime"))/60;
            minutes=(int)rs.getFloat("exitTime")%60;
            if (minutes < 10)
                exitTime.add(hour+ ":" + "0" + minutes);
            else
                exitTime.add(hour+ ":" + minutes);
            hour=(int)(rs.getFloat("enterTime"))/60;
            minutes=(int)rs.getFloat("enterTime")%60;
            if (minutes < 10)
                enterTime.add(hour+ ":" + "0" + minutes);
            else
                enterTime.add(hour+ ":" + minutes);
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
        model.addAttribute("enterTime",enterTime);
        model.addAttribute("exitTime",exitTime);
        model.addAttribute("hours",hours);
        model.addAttribute("dayOfWeek",dayOfWeek);
        model.addAttribute("emplId",employeeId);
        model.addAttribute("reasonsList",reasons);
        model.addAttribute("dateList1",date);

        return "request";
    }
    @RequestMapping("/sendSms")
    public String sendSms(Model model, @RequestParam("login")String phone) throws Exception {
        EmployeeObject employeeObject=persist. getEmployeeByPhone(phone);
       if(employeeObject!=null){
            sendSMS sms=new  sendSMS ();
            sms.sendSms(employeeObject.getPassword());
        }
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
    public String updateWorkTime(Model model, @RequestParam("button") boolean button, @RequestParam("exitTime") float exitTime, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        if (checkCookie(cookie)) {
            EmployeeObject employeeObject=persist. getEmployeeById(getEmployeeId(cookie));

            float exit = exitTime;
            float total = exitTime - enterTime(cookie);
            persist.updateWorktime(exit,total,getEmployeeId(cookie));//updating total and exitTime.
            employeeObject.setEnterOrExit(button);
            persist.save(employeeObject);
            // persist.updateButtonStatus(button,parseInt(getEmployeeId(cookie)));//updating button status.
            ResultSet rs2 = persist.selectLastWorktime(getEmployeeId(cookie),today);//getting the last working time registered in the db by the user.
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
            hoursListDetails.add(timeString(rs.getFloat("enterTime"))+ "  ->   " + timeString (rs.getFloat("exitTime")));
            hoursWorkedDetails.add(timeString(rs.getFloat("totalhoursWorked")));
            dateListDetails.add(formatter.format(rs.getDate("date")));
        }
        ArrayList<ArrayList<String> > worktimeList = new ArrayList<ArrayList<String>>();
        worktimeList.add(dayListDetails);
        worktimeList.add(hoursWorkedDetails);
        worktimeList.add(hoursListDetails);
        worktimeList.add(dateListDetails);
        return  worktimeList;

    }
    //@RequestParam (value = "jsonString", defaultValue = "{id : \"\", month : \"\",year : \"\"}") String jsonString
//,@RequestParam(value = "month", defaultValue = "") String month, @RequestParam(value = "year", defaultValue = "") String year,@RequestParam(value = "id", defaultValue = "") String id
    @RequestMapping("/reports")
    public String reports(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie,@RequestParam(value = "month", defaultValue = "") String month, @RequestParam(value = "year", defaultValue = "") String year,@RequestParam(value = "id", defaultValue = "") String id
    ) throws Exception {

        if (!checkCookie(cookie))
            return "Landing_page";

        try {
           // EmployeeObject employeeObject = persist.loadObject(EmployeeObject.class, 1);;
           // List<EmployeeObject> employeeObjectList = persist.loadList(EmployeeObject.class);

            //JSONObject json=new JSONObject(jsonString);
           // String month=json.getString("month");
           //String year=json.getString("year");
          //  String id=json.getString("id");

            model.addAttribute("employeeName","");

            model.addAttribute("admin",isAdmin(getEmployeeId(cookie)));
            EmployeeObject employeeObject=persist. getEmployeeById(getEmployeeId(cookie));

            if(!id.equals("")&&!month.equals("") && !year.equals("")) {
                model.addAttribute("empId",id);
                //ResultSet result=persist.selectEmployeeById(parseInt(id));
                //String employeeName="";//GET THE NAME OF THE EMPLOYEE.
               // while (result.next()){
                   String employeeName=employeeObject.getName();
               // }
                model.addAttribute("employeeName","פירוט החודש של "+employeeName);
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
                ResultSet rs = persist.selectWorkTimeMonth(parseInt(month),parseInt(year),getEmployeeId(cookie));
                ResultSet rsExitTime;
                ResultSet rsEnterTime;
                ResultSet rsSumTime;

                ArrayList<String> dayList = new ArrayList<String>();
                ArrayList<String> hoursWorked = new ArrayList<String>();
                ArrayList<String> hoursList = new ArrayList<String>();
                ArrayList<Date> dateList = new ArrayList<Date>();

                for (int i = 1; i <= 31; i++) {
                    rsExitTime = persist.selectLastWorktimeDay(parseInt(year),parseInt(month),getEmployeeId(cookie),i);//get the last worktime in a day.
                    rsEnterTime = persist.selectFirstWorktimeDay(parseInt(year),parseInt(month),getEmployeeId(cookie),i);//get the first worktime in a day.;
                    rsSumTime = persist.totalHoursWorkedInDay(parseInt(year),parseInt(month),getEmployeeId(cookie),i);//get the total time worked in a day.

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
    private boolean checkCredentials(String phone, String pass) throws SQLException {
        EmployeeObject employeeObject=persist.getEmployeeByPhone(phone);
        AdminObject adminObject=persist.getAdminByPhone(phone);
        if (adminObject!=null&&adminObject.getPassword().equals(pass))
            return true;
        if (employeeObject!=null&&employeeObject.getPassword().equals(pass))
            return true;
        return false;
       // PreparedStatement myStmt = dbConnection.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  and test2.employee.employeePassword=? ");
       // try {

          //  myStmt.setInt(1, parseInt(phone));
         //   myStmt.setString(2, pass);
       // } catch (Exception exc) {
          //  return false;
       // }
      //  ResultSet rs = myStmt.executeQuery();
      //  return rs.next() ;
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

    private int getEmployeeId(String phone) throws SQLException {
        String employeePhone = convertToken(phone);
        int id = 0;
        EmployeeObject employeeObject=persist.getEmployeeByPhone(employeePhone);
        AdminObject adminObject=persist.getAdminByPhone(employeePhone);
        if (adminObject!=null)
           id=adminObject.getId();
        if (employeeObject!=null)
            id=employeeObject.getId();
       // String employeePhone = convertToken(phone);
        //PreparedStatement statement = dbConnection.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  ");
       // statement.setInt(1, parseInt(employeePhone));
       // ResultSet result = statement.executeQuery();
       // while (result.next()) {
          //  id = result.getString("employeeId");
            //name=result.getString("employeeName");
       // }
        return id;
    }

    private int enterTime(String phone) throws SQLException {
        int timeEnter = 0;
        PreparedStatement statement = dbConnection.prepareStatement("select * from test2.worktime WHERE  id=? order by timeId desc limit 1; ");
        statement.setInt(1, getEmployeeId(phone));
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            timeEnter = (int) result.getFloat("enterTime");
            //name=result.getString("employeeName");
        }
        return timeEnter;
    }
//Integer.valueOf(phone)
    private boolean isPhoneNumberExist (String phone) throws SQLException {
        EmployeeObject employeeObject=persist.getEmployeeByPhone(phone);
        AdminObject adminObject=persist.getAdminByPhone(phone);
        if (adminObject!=null)
            return true;
        if (employeeObject!=null)
            return true;
        return false;

        // PreparedStatement statement = dbConnection.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  ");
        //statement.setInt(1, Integer.valueOf(phone));
        //ResultSet result = statement.executeQuery();
       // return result.next();
    }
    private boolean isAdmin(int id) throws SQLException {
        AdminObject adminObject=persist.getAdminById(id);
       // ResultSet result=persist.selectEmployee(parseInt(id));
        if(adminObject!=null)
        return true;
        else
            return false;
    }
}