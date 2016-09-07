package com.codepath.bibliophile.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.activity.DetailsActivity;
import com.codepath.bibliophile.adapter.BookShelfRecyclerViewAdapter;
import com.codepath.bibliophile.model.BookModel;
import com.codepath.bibliophile.utils.Utils;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class BookShelfFragment extends Fragment {
    public BookShelfRecyclerViewAdapter adapter;
    public ArrayList<BookModel> books;
    public RecyclerView rvItem;

    private RecyclerTouchListener onTouchListener;
    private SwipeRefreshLayout swipeContainer;


    private ParseQuery<BookModel> finalQuery;

    public BookShelfFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.home_fragment, parent, false);
        rvItem = (RecyclerView) v.findViewById(R.id.rvHomePage);
        FloatingActionButton myFab = (FloatingActionButton) v.findViewById(R.id.fabAdd);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new PostFragment()).commit();
            }
        });


        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvItem.setLayoutManager(gridLayoutManager);

        rvItem.setAdapter(adapter);

        onTouchListener = new RecyclerTouchListener(getActivity(), rvItem);
        onTouchListener.setSwipeOptionViews(R.id.edit, R.id.delete, R.id.toggle_listing)
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
                    public void onSwipeOptionClicked(int viewID, final int position) {
                        if (viewID == R.id.edit) {
                            BookModel book = books.get(position);
                            String objectId = book.getObjectId();
                            String title = "Edit Book Details";
                            FragmentManager fm = getFragmentManager();
                            EditBookFragment editBook = EditBookFragment.newInstance(title, objectId);
                            editBook.show(fm, "edit_book_details");

                        } else if (viewID == R.id.delete) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                            alertDialogBuilder.setTitle("Confirm");
                            alertDialogBuilder.setMessage("Do you wish to delete the book from your bookshelf?");
                            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    BookModel book = books.remove(position);
                                    book.deleteEventually();
                                    adapter.notifyItemRemoved(position);
                                }
                            });


                            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else if (viewID == R.id.toggle_listing) {
                            BookModel book = books.get(position);
                            boolean isListed = book.getIsListed();
                            RecyclerView.ViewHolder itemView = rvItem.findViewHolderForAdapterPosition(position);
                            TextView tvListingStatus = (TextView) itemView.itemView.findViewById(R.id.tv_listing_status);
                            ImageView ivListingIcon = (ImageView) itemView.itemView.findViewById(R.id.listing_icon);
                            try {
                                BookModel myBook = book.fetch();
                                if (myBook.getBuyer() == null) {
                                    if (isListed) {
                                        book.setIsListed(false);
                                        tvListingStatus.setText("List");
                                        ivListingIcon.setImageResource(R.drawable.ic_list);
                                        book.saveEventually();
                                    } else {
                                        book.setIsListed(true);
                                        book.setIsTransactionComplete(false);
                                        ivListingIcon.setImageResource(R.drawable.ic_unlist);
                                        tvListingStatus.setText("Unlist");
                                        book.saveEventually();
                                    }

                                } else {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                    alertDialogBuilder.setTitle("Action Unavailable");
                                    String message = "Current buyer is: " + myBook.getBuyer().fetchIfNeeded().getUsername() +"\nCancel the transaction prior to Listing/Unlisting.";
                                    alertDialogBuilder.setMessage(message);
                                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String qValue = "";
                if ((getArguments() != null) && getArguments().containsKey("query")) {
                    qValue = getArguments().getString("query");
                    getBooksUsingQuery(qValue);
                } else {
                    getBooks();
                }
                swipeContainer.setRefreshing(false);
            }
        });


        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        books = new ArrayList<>();
        //construct the adapter from data source
        adapter = new BookShelfRecyclerViewAdapter(getActivity(), books);


        String qValue = "";
        if ((getArguments() != null) && getArguments().containsKey("query")) {
            qValue = getArguments().getString("query");
            getBooksUsingQuery(qValue);
        } else {
            getBooks();
        }
    }

    @Override
    public void onResume() {
        if(Utils.isNetworkAvailable(getContext())) {
            finalQuery.findInBackground(new FindCallback<BookModel>() {
                public void done(List<BookModel> itemList, ParseException e) {
                    updateList(itemList, e);
                }
            });

        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("");
            alertDialog
                    .setMessage("Couldn't get books data since no internet connection!")
                    .setCancelable(true)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog   dialog = alertDialog.create();

            dialog.show();

        }
        rvItem.addOnItemTouchListener(onTouchListener);
        super.onResume();
    }

    private void getBooks() {
        finalQuery = ParseQuery.getQuery(BookModel.class);
        finalQuery.whereEqualTo("seller", ParseUser.getCurrentUser());


        // Final query is executed during onResume
    }

    private void getBooksUsingQuery(String q) {
        // Check if the query is contained within the title or author fields
        ParseQuery<BookModel> queryTitle = ParseQuery.getQuery(BookModel.class);
        queryTitle.whereContains("title", q);
        ParseQuery<BookModel> queryAuthor = ParseQuery.getQuery(BookModel.class);
        queryAuthor.whereContains("author", q);

        List<ParseQuery<BookModel>> clauses = new ArrayList<>();
        clauses.add(queryTitle);
        clauses.add(queryAuthor);

        // If query can be parsed into a long, it might be an ISBN
        try {
            long isbn = Long.parseLong(q);
            ParseQuery<BookModel> queryISBN = ParseQuery.getQuery(BookModel.class);
            queryISBN.whereEqualTo("ISBN", isbn);
            clauses.add(queryISBN);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Create the final query and exclude books posted by the user
        finalQuery = ParseQuery.or(clauses);
        finalQuery.whereNotEqualTo("owner", ParseUser.getCurrentUser());

        // Final query is executed during onResume
    }

    public void addAll(List<BookModel> books) {
        this.books.addAll(books);
        adapter.notifyDataSetChanged();
    }

    private void updateList(List<BookModel> itemList, ParseException e) {
        if (e == null) {
            books.clear();
            books.addAll(itemList);
            adapter.notifyDataSetChanged();
        } else {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        rvItem.removeOnItemTouchListener(onTouchListener);
        super.onPause();
    }

}
