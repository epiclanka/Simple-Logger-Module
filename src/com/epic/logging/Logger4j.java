/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author thilina_h
 *
 *
 *
 * http://stackoverflow.com/questions/22028399/log4j2-property-substitution-default
 * Looking at
 * http://logging.apache.org/log4j/2.x/manual/configuration.html#PropertySubstitution
 * you can specify a default property map in the configuration file. That takes
 * this form:
 *
 * <Configuration status="debug">
 * <Properties>
 * <Property name="oauthLoginLogPath">default/location/of/oauth2.log</Property>
 * </Properties>
 * ...
 * <Appenders>
 * <Appender type="File" name="File" fileName="${sys:oauthLoginLogPath}">
 * .... </Configuration
 *
 * Then, if you start your app with system property
 * -DoauthLoginLogPath=/path/oauth2.log, the File appender fileName value will
 * first be looked up in system properties, but if that fails, it will fall back
 * to the property defined in the Properties section at the top of the
 * log4j2.xml configuration file.
 *
 * By the way, the env prefix is for environment variables (like %PATH% on
 * Windows), and is not related to sys, which is Java system properties. See
 * also http://logging.apache.org/log4j/2.x/manual/lookups.html
 *
 * ------------------------------
 *
 *
 *
 * You can use the same ${sys:propName:-default} syntax like in maven (notice
 * the ':-').
 *
 * Here is the documentation of the string substitutor that log4j2 uses:
 * https://logging.apache.org/log4j/log4j-2.2/log4j-core/apidocs/org/apache/logging/log4j/core/lookup/StrSubstitutor.html
 *
 * To use the same example:
 *
 * <Configuration status="debug"> ...
 * <Appenders>
 * <Appender type="File" name="File" fileName="${sys:oauthLoginLogPath:-default/location/of/oauth2.log}">
 * ....
 * </Appenders>
 * </Configuration>
 *
 *
 *
 */
public class Logger4j {

    static {
      
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {//if log.home is not set set default path
            System.setProperty("log.home", System.getProperty("log.home", "C:/EPIC/mserverLogs2"));
        } else {
            System.setProperty("log.home", System.getProperty("log.home","etc/epic/mserverLogs"));
        }
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");//set async logging

        LOGGR = LogManager.getRootLogger();
        REQ_LOGGER = LogManager.getLogger("LogRequest");
        RESP_LOGGER = LogManager.getLogger("LogReply");
    }
    private static final Logger LOGGR;
    private static final Logger REQ_LOGGER;
    private static final Logger RESP_LOGGER;
    private static final Logger4j INSTANCE = new Logger4j();

    private Logger4j() {

    }

    public static Logger4j getLogger() {
        return INSTANCE;
    }

    /**
     * logs caller method with class name
     *
     * @param msg
     */
    public void fatal(String msg) {
        LogManager.getLogger(getCallerClass_MethodName()).fatal(msg);
    }

    public void fatal(String msg, Throwable thr) {
        LogManager.getLogger(getCallerClass_MethodName()).fatal(msg, thr);
    }

    /**
     * logs caller method with class name
     *
     * @param msg
     */
    public void error(String msg) {

        LogManager.getLogger(getCallerClass_MethodName()).error(msg);
    }

    /**
     * logs caller method with class name
     *
     * @param msg
     * @param thr
     */
    public void error(String msg, Throwable thr) {

        LogManager.getLogger(getCallerClass_MethodName()).error(msg, thr);
    }

    public void warn(String msg) {
        LOGGR.warn(msg);
    }

    /**
     * logs caller method with class name
     *
     * @param msg
     */
    public void warn_with_caller(String msg) {
        LogManager.getLogger(getCallerClass_MethodName()).warn(msg);
    }

    public void info(String msg) {
        LOGGR.info(msg);
    }

    /**
     * logs caller method with class name
     *
     * @param msg
     */
    public void info_with_caller(String msg) {
        LogManager.getLogger(getCallerClass_MethodName()).info(msg);
    }

    public void debug(String msg) {
        LogManager.getLogger(getCallerClass_MethodName()).debug(msg);
    }

    /**
     * logs caller method with class name
     *
     * @param msg
     * @param thr
     */
    public void debug(String msg, Throwable thr) {

        LogManager.getLogger(getCallerClass_MethodName()).debug(msg, thr);
    }

    public void trace(String msg, Throwable thr) {

        LogManager.getLogger(getCallerClass_MethodName()).trace(msg, thr);
    }
    
    public void log_request(String msg){
        REQ_LOGGER.info(msg);
    }
    public void log_response(String msg){
        RESP_LOGGER.info(msg);
    }

    public Logger getREQ_LOGGER() {
        return REQ_LOGGER;
    }

    public Logger getRESP_LOGGER() {
        return RESP_LOGGER;
    }

    public static StackTraceElement getCallerStackTraceElement() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[3];
    }

    public static String getCallerClass() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[3].getClassName();
    }

    public static String getCallerMethodName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[3].getMethodName();
    }

    public static String getCallerClass_MethodName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTraceElements[3];
        return element.getClassName() + "." + element.getMethodName();
    }

}
