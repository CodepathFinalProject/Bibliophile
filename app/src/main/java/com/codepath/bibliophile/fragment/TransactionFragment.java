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
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends Fragment {
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener onActivityTouchListener;
    public TransactionRecyclerViewAdapter adapter;
    public ArrayList<BookModel> books;
    public RecyclerView rvItem;

    public TransactionFragment() {

    }

    @Override
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
        onTouchListener.setSwipeOptionViews(R.id.confirm_transaction, R.id.decline_transaction)
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        final BookModel book = books.get(position);
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        intent.putExtra("title", book.getTitle());
                        intent.putExtra("author", book.getAuthor());
                        intent.putExtra("description", book.getDescription());
                        intent.putExtra("price", book.getPrice().toString());
                        intent.putExtra("cover", book.getBookCover());
                        intent.putExtra("isbn", String.valueOf(book.getISBN()));
                        intent.putExtra("condition", book.getCondition());
                        intent.putExtra("ratingsCount", book.getRatingsCount());
                        try {
                            intent.putExtra("bookSeller", book.getSeller().fetchIfNeeded().getUsername());
                            intent.putExtra("sellerEmail", book.getSeller().fetchIfNeeded().getEmail());
                            intent.putExtra("address", book.getSeller().fetchIfNeeded().getString("address"));
                            ParseGeoPoint location = book.getSeller().fetchIfNeeded().getParseGeoPoint("coordinates");
                            intent.putExtra("latitude", location.getLatitude());
                            intent.putExtra("longitude", location.getLongitude());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        getContext().startActivity(intent);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        // Do something
                    }
                })
                .setSwipeable(R.id.swipe_foreground, R.id.swipe_background, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        BookModel book = books.get(position);

                        if (viewID == R.id.confirm_transaction) {
                            try {

                                ParseUser me = ParseUser.getCurrentUser();
                                book = book.fetch();
                                if (me.getEmail().equals(book.getSeller().fetchIfNeeded().getEmail())) {
                                    book.setSellerConfirmed(true);
                                } else if (me.getEmail().equals(book.getBuyer().fetchIfNeeded().getEmail())) {
                                    book.setBuyerConfirmed(true);
                                }
                                book.saveEventually();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else if (viewID == R.id.decline_transaction) {
                            // Do something

//                            book.setIsListed(true);
//                            //book.setBuyer(null);
//                            book.saveEventually();


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
    public void onPause() {
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
        query.whereEqualTo("isTransactionComplete", false);
        final ArrayList<BookModel> transactionsList = new ArrayList<>();
        final ParseUser me = ParseUser.getCurrentUser();

        query.findInBackground(new FindCallback<BookModel>() {
            public void done(List<BookModel> itemList, ParseException e) {
                if (e == null) {
                    for (BookModel book : itemList) {
                        try {
                            if (book.getBuyer().fetchIfNeeded().getEmail().equals(me.getEmail())) {
                                transactionsList.add(book);
                            } else if (book.getSeller().fetchIfNeeded().getEmail().equals(me.getEmail())) {
                                transactionsList.add(book);
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                    addAll(transactionsList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

}
