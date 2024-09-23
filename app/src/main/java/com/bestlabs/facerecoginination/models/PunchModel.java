package com.bestlabs.facerecoginination.models;

import com.google.gson.annotations.SerializedName;


public class PunchModel {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("satrtTime") // Typo in JSON key, should be "startTime"
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    // Getters and setters for the fields

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
