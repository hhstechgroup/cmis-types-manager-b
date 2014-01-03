package com.engagepoint.util;

/**
 * User: vyacheslav.polulyakh (vyacheslav.polulyakh@engagepoint.com )
 * Date: 12/19/13
 * Time: 4:09 PM
 */

public class CustomStringUtils {
    public static String concat(String... strings){
        StringBuilder builder = new StringBuilder();
        for (String str : strings){
            builder.append(str);
        }
        return builder.toString();
    }
}
