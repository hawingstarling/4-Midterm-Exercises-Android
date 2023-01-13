package com.example.seminar04.api;

import com.example.seminar04.model.AddressQuery;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("representatives?key=AIzaSyDiFbom0eseFuqVrgACExv3kmwB6xQvfPY")
//    &address=1263 Pacific Ave. Kansas City, KS
    Call<AddressQuery> searchAddress(@Query("address") String address);
}
