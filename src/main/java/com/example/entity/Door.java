package com.example.entity;

import java.sql.Time;

public class Door {
    private String DOOR_ID;
    private String DOOR_NUMBER;
    private String DOOR_UNION;
    private String USER_ID;
    private Time START_TIME;
    private Time END_TIME;
    private int DOOR_STATUS;
    private String REMARK;
    private String ACTION;
    private String AUTHORIZATION_TOKEN;

    public String getDOOR_ID() {
        return DOOR_ID;
    }

    public void setDOOR_ID(String DOOR_ID) {
        this.DOOR_ID = DOOR_ID;
    }

    public String getDOOR_NUMBER() {
        return DOOR_NUMBER;
    }

    public void setDOOR_NUMBER(String DOOR_NUMBER) {
        this.DOOR_NUMBER = DOOR_NUMBER;
    }

    public String getDOOR_UNION() {
        return DOOR_UNION;
    }

    public void setDOOR_UNION(String DOOR_UNION) {
        this.DOOR_UNION = DOOR_UNION;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public Time getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(Time START_TIME) {
        this.START_TIME = START_TIME;
    }

    public Time getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(Time END_TIME) {
        this.END_TIME = END_TIME;
    }

    public int getDOOR_STATUS() {
        return DOOR_STATUS;
    }

    public void setDOOR_STATUS(int DOOR_STATUS) {
        this.DOOR_STATUS = DOOR_STATUS;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getAUTHORIZATION_TOKEN() {
        return AUTHORIZATION_TOKEN;
    }

    public void setAUTHORIZATION_TOKEN(String AUTHORIZATION_TOKEN) {
        this.AUTHORIZATION_TOKEN = AUTHORIZATION_TOKEN;
    }

    public String getACTION() {
        return ACTION;
    }

    public void setACTION(String ACTION) {
        this.ACTION = ACTION;
    }
}
