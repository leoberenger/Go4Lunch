package com.openclassrooms.go4lunch.models.googlemaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @SerializedName("periods")
    @Expose
    private List<Period> periods = null;
    @SerializedName("weekday_text")
    @Expose
    private List<Object> weekdayText = null;

    public Boolean getOpenNow() {
        return openNow;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public List<Object> getWeekdayText() {
        return weekdayText;
    }


    public class Period {

        @SerializedName("close")
        @Expose
        private Close close;
        @SerializedName("open")
        @Expose
        private Open open;

        public Close getClose() {
            return close;
        }

        public class Close {

            @SerializedName("day")
            @Expose
            private Integer day;
            @SerializedName("time")
            @Expose
            private String time;

            public Integer getDay() {
                return day;
            }

            public String getTime() {
                return time;
            }
        }

        public Open getOpen() {
            return open;
        }

        public class Open {

            @SerializedName("day")
            @Expose
            private Integer day;
            @SerializedName("time")
            @Expose
            private String time;

            public Integer getDay() {
                return day;
            }

            public String getTime() {
                return time;
            }
        }
    }
}
