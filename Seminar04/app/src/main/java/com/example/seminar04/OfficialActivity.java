package com.example.seminar04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seminar04.model.Channels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfficialActivity extends AppCompatActivity {

    private String governmentOfficials;
    private String officialName;
    private String address;
    private String urls;
    private String photoUrl;
    private String phones;
    private String party;
    private String emails;
    private String loc;
    private String idFacebook;
    private String idTwitter;
    private String idGoogle;
    private String idYoutube;

    @BindView(R.id.government_officials)
    TextView governmentOfficialsTV;
    @BindView(R.id.officialName)
    TextView officialNameTV;
    @BindView(R.id.address)
    TextView addressTV;
    @BindView(R.id.urls)
    TextView urlsTV;
    @BindView(R.id.imageOfficial)
    ImageView photoUrlIV;
    @BindView(R.id.phones)
    TextView phonesTV;
    @BindView(R.id.party)
    TextView partyTV;
    @BindView(R.id.emails)
    TextView emailsTV;
    @BindView(R.id.bgParty)
    LinearLayout bgParty;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.channelsFacebook)
    ImageView channelsFacebook;
    @BindView(R.id.channelsGoogle)
    ImageView channelsGoogle;
    @BindView(R.id.channelsTwitter)
    ImageView channelsTwitter;
    @BindView(R.id.channelsYoutube)
    ImageView channelsYoutube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        ButterKnife.bind(this);
        ArrayList<Channels> channels;

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            governmentOfficials = extras.getString("governmentOfficials");
            officialName = extras.getString("officialName");
            address = extras.getString("address");
            urls = extras.getString("urls");
            photoUrl = extras.getString("photoUrl");
            phones = extras.getString("phones");
            party = extras.getString("party");
            loc = extras.getString("location");
            emails = extras.getString("emails");

            channels = (ArrayList<Channels>)getIntent().getSerializableExtra("channels");

            if (channels != null) {
                for (int c = 0; c < channels.size(); c++) {
                    switch (channels.get(c).getType()) {
                        case "Twitter":
                            channelsTwitter.setImageResource(R.drawable.twitter_image);
                            idTwitter = channels.get(c).getId();
                            break;
                        case "Youtube":
                            channelsYoutube.setImageResource(R.drawable.youtube_image);
                            idYoutube = channels.get(c).getId();
                            break;
                        case "Facebook":
                            channelsFacebook.setImageResource(R.drawable.facebook_image);
                            idFacebook = channels.get(c).getId();
                            break;
                        case "Google":
                            channelsGoogle.setImageResource(R.drawable.google_image);
                            idGoogle = channels.get(c).getId();
                            break;
                        default:
                            break;
                    }
                }
            }

            location.setText(loc);
            governmentOfficialsTV.setText(governmentOfficials);
            officialNameTV.setText(officialName);
            addressTV.setText(address);
            urlsTV.setText(urls);
            phonesTV.setText(phones);
            partyTV.setText("(" + party + ")");
            emailsTV.setText(emails != null ? emails : "No Data Provided");
            PicassoUpload(photoUrl);


            switch (party) {
                case "Democratic Party":
                    bgParty.setBackgroundColor(0xff0000ff);
                    break;
                case "Republican Party":
                    bgParty.setBackgroundColor(0xffff0000);
                    break;
                default:
                    bgParty.setBackgroundColor(0xff000000);
            }
        }

        photoUrlIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                intent.putExtra("governmentOfficials", governmentOfficials);
                intent.putExtra("officialName", officialName);
                intent.putExtra("photoUrl", photoUrl);
                intent.putExtra("party", party);
                intent.putExtra("location", extras.getString("location"));
                startActivity(intent);
            }
        });
    }

    public void PicassoUpload(String urlImage) {
        if (urlImage != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String changeUrl = urlImage.replace("http:", "https:");
                    picasso.load(changeUrl)
                            .error(R.drawable.no_image)
                            .placeholder(R.drawable.load_image)
                            .into(photoUrlIV);
                }
            }).build();

            picasso.load(urlImage)
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.load_image)
                    .into(photoUrlIV);
        } else {
            Picasso.with(this).load(urlImage)
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.load_image)
                    .into(photoUrlIV);
        }
    }

    public void facebookClick(View view) {
        String FACEBOOK_URL = "https://www.facebook.com/" + idFacebook;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana",0).versionCode;
            if (versionCode >= 3002850) {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "facebook.com/" + idFacebook;
            }
        } catch
        (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClick(View view) {
        Intent intent =
                null;
            String name = idTwitter;
            try {
                getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("twitter://user?screen_name=" + name));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" +
                        name));
            }
            startActivity(intent);
    }

    public void googleClick(View view) {
        String name = idGoogle;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
        }

    }

    public void youtubeClick(View view) {
        String name = idYoutube;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void googleMapClick(View view) {
        String map = "http://maps.google.co.in/maps?q=" + governmentOfficials;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(map));
        startActivity(intent);
    }


}