package com.example.seminar04.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddressQuery {
    // SerializedName để map giữa JSON keys và field object
    @SerializedName("normalizedInput")
    private NormalizedInput normalizedInput;
    @SerializedName("offices")
    private ArrayList<Offices> offices;
    @SerializedName("officials")
    private ArrayList<Officials> officials;

    public AddressQuery(NormalizedInput normalizedInput, ArrayList<Offices> offices, ArrayList<Officials> officials) {
        this.normalizedInput = normalizedInput;
        this.offices = offices;
        this.officials = officials;
    }

    public NormalizedInput getNormalizedInput() {
        return normalizedInput;
    }

    public ArrayList<Offices> getOffices() {
        return offices;
    }

    public ArrayList<Officials> getOfficials() {
        return officials;
    }
}
