package com.bestlabs.facerecoginination.models;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordModel {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    public String getTemp_otp() {
        return temp_otp;
    }

    public void setTemp_otp(String temp_otp) {
        this.temp_otp = temp_otp;
    }

    @SerializedName("temp_otp")
    private String temp_otp;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
