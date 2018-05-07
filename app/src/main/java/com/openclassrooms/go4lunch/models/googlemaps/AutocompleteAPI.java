package com.openclassrooms.go4lunch.models.googlemaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutocompleteAPI {
    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public String getStatus() {
        return status;
    }


    public class Prediction {

        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("place_id")
        @Expose
        private String placeId;
        @SerializedName("reference")
        @Expose
        private String reference;
        @SerializedName("types")
        @Expose
        private List<String> types = null;

        public String getDescription() {
            return description;
        }

        public String getId() {
            return id;
        }


        public String getPlaceId() {
            return placeId;
        }


        public String getReference() {
            return reference;
        }

        public List<String> getTypes() {
            return types;
        }
    }

}
