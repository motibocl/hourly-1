package com.elector.Utils;

import com.elector.Enums.ConfigEnum;
import com.elector.Enums.ServicesEnum;
import com.elector.Objects.Entities.*;
import com.elector.Objects.General.BaseEntity;
import com.elector.Services.GeneralManager;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfig;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Definitions.HTTP_REQUEST_POST;

/**
 * Created by Sigal on 5/16/2016.
 */
@Component
public class Utils {

    public static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    @Autowired
    public TemplateUtils templateUtils;

    @Autowired
    private GeneralManager generalManager;

    @Autowired
    private VelocityConfig velocityConfig;

    public static Map<String, TranslationObject> translations = null;
    private static Map<String, Long> blockedIpsMap = new ConcurrentHashMap<>();
    private static Map<String, Integer> requestsPerIpMap = new ConcurrentHashMap<>();
    public static boolean enableBlocking = true;


//    public static long calculateObjectSize(Object object) {
//        return ObjectSizeCalculator.getObjectSize(object);
//    }

    public static long calculateStringSize(Object object) {
        return object.toString().getBytes(Charsets.UTF_8).length;
    }

    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = out.toString("UTF-8");
        return outStr;
    }

    private int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public Date getCurrentMonthFirstDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        LocalDateTime now = LocalDateTime.now();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), getMonth(date), 1);
        return cal.getTime();
    }

    public Date getNextMonthFirstDate(Date date) {
        int month = getMonth(date);
        int nextMonth = 0;
        if (month == 12) {
            nextMonth = 1;
        } else {
            nextMonth = ++month;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), nextMonth, 1);
        return cal.getTime();
    }


    private String getCookieValueByName(HttpServletRequest request, String name) {
        String value = "0";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return hasText(value) ? value : "0";
    }

    public Date dateWithNoTime(Date dateWithTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateWithTime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date yesterdayWithNoTime(Date dateWithTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateWithTime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public long timeToNextBirthday(Date now, Date birthday) {
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTime(now);
        calendarNow.set(Calendar.HOUR_OF_DAY, 0);
        calendarNow.set(Calendar.MINUTE, 0);
        calendarNow.set(Calendar.SECOND, 0);
        calendarNow.set(Calendar.MILLISECOND, 0);
        Calendar calendarBirthday = Calendar.getInstance();
        calendarBirthday.setTime(birthday);
        calendarBirthday.set(Calendar.SECOND, 1);
        calendarBirthday.set(Calendar.YEAR, calendarNow.get(Calendar.YEAR));
        if (calendarNow.after(calendarBirthday)) {
            calendarBirthday.add(Calendar.YEAR, 1);
        }
        return calendarBirthday.getTimeInMillis() - calendarNow.getTimeInMillis();
    }

    public boolean isToday(Date date) {
        return dateWithNoTime(new Date()).equals(dateWithNoTime(date));
    }

    public boolean isTomorrow(Date date) {
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTime(new Date());
        calendarNow.add(Calendar.DAY_OF_MONTH, 1);
        return dateWithNoTime(calendarNow.getTime()).equals(dateWithNoTime(date));
    }

    public List<Object> jsonToListOfObjects(String data, Class className) {
        List<Object> objectList = null;
        try {
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createParser(data);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
            objectList = mapper.readValue(parser, mapper.getTypeFactory().constructCollectionType(List.class, className));
        } catch (Exception e) {
            LOGGER.error("jsonToListOfObjects", e);
        }
        return objectList;
    }

    public static boolean hasText(String str) {
        return str != null && str.length() > 0;
    }

    public boolean hasElements(List list) {
        return list != null && !list.isEmpty();
    }

    public void setDefaultParameters(HttpServletRequest request, HttpServletResponse response, JSONObject results) {
        results.put(PARAM_SITE_NAME, ConfigUtils.getConfig(ConfigEnum.site_name, "Elector"));
    }

    public static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }

    public static String dateStringWithNoTime(Date date) {
        return date == null ? MINUS : new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    public static String getTranslation(String key) {
        return translations != null && translations.get(key) != null ? translations.get(key).getTranslationValue() : key;
    }

    public String electionTimeFrontendFormat(Date electionDate) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(electionDate);
        return String.format("%s-%s-%s-%s-%s", calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.YEAR));
    }

    public boolean isElectionOpen(Date electionDate) {
        return electionDate.before(new Date());
    }

    public boolean isDatesSameDay(Date date1, Date date2) {
        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = GregorianCalendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    public Date getMidnightDate(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    public static JSONObject invokeUrlGet(String domain, Map<String, Object> params) {
        String response = EMPTY;
        try {
            StringBuilder paramsBuilder = new StringBuilder();
            for (String key : params.keySet()) {
                paramsBuilder.append(AND).append(key).append(EQUALS).append(Utils.getEncodedString(params.get(key).toString()));
            }
            HttpURLConnection con = (HttpURLConnection) new URL(domain + paramsBuilder.toString()).openConnection();
            con.setRequestMethod(HTTP_REQUEST_GET);
            int responseCode = con.getResponseCode();
            if (responseCode != HttpStatus.SC_OK) {
                LOGGER.info("Response Code : " + responseCode);
            } else {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder responseBuilder = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    responseBuilder.append(inputLine);
                }
                in.close();
                response = responseBuilder.toString();
            }
        } catch (IOException e) {
            LOGGER.error("invokeUrlGet", e);
        }
        return !Utils.hasText(response) ? new JSONObject() : new JSONObject(response);
    }

    public static boolean pingRest(String path) {
        String response = EMPTY;
        boolean success = false;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(path).openConnection();
            con.setRequestMethod(HTTP_REQUEST_GET);
            con.setConnectTimeout(10 * SECOND);
            con.setReadTimeout(10 * SECOND);

            int responseCode = con.getResponseCode();
            if (responseCode != HttpStatus.SC_OK) {
                LOGGER.info("Response Code : " + responseCode);
            } else {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder responseBuilder = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    responseBuilder.append(inputLine);
                }
                in.close();
                response = responseBuilder.toString();
                if (Utils.hasText(response)) {
                    success = true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("pingRest", e);
        }
        return success;
    }

    public String parseVelocity(ModelMap model, String file) {
        String text = EMPTY;
        try {
            text = VelocityEngineUtils.mergeTemplateIntoString(velocityConfig.getVelocityEngine(), file, UTF_8, model);
        } catch (Exception e) {
            LOGGER.error("parseVelocity", e);
        }
        return text;
    }

    public static String generateRandomPassword() {
        return RandomStringUtils.random(8, false, true);
    }


    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static long yearsBetween(Date startDate, Date endDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        LocalDate start = LocalDate.of(year, month, day);
        cal.setTime(endDate);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        LocalDate end = LocalDate.of(year, month, day);
        return ChronoUnit.YEARS.between(start, end);
    }

    public static boolean isFileType(String fileName, int requiredType) {
        List<String> typesToAllow = new ArrayList<>();
        switch (requiredType) {
            case FILE_TYPE_IMAGE:
                typesToAllow = Arrays.asList("png", "jpg", "jpeg");
                break;
            case FILE_TYPE_SPREADSHEET:
                typesToAllow = Arrays.asList("csv", "xls", "xlsx");
                break;
            case FILE_TYPE_TEXT:
                typesToAllow = Arrays.asList("txt");
                break;
        }
        return typesToAllow.contains(FilenameUtils.getExtension(fileName));
    }

    private static String symbolRemove(String symbol, String string) {
        return string != null ? string.replace(symbol, EMPTY).trim() : EMPTY;
    }

    public static String formatPhoneNumber(String phone) {
        return symbolRemove(MINUS, phone);
    }

    public static Date tryParseDate(String dateString) {
        Date date = null;
        if (dateString != null) {
            List<String> dateFormats = Arrays.asList
                    ("dd-MM-yy", "dd-MM-yyyy", "dd/MM/yy", "dd/MM/yyyy", "dd.MM.yy", "dd.MM.yyyy");
            for (String format : dateFormats) {
                try {
                    date = new SimpleDateFormat(format).parse(dateString);
                    break;
                } catch (ParseException e) {
                }
            }
        }
        return date;

    }

    public static String getStringFromList(List<String> stringList, Integer index) {
        String value = null;
        if (index != null) {
            value = stringList.get(index);
            if (value.equals(EMPTY)) {
                value = null;
            }
        }
        return value;
    }

    public boolean equalPhoneNumbers(String phone1, String phone2) {
        return phone1.replace(MINUS, EMPTY).trim().equals(phone2.replace(MINUS, EMPTY).trim());
    }

    public static String getIpFromRequest(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (!(hasText(ip))) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static int ballotBoxType(String description) {
        int type = -1;
        description = description.trim();
        if (description.equals("כולו")) {
            type = BALLOT_BOX_TYPE_ALL;
        } else if (description.equals("רציף")) {
            type = BALLOT_BOX_TYPE_CONTINUOUS;
        } else if (description.equals("זוגי")) {
            type = BALLOT_BOX_TYPE_EVEN;
        } else if (description.equals("אי-זוגי")) {
            type = BALLOT_BOX_TYPE_ODD;
        } else if (description.equals("1 אות משפ'")) {
            type = BALLOT_BOX_TYPE_ONE_LAST_NAME;
        } else if (description.equals("-")) {
            type = BALLOT_BOX_TYPE_MINUS;
        } else if (description.equals("2 אותיות משפ'")) {
            type = BALLOT_BOX_TWO_LAST_NAME;
        } else if (description.equals("1 משפ' + 1 פרטי")) {
            type = BALLOT_BOX_ONE_FIRST_ONE_LAST;
        } else if (description.equals("2 משפ' + 2 פרטי")) {
            type = BALLOT_BOX_TWO_FIRST_TWO_LAST;
        } else {
            System.out.println("-----" + description);
        }
        return type;
    }

    public static String eliminateLeadingZeros(String source) {
        return source.replaceFirst("^0+(?!$)", EMPTY);
    }

    public static String reformatVoterId(String voterId) {
        voterId = voterId.replaceAll(MINUS, EMPTY).trim();
        if (StringUtils.isNumeric(voterId)) {
            if (voterId.length() != 9) {
                if (voterId.length() == 8) {
                    voterId = "0" + voterId;
                } else {
                    voterId = EMPTY;
                }
            }
        } else {
            voterId = EMPTY;
        }
        return voterId;
    }

    public static String reformatPhone(String phone) {
        boolean invalidPhone = false;
        phone = phone.replace(MINUS, EMPTY).trim();
        if (!phone.startsWith("0")) {
            phone = "0" + phone;
        }
        if (phone.startsWith("05") && phone.length() != 10) {
            invalidPhone = true;
        }
        if (!phone.startsWith("05") && phone.length() != 9) {
            invalidPhone = true;
        }
        if (invalidPhone) {
            phone = EMPTY;
        }
        return phone;
    }

    public static List<String> jsonArrayToStringList(String data) {
        List<String> result = new ArrayList<>();
        if (data != null) {
            data = data.replace("[", EMPTY).replace("]", EMPTY);
            result = Arrays.asList(data.split(COMMA));
        }
        return Arrays.asList(data.split(COMMA));
    }

    public static Field[] getAllEntityFields(Class clazz) {
        Field[] fields = new Field[0];
        do {
            fields = (Field[]) ArrayUtils.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();

        } while (!clazz.equals(BaseEntity.class));
        return fields;
    }

    public static boolean isValidPhone(String phone) {
        return isValidCellPhone(phone) || isValidHomePhone(phone);
    }

    public static boolean isValidCellPhone(String phone) {
        return phone != null && Pattern.compile("^05[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]").matcher(phone).find() && phone.trim().length() == 10;
    }

    public static boolean isValidHomePhone(String phone) {
        return hasText(phone);
    }

    public static List<String> stringToStringList(String data) {
        List<String> stringList = new ArrayList<>();
        try {
            if (hasText(data)) {
                List<String> tempStrings = Arrays.asList(data.split(COMMA));
                for (String temp : tempStrings) {
                    if (hasText(temp)) {
                        stringList.add(temp);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("stringToStringList", e);
        }
        return stringList;
    }

    public static List<Integer> stringToIntegerList(String data) {
        List<Integer> integerList = new ArrayList<>();
        try {
            if (hasText(data)) {
                List<String> tempStrings = Arrays.asList(data.split(COMMA));
                for (String temp : tempStrings) {
                    if (hasText(temp)) {
                        integerList.add(Integer.valueOf(temp));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("stringToIntegerList", e);
        }
        return integerList;
    }

    public static String getRequestIP(HttpServletRequest request) {
        String ip = null;
        try {
            String header = request.getHeader("x-forwarded-for");
            ip = hasText(header) ? header : request.getRemoteAddr();
        } catch (Exception e) {
            LOGGER.error(String.format("cannot get ip from url: %s, query string: %s, x-forwarded-for: %s", request.getRequestURL(), request.getQueryString(), request.getHeader("x-forwarded-for")), e);
        }
        return ip;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        list = Lists.reverse(list);
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String getPrintableSupportStatus(int supportStatus) {
        String text = EMPTY;
        switch (supportStatus) {
            case PARAM_SUPPORT_STATUS_UNKNOWN:
                text = Utils.getTranslation("support.unknown");
                break;
            case PARAM_SUPPORT_STATUS_SUPPORTING:
                text = Utils.getTranslation("supporting");
                break;
            case PARAM_SUPPORT_STATUS_NOT_SUPPORTING:
                text = Utils.getTranslation("not.supporting");
                break;
            case PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING:
                text = Utils.getTranslation("unverified.supporting");
                break;
        }
        return text;
    }

    public static void addTranslation(TranslationObject translationObject) {
        translations.put(translationObject.getTranslationKey(), translationObject);
    }

    public static TranslationObject getTranslationObject(String key) {
        return translations != null && translations.get(key) != null ? translations.get(key) : null;
    }

    public static int calculateAge(Date birthDate) {
        int age = 0;
        if ((birthDate != null)) {
            age = Period.between(birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getYears();
        }
        return age;
    }

    public static String getDecodedString(String source) {
        String decoded = EMPTY;
        try {
            decoded = URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(String.format("error decoding string %s", source), e);
            decoded = URLDecoder.decode(source);
        }
        return decoded;
    }

    public static String getEncodedString(String source) {
        String encoded = EMPTY;
        try {
            encoded = URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(String.format("error encoding string %s", source), e);
            encoded = URLEncoder.encode(source);
        }
        return encoded;
    }

    public static String getMD5String(String source) {
        String result = EMPTY;
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(source.getBytes("UTF-8"));
            StringBuilder ret = new StringBuilder(bytes.length);
            for (byte bt : bytes) {
                String hex = Integer.toHexString(0x0100 + (bt & 0x00FF)).substring(1);
                ret.append(hex.length() < 2 ? "0" : "").append(hex);
            }
            result = ret.toString();
        } catch (Exception e) {
            LOGGER.error(String.format("error generating md5 string from %s", source), e);

        }
        return result;
    }

    public static String timePassedAsString(long millis) {
        return String.format("%d minutes and %d seconds",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public static void printRequestData(HttpServletRequest request) {
        LOGGER.info(String.format(
                "request data: url - %s, query string - %s, ip - %s, referrer - %s, user-agent: %s",
                request.getRequestURI(),
                request.getQueryString(),
                getIpFromRequest(request),
                request.getHeader("referer"),
                request.getHeader("User-Agent")
        ));
    }

    public static String getPhoneWithoutCountryCodePrefix(String phone) {
        phone = phone.trim();
        if ((phone.length() == 13 && phone.startsWith("+972"))) {
            phone = phone.substring(4);
        } else if ((phone.length() == 12 && phone.startsWith("972"))) {
            phone = phone.substring(3);
        }
        return phone;
    }

    public static String stringWithSign(int number) {
        return String.format(number > 0 ? "+%s" : "%s", number);
    }

    public static String getPrintableNeedsRideStatus(int needsRideStatus) {
        String text = EMPTY;
        switch (needsRideStatus) {
            case PARAM_NEED_RIDE_STATUS_UNKNOWN:
                text = Utils.getTranslation("unknown");
                break;
            case PARAM_NEED_RIDE_STATUS_YES:
                text = Utils.getTranslation("yes");
                break;
            case PARAM_NEED_RIDE_STATUS_NO:
                text = Utils.getTranslation("no");
                break;
        }
        return text;
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("sleep", e);
        }
    }

    public List<Integer> entitiesToOidsList(List<? extends BaseEntity> entities) {
        List<Integer> oids = new ArrayList<>();
        if (entities != null) {
            oids = entities.stream().map(BaseEntity::getOid).collect(Collectors.toList());
        }
        return oids;
    }

    private Integer incrementAndGetRequestsCount(String ip) {
        Integer count = 0;
        if (hasText(ip)) {
            count = requestsPerIpMap.get(ip);
            if (count == null) {
                count = 0;
            }
            count++;
            requestsPerIpMap.put(ip, count);
        }
        return count;
    }


    public void clearAllBlocked() {
        blockedIpsMap.clear();
    }

    public void disableBlocking() {
        enableBlocking = false;
    }

    public String removeAllUnicodeChars(String str) {
        return str.replaceAll("\\P{Print}", EMPTY);
    }

    private static final int REQUEST_TIMEOUT = 10 * SECOND;

    public static boolean restartService(int service) {
        boolean error = false;
        String url = EMPTY;
        ServicesEnum servicesEnum = null;
        switch (service) {
            case SERVICE_ELECTOR:
                url = ConfigUtils.getConfig(ConfigEnum.elector_url, "http://localhost:8787/");
                servicesEnum = ServicesEnum.elector;
                break;
            case SERVICE_VOTERS:
                url = ConfigUtils.getConfig(ConfigEnum.voters_in_memory_service, "http://localhost:8789/");
                servicesEnum = ServicesEnum.voters_in_memory_service;
                break;
            case SERVICE_ELECTION_DAY:
                url = ConfigUtils.getConfig(ConfigEnum.election_day_in_memory_service, "http://localhost:8788/");
                servicesEnum = ServicesEnum.election_day_in_memory_service;
                break;
            case SERVICE_SMS:
                url = ConfigUtils.getConfig(ConfigEnum.sms_service, "http://localhost:8791/");
                servicesEnum = ServicesEnum.sms_service;
                break;
            case SERVICE_JOBS:
                url = ConfigUtils.getConfig(ConfigEnum.general_jobs_service, "http://localhost:8790/");
                servicesEnum = ServicesEnum.general_jobs_service;
                break;
            default:
                error = true;
        }
        if (!error) {
            if (ping(url, REQUEST_TIMEOUT)) {
                LOGGER.info(String.format("service still up - %s", url));
                error = !terminateService(url) || !startService(url, servicesEnum);
            } else {
                LOGGER.info("service is down");
                LOGGER.info(String.format("service is down - %s", url));
                error = startService(url, servicesEnum);
            }
        } else {
            LOGGER.warn(String.format("url not defined for this service, %s", service));
            error = true;
        }
        return !error;
    }

    private static boolean terminateService(String url) {
        final int MAX_SECOND_TO_WAIT_FOR_TERMINATION = 60;
        Utils.invokePostNoResponse(String.format("%s/terminate", url), new HashMap<>());
        LOGGER.info(String.format("requested termination - %s", url));
        boolean terminated = false;
        int tries = 0;
        while (ping(url, REQUEST_TIMEOUT) && tries < MAX_SECOND_TO_WAIT_FOR_TERMINATION) {
            tries++;
            Utils.sleep(SECOND);
            LOGGER.info(String.format("service still up - %s", url));
        }
        if (tries == MAX_SECOND_TO_WAIT_FOR_TERMINATION) {
            LOGGER.info(String.format("service was not terminated - %s", url));
        } else {
            terminated = true;
            LOGGER.info(String.format("service down - %s", url));
        }
        return terminated;
    }

    private static void invokePostNoResponse(String domain, Map<String, Object> params) {
        try {
            StringBuilder paramsBuilder = new StringBuilder();
            for (String key : params.keySet()) {
                paramsBuilder.append(AND).append(key).append(EQUALS).append(Utils.getEncodedString(params.get(key).toString()));
            }
            HttpURLConnection con = (HttpURLConnection) new URL(domain + paramsBuilder.toString()).openConnection();
            con.setRequestMethod(HTTP_REQUEST_POST);
            int responseCode = con.getResponseCode();
            LOGGER.info(String.format("response code: %s", responseCode));
        } catch (IOException e) {
            LOGGER.error("invokePostNoResponse", e);
        }
    }

    private static boolean startService(String url, ServicesEnum servicesEnum) {
        boolean error = false;
        try {

            Runtime.getRuntime().exec(
                    String.format(
                            "cmd /c start \"\" %s\\%s.bat",
                            ConfigUtils.getConfig(ConfigEnum.path_to_desktop, "C:\\Users\\Administrator\\Desktop"),
                            servicesEnum));
        } catch (Exception e) {
            error = true;
            LOGGER.error("startService", e);
        }
        if (!error) {
            LOGGER.info(String.format("service has started, waiting to ping - %s", url));

            Utils.sleep(10 * SECOND);
            final int MAX_SECONDS_TO_WAIT_FOR_PING = 50;
            boolean restarted = false;
            int tries = 0;
            while (!restarted && tries < MAX_SECONDS_TO_WAIT_FOR_PING) {
                LOGGER.info(String.format("service not up yet - %s", url));
                restarted = ping(url, REQUEST_TIMEOUT);
                tries++;
            }
            if (tries == MAX_SECONDS_TO_WAIT_FOR_PING) {
                LOGGER.info(String.format("could not ping to service, giving up - %s", url));
                error = true;
            } else {
                LOGGER.info(String.format("service restarted successfully - %s", url));

            }
        }
        return !error;
    }

    public static boolean ping(String url, int timeout) {
        boolean success = false;
        try (Socket socket = new Socket()) {
            URL urlObject = new URL(url);
            socket.connect(new InetSocketAddress(urlObject.getHost(), urlObject.getPort()), timeout);
            success = true;
        } catch (IOException e) {
        }
        return success;
    }

    public static String getServiceUrlByType (int service) {
        String url = EMPTY;
        switch (service) {
            case SERVICE_ELECTOR:
                url = ConfigUtils.getConfig(ConfigEnum.elector_url, "http://localhost:8787/");
                break;
            case SERVICE_VOTERS:
                url = ConfigUtils.getConfig(ConfigEnum.voters_in_memory_service, "http://localhost:8789/");
                break;
            case SERVICE_ELECTION_DAY:
                url = ConfigUtils.getConfig(ConfigEnum.election_day_in_memory_service, "http://localhost:8788/");
                break;
            case SERVICE_SMS:
                url = ConfigUtils.getConfig(ConfigEnum.sms_service, "http://localhost:8791/");
                break;
            case SERVICE_JOBS:
                url = ConfigUtils.getConfig(ConfigEnum.general_jobs_service, "http://localhost:8790/");
                break;
            default:
        }
        return url;
    }

    public static String getApplicationIp () {
        String ip = EMPTY;
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            LOGGER.error("cannot get application ip");
        }
        return ip;
    }


    public static String reformatPhoneNumber (String phone) {
        String formattedPhone = null;
        if (Utils.hasText(phone)) {
            phone = phone.replaceAll(MINUS, EMPTY).trim();
            if (!phone.equals("0")) {
                if (phone.length() > 0) {
                    phone = Utils.getPhoneWithoutCountryCodePrefix(phone);
                    if (!Utils.isValidCellPhone(phone.replaceAll(MINUS, EMPTY)) && Utils.isValidPhone("0" + phone.replaceAll(MINUS, EMPTY))) {
                        phone = "0" + phone;
                    }
                    formattedPhone = phone;
                }
            } else {
                formattedPhone = EMPTY;
            }
        }
        return formattedPhone;
    }


}
