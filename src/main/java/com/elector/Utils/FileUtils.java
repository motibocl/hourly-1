package com.elector.Utils;

import java.io.*;

public class FileUtils {

    public static Writer getWriter(String path) throws FileNotFoundException, UnsupportedEncodingException{
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(new File(path)), "UTF-8"));
        return writer;
    }

    public static Reader getReader(String path) throws FileNotFoundException, UnsupportedEncodingException {
        BufferedReader br  = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), "UTF-8"));
        return br;
    }
}
