package com.codepath.bibliophile.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.model.BookModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.home_fragment, parent, false);

        rvItem = (RecyclerView)v.findViewById(R.id.rvHomePage);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvItem.setLayoutManager(gridLayoutManager);


        //Define the class we would like to query


        rvItem.setAdapter(adapter);
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.home_fragment, parent, false);
        return  v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String qValue = "";
        if (getArguments() != null && getArguments().containsKey("query")) {
            qValue = getArguments().getString("query");
            Log.d("qVal", "onCreate: " + qValue);
            populateTimelineQuery(qValue);
        } else {
            populateTimeline();
        }
    }

    private void populateTimeline() {
        ParseQuery<BookModel> query = ParseQuery.getQuery(BookModel.class);
        query.whereNotEqualTo("owner", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<BookModel>() {
            public void done(List<BookModel> itemList, ParseException e) {
                updateList(itemList, e);

            }
        });
    }

    private void populateTimelineQuery(String q) {
        Log.d("populate", "populateTimeline: " + q);

        ParseQuery<BookModel> query = ParseQuery.getQuery(BookModel.class);
        query.whereEqualTo("bookTitle", q);
      /*  ParseQuery<BookModel> query1 = ParseQuery.getQuery(BookModel.class);
            query.whereEqualTo("Author","Tom Knox");

            List<ParseQuery<BookModel>> queries = new ArrayList<ParseQuery<BookModel>>();
            queries.add(query);
            queries.add(query1);

            ParseQuery<BookModel> mainQuery = ParseQuery.or(queries);
*/
        query.findInBackground(new FindCallback<BookModel>() {
            @Override
            public void done(List<BookModel> itemList, ParseException e) {
                updateList(itemList, e);
            }
        });
    }

    private void updateList(List<BookModel> itemList, ParseException e) {
        if (e == null) {
            model.clear();
            addAll(itemList);
            adapter.notifyDataSetChanged();
        } else {
            Log.d("item", "Error: " + e.getMessage());
        }
    }

}
