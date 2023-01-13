package com.example.seminar04.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Channels implements Serializable {
    @SerializedName("type")
    private String type;
    @SerializedName("id")
    private String id;

    public Channels(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
