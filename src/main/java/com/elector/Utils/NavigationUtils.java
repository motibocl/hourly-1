package com.elector.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NavigationUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationUtils.class);

    public static final String PATH_TO_ELECTOR_DIRECTORY = "C:\\elector";
    private static final String PATH_TO_COMMON_VOTERS_BOOKS = String.format("%s\\voters_books", PATH_TO_ELECTOR_DIRECTORY);


    public static String getPathToCommonVotersBookFiles (int campaignOid) {
        return String.format("%s\\%s.csv", PATH_TO_COMMON_VOTERS_BOOKS, campaignOid);
    }


}
