package com.example.seminar04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seminar04.adapter.MainActivityAdapter;
import com.example.seminar04.api.Api;
import com.example.seminar04.controller.RetrofitClient;
import com.example.seminar04.model.Address;
import com.example.seminar04.model.AddressQuery;
import com.example.seminar04.model.Offices;
import com.example.seminar04.model.Officials;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView loc;
    @BindView(R.id.official_recycler)
    RecyclerView officialRecycler;
    @BindView(R.id.topAppBar)
    MaterialToolbar topAppBar;
    @BindView(R.id.noNetworking)
    LinearLayout noNetworking;
    @BindView(R.id.hasNetworking)
    LinearLayout hasNetworking;
    private String location;


    MainActivityAdapter mainActivityAdapter;
    List<Offices> officesList = new ArrayList<>();
    List<Officials> officialsList = new ArrayList<>();
    private MainActivityAdapter.officialClickListener officialClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        // Bind element
        ButterKnife.bind(this);

        if (isOnline()) {
            hasNetworking.setVisibility(View.VISIBLE);
        } else {
            noNetworking.setVisibility(View.VISIBLE);
        }

        System.out.println("================ " + location);

        loc = findViewById(R.id.location);

        new FetchAPI().execute();

    }

    public boolean isOnline() {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("No networking", e.getMessage());
        }
        return connected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:  {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.search: {
                LocationDialog();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void LocationDialog() {
        final EditText locationText = new EditText(MainActivity.this);
        AlertDialog.Builder locationDialog = new AlertDialog.Builder(MainActivity.this);
        locationDialog.setTitle("Enter a City, State or a Zip Code:");
        locationDialog.setView(locationText);

        locationDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                location = locationText.getText().toString();
                new FetchAPI().execute();
            }
        });

        locationDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        locationDialog.show();
    }

    private class FetchAPI extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Api apiInterface = RetrofitClient.getRetrofit().create(Api.class);

            Call<AddressQuery> call;
            if (location != null) {
                call = apiInterface.searchAddress(location);
            } else {
                call = apiInterface.searchAddress("1263 Pacific Ave. Kansas City, KS");
            }

            call.enqueue(new Callback<AddressQuery>() {
                @Override
                public void onResponse(Call<AddressQuery> call, Response<AddressQuery> response) {
                    if (response.isSuccessful()) {
                        officesList = response.body().getOffices();
                        officialsList = response.body().getOfficials();

                        loc.setText(response.body().getNormalizedInput().getCity() + " " + response.body().getNormalizedInput().getState() + " " + response.body().getNormalizedInput().getZip());

                        System.out.println("-_._-_._-_._- Response successful -_._-_._-_._-");

                        // Event Click each RecyclerView
                        officialClickListener = new MainActivityAdapter.officialClickListener() {
                            @Override
                            public void onClick(View v, int position) {
                                Intent intent = new Intent(getApplicationContext(), OfficialActivity.class);
                                intent.putExtra("location", response.body().getNormalizedInput().getCity() + " " + response.body().getNormalizedInput().getState() + " " + response.body().getNormalizedInput().getZip());
                                
                                //intent.putExtra("governmentOfficials", officesList.get(position).getName());
                                intent.putExtra("officialName", officialsList.get(position).getName());
                                if (officialsList.get(position).getAddress() != null) {
                                    intent.putExtra("address", officialsList.get(position).getAddress().get(0).getLine1()
                                            + " " + officialsList.get(position).getAddress().get(0).getCity()
                                            + ", " + officialsList.get(position).getAddress().get(0).getState()
                                            + " " + officialsList.get(position).getAddress().get(0).getZip());
                                }
                                intent.putExtra("urls", officialsList.get(position).getUrls().get(0));
                                intent.putExtra("phones", officialsList.get(position).getPhones().get(0));
                                if (officialsList.get(position).getChannels() != null) {
                                    intent.putExtra("channels", officialsList.get(position).getChannels());
                                }

                                if (officialsList.get(position).getEmails() != null) {
                                    intent.putExtra("emails", officialsList.get(position).getEmails().get(0));
                                }

                                intent.putExtra("photoUrl", officialsList.get(position).getPhotoUrl());
                                intent.putExtra("party", officialsList.get(position).getParty());
                                
                                for (int i = 0; i < officesList.size(); i++) {
                                    for (int j = 0; j < officesList.get(i).getOfficialIndices().size(); j++) {
                                        if (officesList.get(i).getOfficialIndices().get(j) == position) {
                                            intent.putExtra("governmentOfficials", officesList.get(i).getName());
                                            startActivity(intent);
                                        }
                                    }
                                }
                            }
                        };
                        mainActivityAdapter = new MainActivityAdapter(MainActivity.this, officesList, officialsList, officialClickListener);
                        officialRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        // Dau phan cach item
                        officialRecycler.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
                        officialRecycler.setAdapter(mainActivityAdapter);
                    }
                }

                @Override
                public void onFailure(Call<AddressQuery> call, Throwable t) {
                    System.out.println("-_._-_._-_._- Response failure -_._-_._-_._-");
                }
            });
            return null;
        }
    }
}