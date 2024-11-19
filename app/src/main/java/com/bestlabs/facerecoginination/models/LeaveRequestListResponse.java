package com.bestlabs.facerecoginination.models;

import java.util.List;

public class LeaveRequestListResponse {
    private String status;
    private String message;
    private List<Leave> result;
    private int totalRecords;
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

    public List<Leave> getResult() {
        return result;
    }

    public void setResult(List<Leave> result) {
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

    public class Leave {
        private String LeaveGroupCode;
        private String LeaveGroupName;
        private int leaveID;
        private int leaveClientID;
        private int leaveEmpID;
        private String leaveFrom;
        private String leaveTo;
        private int leaveNoOfDays;
        private String leaveRemarks;
        private String leaveAttachment;
        private String leaveStatus;
        private String leaveCreatedOn;

        // Getters and Setters
        public String getLeaveGroupCode() {
            return LeaveGroupCode;
        }

        public void setLeaveGroupCode(String leaveGroupCode) {
            LeaveGroupCode = leaveGroupCode;
        }

        public String getLeaveGroupName() {
            return LeaveGroupName;
        }

        public void setLeaveGroupName(String leaveGroupName) {
            LeaveGroupName = leaveGroupName;
        }

        public int getLeaveID() {
            return leaveID;
        }

        public void setLeaveID(int leaveID) {
            this.leaveID = leaveID;
        }

        public int getLeaveClientID() {
            return leaveClientID;
        }

        public void setLeaveClientID(int leaveClientID) {
            this.leaveClientID = leaveClientID;
        }

        public int getLeaveEmpID() {
            return leaveEmpID;
        }

        public void setLeaveEmpID(int leaveEmpID) {
            this.leaveEmpID = leaveEmpID;
        }

        public String getLeaveFrom() {
            return leaveFrom;
        }

        public void setLeaveFrom(String leaveFrom) {
            this.leaveFrom = leaveFrom;
        }

        public String getLeaveTo() {
            return leaveTo;
        }

        public void setLeaveTo(String leaveTo) {
            this.leaveTo = leaveTo;
        }

        public int getLeaveNoOfDays() {
            return leaveNoOfDays;
        }

        public void setLeaveNoOfDays(int leaveNoOfDays) {
            this.leaveNoOfDays = leaveNoOfDays;
        }

        public String getLeaveRemarks() {
            return leaveRemarks;
        }

        public void setLeaveRemarks(String leaveRemarks) {
            this.leaveRemarks = leaveRemarks;
        }

        public String getLeaveAttachment() {
            return leaveAttachment;
        }

        public void setLeaveAttachment(String leaveAttachment) {
            this.leaveAttachment = leaveAttachment;
        }

        public String getLeaveStatus() {
            return leaveStatus;
        }

        public void setLeaveStatus(String leaveStatus) {
            this.leaveStatus = leaveStatus;
        }

        public String getLeaveCreatedOn() {
            return leaveCreatedOn;
        }

        public void setLeaveCreatedOn(String leaveCreatedOn) {
            this.leaveCreatedOn = leaveCreatedOn;
        }
    }
}

