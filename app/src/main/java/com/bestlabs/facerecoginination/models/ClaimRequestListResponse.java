package com.bestlabs.facerecoginination.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClaimRequestListResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private List<Claim> result;

    @SerializedName("totalRecords")
    private int totalRecords;

    @SerializedName("totalPages")
    private int totalPages;

    // Getters and Setters
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

    public List<Claim> getResult() {
        return result;
    }

    public void setResult(List<Claim> result) {
        this.result = result;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public class Claim {

        @SerializedName("ClaimLimitName")
        private String claimLimitName;

        @SerializedName("ClaimGroupID")
        private int claimGroupID;

        @SerializedName("ClaimGroupCode")
        private String claimGroupCode;

        @SerializedName("ClaimGroupName")
        private String claimGroupName;

        @SerializedName("ClaimID")
        private int claimID;

        @SerializedName("ClaimEmpID")
        private int claimEmpID;

        @SerializedName("ClaimLimitPkID")
        private int claimLimitPkID;

        @SerializedName("ClaimGroupPkID")
        private int claimGroupPkID;

        @SerializedName("ClaimAmount")
        private String claimAmount;

        @SerializedName("ClaimRemarks")
        private String claimRemarks;

        @SerializedName("ClaimAttachment")
        private String claimAttachment;

        @SerializedName("ClaimStatus")
        private String claimStatus;

        @SerializedName("ClaimCreatedOn")
        private String claimCreatedOn;

        // Getters and Setters
        public String getClaimLimitName() {
            return claimLimitName;
        }

        public void setClaimLimitName(String claimLimitName) {
            this.claimLimitName = claimLimitName;
        }

        public int getClaimGroupID() {
            return claimGroupID;
        }

        public void setClaimGroupID(int claimGroupID) {
            this.claimGroupID = claimGroupID;
        }

        public String getClaimGroupCode() {
            return claimGroupCode;
        }

        public void setClaimGroupCode(String claimGroupCode) {
            this.claimGroupCode = claimGroupCode;
        }

        public String getClaimGroupName() {
            return claimGroupName;
        }

        public void setClaimGroupName(String claimGroupName) {
            this.claimGroupName = claimGroupName;
        }

        public int getClaimID() {
            return claimID;
        }

        public void setClaimID(int claimID) {
            this.claimID = claimID;
        }

        public int getClaimEmpID() {
            return claimEmpID;
        }

        public void setClaimEmpID(int claimEmpID) {
            this.claimEmpID = claimEmpID;
        }

        public int getClaimLimitPkID() {
            return claimLimitPkID;
        }

        public void setClaimLimitPkID(int claimLimitPkID) {
            this.claimLimitPkID = claimLimitPkID;
        }

        public int getClaimGroupPkID() {
            return claimGroupPkID;
        }

        public void setClaimGroupPkID(int claimGroupPkID) {
            this.claimGroupPkID = claimGroupPkID;
        }

        public String getClaimAmount() {
            return claimAmount;
        }

        public void setClaimAmount(String claimAmount) {
            this.claimAmount = claimAmount;
        }

        public String getClaimRemarks() {
            return claimRemarks;
        }

        public void setClaimRemarks(String claimRemarks) {
            this.claimRemarks = claimRemarks;
        }

        public String getClaimAttachment() {
            return claimAttachment;
        }

        public void setClaimAttachment(String claimAttachment) {
            this.claimAttachment = claimAttachment;
        }

        public String getClaimStatus() {
            return claimStatus;
        }

        public void setClaimStatus(String claimStatus) {
            this.claimStatus = claimStatus;
        }

        public String getClaimCreatedOn() {
            return claimCreatedOn;
        }

        public void setClaimCreatedOn(String claimCreatedOn) {
            this.claimCreatedOn = claimCreatedOn;
        }
    }

}

