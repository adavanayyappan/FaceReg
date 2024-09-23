package com.bestlabs.facerecoginination.models;

import com.google.gson.annotations.SerializedName;

public class FaceAddResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("faceImage")
    private String faceImage;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getFaceImage() {
        return faceImage;
    }
}

