package com.elector.Utils;

import com.elector.Objects.Entities.TranslationObject;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Definitions.COMMA;
import static com.elector.Utils.Definitions.DOUBLE_QUOTE;
import static org.springframework.util.StringUtils.hasText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sigal on 5/18/2016.
 */
@Component
public class TemplateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateUtils.class);

    public String getTranslation(String key, Integer lang) {
        if (lang == null) {
            lang = PARAM_LANG_DEFAULT;
        }
        String value = "";
        try {
            TranslationObject translationObject = Utils.translations.get(key.toLowerCase());
            if (translationObject != null) {
                switch (lang) {
                    case PARAM_LANG_EN:
                        value = translationObject.getEn();
                        break;
                }
                if (!hasText(value)) {
                    value = hasText(translationObject.getTranslationValue()) ? translationObject.getTranslationValue() : key;
                }
            } else {
                value = key;
            }
        } catch (Exception e) {
            LOGGER.error("getTranslation", e);
        }
        return value;
    }


    public String getTranslation(String key, String param) {
        try {
            String value = getTranslation(key);
            return value.replace("%@", param);

        } catch (Exception e) {
            LOGGER.error("getTranslation", e);
        }
        return "";
    }


    public static String formatDateExcludeTime(Date date) {
        String dateAsString = "";
        if (date != null) {
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                dateAsString = formatter.format(date);
            } catch (Exception e) {
                LOGGER.error("formatDateExcludeTime", e);
            }
        }
        return dateAsString;
    }

    public static String formatDateExcludeSeconds(Date date) {
        String dateAsString = EMPTY;
        if (date != null) {
            try {
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                dateAsString = formatter.format(date);
            } catch (Exception e) {
                LOGGER.error("formatDateExcludeTime", e);
            }
        }
        return dateAsString;
    }


    public Date convertStringToDate(String stringDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date date;
        try {
            date = dateFormat.parse(stringDate);
        } catch (Exception e) {
            LOGGER.error("convertStringToDate", e);
            date = null;
        }
        return date;
    }


    public String getHebrewMonth(String month) {
        return month.replace("Jan", "ינואר")
                .replace("Feb", "פברואר")
                .replace("Mar", "מרץ")
                .replace("Apr", "אפריל")
                .replace("May", "מאי")
                .replace("Jun", "יוני")
                .replace("Jul", "יולי")
                .replace("Aug", "אוגוסט")
                .replace("Sep", "ספטמבר")
                .replace("Oct", "אוקטובר")
                .replace("Nov", "נובמבר")
                .replace("Dec", "דצמבר");
    }

    public String getTranslation(String key) {
        return getTranslation(key, 0);
    }

    private void appendField(StringBuilder json, String key, String value) {
        json.append(SPACE);
        json.append(key);
        json.append(COLON);
        json.append(DOUBLE_QUOTE);
        json.append(value);
        json.append(DOUBLE_QUOTE);
        json.append(COMMA);

    }





}
