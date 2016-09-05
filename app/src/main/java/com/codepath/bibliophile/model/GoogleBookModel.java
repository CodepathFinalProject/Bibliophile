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

@Parcel
public class GoogleBookModel {
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


    private GoogleBookModel() {
    }

    public static GoogleBookModel fromJson(long ISBN, JSONObject volumeInfo) {
        GoogleBookModel googleBookModel = new GoogleBookModel();
        googleBookModel.authors = new ArrayList<>();

        try {
            googleBookModel.title = volumeInfo.getString("title");
            googleBookModel.description = volumeInfo.getString("description");
            googleBookModel.thumbnailUrl = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
            googleBookModel.googleWebUrl = volumeInfo.getString("canonicalVolumeLink");
            googleBookModel.averageRating = volumeInfo.getDouble("averageRating");
            googleBookModel.ratingsCount = volumeInfo.getInt("ratingsCount");
            googleBookModel.ISBN = ISBN;

            JSONArray authors = volumeInfo.getJSONArray("authors");
            for (int i = 0; i < authors.length(); i++) {
                googleBookModel.authors.add(authors.getString(i));
            }
            googleBookModel.subtitle = volumeInfo.getString("subtitle"); // Since many books do not have subtitle, it's causing widespread parsing failures
            if (googleBookModel.subtitle == null) {
                googleBookModel.subtitle = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googleBookModel;
    }

    public static GoogleBookModel fromJsonResponse(Context context, long ISBN, JSONObject response) {
        GoogleBookModel googleBookModel = null;

        try {
            // If Google Books API does not recognize the ISBN number, totalItems will be 0
            if (response.getInt("totalItems") < 1) {
                Toast.makeText(context, R.string.API_response_validation_empty, Toast.LENGTH_SHORT).show();
                return googleBookModel;
            }

            JSONArray items = response.getJSONArray("items");
            JSONObject firstBookObject = items.getJSONObject(0);
            JSONObject volumeInfo = firstBookObject.getJSONObject("volumeInfo");
            googleBookModel = fromJson(ISBN, volumeInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googleBookModel;
    }

}