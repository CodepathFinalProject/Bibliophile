package com.codepath.bibliophile.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.camera.BarcodeCaptureActivity;
import com.codepath.bibliophile.client.GoogleBooksClient;
import com.codepath.bibliophile.model.Book;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

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
        public void onSearchBookClicked(Book book);
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
                        long ISBN = Long.parseLong(etISBN.getText().toString());
                        Book book = Book.fromJsonResponse(ISBN, response);
                        listener.onSearchBookClicked(book);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("failure", Integer.toString(statusCode));
                    }
                });
            }
        });

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
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    Toast.makeText(getActivity(), R.string.scan_barcode_failure, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Toast.makeText(getActivity(), R.string.scan_barcode_error, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Error scanning barcode");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
