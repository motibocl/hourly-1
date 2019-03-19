package com.elector.Utils;

import com.elector.Objects.Entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.elector.Utils.Definitions.*;

/**
 * Created by Sigal on 9/9/2017.
 */
public class JsonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);



    public static JSONArray translationsToJson () {
        JSONArray array = new JSONArray();
        for (TranslationObject translationObject : Utils.translations.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, translationObject.getOid());
            jsonObject.put(PARAM_KEY, translationObject.getTranslationKey());
            jsonObject.put(PARAM_VALUE, translationObject.getTranslationValue());
            jsonObject.put(PARAM_ENGLISH, translationObject.getEn());
            array.put(jsonObject);
        }
        return array;
    }


}
