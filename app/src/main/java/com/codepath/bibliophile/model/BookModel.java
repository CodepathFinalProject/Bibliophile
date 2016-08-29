package com.codepath.bibliophile.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("BookModel")
public class BookModel extends ParseObject {

    // Add a constructor that contains core properties
    public BookModel() {
    }

    public BookModel(Book book) {
        super();
        setTitle(book.getTitle());
        setAuthor(book.getPrimaryAuthor()); // TODO edge cases
        setBookCover(book.getThumbnailUrl());
        setDescription(book.getDescription());
        setPrice(book.getPrice());
        setAverageRating(book.getAverageRating());
        setCondition(book.getCondition());
        setISBN(book.getISBN());
    }

    // Use getString and others to access fields
    public String getTitle() {
        return getString("title");
    }

    public String getAuthor() {
        return getString("author");
    }

    public String getBookCover() {
        return getString("cover");
    }

    public String getDescription() {
        return getString("description");
    }

    public Double getPrice() {
        return getDouble("price");
    }

    public Double getAverageRating() {
        return getDouble("averageRating");
    }


    public int getRatingsCount() {
        return getInt("ratingsCount");
    }

    public long getISBN() {
        return getLong("ISBN");
    }

    public String getCondition() {
        return getString("condition");
    }

    public ParseUser getUser() {
        return getParseUser("owner");
    }

    public String getContactEmail() {
        return getString("email");
    }

    public String getBookOwner() {
        return getString("bookOwner");
    }


    public String getBookId(){
        return getString("_id");
    }
    public boolean getIsListed() {
        return getBoolean("isListed");
    }

    public boolean getIsTransactionComplete() {
        return getBoolean("isTransactionComplete");
    }


    // Use put to modify field values
    public void setTitle(String value) {
        put("title", value);
    }

    public void setAuthor(String value) {
        put("author", value);
    }

    public void setBookCover(String value) {
        put("cover", value);
    }

    public void setDescription(String value) {
        put("description", value);
    }

    public void setPrice(Double value) {
        put("price", value);
    }

    public void setAverageRating(Double value) {
        put("averageRating", value);
    }

    public void setRatingsCount(int value) {
        put("ratingsCount", value);
    }

    public void setISBN(long value) {
        put("ISBN", value);
    }

    public void setCondition(String value) {
        put("condition", value);
    }

    public void setOwner(ParseUser user) {
        put("owner", user);
    }

    public void setContactEmail(String email) {
        put("ownerEmail", email);
    }

    public void setBookOwner(String email) {
        put("bookOwner", email);
    }

    public void setIsListed(boolean isListed) {
        put("isListed", isListed);
    }

    public void setIsTransactionComplete(boolean isTransactionComplete) {
        put("isTransactionComplete", isTransactionComplete);
    }

    public void setBuyer(ParseUser user) {
        put("buyer", user);
    }
}



