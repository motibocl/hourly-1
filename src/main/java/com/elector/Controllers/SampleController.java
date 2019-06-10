package com.elector.Controllers;


import com.elector.Objects.Entities.*;
import com.elector.Persist;
//import com.elector.Sms.model.Phone;
import com.elector.Utils.sendSMS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
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
        dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "tuRgmhuI1");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String getdata(@RequestParam String login, @RequestParam String pass, HttpServletResponse response) throws SQLException {
        if (checkCredentials(login, pass)) {//if pass and phone correct
            response.addCookie(new Cookie(SESSION, generateToken(login)));//making a cookie
            if(isAdmin(login))
                return "redirect:/adminMain";
            return "redirect:/main";
        } else
            return "Landing_page";
    }

    @RequestMapping("/administration")
    public String admin(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
        String phone = convertToken(cookie);
        if (!isPhoneNumberExist(phone))
            return "Landing_page";
        List<EmployeeObject>employeeObjectList=persist.loadList(EmployeeObject.class);
        ArrayList<Integer>id=new ArrayList<Integer>() ;
        ArrayList<String>name=new ArrayList<String>() ;
        ArrayList<String>password=new ArrayList<String>() ;
        ArrayList<String>phoneNum=new ArrayList<String>() ;
        for (int i=0;i<employeeObjectList.size();i++){
            id.add(employeeObjectList.get(i).getId());
            name.add(employeeObjectList.get(i).getName());
            password.add(employeeObjectList.get(i).getPassword());
            phoneNum.add(employeeObjectList.get(i).getPhone());
        }
        model.addAttribute("id",id);
        model.addAttribute("name",name);
        model.addAttribute("password",password);
        model.addAttribute("phone",phoneNum);

        return "administration";

    }
     //adding reason if the employee forgot to press the enter and exit button.
     @RequestMapping(value = "/addReason", method = RequestMethod.GET)
     public String getText(@RequestParam String text,@RequestParam String enterHours,@RequestParam String enterMinutes,@RequestParam String exitHours,@RequestParam String exitMinutes, @RequestParam String reasonDate, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException, ParseException {
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         String dateInString = reasonDate;
         Date dateChoosed = formatter.parse(dateInString);
         java.sql.Date sqldate = new java.sql.Date(dateChoosed.getTime());

         float enterTime=parseInt(enterHours)*60+parseInt(enterMinutes);
         float exitTime=parseInt(exitHours)*60+parseInt(exitMinutes);
         EmployeeObject employeeObject=persist.getEmployeeById(getEmployeeId(cookie));
         ReasonObject reasonObject=new ReasonObject(exitTime,enterTime,text,sqldate,employeeObject,(int)(exitTime-enterTime));
         persist.save(reasonObject);
         //persist.sendReason(getEmployeeId(cookie), text, reasonDate,enterTime,exitTime);//adding reason to dataBase
         return "redirect:/main";
     }

    @RequestMapping("/add-employee")
    public @ResponseBody ResponseEntity addEmployee(@RequestParam(value = "id", defaultValue = "") String id,@RequestParam(value = "name", defaultValue = "") String name,@RequestParam(value = "phone", defaultValue = "") String phone,@RequestParam(value = "password", defaultValue = "") String password)  throws SQLException {
        CompanyObject companyObject= persist.loadObject(CompanyObject.class, 1);
        EmployeeObject employeeObject=new EmployeeObject();
        employeeObject.setId(parseInt(id));
        employeeObject.setCompanyObject(companyObject);
        employeeObject.setName(name);
        employeeObject.setPassword(password);
        employeeObject.setPhone(phone);
        employeeObject.setEnterOrExit(false);
        persist.save(employeeObject);
        JSONObject json=new JSONObject();
        json.put("name",name);
        json.put("id",id);
        json.put("password",password);
        json.put("phone",phone);




        // persist.addEmployee(parseInt(id),name,parseInt(empPhone),password);

        //return "redirect:/administration";
        return new ResponseEntity<String>(json.toString(), HttpStatus.OK);

    }
    //this function returning all the ids an year numbers that are in the db.
    @RequestMapping(value = "/idNumbers", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity idNumbers() throws SQLException {
        JSONObject json=new JSONObject();
        List<EmployeeObject> employeeObject=persist.loadList(EmployeeObject.class);
        int[] ids=new int[employeeObject.size()];
        for (int i=0;i<employeeObject.size();i++)
            ids[i] = employeeObject.get(i).getId();



        json.put("idArray",ids);
        return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    }
    @RequestMapping("/confirmAndAdd")
    public String confirmAndAdd(@RequestParam (value = "emplid", defaultValue = "")int emplid,@RequestParam(value = "enterTime", defaultValue = "")Float enterTime,@RequestParam(value = "exitTime", defaultValue = "")Float exitTime,@RequestParam(value = "date", defaultValue = "")String date,@RequestParam(value = "day", defaultValue = "")String day,@RequestParam(value = "reason", defaultValue = "")String comment) throws SQLException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = date;
        Date dateChoosed = formatter.parse(dateInString);
        java.sql.Date sqldate = new java.sql.Date(dateChoosed.getTime());
        //persist.addAfterConfirm(emplid,enterTime,exitTime,sqldate,day,comment);
        EmployeeObject employeeObject=persist.getEmployeeById(emplid);
        WorktimeObject worktimeObject=new WorktimeObject( enterTime, exitTime,employeeObject, sqldate, exitTime-enterTime, day,comment);
        persist.save(worktimeObject);
        List<ReasonObject> reasonObjectList = persist.loadList(ReasonObject.class);
        for (int i = 0; i < reasonObjectList.size(); i++) {//changing the status of the reason
            if (reasonObjectList.get(i).getEmployeeObject().getId() == emplid && reasonObjectList.get(i).getEnterTime() == enterTime && reasonObjectList.get(i).getExitTime() == exitTime && reasonObjectList.get(i).getDate().equals(sqldate)) {
                reasonObjectList.get(i).setAcceptOrNot(1);//1=accepted
                persist.save(reasonObjectList.get(i));
            }
        }
        return "requests";
    }
    @RequestMapping("/removeReason")
    public String removeReason(@RequestParam(value = "emplid", defaultValue = "") int emplid, @RequestParam(value = "enterTime", defaultValue = "") Float enterTime, @RequestParam(value = "exitTime", defaultValue = "") Float exitTime, @RequestParam(value = "date", defaultValue = "") String date, @RequestParam(value = "reason", defaultValue = "") String comment) throws SQLException, ParseException {        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = date;
        Date dateChoosed = formatter.parse(dateInString);
        java.sql.Date sqldate = new java.sql.Date(dateChoosed.getTime());
        List<ReasonObject> reasonObjectList = persist.loadList(ReasonObject.class);
        for (int i = 0; i < reasonObjectList.size(); i++) {//changing the status of the reason
            if (reasonObjectList.get(i).getEmployeeObject().getId() == emplid && reasonObjectList.get(i).getEnterTime() == enterTime && reasonObjectList.get(i).getExitTime() == exitTime && reasonObjectList.get(i).getDate().equals(sqldate)) {
                reasonObjectList.get(i).setAcceptOrNot(2);//2=Declined
                persist.save(reasonObjectList.get(i));
            }
        }
        //persist.removeReason(emplid,sqldate,comment);
        return "requests";
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
    @RequestMapping("/register")
    public String register(Model model,@RequestParam String adminName,@RequestParam String companyName,@RequestParam String adminId,@RequestParam String password,@RequestParam String companyAdress,@RequestParam String companyId,@RequestParam String phone,@RequestParam String email)  throws SQLException {
        CompanyObject companyObject=new CompanyObject(parseInt(companyId),companyName,companyAdress);
        persist.save(companyObject);
        AdminObject adminObject=new AdminObject(adminName,password,email,parseInt(adminId),phone,companyObject);
        persist.save(adminObject);


        return "Landing_page";
    }
    @RequestMapping("/registration")
    public String registration(Model model)  throws SQLException {
        return "registration";
    }

    //Sending comments "What did you do today"
    @RequestMapping(value = "/sendComment", method = RequestMethod.POST)
    public String getCommentary(@RequestParam String comment, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws SQLException {
       // persist.sendComment(getEmployeeId(cookie),commentary);//sendin the comment
        WorktimeObject worktimeObject=persist.getLastWorktime(getEmployeeId(cookie));
        worktimeObject.setComment(comment);
        persist.save(worktimeObject);
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
            model.addAttribute("admin",isAdmin(phone));
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
           // ResultSet rs2 = persist.selectWorkTimeByDay(getEmployeeId(cookie),today);
                List<WorktimeObject> worktimeObjectList = persist.getWorkTimeByDay(getEmployeeId(cookie),today);
                float workedToday = 0;
                ArrayList<String> timeWorkList = new ArrayList<String>();
            //counting how many hours the employee worked this day.
            for (int i=0;i<worktimeObjectList.size();i++){
                workedToday += worktimeObjectList.get(i).getTotalHoursWorked();
                timeWorkList.add("זמן עבודה: " + timeString(worktimeObjectList.get(i).getEnterTime()) + "  ->   " + timeString(worktimeObjectList.get(i).getExitTime()));
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
               // ResultSet rs3 = persist.selectWorkTimeMonth(todayMonth + 1, todayYear, getEmployeeId(cookie));//getting resultset for all worked time in a month.
                 worktimeObjectList = persist.getWorkTimeMonth(todayMonth + 1,todayYear,getEmployeeId(cookie));

                float workedThisMonth = 0;//count the month working time.
                for (int i=0;i<worktimeObjectList.size();i++)
                    workedThisMonth+=worktimeObjectList.get(i).getTotalHoursWorked();
               /* while (rs3.next()) {
                    workedThisMonth += rs3.getFloat("totalhoursWorked");
                }*/
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
        EmployeeObject employeeObject=persist.getEmployeeById(getEmployeeId(cookie));

       // ResultSet rs =  persist.selectEmployeeById(parseInt(getEmployeeId(cookie)));//getting buton status
       // boolean enterOrExit = false;
       // if(rs.next()) {
        boolean  enterOrExit = employeeObject.isEnterOrExit();
       // }
        return enterOrExit;
    }
    @RequestMapping("/addWorkTime")
    public String addWorkTime(Model model, @RequestParam("button") boolean button, @CookieValue(value = SESSION, defaultValue = "") String cookie, @RequestParam("enterTime") Float enterTime) throws Exception {
        //לשנות לא צריך
            EmployeeObject employeeObject=persist.getEmployeeById(getEmployeeId(cookie));
            employeeObject.setEnterOrExit(button);
            persist.save(employeeObject);//for saving updates.
            //  persist.updateButtonStatus(button,parseInt(getEmployeeId(cookie)));
            Date days = new Date();
            java.sql.Date day=new java.sql.Date(days.getTime());
            Calendar c = Calendar.getInstance();
            c.setTime(day);
            String[] dayOfTheWeek = {"יום א", "יום ב", "יום ג", "יום ד", "יום ה", "יום ו", "יום ז"};
            WorktimeObject worktimeObject=new WorktimeObject(enterTime,0,employeeObject,day,0,dayOfTheWeek[c.get(Calendar.DAY_OF_WEEK) - 1],"");           // persist.addWorktime(getEmployeeId(cookie),enterTime,today,dayOfTheWeek[c.get(Calendar.DAY_OF_WEEK) - 1]);
            persist.save(worktimeObject);
            model.addAttribute("url", "css/images/exit-button.png");

             return "redirect:/main";

    }


    @RequestMapping("/requests")
    public String req(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        String phone = convertToken(cookie);

        model.addAttribute("admin", isAdmin(phone));
        String days[] = {"יום א", "יום ב", "יום ג", "יום ד", "יום ה", "יום ו", "יום ז"};
      /*  ArrayList<String> reasons=new ArrayList<>();
        ArrayList<Integer> employeeId=new ArrayList<Integer>();
        ArrayList<Date> date=new ArrayList<>();
        ArrayList<Integer> hours=new ArrayList<Integer>();
        ArrayList<String> names=new ArrayList<>();*/
        ArrayList<String> dayOfWeek0 = new ArrayList<>();
        ArrayList<String> dayOfWeek1 = new ArrayList<>();
        ArrayList<String> dayOfWeek2 = new ArrayList<>();
        ArrayList<String> enterTime0=new ArrayList<>();
        ArrayList<String> enterTime1=new ArrayList<>();
        ArrayList<String> enterTime2=new ArrayList<>();
        ArrayList<String> exitTime0=new ArrayList<>();
        ArrayList<String> exitTime1=new ArrayList<>();
        ArrayList<String> exitTime2=new ArrayList<>();
       /* ArrayList<String> reasonsAccepted=new ArrayList<>();
        ArrayList<Integer> employeeIdAccepted=new ArrayList<Integer>();
        ArrayList<Date> dateAccepted=new ArrayList<>();
        ArrayList<Integer> hoursAccepted=new ArrayList<Integer>();
        ArrayList<String> namesAccepted=new ArrayList<>();
        ArrayList<String> dayOfWeekAccepted=new ArrayList<>();
        ArrayList<String> enterTimeAccepted=new ArrayList<>();
        ArrayList<String> exitTimeAccepted=new ArrayList<>();
        ArrayList<String> reasonsDeclined=new ArrayList<>();
        ArrayList<Integer> employeeIdDeclined=new ArrayList<Integer>();
        ArrayList<Date> dateDeclined=new ArrayList<>();
        ArrayList<Integer> hoursDeclined=new ArrayList<Integer>();
        ArrayList<String> namesDeclined=new ArrayList<>();
        ArrayList<String> dayOfWeekDeclined=new ArrayList<>();
        ArrayList<String> enterTimeDeclined=new ArrayList<>();
        ArrayList<String> exitTimeDeclined=new ArrayList<>();*/
        //  List<ReasonObject> reasonObjectList=persist.loadList(ReasonObject.class);
        List<ReasonObject> reasonsAccepted = persist.reasonsAccepted();
        List<ReasonObject> reasonsDeclined = persist.reasonsDeclined();
        List<ReasonObject> reasonsNotRead = persist.reasonsNotRead();
        Calendar cal = Calendar.getInstance();
        int day;
        float exit,enter;
        for (int i = 0; i < reasonsAccepted.size(); i++) {
            cal.setTime(reasonsAccepted.get(i).getDate());
            day = cal.get(Calendar.DAY_OF_WEEK);
            dayOfWeek1.add(days[day - 1]);
            enter=reasonsAccepted.get(i).getEnterTime();
            exit=reasonsAccepted.get(i).getExitTime();
            enterTime1.add(changeMinutesToHours(enter));
            exitTime1.add(changeMinutesToHours(exit));

        }
        for (int i = 0; i < reasonsDeclined.size(); i++) {
            cal.setTime(reasonsDeclined.get(i).getDate());
            day = cal.get(Calendar.DAY_OF_WEEK);
            dayOfWeek2.add(days[day - 1]);
            enter=reasonsDeclined.get(i).getEnterTime();
            exit=reasonsDeclined.get(i).getExitTime();
            enterTime2.add(changeMinutesToHours(enter));
            exitTime2.add(changeMinutesToHours(exit));


        }
        for (int i = 0; i < reasonsNotRead.size(); i++) {
            cal.setTime(reasonsNotRead.get(i).getDate());
            day = cal.get(Calendar.DAY_OF_WEEK);
            dayOfWeek0.add(days[day - 1]);
            enter=reasonsNotRead.get(i).getEnterTime();
            exit=reasonsNotRead.get(i).getExitTime();
            enterTime0.add(changeMinutesToHours(enter));
            exitTime0.add(changeMinutesToHours(exit));

        }
        model.addAttribute("reasonsAccepted", reasonsAccepted);
        model.addAttribute("reasonsDeclined", reasonsDeclined);
        model.addAttribute("reasonsNotRead", reasonsNotRead);
        model.addAttribute("dayOfWeek0", dayOfWeek0);
        model.addAttribute("dayOfWeek1", dayOfWeek1);
        model.addAttribute("dayOfWeek2", dayOfWeek2);
        model.addAttribute("exitTime0",exitTime0);
        model.addAttribute("exitTime1",exitTime1);
        model.addAttribute("exitTime2",exitTime2);
        model.addAttribute("enterTime0",enterTime0);
        model.addAttribute("enterTime1",enterTime1);
        model.addAttribute("enterTime2",enterTime2);

       /* Calendar cal = Calendar.getInstance();
        int hour,minutes;
        for (int i=0;i<reasonObjectList.size();i++) {
            hour = (int) (reasonObjectList.get(i).getExitTime()) / 60;
            minutes = (int) (reasonObjectList.get(i).getExitTime()) % 60;
            if (reasonObjectList.get(i).getAcceptOrNot() == 0) {//if not read
               reasonLists(days,cal,i,hour,minutes,reasonObjectList,reasons,employeeId,date,hours,names,dayOfWeek,enterTime,exitTime);
            }
            else if (reasonObjectList.get(i).getAcceptOrNot() == 1) {//if accepted
                reasonLists(days,cal,i,hour,minutes,reasonObjectList,reasonsAccepted,employeeIdAccepted,dateAccepted,hoursAccepted,namesAccepted,dayOfWeekAccepted,enterTimeAccepted,exitTimeAccepted);

            }
            else if (reasonObjectList.get(i).getAcceptOrNot() == 2) {//if declined
                reasonLists(days,cal,i,hour,minutes,reasonObjectList,reasonsDeclined,employeeIdDeclined,dateDeclined,hoursDeclined,namesDeclined,dayOfWeekDeclined,enterTimeDeclined,exitTimeDeclined);

            }
        }
        model.addAttribute("names",names);
        model.addAttribute("enterTime",enterTime);
        model.addAttribute("exitTime",exitTime);
        model.addAttribute("hours",hours);
        model.addAttribute("dayOfWeek",dayOfWeek);
        model.addAttribute("emplId",employeeId);
        model.addAttribute("reasonsList",reasons);
        model.addAttribute("dateList1",date);
        model.addAttribute("namesAccepted",namesAccepted);
        model.addAttribute("enterTimeAccepted",enterTimeAccepted);
        model.addAttribute("exitTimeAccepted",exitTimeAccepted);
        model.addAttribute("hoursAccepted",hoursAccepted);
        model.addAttribute("dayOfWeekAccepted",dayOfWeekAccepted);
        model.addAttribute("emplIdAccepted",employeeIdAccepted);
        model.addAttribute("reasonsListAccepted",reasonsAccepted);
        model.addAttribute("dateList1Accepted",dateAccepted);
        model.addAttribute("namesDeclined",namesDeclined);
        model.addAttribute("enterTimeDeclined",enterTimeDeclined);
        model.addAttribute("exitTimeDeclined",exitTimeDeclined);
        model.addAttribute("hoursDeclined",hoursDeclined);
        model.addAttribute("dayOfWeekDeclined",dayOfWeekDeclined);
        model.addAttribute("emplIdDeclined",employeeIdDeclined);
        model.addAttribute("reasonsListDeclined",reasonsDeclined);
        model.addAttribute("dateList1Declined",dateDeclined);*/

        return "requests";
    }

    @RequestMapping("/sendSms")
    public String sendSms(Model model, @RequestParam("phone") String phone) throws Exception {
        EmployeeObject employeeObject = persist.getEmployeeByPhone(phone);
        if (employeeObject != null) {
            sendSMS sms = new sendSMS();
            sms.sendSms(employeeObject.getPassword());
        }
        return "Landing_page";
    }

    @RequestMapping("/forgotPass")
    public String forgotPass(Model model) throws Exception {

        return "forgotPass";
    }


    @RequestMapping("/")
    public String loginPage(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        String phone=convertToken(cookie);
        if(isPhoneNumberExist(phone)){
            if(isAdmin(phone))
                return "redirect:/adminMain";
            return "redirect:/main";
        }
        else
        return "Landing_page";
    }


    @ResponseBody
    @RequestMapping("/updateWorkTime")
    public String updateWorkTime(Model model, @RequestParam("button") boolean button, @RequestParam("exitTime") float exitTime, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        EmployeeObject employeeObject = persist.getEmployeeById(getEmployeeId(cookie));
        // float exit = exitTime;
        WorktimeObject worktimeObject = persist.getLastWorktime(employeeObject.getId());
        float total = exitTime - worktimeObject.getEnterTime();
        // WorktimeObject worktimeObject=persist.getWorktime(employeeObject.getId());
        worktimeObject.setExitTime(exitTime);
        worktimeObject.setTotalHoursWorked(total);
        persist.save(worktimeObject);
        // persist.updateWorktime(exit,total,getEmployeeId(cookie));//updating total and exitTime.
        employeeObject.setEnterOrExit(button);
        persist.save(employeeObject);
        // persist.updateButtonStatus(button,parseInt(getEmployeeId(cookie)));//updating button status.
        // ResultSet rs2 = persist.selectLastWorktime(getEmployeeId(cookie),today);//getting the last working time registered in the db by the user.
        float workedToday = 0;
        StringBuilder workingTime = new StringBuilder();
        //counting how many hours the employee worked this day.
        //  while (rs2.next()) {
        workingTime.append("זמן עבודה: ").append(timeString(worktimeObject.getEnterTime())).append("  ->   ").append(timeString(worktimeObject.getExitTime())).append("</br>");
        //adding to a list of working hours.
        // }
        return workingTime.toString();

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
        //ResultSet rs;
        List<WorktimeObject>worktimeObjects=persist.getWorkTimeByDay(parseInt(id),dateChoosed);
           // rs=persist.selectWorktimeDay(year,month+1,parseInt(id),day);
       // while (rs.next()) {
        for (int i=0;i<worktimeObjects.size();i++){
            dayListDetails.add(worktimeObjects.get(i).getDayOfTheWeek());
            hoursListDetails.add(timeString(worktimeObjects.get(i).getEnterTime())+ "  ->   " + timeString (worktimeObjects.get(i).getExitTime()));
            hoursWorkedDetails.add(timeString(worktimeObjects.get(i).getTotalHoursWorked()));
            dateListDetails.add(formatter.format(worktimeObjects.get(i).getDate()));
        }
        ArrayList<ArrayList<String>> worktimeList = new ArrayList<ArrayList<String>>();
        worktimeList.add(dayListDetails);
        worktimeList.add(hoursWorkedDetails);
        worktimeList.add(hoursListDetails);
        worktimeList.add(dateListDetails);
        return worktimeList;

    }
    //@RequestParam (value = "jsonString", defaultValue = "{id : \"\", month : \"\",year : \"\"}") String jsonString
//,@RequestParam(value = "month", defaultValue = "") String month, @RequestParam(value = "year", defaultValue = "") String year,@RequestParam(value = "id", defaultValue = "") String id
    @RequestMapping("/reports")
    public String reports(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie,@RequestParam(value = "month", defaultValue = "") String month, @RequestParam(value = "year", defaultValue = "") String year,@RequestParam(value = "id", defaultValue = "") String id) throws Exception {
        String phone = convertToken(cookie);

        if (!isPhoneNumberExist(phone))
            return "Landing_page";
        try {
           // EmployeeObject employeeObject = persist.loadObject(EmployeeObject.class, 1);;
           // List<EmployeeObject> employeeObjectList = persist.loadList(EmployeeObject.class);

            //JSONObject json=new JSONObject(jsonString);
           // String month=json.getString("month");
           //String year=json.getString("year");
          //  String id=json.getString("id");

            model.addAttribute("employeeName","");

            model.addAttribute("admin",isAdmin(phone));
            EmployeeObject employeeObject=persist.getEmployeeById(getEmployeeId(cookie));
            float total=0;

            if(!id.equals("")&&!month.equals("") && !year.equals("")) {
                EmployeeObject employeeObjectToView = persist.getEmployeeById(parseInt(id));
                if (employeeObjectToView != null){
                    model.addAttribute("empId", id);

                    //ResultSet result=persist.selectEmployeeById(parseInt(id));
                    //String employeeName="";//GET THE NAME OF THE EMPLOYEE.
                    // while (result.next()){
                    String employeeName = employeeObjectToView.getName();
                    // }
                    model.addAttribute("employeeName", "פירוט החודש של " + employeeName);
                    //ResultSet rs = persist.selectWorkTimeMonth(parseInt(month),parseInt(year),parseInt(id));
                    List<WorktimeObject> worktimeObject = persist.getWorkTimeMonth(parseInt(month), parseInt(year), parseInt(id));
                    WorktimeObject exitTimeObject;
                    WorktimeObject enterTimeObject;
                    List<WorktimeObject> sumTimeObject;
                    ArrayList<String> dayList = new ArrayList<String>();
                    ArrayList<String> hoursWorked = new ArrayList<String>();
                    ArrayList<String> hoursList = new ArrayList<String>();
                    ArrayList<Date> dateList = new ArrayList<Date>();

                    for (int i = 1; i <= 31; i++) {
                        total = 0;
                        exitTimeObject = persist.selectLastWorktimeInAday(parseInt(year), parseInt(month), parseInt(id), i);
                        // rsExitTime = persist.selectLastWorktimeDay(parseInt(year),parseInt(month),parseInt(id),i);//get the last worktime in a day.
                        // rsExitTime=persist.selectLastWorktimeInAday(parseInt(year),parseInt(month),parseInt(id),i).getExitTime();
                        enterTimeObject = persist.selectFirstWorktimeInAday(parseInt(year), parseInt(month), parseInt(id), i);//get the first worktime in a day.;
                        sumTimeObject = persist.workTimeInADay(parseInt(year), parseInt(month), parseInt(id), i);//get the total time worked in a day.
                        for (int j = 0; j < sumTimeObject.size(); j++)
                            total += sumTimeObject.get(j).getTotalHoursWorked();
                        if (enterTimeObject != null && exitTimeObject != null) {
                            dayList.add(enterTimeObject.getDayOfTheWeek());
                            hoursWorked.add(timeString(total));

                            hoursList.add(timeString(enterTimeObject.getEnterTime()) + "   ->   " + timeString(exitTimeObject.getExitTime()));
                            dateList.add(enterTimeObject.getDate());

                        }
                    }
                    model.addAttribute("days", dayList);
                    model.addAttribute("hours", hoursList);
                    model.addAttribute("hoursWorked", hoursWorked);
                    model.addAttribute("dateWorked", dateList);
                }
            }
            else if (!month.equals("") && !year.equals("")) {
                    model.addAttribute("empId", getEmployeeId(cookie));
                    //     ResultSet rs = persist.selectWorkTimeMonth(parseInt(month),parseInt(year),getEmployeeId(cookie));//ADMIN DONT HAVE WORKTIME!!NEED TO BE FIXED.
                    List<WorktimeObject> worktimeObject = persist.getWorkTimeMonth(parseInt(month), parseInt(year), getEmployeeId(cookie));
                    WorktimeObject exitTimeObject;
                    WorktimeObject enterTimeObject;
                    List<WorktimeObject> sumTimeObject;

                    ArrayList<String> dayList = new ArrayList<String>();
                    ArrayList<String> hoursWorked = new ArrayList<String>();
                    ArrayList<String> hoursList = new ArrayList<String>();
                    ArrayList<Date> dateList = new ArrayList<Date>();

                    for (int i = 1; i <= 31; i++) {
                        total = 0;
                        exitTimeObject = exitTimeObject = persist.selectLastWorktimeInAday(parseInt(year), parseInt(month), getEmployeeId(cookie), i);//get the last worktime in a day.
                        enterTimeObject = persist.selectFirstWorktimeInAday(parseInt(year), parseInt(month), getEmployeeId(cookie), i);//get the first worktime in a day.;
                        sumTimeObject = persist.workTimeInADay(parseInt(year), parseInt(month), getEmployeeId(cookie), i);//get the total time worked in a day.

                        for (int j = 0; j < sumTimeObject.size(); j++)
                            total += sumTimeObject.get(j).getTotalHoursWorked();
                        if (enterTimeObject != null && exitTimeObject != null) {
                            dayList.add(enterTimeObject.getDayOfTheWeek());
                            hoursWorked.add(timeString(total));
                            hoursList.add(timeString(enterTimeObject.getEnterTime()) + "   ->   " + timeString(exitTimeObject.getExitTime()));
                            dateList.add(enterTimeObject.getDate());

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
        String phone = convertToken(cookie);
        if (!isPhoneNumberExist(phone))
            return "Landing_page";
        model.addAttribute("admin",isAdmin(phone));

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

   /* private boolean checkCookie(String cookie) throws SQLException {
        PreparedStatement statement = dbConnection.prepareStatement("select * from test2.employee WHERE  test2.employee.employeePhone=?  ");
        statement.setInt(1, parseInt(convertToken(cookie)));
        ResultSet result = statement.executeQuery();
        return result.next();
    }*/

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

    public String changeMinutesToHours(float minutes1) {
        int hour, minutes;
            hour = (int) minutes1 / 60;
            minutes = (int) minutes1 % 60;
            if (hour < 10 && minutes < 10)
                return  "0" + hour + ":0" + minutes;
            else if (hour < 10)
                 return  "0" + hour + ":" + minutes;
            else if (minutes < 10)
                return hour + ":" + "0" + minutes;
            else
                return hour + ":" + minutes;
    }
/*private void reasonLists(String[] days,Calendar cal,int i,int hour,int minutes,List<ReasonObject> reasonObjectList,ArrayList<String> reasons, ArrayList<Integer> employeeId, ArrayList<Date> date, ArrayList<Integer> hours, ArrayList<String> names, ArrayList<String> dayOfWeek, ArrayList<String> enterTime,ArrayList<String> exitTime){
    if (hour < 10 && minutes < 10)
        exitTime.add("0" + hour + ":0" + minutes);
    else if (hour < 10)
        exitTime.add("0" + hour + ":" + minutes);
    else if (minutes < 10)
        exitTime.add(hour + ":" + "0" + minutes);
    else
        exitTime.add(hour + ":" + minutes);
    hour = (int) (reasonObjectList.get(i).getEnterTime()) / 60;
    minutes = (int) (reasonObjectList.get(i).getEnterTime()) % 60;
    if (hour < 10 && minutes < 10)
        enterTime.add("0" + hour + ":0" + minutes);
    else if (hour < 10)
        enterTime.add("0" + hour + ":" + minutes);
    else if (minutes < 10)
        enterTime.add(hour + ":" + "0" + minutes);
    else
        enterTime.add(hour + ":" + minutes);
    cal.setTime(reasonObjectList.get(i).getDate());
    int day = cal.get(Calendar.DAY_OF_WEEK);
    dayOfWeek.add(days[day - 1]);
    names.add(reasonObjectList.get(i).getEmployeeObject().getName());
    hours.add(reasonObjectList.get(i).getHowManyHours());
    employeeId.add(reasonObjectList.get(i).getEmployeeObject().getId());
    date.add(reasonObjectList.get(i).getDate());
    reasons.add(reasonObjectList.get(i).getReasonText());
}*/
   /* private int enterTime(String phone) throws SQLException {
        int timeEnter = 0;
        PreparedStatement statement = dbConnection.prepareStatement("select * from test2.worktime WHERE  id=? order by timeId desc limit 1; ");
        statement.setInt(1, getEmployeeId(phone));
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            timeEnter = (int) result.getFloat("enterTime");
            //name=result.getString("employeeName");
        }
        return timeEnter;
    }*/
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
    private boolean isAdmin(String phone) throws SQLException {
        AdminObject adminObject=persist.getAdminByPhone(phone);
       // ResultSet result=persist.selectEmployee(parseInt(id));
        return adminObject != null;
    }
    @RequestMapping("/adminMain")
    public String adminMain(Model model, @CookieValue(value = SESSION, defaultValue = "") String cookie) throws Exception {
        Date today=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int todayDay = calendar.get(Calendar.DATE);
        int todayMonth = calendar.get(Calendar.MONTH);//month 0-11
        int todayYear = calendar.get(Calendar.YEAR);//year current
        List<WorktimeObject> worktimeObjectList2;

        List<WorktimeObject> worktimeObjectList;
        String phone = convertToken(cookie);
        String adminName=persist.getAdminByPhone(phone).getName();
        int companyId=persist.getAdminByPhone(phone).getCompanyObject().getId();
        List <EmployeeObject> listOfEmployees=persist.getEmployees(companyId);
        ArrayList<String> inPosition=new ArrayList<>();
        float totalInMonth = 0;//count the month working time.
        float totalInDay=0;
        float totalDay=0;

        for(int i=0;i<listOfEmployees.size();i++){
            worktimeObjectList= persist.getWorkTimeMonth(todayMonth + 1,todayYear,listOfEmployees.get(i).getId());
            worktimeObjectList2=persist.workTimeInADay(todayYear,todayMonth + 1,listOfEmployees.get(i).getId(),todayDay);
            for (int j=0;j<worktimeObjectList.size();j++){
                totalInMonth+=worktimeObjectList.get(j).getTotalHoursWorked();
            }
            for(int k=0;k<worktimeObjectList2.size();k++){
                totalInDay+=worktimeObjectList2.get(k).getTotalHoursWorked();
            }
            if(listOfEmployees.get(i).isEnterOrExit()==true){
                inPosition.add(listOfEmployees.get(i).getName());
                totalDay=totalInDay;
              /*  for(int y=0;y<worktimeObjectList2.size();y++){
                    totalDay+=worktimeObjectList2.get(y).getTotalHoursWorked();
                }*/

            }


        }
        model.addAttribute("name","Welcome "+adminName);
        model.addAttribute("inPosition",inPosition);
        model.addAttribute("totalToEmployee",timeString(totalDay));
        model.addAttribute("totalHoursInMonth",timeString(totalInMonth));
        model.addAttribute("totalHoursInDay",timeString(totalInDay));
        return "adminMain";
    }
}