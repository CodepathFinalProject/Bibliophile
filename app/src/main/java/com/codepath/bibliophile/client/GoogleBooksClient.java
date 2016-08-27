package com.codepath.bibliophile.client;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by bobbywei on 8/19/16.
 */
public class GoogleBooksClient extends AsyncHttpClient {

    private static GoogleBooksClient client = new GoogleBooksClient();

    private static final String REST_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String REST_CONSUMER_KEY = "AIzaSyAYcGB3e02jPzmF2IuSCclxeEyEuRoEYKc";
    private static final String PARAM_KEY_KEY = "key";
    private static final String PARAM_KEY_Q = "q";
    private static final String FORMAT_Q = "isbn:%s";

    private GoogleBooksClient() {
    }

    public static GoogleBooksClient getClient() {
        return client;
    }

    public void getBookDetailsFromISBN(String ISBN, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.add(PARAM_KEY_KEY, REST_CONSUMER_KEY);
        params.add(PARAM_KEY_Q, String.format(FORMAT_Q, ISBN));

        client.get(REST_URL, params, handler);
    }
}