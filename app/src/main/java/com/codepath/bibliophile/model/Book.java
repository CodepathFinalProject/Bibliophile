package com.codepath.bibliophile.model;

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
    public String title;
    public String subtitle;
    public List<String> authors;
    public String description;
    public String thumbnailUrl;
    public String googleWebUrl;
    public double averageRating;
    public int ratingsCount;

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public List<String> getAuthors() {
        return authors;
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

    private Book() {
    }

    public static Book fromJson(JSONObject volumeInfo) {
        Book book = new Book();
        book.authors = new ArrayList<>();

        try {
            book.title = volumeInfo.getString("title");
            book.subtitle = volumeInfo.getString("subtitle");
            book.description = volumeInfo.getString("description");
            book.thumbnailUrl = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
            book.googleWebUrl = volumeInfo.getString("canonicalVolumeLink");
            book.averageRating = volumeInfo.getDouble("averageRating");
            book.ratingsCount = volumeInfo.getInt("ratingsCount");

            JSONArray authors = volumeInfo.getJSONArray("authors");
            for (int i = 0; i < authors.length(); i++) {
                book.authors.add(authors.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return book;
    }

    public static Book fromJsonResponse(JSONObject response) {
        Book book = null;

        try {
            JSONArray items = response.getJSONArray("items");
            JSONObject firstBookObject = items.getJSONObject(0);
            JSONObject volumeInfo = firstBookObject.getJSONObject("volumeInfo");
            book = fromJson(volumeInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return book;
    }
}
