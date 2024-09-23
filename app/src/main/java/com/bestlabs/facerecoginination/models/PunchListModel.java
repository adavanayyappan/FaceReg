package com.bestlabs.facerecoginination.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PunchListModel {
    private String status;
    private String message;
    private List<PunchListItem> result;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<PunchListItem> getResult() {
        return result;
    }

    public class PunchListItem {
        @SerializedName("dateformat")
        private String dateFormat;

        @SerializedName("date")
        private String date;

        @SerializedName("day")
        private String day;

        @SerializedName("month")
        private String month;

        @SerializedName("satrtTime")
        private String startTime;

        @SerializedName("endTime")
        private String endTime;

        @SerializedName("totalHrs")
        private String totalHours;

        public String getDateFormat() {
            return dateFormat;
        }

        public String getDate() {
            return date;
        }

        public String getDay() {
            return day;
        }

        public String getMonth() {
            return month;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getTotalHours() {
            return totalHours;
        }
    }
}

