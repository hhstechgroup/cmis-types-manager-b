package com.engagepoint.utils;

/**
 * User: vyacheslav.polulyakh (vyacheslav.polulyakh@engagepoint.com )
 * Date: 12/19/13
 * Time: 4:09 PM
 */

public class CustomStringUtils extends org.apache.commons.lang.StringUtils{

    public static String concatenate(String...strings){
        StringBuilder builder = new StringBuilder();
        for (String str : strings){
            builder.append(str);
        }
        return builder.toString();
    }
}
