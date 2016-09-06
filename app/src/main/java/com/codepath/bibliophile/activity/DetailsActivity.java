package com.codepath.bibliophile.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.bibliophile.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends AppCompatActivity {

    ImageView ivCover;
    TextView tvDTitle;
    TextView tvDAuthor;
    RatingBar ratingBar;
    TextView tvRatingsCount;
    TextView tvDDescription;
    TextView tvDPrice;
    TextView tvCondition;
    TextView tvISBN;
    TextView tvSellerName;
    ImageView ivSellerEmail;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        tvDTitle = (TextView) findViewById(R.id.book_title);
        tvDAuthor = (TextView) findViewById(R.id.book_author);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        tvRatingsCount = (TextView) findViewById(R.id.ratings_count);
        tvDDescription = (TextView) findViewById(R.id.description);
        tvDPrice = (TextView) findViewById(R.id.tvBookPrice);
        ivCover = (ImageView) findViewById(R.id.book_image);

        tvCondition = (TextView) findViewById(R.id.condition_status);
        tvISBN = (TextView) findViewById(R.id.tvIsbn);
        tvSellerName = (TextView) findViewById(R.id.seller_name);
        ivSellerEmail = (ImageView) findViewById(R.id.seller_email);
        mapView = (MapView) findViewById(R.id.book_location);

        Bundle args = getIntent().getExtras();
        tvDTitle.setText(args.getString("title"));
        tvDAuthor.setText(args.getString("author"));
        tvDDescription.setText(args.getString("description"));
        tvDPrice.setText(args.getString("price"));
        tvCondition.setText(args.getString("condition"));
        tvISBN.setText(args.getString("isbn"));
        String formattedSeller = "Email " + args.getString("bookSeller");
        tvSellerName.setText(formattedSeller);
        int ratingsCount = args.getInt("ratingsCount");
        final String formattedRatingsCount = "(" + String.valueOf(ratingsCount) + ")";
        tvRatingsCount.setText(formattedRatingsCount);
        final String sellerEmail = args.getString("sellerEmail");
        final String address = args.getString("address");
        Double latitude = args.getDouble("latitude");
        Double longitude = args.getDouble("longitude");
        final LatLng location = new LatLng(latitude, longitude);
        ivCover.setImageResource(0);
        Glide.with(this).load(args.getString("cover")).into(ivCover);

        ivSellerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{sellerEmail});
                sendIntent.setData(Uri.parse("mailto:"));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Interested in buying the book \"" + tvDTitle.getText().toString() + "\"");
                sendIntent.setType("plain/text");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(sendIntent);
            }
        });
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(address));
                dropPinEffect(marker);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(location)
                        .zoom(15)
                        .build();

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 300, null);
                mapView.onResume();


            }
        });
    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }
}
