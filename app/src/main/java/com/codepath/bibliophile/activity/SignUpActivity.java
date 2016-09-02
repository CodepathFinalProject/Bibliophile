package com.codepath.bibliophile.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.model.UserModel;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        Button saveBtn = (Button) findViewById(R.id.btnSave);
        TextView welcomeMsg = (TextView) findViewById(R.id.welcome_msg);

        final UserModel user = getUserInfoFromFB(etEmail, welcomeMsg);

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Enter your Address");
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input))
                .setTextColor(Color.parseColor("#ffffff"));
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input))
                .setTextSize(22.0f);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                autocompleteFragment.setText(place.getAddress());
                double lat = place.getLatLng().latitude;
                double lng = place.getLatLng().longitude;
                user.setCoordinates(new ParseGeoPoint(lat, lng));
                user.setAddress(place.getAddress().toString());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("DEBUG", "An error occurred: " + status);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToParse(user);
            }
        });
    }


    private UserModel getUserInfoFromFB(final EditText etEmail, final TextView welcomeMsg) {
        final UserModel user = new UserModel();
        final Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.type(large)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", parameters, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(final GraphResponse response) {
                if (response != null) {
                    try {
                        JSONObject data = response.getJSONObject();
                        if (data.has("picture")) {
                            String profilePic = data.getJSONObject("picture").getJSONObject("data").getString("url");
                            user.setProfilePicUrl(profilePic);
                        }
                        Log.d("3764", "onCompleted: " + response.getJSONObject().toString());
                        String email = response.getJSONObject().getString("email");
                        etEmail.setText(email);
                        user.setEmailAddress(email);

                        String name = response.getJSONObject().getString("name");
                        welcomeMsg.setText("Welcome " + name + "!");

                        user.setName(name);

                    } catch (final JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).executeAsync();

        return user;

    }


    private void saveToParse(UserModel user) {
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.setEmail(user.getEmailAddress());
        parseUser.setUsername(user.getName());
        parseUser.put("address", user.getAddress());
        parseUser.put("profilePic", user.getProfilePicUrl());
        parseUser.put("coordinates", user.getCoordinates());
        parseUser.saveEventually();
        Intent intent = new Intent(SignUpActivity.this,HomeMainActivity.class);
        startActivity(intent);
    }
}
