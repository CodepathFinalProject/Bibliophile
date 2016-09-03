package com.codepath.bibliophile.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.bibliophile.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends AppCompatActivity {

    private SupportMapFragment mapFragment;
    private GoogleMap map;

    ImageView ivCover;
    TextView tvDTitle;
    TextView tvDAuthor;
    TextView tvDDescription;
    TextView tvDPrice;
    TextView tvCondition;
    TextView tvISBN;
    TextView tvDistance;
    TextView tvOwnerName;
    TextView tvOwnerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        tvDTitle = (TextView) findViewById(R.id.tvDTitle);
        tvDAuthor = (TextView) findViewById(R.id.tvDAuthor);
        tvDDescription = (TextView) findViewById(R.id.tvDDescription);
        tvDPrice = (TextView) findViewById(R.id.tvDPrice);
        ivCover = (ImageView) findViewById(R.id.ivDCover);
        tvCondition = (TextView) findViewById(R.id.tvCondition);
        tvISBN = (TextView) findViewById(R.id.tvISBN);
        tvOwnerName = (TextView) findViewById(R.id.tvDOwnerName);
        tvOwnerEmail = (TextView) findViewById(R.id.tvDOwnerEmail);
        tvDistance = (TextView) findViewById(R.id.tvDistance);

        Bundle args = getIntent().getExtras();
        tvDTitle.setText(args.getString("title"));
        tvDAuthor.setText(args.getString("author"));
        tvDDescription.setText(args.getString("description"));
        tvDPrice.setText(args.getString("price"));
        tvCondition.setText(args.getString("condition"));
        tvISBN.setText(args.getString("isbn"));
        tvDistance.setText(args.getString("distance"));
        tvOwnerName.setText(args.getString("ownerName"));
        tvOwnerEmail.setText(args.getString("ownerEmail"));
        ivCover.setImageResource(0);

        Glide.with(this).load(args.getString("cover")).into(ivCover);

        final String owner = args.getString("ownerName");
        final String address = args.getString("ownerAddress");
        final LatLng location = new LatLng(args.getDouble("lat"), args.getDouble("lng"));

        // Set up map
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    // Add a marker for at the book owner's location
                    map.addMarker(new MarkerOptions()
                            .position(location)
                            .title(owner)
                            .snippet(address));

                    loadMap(map);

                    // Configure the map to center around the book location and set the zoom so you can view the streets
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(location)      // Sets the center of the map to the book location
                            .zoom(15)                   // Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder

                    // Move the map camera animation
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }

        // Set up email intent
        tvOwnerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{tvOwnerEmail.getText().toString()});
                sendIntent.setData(Uri.parse("mailto:"));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Interested in buying the book \"" + tvDTitle.getText().toString() + "\"");
                sendIntent.setType("plain/text");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(sendIntent);
            }
        });
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
    }
}

