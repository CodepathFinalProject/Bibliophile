package com.codepath.bibliophile.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.client.GoogleBooksClient;
import com.codepath.bibliophile.model.Book;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment{

    private Button btnSearchByISBN;
    private Button btnSearchBarcode;
    private EditText etISBN;

    private GoogleBooksClient client;

    public PostFragment() {
        // Required empty public constructor
    }

    private OnSearchBookListener listener;

    // Tell the parent activity to dynamically embed a new fragment
    public interface OnSearchBookListener {
        public void onSearchBookClicked(Book book);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchBookListener) {
            listener = (OnSearchBookListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get references
        client = GoogleBooksClient.getClient();

        etISBN = (EditText) view.findViewById(R.id.etISBN);
        btnSearchBarcode = (Button) view.findViewById(R.id.btnSearchBarcode);
        btnSearchByISBN = (Button) view.findViewById(R.id.btnSearchISBN);

        btnSearchByISBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.getBookDetailsFromISBN(etISBN.getText().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Book book = Book.fromJsonResponse(response);
                        Log.d("debug", "got network response");
                        listener.onSearchBookClicked(book);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("failure", errorResponse.toString());
                    }
                });
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
