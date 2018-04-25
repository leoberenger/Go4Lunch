package com.openclassrooms.go4lunch.models.googlemaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesAPI {
    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public List<Result> getResults() {
        return results;
    }

    public Result getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }

    public class Result {

        @SerializedName("address_components")
        @Expose
        private List<AddressComponent> addressComponents = null;
        @SerializedName("adr_address")
        @Expose
        private String adrAddress;
        @SerializedName("formatted_address")
        @Expose
        private String formattedAddress;
        @SerializedName("formatted_phone_number")
        @Expose
        private String formattedPhoneNumber;
        @SerializedName("geometry")
        @Expose
        private Geometry geometry;
        @SerializedName("icon")
        @Expose
        private String icon;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("international_phone_number")
        @Expose
        private String internationalPhoneNumber;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("opening_hours")
        @Expose
        private OpeningHours openingHours;
        @SerializedName("photos")
        @Expose
        private List<Photo> photos = null;
        @SerializedName("place_id")
        @Expose
        private String placeId;
        @SerializedName("rating")
        @Expose
        private Double rating;
        @SerializedName("reference")
        @Expose
        private String reference;
        @SerializedName("reviews")
        @Expose
        private List<Review> reviews = null;
        @SerializedName("scope")
        @Expose
        private String scope;
        @SerializedName("types")
        @Expose
        private List<String> types = null;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("utc_offset")
        @Expose
        private Integer utcOffset;
        @SerializedName("vicinity")
        @Expose
        private String vicinity;
        @SerializedName("website")
        @Expose
        private String website;

        public List<AddressComponent> getAddressComponents() {
            return addressComponents;
        }

        public String getAdrAddress() {
            return adrAddress;
        }

        public String getFormattedAddress() {
            return formattedAddress;
        }

        public String getFormattedPhoneNumber() {
            return formattedPhoneNumber;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public String getIcon() {
            return icon;
        }

        public String getId() {
            return id;
        }

        public String getInternationalPhoneNumber() {
            return internationalPhoneNumber;
        }

        public String getName() {
            return name;
        }

        public OpeningHours getOpeningHours() {
            return openingHours;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public String getPlaceId() {
            return placeId;
        }

        public Double getRating() {
            return rating;
        }

        public String getReference() {
            return reference;
        }

        public List<Review> getReviews() {
            return reviews;
        }

        public String getScope() {
            return scope;
        }

        public List<String> getTypes() {
            return types;
        }

        public String getUrl() {
            return url;
        }

        public Integer getUtcOffset() {
            return utcOffset;
        }

        public String getVicinity() {
            return vicinity;
        }

        public String getWebsite() {
            return website;
        }
    }
}