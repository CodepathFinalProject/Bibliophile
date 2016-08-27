package com.codepath.bibliophile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.bibliophile.R;
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
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        info = (TextView) findViewById(R.id.info);

        final List<String> permissions = Arrays.asList("public_profile", "email");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d("555", "Uh oh. Error occurred" + e.toString());
                    Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (user == null) {
                    Log.d("555", "The user cancelled the Facebook Login");
                    Toast.makeText(LoginActivity.this, "User cancelled login", Toast.LENGTH_SHORT).show();
                    return;
                } else if (user.isNew()) {
                    Log.d("debug", "new facebook user!");
                    Intent intent = new Intent(LoginActivity.this, AddressActivity.class);
                    startActivity(intent);
                } else {
                        Log.d("555", "User signed up and logged in through Facebook!");
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