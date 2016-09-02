package com.codepath.bibliophile.model;

import android.content.Context;
import android.widget.Toast;

import com.codepath.bibliophile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobbywei on 8/20/16.
 */
@Parcel
public class Book {
    // Making all instance variables public to avoid Reflection from Parceler library
    String title; //
    String subtitle;
    List<String> authors;
    String description;
    String thumbnailUrl;
    String googleWebUrl;
    double averageRating;
    int ratingsCount;
    double price;
    String condition;
    long ISBN;


    public long getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getPrimaryAuthor() {
        String primaryAuthor = "";

        if (authors != null && authors.size() > 0) {
            primaryAuthor = authors.get(0);
        }

        return primaryAuthor;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getGoogleWebUrl() {
        return googleWebUrl;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public double getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }


    private Book() {
    }

    public static Book fromJson(long ISBN, JSONObject volumeInfo) {
        Book book = new Book();
        book.authors = new ArrayList<>();

        try {
            book.title = volumeInfo.getString("title");
            book.description = volumeInfo.getString("description");
            book.thumbnailUrl = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
            book.googleWebUrl = volumeInfo.getString("canonicalVolumeLink");
            book.averageRating = volumeInfo.getDouble("averageRating");
            book.ratingsCount = volumeInfo.getInt("ratingsCount");
            book.ISBN = ISBN;

            JSONArray authors = volumeInfo.getJSONArray("authors");
            for (int i = 0; i < authors.length(); i++) {
                book.authors.add(authors.getString(i));
            }
            book.subtitle = volumeInfo.getString("subtitle"); // Since many books do not have subtitle, it's causing widespread parsing failures
            if (book.subtitle == null) {
                book.subtitle = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return book;
    }

    public static Book fromJsonResponse(Context context, long ISBN, JSONObject response) {
        Book book = null;

        try {
            // If Google Books API does not recognize the ISBN number, totalItems will be 0
            if (response.getInt("totalItems") < 1) {
                Toast.makeText(context, R.string.API_response_validation_empty, Toast.LENGTH_SHORT).show();
                return book;
            }

            JSONArray items = response.getJSONArray("items");
            JSONObject firstBookObject = items.getJSONObject(0);
            JSONObject volumeInfo = firstBookObject.getJSONObject("volumeInfo");
            book = fromJson(ISBN, volumeInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return book;
    }

}