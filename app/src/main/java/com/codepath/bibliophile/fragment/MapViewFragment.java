package com.codepath.bibliophile.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.codepath.bibliophile.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewFragment extends DialogFragment {
    private TextView title;

    public MapViewFragment() {
        // Empty constructor required for DialogFragment
    }

    public static MapViewFragment newInstance(String title, String address, Double latitude, Double longitude) {
        MapViewFragment frag = new MapViewFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("address", address);
        args.putDouble("latitude", latitude);
        args.putDouble("longitude", longitude);
        frag.setArguments(args);
        return frag;
    }

    MapView mapView;
    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        final String title = getArguments().getString("title");
        final String address = getArguments().getString("address");
        Double latitude = getArguments().getDouble("latitude");
        Double longitude = getArguments().getDouble("longitude");
        final LatLng location = new LatLng(latitude, longitude);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.map_view_toolbar);
        toolbar.inflateMenu(R.menu.menu_map_view);
        toolbar.setBackgroundColor(Color.parseColor("#598392"));

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.clear:
                        dismiss();
                        return true;
                }
                return true;

            }
        });
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        this.setHasOptionsMenu(true);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.map_location);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(address));
                dropPinEffect(marker);
                map = googleMap;
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(location)
                        .zoom(15)
                        .build();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 300, null);

            }
        });
        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        mapView.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
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
