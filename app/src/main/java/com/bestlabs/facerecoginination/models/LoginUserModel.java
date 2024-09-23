package com.bestlabs.facerecoginination.models;

import com.google.gson.annotations.SerializedName;

public class LoginUserModel {
    @SerializedName("status")
    private String status;

    @SerializedName("result")
    private Result result;

    public String getStatus() {
        return status;
    }

    public Result getResult() {
        return result;
    }

    public static class Result {
        @SerializedName("EmployeeID")
        private int employeeID;

        @SerializedName("EmployeeCode")
        private String employeeCode;

        @SerializedName("EmployeeName")
        private String employeeName;

        @SerializedName("EmployeeClientID")
        private int employeeClientID;

        @SerializedName("EmployeeCompanyID")
        private int employeeCompanyID;

        @SerializedName("faceImage")
        private String faceImage;

        @SerializedName("allowApprove")
        private String allowApprove;

        private String token;

        public int getEmployeeID() {
            return employeeID;
        }

        public String getEmployeeCode() {
            return employeeCode;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public int getEmployeeClientID() {
            return employeeClientID;
        }

        public int getEmployeeCompanyID() {
            return employeeCompanyID;
        }

        public String getToken() {
            return token;
        }

        public String getFaceImage() {
            return faceImage;
        }

        public void setFaceImage(String faceImage) {
            this.faceImage = faceImage;
        }

        public String getAllowApprove() {
            return allowApprove;
        }

        public void setAllowApprove(String allowApprove) {
            this.allowApprove = allowApprove;
        }

    }
}
