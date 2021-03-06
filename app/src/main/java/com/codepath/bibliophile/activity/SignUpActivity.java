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
import android.widget.EditText;
import android.widget.ImageView;
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
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        ImageView saveIcon = (ImageView) findViewById(R.id.saveIcon);
        TextView welcomeMsg = (TextView) findViewById(R.id.welcome_msg);

        final UserModel user = getUserInfoFromFB(tvEmail, welcomeMsg);

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Enter your Address");
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input))
                .setTextColor(Color.parseColor("#ffffff"));
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input))
                .setHintTextColor(Color.parseColor("#ffffff"));
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
                alertOnInvalidAddress();
            }
        });
        saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getCoord() == null){
                    alertOnInvalidAddress();


                }else{
                    saveToParse(user);
                }

            }
        });
    }


    private UserModel getUserInfoFromFB(final TextView tvEmail, final TextView welcomeMsg) {
        final UserModel user = new UserModel();
        final Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,cover.type(large),picture.type(large)");
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", parameters, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(final GraphResponse response) {
                String coverPic;
                if (response != null) {
                    try {
                        JSONObject data = response.getJSONObject();
                        if (data.has("picture")) {
                            String profilePic = data.getJSONObject("picture").getJSONObject("data").getString("url");
                            user.setProfilePicUrl(profilePic);
                        }
                        if (data.has("cover")) {
                            coverPic = data.getString("cover");

                            if (coverPic.equals("null")) {
                                coverPic = null;
                            } else {
                                JSONObject JOCover = data.optJSONObject("cover");

                                if (JOCover.has("source"))  {
                                    coverPic = JOCover.getString("source");
                                    Log.d("CoverPicURL", coverPic);
                                    user.setCoverPicUrl(coverPic);
                                } else {
                                    coverPic = null;
                                }
                            }
                        } else {
                            coverPic = null;
                        }
                        String email = response.getJSONObject().getString("email");
                        tvEmail.setText(email);
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
        if(Utils.isNetworkAvailable(this)) {
            ParseUser parseUser = ParseUser.getCurrentUser();
            parseUser.setEmail(user.getEmailAddress());
            parseUser.setUsername(user.getName());
            parseUser.put("address", user.getAddress());
            parseUser.put("profilePic", user.getProfilePicUrl());
            if (user.getCoverPicUrl() == null){
                String defautCoverPic = "http://i.imgur.com/MJHgcby.png";
                user.setCoverPicUrl(defautCoverPic);
            }
            parseUser.put("coverPic", user.getCoverPicUrl());
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

    private void alertOnInvalidAddress(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
        alertDialogBuilder.setTitle("Action Unavailable");
        String message = "Address cannot be empty or invalid. Please entering it again.";
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
