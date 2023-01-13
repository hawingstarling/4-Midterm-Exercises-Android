package com.example.seminar04.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Offices {
    @SerializedName("name")
    private String name;
    @SerializedName("officialIndices")
    private ArrayList<Integer> officialIndices;

    public Offices(String name, ArrayList<Integer> officialIndices) {
        this.name = name;
        this.officialIndices = officialIndices;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getOfficialIndices() {
        return officialIndices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOfficialIndices(ArrayList<Integer> officialIndices) {
        this.officialIndices = officialIndices;
    }
}
