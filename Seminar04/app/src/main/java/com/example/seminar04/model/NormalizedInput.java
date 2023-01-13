package com.example.seminar04.model;

import com.google.gson.annotations.SerializedName;

public class NormalizedInput {
    @SerializedName("line1")
    private String line1;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("zip")
    private String zip;

    public NormalizedInput(String city, String state, String zip) {
        this.city = city;
        this.state = state;
        this.zip = zip;
    }


    public String getLine1() {
        return line1;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }
}
