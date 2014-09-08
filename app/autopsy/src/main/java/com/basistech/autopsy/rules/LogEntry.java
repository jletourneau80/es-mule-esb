package com.basistech.autopsy.rules;

/**
 * Created by IntelliJ IDEA.
 * User: jletourneau
 * Date: Feb 24, 2010
 * Time: 10:24:25 AM
 * To change this template use File | Settings | File Templates.
 */
import java.util.Date;
import java.util.UUID;

public class LogEntry {


    private int id;
    private String session;
    private String level;
    private Date timeOccured;
    private String logger;
    private String className;
    private String methodName;
    private String oldValue;
    private String currentValue;
    private String message;

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String newValue) {
        this.currentValue = newValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LogEntry() {}

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setLevel(String level) {
        this.level = level;
    }

   

    public Date getTimeOccured() {
        return timeOccured;
    }

    public void setTimeOccured(Date date) {
        this.timeOccured = date;
    }

    public String getLevel() {
        return level;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
