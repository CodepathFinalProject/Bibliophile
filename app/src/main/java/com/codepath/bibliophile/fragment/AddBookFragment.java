package com.codepath.bibliophile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.model.Book;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBookFragment extends Fragment {

    private Book book;

    private TextView tvBookTitle;

    public AddBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        book = Parcels.unwrap(getArguments().getParcelable("book"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvBookTitle = (TextView) view.findViewById(R.id.tvBookTitle);
        tvBookTitle.setText(book.getTitle());
    }
}
