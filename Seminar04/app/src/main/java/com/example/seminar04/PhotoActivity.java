package com.example.seminar04;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends AppCompatActivity {
    private String governmentOfficials;
    private String officialName;
    private String photoUrl;
    private String party;
    private String loc;

    @BindView(R.id.government_officials)
    TextView governmentOfficialsTV;
    @BindView(R.id.officialName)
    TextView officialNameTV;
    @BindView(R.id.imageOfficial)
    ImageView imageOfficial;
    @BindView(R.id.bgParty)
    LinearLayout bgParty;
    @BindView(R.id.location)
    TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            governmentOfficials = extras.getString("governmentOfficials");
            officialName = extras.getString("officialName");
            photoUrl = extras.getString("photoUrl");
            party = extras.getString("party");
            loc = extras.getString("location");

            PicassoUpload(photoUrl);
            governmentOfficialsTV.setText(governmentOfficials);
            officialNameTV.setText(officialName);
            location.setText(loc);

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
                            .into(imageOfficial);
                }
            }).build();

            picasso.load(urlImage)
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.load_image)
                    .into(imageOfficial);
        } else {
            Picasso.with(this).load(urlImage)
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.load_image)
                    .into(imageOfficial);
        }
    }
}