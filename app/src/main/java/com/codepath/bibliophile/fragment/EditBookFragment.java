package com.codepath.bibliophile.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.bibliophile.R;
import com.codepath.bibliophile.model.BookModel;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class EditBookFragment extends DialogFragment {
//    private TextView title;
    public BookModel book;

    public EditBookFragment() {
        // Empty constructor required for DialogFragment
    }

    public static EditBookFragment newInstance(String title, String objectId) {
        EditBookFragment frag = new EditBookFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("objectId", objectId);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_edit_book, container, false);
        View v = inflater.inflate(R.layout.fragment_edit_book, container, false);
        final TextView bookTitle = (TextView) v.findViewById(R.id.book_title);
        final TextView bookAuthor = (TextView) v.findViewById(R.id.book_author);
        final ImageView bookImage = (ImageView) v.findViewById(R.id.book_image);
        final RatingBar ratingBar = (RatingBar) v.findViewById(R.id.rating);
        final TextView ratingsCount = (TextView) v.findViewById(R.id.ratings_count);
        final TextView description = (TextView) v.findViewById(R.id.description);
        final EditText price = (EditText) v.findViewById(R.id.etBookPrice);
        final Spinner conditionSpinner = (Spinner) v.findViewById(R.id.condition_spinner);


        final String title = getArguments().getString("title");
        final String objectId = getArguments().getString("objectId");



        ParseQuery query = ParseQuery.getQuery(BookModel.class);
        query.whereEqualTo("objectId", objectId);
        query.getFirstInBackground(new GetCallback<BookModel>() {
            @Override
            public void done(BookModel object, ParseException e) {
                book = object;
                bookTitle.setText(book.getTitle());
                bookAuthor.setText(book.getAuthor());
                Glide.with(getContext()).
                        load(book.getBookCover()).into(bookImage);

                ratingBar.setRating((float) book.getAverageRating().doubleValue());

                String ratingsCountFormattedString = "(" + String.valueOf(book.getRatingsCount()) + ")";
                ratingsCount.setText(ratingsCountFormattedString);

                description.setText(book.getDescription());

                price.setText(String.valueOf(book.getPrice()));
                price.setSelection(price.length());

            }

        });

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.edit_book_toolbar);
        toolbar.inflateMenu(R.menu.menu_edit_book);
        toolbar.setBackgroundColor(Color.parseColor("#01161E"));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.condition_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(adapter);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save:

                        Double newPrice = Double.valueOf(price.getText().toString());
                        book.setPrice(newPrice);

                        String newCondition = conditionSpinner.getSelectedItem().toString();
                        book.setCondition(newCondition);

                        book.saveEventually();
                        dismiss();

                    case R.id.clear:
                        dismiss();
                        return true;
                }
                return true;

            }
        });
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        this.setHasOptionsMenu(true);
        return v;
    }


    @Override
    public void onResume() {
        // Get existing layout params for the window

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

}