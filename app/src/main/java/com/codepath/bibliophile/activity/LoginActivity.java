package com.codepath.bibliophile.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.utils.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final List<String> FACEBOOK_PERMISSIONS = Arrays.asList("public_profile", "email");
    private Button btnFacebookLogin;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        // If there's no user logged in (i.e. first-time app user or if the user was logged out), show them the login screen
        if ((ParseUser.getCurrentUser() == null)) {
            setContentView(R.layout.activity_login);
            btnFacebookLogin = (Button) findViewById(R.id.btnFacebookLogin);
            btnFacebookLogin.setOnClickListener(this);
        } else {
            // If there is a user logged in, send them directly to the HomeMain activity
            Intent intent = new Intent(LoginActivity.this, HomeMainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }   

    @Override
    public void onClick(View view) {
        if(Utils.isNetworkAvailable(this)) {

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, FACEBOOK_PERMISSIONS, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (user == null) {
                    Toast.makeText(LoginActivity.this, "User cancelled login", Toast.LENGTH_SHORT).show();
                    return;
                } else if (user.isNew()) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.this, HomeMainActivity.class);
                    startActivity(intent);
                }
            }
        });
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
    }
}