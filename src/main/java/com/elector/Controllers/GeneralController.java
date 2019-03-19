package com.elector.Controllers;


import com.elector.Services.GeneralManager;
import com.elector.Utils.ConfigUtils;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static com.elector.Utils.Definitions.*;

@CrossOrigin
@Controller
public class GeneralController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralController.class);

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private GeneralManager generalManager;

    private static final int PAGE_MAIN = 0;
    private static final int PAGE_SECOND = 1;
    private static final int PAGE_LOGIN = 2;
    private static final int PAGE_DASHBOARD = 3;

    private static final Map<Integer, String> PAGES_NAMES = new HashMap<>();
    static {
        PAGES_NAMES.put(PAGE_MAIN, "עמוד ראשי");
        PAGES_NAMES.put(PAGE_SECOND, "עמוד שני");
        PAGES_NAMES.put(PAGE_LOGIN, "עמוד LOGIN");
        PAGES_NAMES.put(PAGE_DASHBOARD, "עמוד dashboard");
    }


    @ModelAttribute
    public void preProcess (HttpServletRequest request, Model model) {
        LOGGER.info(String.format("request: %s", request.getRequestURI()));
        model.addAttribute("pagesNames", PAGES_NAMES);

    }

    @RequestMapping("/")
    public String main(Model model) throws Exception {
        model.addAttribute("page", PAGE_MAIN);
        boolean error = false;
        Integer code = null;
        try {
            model.addAttribute(PARAM_NAME, "TEST");
            model.addAttribute("age", 64344);
        } catch (Exception e) {
            LOGGER.error("main", e);
            error = true;
            code = PARAM_ERROR_GENERAL;
        }
        model.addAttribute(PARAM_ERROR, error);
        model.addAttribute(PARAM_CODE, code);

        return "tmpl_main";
    }

    @RequestMapping("/second")
    public String second(Model model) throws Exception {
        model.addAttribute("page", PAGE_SECOND);
        boolean error = false;
        Integer code = null;
        try {
            model.addAttribute(PARAM_NAME, "SECOND");

            model.addAttribute("age", 5436);
        } catch (Exception e) {
            LOGGER.error("main", e);
            error = true;
            code = PARAM_ERROR_GENERAL;
        }
        model.addAttribute(PARAM_ERROR, error);
        model.addAttribute(PARAM_CODE, code);

        return "tmpl_main";
    }


    /***************************/
    @RequestMapping("/login")
    public String login(Model model) throws Exception {
        model.addAttribute("page", PAGE_LOGIN);
        boolean error = false;
        Integer code = null;
        try {
            model.addAttribute(PARAM_NAME, "SECOND");

            //model.addAttribute("age", 5436);
        } catch (Exception e) {
            LOGGER.error("main", e);
            error = true;
            code = PARAM_ERROR_GENERAL;
        }
        model.addAttribute(PARAM_ERROR, error);
        model.addAttribute(PARAM_CODE, code);

        return "tmpl_main";
    }

    @RequestMapping("/login-params")
    public void loginParams(HttpServletResponse response, String phone, String password) throws Exception {
        boolean error = false;
        Integer code = null;
        JSONObject jsonObject = new JSONObject();
        try {
            if(phone.equals("0504230780") && password.equals("123132")) {
                jsonObject.put("success", true);
            } else {
                jsonObject.put("success", false);
            }

        } catch (Exception e) {
            LOGGER.error("appVersion", e);
            error = true;
            code = PARAM_ERROR_GENERAL;
        }
        response.setContentType("application/json; charset=UTF-8");
        jsonObject.put(PARAM_ERROR, error);
        jsonObject.put(PARAM_CODE, code);
        response.getWriter().print(jsonObject);

    }

    @RequestMapping("/dashboard" )
    public String dashboard(Model model) throws Exception {
        model.addAttribute("page", PAGE_DASHBOARD);
        boolean error = false;
        Integer code = null;
        try {
            model.addAttribute(PARAM_NAME, "dashboard");

            //model.addAttribute("age", 5436);
        } catch (Exception e) {
            LOGGER.error("main", e);
            error = true;
            code = PARAM_ERROR_GENERAL;
        }
        model.addAttribute(PARAM_ERROR, error);
        model.addAttribute(PARAM_CODE, code);

        return "tmpl_main";
    }



    @RequestMapping("/home" )
    public String home(Model model) throws Exception {
        //1. query db
        //2. fetch relevant data
        //3. send data to the client (html)

//        model.addAttribute("page", PAGE_DASHBOARD);
//        boolean error = false;
//        Integer code = null;
//        try {
//            model.addAttribute(PARAM_NAME, "dashboard");
//
//            //model.addAttribute("age", 5436);
//        } catch (Exception e) {
//            LOGGER.error("main", e);
//            error = true;
//            code = PARAM_ERROR_GENERAL;
//        }
//        model.addAttribute(PARAM_ERROR, error);
//        model.addAttribute(PARAM_CODE, code);

        return "Landing_page";
    }


    @RequestMapping("/reports" )
    public String reports(Model model) throws Exception {
//        model.addAttribute("page", PAGE_DASHBOARD);
//        boolean error = false;
//        Integer code = null;
//        try {
//            model.addAttribute(PARAM_NAME, "dashboard");
//
//            //model.addAttribute("age", 5436);
//        } catch (Exception e) {
//            LOGGER.error("main", e);
//            error = true;
//            code = PARAM_ERROR_GENERAL;
//        }
//        model.addAttribute(PARAM_ERROR, error);
//        model.addAttribute(PARAM_CODE, code);

        return "reports";
    }
    @RequestMapping("/special_reports" )
    public String special_reports(Model model) throws Exception {
//        model.addAttribute("page", PAGE_DASHBOARD);
//        boolean error = false;
//        Integer code = null;
//        try {
//            model.addAttribute(PARAM_NAME, "dashboard");
//
//            //model.addAttribute("age", 5436);
//        } catch (Exception e) {
//            LOGGER.error("main", e);
//            error = true;
//            code = PARAM_ERROR_GENERAL;
//        }
//        model.addAttribute(PARAM_ERROR, error);
//        model.addAttribute(PARAM_CODE, code);

        return "special_reports";
    }
    @RequestMapping("/mainPage" )
    public String mainPage(Model model) throws Exception {
//        model.addAttribute("page", PAGE_DASHBOARD);
//        boolean error = false;
//        Integer code = null;
//        try {
//            model.addAttribute(PARAM_NAME, "dashboard");
//
//            //model.addAttribute("age", 5436);
//        } catch (Exception e) {
//            LOGGER.error("main", e);
//            error = true;
//            code = PARAM_ERROR_GENERAL;
//        }
//        model.addAttribute(PARAM_ERROR, error);
//        model.addAttribute(PARAM_CODE, code);

        return "main";
    }

}
