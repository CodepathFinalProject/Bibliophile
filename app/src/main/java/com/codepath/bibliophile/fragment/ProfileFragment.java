package com.codepath.bibliophile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.bibliophile.R;
import com.codepath.bibliophile.adapter.HomeRecyclerViewAdapter;
import com.codepath.bibliophile.model.BookModel;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    public HomeRecyclerViewAdapter adapter;
    public ArrayList<BookModel> books;
    public RecyclerView rvItem;


    EditText etAddress;
    TextView etName;
    ImageView ivProfilePic;
    TextView etEmail;
    Button btnSaveProfile;
    ImageView ivCoverPic;

    public ProfileFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        books = new ArrayList<>();
        //construct the adapter from data source
        adapter = new HomeRecyclerViewAdapter(getActivity(), books);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle(R.string.fragment_profile);
        etName = (TextView) view.findViewById(R.id.etName);
        etName.setText(ParseUser.getCurrentUser().getString("username"));
//        etName.setSelection(etName.length());
        etEmail = (TextView) view.findViewById(R.id.etEmail);
        etEmail.setText(ParseUser.getCurrentUser().getString("email"));
//        etEmail.setSelection(etEmail.length());
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etAddress.setText(ParseUser.getCurrentUser().getString("address"));
        etAddress.setSelection(etAddress.length());
        btnSaveProfile = (Button) view.findViewById(R.id.btnSaveProfile);
        ivProfilePic = (ImageView) view.findViewById(R.id.ivProfileImage);
        ivCoverPic = (ImageView)view.findViewById(R.id.ivCoverPic);

        Glide.with(this).load(ParseUser.getCurrentUser().getString("profilePic")).into(ivProfilePic);
        Glide.with(this).load(ParseUser.getCurrentUser().getString("coverPic")).into(ivCoverPic);

        etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser parseUser = ParseUser.getCurrentUser();
                parseUser.put("address",etAddress.getText().toString());
                parseUser.saveEventually();

            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser parseUser = ParseUser.getCurrentUser();
//                parseUser.setEmail(etEmail.getText().toString());
//                parseUser.setUsername(etName.getText().toString());
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

        return view;
    }

    public void addAll(List<BookModel> books) {
        this.books.addAll(books);
        adapter.notifyDataSetChanged();
    }
}
