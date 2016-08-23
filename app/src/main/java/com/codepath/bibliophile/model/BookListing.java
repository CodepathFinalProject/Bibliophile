package com.codepath.bibliophile.model;

/**
 * Created by bobbywei on 8/20/16.
 */
public class BookListing {

    private Book book;
    private double price;
    private String condition;

    public BookListing(Book book) {
        this.book = book;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Book getBook() {
        return book;
    }

    public double getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }
}