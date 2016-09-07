package com.codepath.bibliophile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.bibliophile.R;

public class AboutFragment extends Fragment {

    TextView tvAbout;
    public AboutFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_about, parent, false);

        tvAbout = (TextView) v.findViewById(R.id.tvAbout);

        // add the about message into the tvAbout textview
        String data = "Bibliophile is a mobile-first peer to peer app, where we provide a fully functional marketplace heavily optimized for buying and selling books.";
        tvAbout.setText(data);
        return v;
    }
}
