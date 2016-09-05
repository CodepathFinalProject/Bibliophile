package com.codepath.bibliophile.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.camera.BarcodeCaptureActivity;
import com.codepath.bibliophile.client.GoogleBooksClient;
import com.codepath.bibliophile.model.GoogleBookModel;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment implements View.OnClickListener {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

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
        public void onSearchBookClicked(GoogleBookModel googleBookModel);
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
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onClick(View view) {
        // If users don't enter an ISBN, complain
        if (etISBN.getText() == null || etISBN.getText().toString().equals("")) {
            Toast.makeText(getContext(), R.string.ISBN_validation_none, Toast.LENGTH_SHORT).show();

        // If users enter a non-long ISBN, complain
        } else if (!isParseableIntoLong(etISBN.getText().toString())) {
            Toast.makeText(getContext(), R.string.ISBN_validation_long, Toast.LENGTH_SHORT).show();

        // If ISBN is well-formed long, send API call
        } else {
            client.getBookDetailsFromISBN(etISBN.getText().toString(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    long ISBN = Long.parseLong(etISBN.getText().toString());
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get references
        client = GoogleBooksClient.getClient();

        etISBN = (EditText) view.findViewById(R.id.etISBN);
        btnSearchBarcode = (Button) view.findViewById(R.id.btnSearchBarcode);
        btnSearchByISBN = (Button) view.findViewById(R.id.btnSearchISBN);

        btnSearchByISBN.setOnClickListener(this);

        btnSearchBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    etISBN.setText(barcode.displayValue);
                    Toast.makeText(getActivity(), R.string.scan_barcode_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.scan_barcode_failure, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.scan_barcode_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean isParseableIntoLong(String text) {
        boolean isParseable = true;
        try {
            final long ISBN = Long.parseLong(text);
        } catch (NumberFormatException e) {
            isParseable = false;
            e.printStackTrace();
        }

        return isParseable;
    }
}
