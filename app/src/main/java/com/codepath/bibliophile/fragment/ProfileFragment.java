package com.codepath.bibliophile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.codepath.bibliophile.R;
import com.parse.ParseUser;

public class ProfileFragment extends BaseFragment {

    EditText etAddress;
    EditText etName;
    ImageView ivProfilePic;
    EditText etEmail;
    Button btnSaveProfile;

    public ProfileFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("bookshelf", "onCreateView: ");
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = (EditText) view.findViewById(R.id.etName);
        etName.setText(ParseUser.getCurrentUser().getString("username"));
        etName.setSelection(etName.length());
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etEmail.setText(ParseUser.getCurrentUser().getString("email"));
        etEmail.setSelection(etEmail.length());
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etAddress.setText(ParseUser.getCurrentUser().getString("address"));
        etAddress.setSelection(etAddress.length());
        btnSaveProfile = (Button) view.findViewById(R.id.btnSaveProfile);

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser parseUser = ParseUser.getCurrentUser();
                parseUser.setEmail(etEmail.getText().toString());
                parseUser.setUsername(etName.getText().toString());
                parseUser.put("address",etAddress.getText().toString());
                parseUser.saveEventually();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                // set dialog message
                alertDialogBuilder
                        .setMessage("Profile Saved Succesfully!")
                        .setCancelable(true);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }
}
