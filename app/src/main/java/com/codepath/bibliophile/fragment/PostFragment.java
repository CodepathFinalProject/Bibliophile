package com.codepath.bibliophile.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.activity.BarcodeScannerActivity;
import com.codepath.bibliophile.client.GoogleBooksClient;
import com.codepath.bibliophile.model.GoogleBookModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    private static final int RC_BARCODE_CAPTURE = 1;
    private static final String TAG = "BarcodeMain";

    private ImageView searchISBN;
    private ImageView scanBarcode;
    private EditText isbnEntryText;

    private GoogleBooksClient client;

    public PostFragment() {
        // Required empty public constructor
    }

    private OnSearchBookListener listener;

    // Tell the parent activity to dynamically embed a new fragment
    public interface OnSearchBookListener {
        void onSearchBookClicked(GoogleBookModel googleBookModel);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchBookListener) {
            listener = (OnSearchBookListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement PostFragment.OnSearchBookListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.fragment_post);
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get references
        client = GoogleBooksClient.getClient();

        scanBarcode = (ImageView) view.findViewById(R.id.barcode_icon);
        searchISBN = (ImageView) view.findViewById(R.id.isbn_icon);
        isbnEntryText = (EditText) view.findViewById(R.id.etISBN);

        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BarcodeScannerActivity.class);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });

        searchISBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isbnEntryText.setVisibility(View.VISIBLE);
                isbnEntryText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(isbnEntryText, InputMethodManager.SHOW_IMPLICIT);
                isbnEntryText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE) {
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(isbnEntryText.getWindowToken(), 0);
                            String isbn = isbnEntryText.getText().toString();
                            displayBookDetails(isbn);
                            return true;
                        }
                        return false;

                    }
                });


            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                final String barcode = data.getStringExtra("barcode");
                displayBookDetails(barcode);
            }

        }
    }

    private void displayBookDetails(final String isbn) {
        client.getBookDetailsFromISBN(isbn, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                long ISBN = Long.parseLong(isbn);
                GoogleBookModel googleBookModel = GoogleBookModel.fromJsonResponse(getContext(), ISBN, response);

                // If at least one book is returned, go to AddBook fragment
                if (googleBookModel != null) {
                    listener.onSearchBookClicked(googleBookModel);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }
}
