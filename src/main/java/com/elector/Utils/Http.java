package com.elector.Utils;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {
    private static final Logger LOGGER = LoggerFactory.getLogger(Http.class);

    public static String post(String urlString,String urlParameters){
        URL url;
        HttpURLConnection connection = null;
        try
        {
            //Create connection
            url = new URL(urlString);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "x-www-form-urlencoded");

            connection.setRequestProperty("Accept","text/html");
            connection.setRequestProperty("Accept","application/xhtml+xml");
            connection.setRequestProperty("Accept","application/xml");
            connection.setRequestProperty("Cache-Control","max-age=0");

            connection.setRequestProperty("Accept-Language", "he-IL");
            connection.setRequestProperty("Accept-Language", "en");
            connection.setRequestProperty("Accept-Language", "en-US");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes(Charsets.UTF_8).length));
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.write(urlParameters.getBytes(Charsets.UTF_8));
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            String responseString = response.toString();
//            LOGGER.info("Receive response:");
//            LOGGER.info(responseString);

            return responseString;
        } catch (Exception e) {
            LOGGER.error("Error while sending request");
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
