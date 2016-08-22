package com.codepath.bibliophile.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.bibliophile.R;


public class AddBooksFragment extends BaseFragment {

    public AddBooksFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("bookshelf", "onCreateView: ");
        return inflater.inflate(R.layout.add_book, container, false);
    }
}
