
package com.codepath.bibliophile.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.bibliophile.model.BookModel;
import com.codepath.bibliophile.model.GoogleBookModel;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBookFragment extends Fragment {

    private GoogleBookModel googleBookModel;

    private OnPostBookListener listener;

    public AddBookFragment() {
        // Required empty public constructor
    }

    // Tell the parent activity to dynamically embed a new fragment
    public interface OnPostBookListener {
        public void onPostClicked(GoogleBookModel listing);

        public void onCancelClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostBookListener) {
            listener = (OnPostBookListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement AddBookFragment.OnPostBookListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleBookModel = Parcels.unwrap(getArguments().getParcelable("book"));
        googleBookModel.setCondition("New");
        googleBookModel.setPrice(0);
        BookModel book = new BookModel(googleBookModel);
        ParseUser seller = ParseUser.getCurrentUser();
        book.setSeller(seller);

        try {
            book.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String objectId = book.getObjectId();
        String title = "List a book in Marketplace";
        FragmentManager fm = getFragmentManager();
        EditBookFragment editBook = EditBookFragment.newInstance(title, objectId);
        editBook.show(fm, "upload_book");

    }

}