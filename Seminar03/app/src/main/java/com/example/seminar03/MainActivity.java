package com.example.seminar03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.seminar03.api.Api;
import com.example.seminar03.controller.RetrofitClient;
import com.example.seminar03.model.RssFeed;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Api apiInterface = RetrofitClient.getRetrofit().create(Api.class);
        Call<RssFeed> call = apiInterface.getFeed();

        call.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                if (response.isSuccessful()) {
                    System.out.println("-_._-_._-_._- Response successful -_._-_._-_._-");
                    System.out.println("Data response: " + response.body().channel.item.get(0).mediaContent.url);
                }
            }

            @Override
            public void onFailure(Call<RssFeed> call, Throwable t) {
                System.out.println("-_._-_._-_._- Response failure -_._-_._-_._-" + t);
            }
        });

    }
}