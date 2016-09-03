package com.codepath.bibliophile.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("BookModel")
public class BookModel extends ParseObject {

    // Add a constructor that contains core properties
    public BookModel() {
    }

    public BookModel(GoogleBookModel googleBookModel) {
        super();
        setTitle(googleBookModel.getTitle());
        setAuthor(googleBookModel.getPrimaryAuthor()); // TODO edge cases
        setDescription(googleBookModel.getDescription());
        setBookCover(googleBookModel.getThumbnailUrl());
        setPrice(googleBookModel.getPrice());
        setISBN(googleBookModel.getISBN());
        setAverageRating(googleBookModel.getAverageRating());
        setCondition(googleBookModel.getCondition());
        setRatingsCount(googleBookModel.getRatingsCount());
        setIsTransactionComplete(false);
        setIsListed(true);
        setBuyerConfirmed(false);
        setSellerConfirmed(false);
    }

    // Use getString and others to access fields
    public String getTitle() {
        return getString("title");
    }

    public String getAuthor() {
        return getString("author");
    }

    public String getDescription() {
        return getString("description");
    }

    public String getBookCover() {
        return getString("cover");
    }

    public Double getPrice() {
        return getDouble("price");
    }

    public long getISBN() {
        return getLong("ISBN");
    }

    public String getCondition() {
        return getString("condition");
    }

    public Double getAverageRating() {
        return getDouble("averageRating");
    }

    public int getRatingsCount() {
        return getInt("ratingsCount");
    }

    public ParseUser getSeller() {
        return getParseUser("seller");
    }

    public ParseUser getBuyer() {
        return getParseUser("buyer");
    }

    public boolean getIsTransactionComplete() {
        return getBoolean("isTransactionComplete");
    }

    public boolean getIsListed() {
        return getBoolean("isListed");
    }

    public boolean getBuyerConfirmed() {
        return getBoolean("buyerConfirmed");
    }

    public boolean getSellerConfirmed() {
        return getBoolean("sellerConfirmed");
    }


    // Use put to modify field values
    public void setTitle(String value) {
        put("title", value);
    }

    public void setAuthor(String value) {
        put("author", value);
    }

    public void setDescription(String value) {
        put("description", value);
    }

    public void setBookCover(String value) {
        put("cover", value);
    }

    public void setPrice(Double value) {
        put("price", value);
    }

    public void setISBN(long value) {
        put("ISBN", value);
    }

    public void setCondition(String value) {
        put("condition", value);
    }

    public void setAverageRating(Double value) {
        put("averageRating", value);
    }

    public void setRatingsCount(int value) {
        put("ratingsCount", value);
    }

    public void setSeller(ParseUser seller) {
        put("seller", seller);
    }

    public void setBuyer(ParseUser buyer) {
        put("buyer", buyer);

    }

    public void setIsTransactionComplete(boolean isTransactionComplete) {
        put("isTransactionComplete", isTransactionComplete);
    }

    public void setIsListed(boolean isListed) {
        put("isListed", isListed);
    }

    public void setBuyerConfirmed(boolean buyerConfirmed) {
        put("buyerConfirmed", buyerConfirmed);
    }

    public void setSellerConfirmed(boolean sellerConfirmed) {
        put("sellerConfirmed", sellerConfirmed);
    }

}



