package com.bestlabs.facerecoginination.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaveCategoryModel {
    private String status;
    private String message;
    private List<LeaveCategory> result;

    // Getter and setter methods
    // You can generate these using your IDE or manually write them
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<LeaveCategory> getResult() {
        return result;
    }


    public class LeaveCategory {
        private int value;
        private String label;

        // Getter and setter methods
        // You can generate these using your IDE or manually write them

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}

