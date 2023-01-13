package com.example.seminar03.api;

import com.example.seminar03.model.RssFeed;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("212-pet-food-ingredients")
    Call<RssFeed> getFeed();
}
