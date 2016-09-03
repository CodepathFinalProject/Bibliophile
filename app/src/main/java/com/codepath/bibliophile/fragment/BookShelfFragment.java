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
import android.widget.TextView;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.activity.DetailsActivity;
import com.codepath.bibliophile.adapter.BookShelfRecyclerViewAdapter;
import com.codepath.bibliophile.adapter.HomeRecyclerViewAdapter;
import com.codepath.bibliophile.model.BookModel;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class BookShelfFragment extends Fragment {
    public BookShelfRecyclerViewAdapter adapter;
    public ArrayList<BookModel> books;
    public RecyclerView rvItem;
    private RecyclerTouchListener onTouchListener;
    private HomeRecyclerViewAdapter homeRecyclerView;



    public BookShelfFragment() {

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
        onTouchListener.setSwipeOptionViews(R.id.edit, R.id.delete, R.id.unlist)
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
                        try {
                            intent.putExtra("bookOwner",book.getSeller().fetchIfNeeded().getUsername());
                            intent.putExtra("ownerEmail",book.getSeller().fetchIfNeeded().getEmail());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
                        if (viewID == R.id.edit) {
                            Log.d("onSwipeOptionClicked: ", "Clicked edit");
                            final BookModel book = books.get(position);
//                            Intent intent = new Intent(getContext(), DetailsActivity.class);
//                            intent.putExtra("title", book.getTitle());
//                            intent.putExtra("author",book.getAuthor());
//                            intent.putExtra("description",book.getDescription());
//                            intent.putExtra("price",book.getPrice().toString());
//                            intent.putExtra("cover",book.getBookCover());
//                            intent.putExtra("isbn",String.valueOf(book.getISBN()));
//                            intent.putExtra("condition",book.getCondition());
//                            intent.putExtra("bookOwner",book.getBookOwner());
//                            intent.putExtra("ownerEmail",book.getContactEmail());
//                            getContext().startActivity(intent);

                        } else if (viewID == R.id.delete) {
                            // Do something
                            Log.d("onSwipeOptionClicked: ", "Clicked delete");
                            books.remove(position).deleteEventually();
                            adapter.notifyItemRemoved(position);

                        }else if (viewID == R.id.unlist){
                            Boolean isListed = true;
                            TextView unlistText = (TextView)v.findViewById(R.id.unlist_textView);

                            if(isListed == true){
                                Log.d("onSwipeOptionClicked: ", "Clicked unlist");
                                BookModel book = books.get(position);
                                book.setIsListed(false);
                                unlistText.setText("List");
                                isListed = false;
                                book.saveEventually();
                            }else{
                                BookModel book = books.get(position);
                                book.setIsListed(true);
                                unlistText.setText("Unlist");
                                book.saveEventually();
                                isListed = true;
                            }


                            //Add List Toggle idea here.


                        }
                    }
                });
        return v;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        books = new ArrayList<>();
        //construct the adapter from data source
        adapter = new BookShelfRecyclerViewAdapter(getActivity(), books);
        populateTimeline();

    }
    public void addAll(List<BookModel> books) {
        this.books.addAll(books);
        adapter.notifyDataSetChanged();
    }

    private void populateTimeline() {
        ParseQuery<BookModel> query = ParseQuery.getQuery(BookModel.class);
        query.whereEqualTo("seller", ParseUser.getCurrentUser());
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
