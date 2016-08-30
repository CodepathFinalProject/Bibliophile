package com.codepath.bibliophile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.activity.DetailsActivity;
import com.codepath.bibliophile.adapter.TransactionRecyclerViewAdapter;
import com.codepath.bibliophile.model.BookModel;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends Fragment{
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener onActivityTouchListener;
    public TransactionRecyclerViewAdapter adapter;
    public ArrayList<BookModel> books;
    public RecyclerView rvItem;

    public TransactionFragment(){

}   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.home_fragment, container, false);

        rvItem = (RecyclerView) v.findViewById(R.id.rvHomePage);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvItem.setLayoutManager(gridLayoutManager);
        rvItem.setAdapter(adapter);
        onTouchListener = new RecyclerTouchListener(getActivity(), rvItem);
        onTouchListener.setSwipeOptionViews(R.id.mark_complete, R.id.cancel)
                .setClickable(new RecyclerTouchListener.OnRowClickListener(){
                    @Override
                    public void onRowClicked(int position) {
                        Log.d("SUPRIYA", "ROW Clickesh");
                        final BookModel book = books.get(position);
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        intent.putExtra("title", book.getTitle());
                        intent.putExtra("author",book.getAuthor());
                        intent.putExtra("description",book.getDescription());
                        intent.putExtra("price",book.getPrice().toString());
                        intent.putExtra("cover",book.getBookCover());
                        intent.putExtra("isbn",String.valueOf(book.getISBN()));
                        intent.putExtra("condition",book.getCondition());
                        intent.putExtra("bookOwner",book.getBookOwner());
                        intent.putExtra("ownerEmail",book.getContactEmail());
                        getContext().startActivity(intent);
                        //RecyclerTouchListener tmp = onTouchListener;
//                        onTouchListener.setUnSwipeableRows(position);

                        //rvItem.addOnItemTouchListener(tmp);

                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        // Do something
                    }
                })
                .setSwipeable(R.id.card_view, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if (viewID == R.id.mark_complete) {
                            // Do something
                            books.remove(position).deleteEventually();
                            adapter.notifyDataSetChanged();

                        } else if (viewID == R.id.cancel) {
                            // Do something
                            books.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        books = new ArrayList<>();
        //construct the adapter from data source
        adapter = new TransactionRecyclerViewAdapter(getActivity(), books);

       populateTransaction();
    }
    @Override
    public void onResume() {
        rvItem.addOnItemTouchListener(onTouchListener);
        super.onResume();
    }
    @Override
    public void onPause(){
        rvItem.removeOnItemTouchListener(onTouchListener);
        super.onPause();
    }

    public void addAll(List<BookModel> books) {
        this.books.addAll(books);
        adapter.notifyDataSetChanged();
    }

    private void populateTransaction() {


        ParseQuery<BookModel> query = ParseQuery.getQuery(BookModel.class);
        query.whereEqualTo("isListed", false);
        query.whereNotEqualTo("buyer", null);

        query.findInBackground(new FindCallback<BookModel>() {
            public void done(List<BookModel> itemList, ParseException e) {
                if (e == null) {
                    addAll(itemList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

}
