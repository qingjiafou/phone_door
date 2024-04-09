package com.example.entity;

public class User {
    private String USER_NAME;
    private String USER_ID;
    private String USER_PASSWORD;
    private String FACE_INFORMATION;
    private String FINGER_INFORMATION;
    private int ADMIN_AUTORITY;
    private String DOOR_ID;
    private String DOOR_NUMBER;
    private String DOOR_UNION;
    private String AUTHORIZATION_TOKEN;

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSER_PASSWORD() {
        return USER_PASSWORD;
    }

    public void setUSER_PASSWORD(String USER_PASSWORD) {
        this.USER_PASSWORD = USER_PASSWORD;
    }

    public String getFACE_INFORMATION() {
        return FACE_INFORMATION;
    }

    public void setFACE_INFORMATION(String FACE_INFORMATION) {
        this.FACE_INFORMATION = FACE_INFORMATION;
    }

    public String getFINGER_INFORMATION() {
        return FINGER_INFORMATION;
    }

    public void setFINGER_INFORMATION(String FINGER_INFORMATION) {
        this.FINGER_INFORMATION = FINGER_INFORMATION;
    }

    public int getADMIN_AUTORITY() {
        return ADMIN_AUTORITY;
    }

    public void setADMIN_AUTORITY(int ADMIN_AUTORITY) {
        this.ADMIN_AUTORITY = ADMIN_AUTORITY;
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

    public String getAUTHORIZATION_TOKEN() {
        return AUTHORIZATION_TOKEN;
    }

    public void setAUTHORIZATION_TOKEN(String AUTHORIZATION_TOKEN) {
        this.AUTHORIZATION_TOKEN = AUTHORIZATION_TOKEN;
    }

}