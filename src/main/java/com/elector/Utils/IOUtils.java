package com.elector.Utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    public static void sendFileInResponse (HttpServletResponse response, File file) {
        try {
            response.setContentType("application/CSV");
            response.setHeader("Content-Disposition", String.format("inline;filename=%s.csv", file.getName()));
            response.setContentLength(((Long) file.length()).intValue());
            copyFromStreamToStream(new FileInputStream(file), response.getOutputStream());
            response.setHeader("Cache-Control", "private");
            response.setDateHeader("Expires", 0);
        } catch (IOException e) {
            LOGGER.error("sendFileInResponse", e);
        }

    }

    public static void close (BufferedReader bufferedReader) {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                LOGGER.error("close", e);
            }
        }

    }
    public static void copyFromStreamToStream(File file, OutputStream output) throws IOException {
        if (file.exists())
            copyFromStreamToStream(new FileInputStream(file), output, true, true);
    }

    public static void copyFromStreamToStream(InputStream input, OutputStream output) throws IOException {
        copyFromStreamToStream(input, output, true, true);
    }

    public static void copyFromStreamToStream(InputStream input, OutputStream output, boolean closeInputStream, boolean closeOutputStream) throws IOException {
        try {
            byte[] buff = new byte[2048];
            int length = 0;
            while ((length = input.read(buff)) > 0) {
                output.write(buff, 0, length);
                output.flush();
            }
            if (closeOutputStream) {
                close(output);
            }
            if (closeInputStream) {
                close(input);
            }

            if (closeOutputStream) {
                close(output);
            }
            if (closeInputStream) {
                close(input);
            }
        } catch (IOException e) {
            LOGGER.error("copyFromStreamToStream", e);
            //do nothing (prints to much in the log when user aborts the request)
        }
    }

    public static void close(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
                LOGGER.error("copyFromStreamToStream", e);

            }
        }
    }

    public static void close(Writer writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (Exception e) {
            LOGGER.error("close", e);
        }
    }

    public static void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
                LOGGER.error("close", e);

            }
        }
    }


}
