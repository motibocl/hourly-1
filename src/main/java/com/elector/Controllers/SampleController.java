package com.elector.Controllers;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;


//
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.sql.*;
import java.util.ArrayList;

@Controller
@RestController
@EnableAutoConfiguration

public class SampleController {


//    @RequestMapping("/elector")
//    String Elector() {
//        return getPage("src\\\\main\\\\java\\\\name.html");
//    }

    @RequestMapping(value = "/getAlldata", method = RequestMethod.POST)
    String getdata(@RequestParam String login, @RequestParam String pass) throws SQLException {
        return getAllData(login, pass);
    }


    //@RequestMapping(value = "/addval", method = RequestMethod.POST)
    //String addval(@RequestParam String login, @RequestParam String pass) {
      //  return addVal(login, pass);
    //}


    /*@RequestMapping(value = "/test", method = RequestMethod.POST)
    String test(@RequestParam String login, @RequestParam String pass) {
        return "login =" + login + "<br>" + "pass = " + pass;
    }


    final static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    final static String DB_CONNECTION = "jdbc:mysql://http://localhost:8666/test2?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    final static String DB_USER = "root";
    final static String DB_PASSWORD = "tuRgmhuI1";
*/
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
        //    addVal("29", "29");
        //    getAllData("");
    }

//    private String getPage(String path) {
//        String res = "";
//        int c = 0;
//
//        try (FileReader fileReader = new FileReader(path)) {
//            while ((c = fileReader.read()) != -1) {
//                res += (char) c;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return res;
//    }

    static class User {
        String num;
        String pass;

        public User(String num, String pass) {
            this.num = num;
            this.pass = pass;
        }
    }

    private static String getAllData(String login, String pass) throws SQLException {
        ArrayList<User> data = new ArrayList<>();
        String res = "";
        int c = 0;



        Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2?autoReconnect=true&useSSL=false", "root", "RAMI2018");
        // Statement myStmt=myConn.createStatement();
        PreparedStatement myStmt = null;
        String sql = "select * from test2.employee WHERE test2.employee.companyId='1' and test2.employee.employeePassword=? and test2.employee.employeePhone=?";
        try {
            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, login);
            myStmt.setString(2, pass);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (myStmt != null) {
                try {
                    myStmt.close();
                } catch (Exception ignored) {
                }
            }
        }
        if (sql != null)
            System.out.println(login);  // return "main";
        else
            System.out.println(pass);
            //return "Landing_page";
        //String selectTableSQL = "SELECT * \n" +
        //"FROM elector \n" +
        //"LIMIT 0 , 30";
 return "main";
       /* try {
            Connection dbConnection = null;
            Statement statement = null;
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            // выбираем данные с БД
            ResultSet rs = statement.executeQuery(selectTableSQL);

            // И если что-то было получено то цикл while сработает
            while (rs.next()) {

                String Telephonenumber = rs.getString("elector.Telephone number");
                String Password = rs.getString("elector.Password");

                data.add(new User(Telephonenumber, Password));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).num.equals(login) & data.get(i).pass.equals(pass))
            {
                break;
            }
            else {
                c++;
            }
        }

        if (c<data.size()&&(pass!="")){res="Login Successfully";}
        else if (c==data.size()&&(pass!="")){res="username/pass is incorrect";}
        else  {res="Enter password";}

        return res;
    }

    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbConnection;
    }

    private static String addVal(String Telephonenumber, String Password) {
        ArrayList<String> numbers = new ArrayList<>();
        String res="";
        int c=0;

        String sqlVoice = "INSERT INTO `g-network`.`elector`(`Telephone number`, `Password`) VALUES (" + Telephonenumber + "," + Password + ")";

        String selectTableSQL = "SELECT * \n" +
                "FROM elector \n" +
                "LIMIT 0 , 30";

        Connection dbConnection = null;
        Statement statement = null;
        dbConnection = getDBConnection();


        try {
            statement = dbConnection.createStatement();

            // выбираем данные с БД
            ResultSet rs = statement.executeQuery(selectTableSQL);

            // Заполняет ArrayList
            while (rs.next()) {

                String Number = rs.getString("elector.Telephone number");
                numbers.add(Number);
            }

            if (numbers.size()!=0) {

                for (int i = 0; i < numbers.size(); i++) {
                    if (numbers.get(i).equals(Telephonenumber) ) {
                        break;
                    } else {
                        c++;
                    }
                }
            }

            if (c<numbers.size()&&(Password!="")){res = "User already exists!!!";}
            else if (c==numbers.size()&& (Password!=""))
            {
                statement.execute(sqlVoice);
                res = "User was added";}
             else {res="Enter password";}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    */
    }
}


