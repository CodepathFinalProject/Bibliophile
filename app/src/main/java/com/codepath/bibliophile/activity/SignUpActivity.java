package com.codepath.bibliophile.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.model.UserModel;
import com.codepath.bibliophile.utils.Utils;
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
                user.setCoord(place.getLatLng());
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
        if(Utils.isNetworkAvailable(this)) {

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
                        String email = response.getJSONObject().getString("email");
                        etEmail.setText(email);
                        user.setEmailAddress(email);
                        etEmail.setSelection(etEmail.length());
                        String name = response.getJSONObject().getString("name");
                        welcomeMsg.setText("Welcome " + name + "!");

                        user.setName(name);


                    } catch (final JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).executeAsync();
        } else {
            final Context context = this;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("");
            alertDialog
                    .setMessage("NO INTERNET CONNECTION")
                    .setCancelable(true)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog   dialog = alertDialog.create();

            dialog.show();

        }
        return user;

    }


    private void saveToParse(UserModel user) {
        if(Utils.isNetworkAvailable(this)) {
            ParseUser parseUser = ParseUser.getCurrentUser();
            parseUser.setEmail(user.getEmailAddress());
            parseUser.setUsername(user.getName());
            parseUser.put("address", user.getAddress());
            parseUser.put("profilePic", user.getProfilePicUrl());
            parseUser.put("coordinates", new ParseGeoPoint(user.getCoord().latitude, user.getCoord().longitude));
            parseUser.saveEventually();
            Intent intent = new Intent(SignUpActivity.this,HomeMainActivity.class);
            startActivity(intent);
        } else {
            final Context context = this;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("");
            alertDialog
                    .setMessage("Couldn't save the information because of no internet connection !")
                    .setCancelable(true)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog   dialog = alertDialog.create();

            dialog.show();

        }
    }
}
