package com.amplify.stofor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogUtils {
    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("\\.(\\w+)$");
    private static final String LOG_IDENTIFIER = "amp.";
    private static final String LOGGER_NAME_PATTERN = LOG_IDENTIFIER + "%s";
    private static final int LOGGER_NAME_LENGTH_LIMIT = 23;

    private LogUtils(){}

    public static Logger getLogger(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String fullClassNameOfRequestingClass = stackTraceElements[3].getClassName();
        Matcher matcher = CLASS_NAME_PATTERN.matcher(fullClassNameOfRequestingClass);
        if(!matcher.find()){
            return LoggerFactory.getLogger(String.format(LOGGER_NAME_PATTERN, "Unidentified"));
        }
        String className = matcher.group(1);
        if(loggerNameIsTooLong(className)){
            className = truncateClassName(className);
        }
        return LoggerFactory.getLogger(String.format(LOGGER_NAME_PATTERN, className));
    }

    private static String truncateClassName(String className) {
        int charsAllowed = LOGGER_NAME_LENGTH_LIMIT - LOG_IDENTIFIER.length();
        return className.substring(0, charsAllowed);
    }

    private static boolean loggerNameIsTooLong(String className) {
        return String.format(LOGGER_NAME_PATTERN, className).length() > LOGGER_NAME_LENGTH_LIMIT;
    }

    public static void info(Logger log, String format, Object... args){
        log.info(String.format(format, args));
    }

    public static void error(Logger log, Exception e, String format, Object... args){
        log.error(String.format(format, args), e);
    }

    public static void error(Logger log, String format,Object... args){
        log.error(String.format(format, args));
    }

    public static void error(Logger log, String format,Exception e){
        log.error(format, e);
    }


}
