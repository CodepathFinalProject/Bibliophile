package com.codepath.bibliophile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.codepath.bibliophile.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        info = (TextView) findViewById(R.id.info);
        //loginButton = (LoginButton) findViewById(R.id.login_button);

//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                //   loginButton.setVisibility(View.INVISIBLE);
//                Intent intent = new Intent(LoginActivity.this, HomeMainActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(getApplicationContext(), "Login attempt was cancelled", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//                Log.d("111", "onError: " + e.toString());
//            }
//        });

        List<String> permissions = Arrays.asList("public_profile", "email");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {

                if (user == null) {
                    Log.d("debug", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("debug", "User signed up and logged in through Facebook!");
                    ParseFacebookUtils.linkWithReadPermissionsInBackground(user, LoginActivity.this, null);
                    ParseFacebookUtils.linkInBackground(user, AccessToken.getCurrentAccessToken());
                    Intent intent = new Intent(LoginActivity.this, AddressActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("debug", "User logged in through Facebook!");
                    ParseFacebookUtils.linkWithReadPermissionsInBackground(user, LoginActivity.this, null);
                    ParseFacebookUtils.linkInBackground(user, AccessToken.getCurrentAccessToken());
                    Intent intent = new Intent(LoginActivity.this, HomeMainActivity.class);
                    startActivity(intent);
                }
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}