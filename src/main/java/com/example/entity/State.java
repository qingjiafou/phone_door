package com.example.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

public class State {
    private int state_code;
    private String state_msg;

    public int getstate_code() {
        return state_code;
    }

    public void setstate_code(int state_code) {
        this.state_code = state_code;
    }

    public String getstate_msg() {
        return state_msg;
    }

    public void setstate_msg(String state_msg) {
        this.state_msg = state_msg;
    }

    // Convert object to JSON string
    public String toJSONString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"state_code\": 500, \"state_msg\": \"Failed to serialize state object to JSON.\"}";
        }
    }

}
