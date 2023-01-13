package com.example.seminar04.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Officials {
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private ArrayList<Address> address;
    @SerializedName("party")
    private String party;
    @SerializedName("phones")
    private ArrayList<String> phones;
    @SerializedName("urls")
    private ArrayList<String> urls;
    @SerializedName("emails")
    private ArrayList<String> emails;
    @SerializedName("photoUrl")
    private String photoUrl;
    @SerializedName("channels")
    private ArrayList<Channels> channels;

    public Officials(String name, ArrayList<Address> address, String party, ArrayList<String> phones, ArrayList<String> urls, ArrayList<String> emails, String photoUrl, ArrayList<Channels> channels) {
        this.name = name;
        this.address = address;
        this.party = party;
        this.phones = phones;
        this.urls = urls;
        this.emails = emails;
        this.photoUrl = photoUrl;
        this.channels = channels;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Address> getAddress() {
        return address;
    }

    public String getParty() {
        return party;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public ArrayList<String> getEmails() {
        return emails;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public ArrayList<Channels> getChannels() {
        return channels;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setChannels(ArrayList<Channels> channels) {
        this.channels = channels;
    }
}
