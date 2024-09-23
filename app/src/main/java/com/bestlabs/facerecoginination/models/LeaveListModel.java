package com.bestlabs.facerecoginination.models;

import java.util.List;

public class LeaveListModel {

    private String status;
    private String message;
    private List<LeaveType> result;

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

    public List<LeaveType> getResult() {
        return result;
    }

    public void setResult(List<LeaveType> result) {
        this.result = result;
    }

    public static class LeaveType {
        private String label;
        private int value;
        private int balanceleave;

        // Getters and Setters
        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getBalanceleave() {
            return balanceleave;
        }

        public void setBalanceleave(int balanceleave) {
            this.balanceleave = balanceleave;
        }
    }
}
