package com.example.seminar04.model;

import com.google.gson.annotations.SerializedName;

public class Address {
    @SerializedName("line1")
    private String line1;
    @SerializedName("line2")
    private String line2;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("zip")
    private String zip;

    public Address(String line1, String line2, String city, String state, String zip) {
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
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
