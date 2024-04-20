package com.example.entity;

import java.sql.Date;

public class UserTime {
    private String USER_ID;
    private String DOOR_ID;
    private String DOOR_NUMBER;
    private String DOOR_UNION;
    private Date now_time;

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

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

    public Date getnow_time() {
        return now_time;
    }

    public void setnow_time(Date now_time) {
        this.now_time = now_time;
    }
}
