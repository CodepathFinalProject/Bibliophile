package com.codepath.bibliophile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.bibliophile.adapter.HomeRecyclerViewAdapter;
import com.codepath.bibliophile.model.BookModel;

import java.util.ArrayList;
import java.util.List;


public class BaseFragment extends Fragment {
    public HomeRecyclerViewAdapter adapter;
    public ArrayList<BookModel> model;
    public RecyclerView rvItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ArrayList<>();
        //construct the adapter from data source
        adapter = new HomeRecyclerViewAdapter(getActivity(), model);
    }

    public void addAll(List<BookModel> books) {
        Log.d("MU HU HA HA", String.valueOf(books.size()));
        model.addAll(books);
        adapter.notifyDataSetChanged();

    }


}
