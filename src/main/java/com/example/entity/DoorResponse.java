package com.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DoorResponse {
    @JsonProperty("state")
    private State state;

    @JsonProperty("data")
    private String data;

    public DoorResponse() {
    }

    public DoorResponse(State state, String data) {
        this.state = state;
        this.data = data;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}