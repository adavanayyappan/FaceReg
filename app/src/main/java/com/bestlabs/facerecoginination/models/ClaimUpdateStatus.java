package com.bestlabs.facerecoginination.models;

public class ClaimUpdateStatus {
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

    public String getClaimID() {
        return ClaimID;
    }

    public void setClaimID(String claimID) {
        ClaimID = claimID;
    }

    private String status;
    private String message;
    private String ClaimID;
}
