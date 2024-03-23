package com.bestlabs.facerecoginination.models;


public class LeaveModel {
    String name;
    String reason;
    String emp_id;
    String date;
    String status;

    public LeaveModel(String name, String reason, String emp_id, String date, String status) {
        this.name = name;
        this.reason = reason;
        this.emp_id = emp_id;
        this.date = date;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
