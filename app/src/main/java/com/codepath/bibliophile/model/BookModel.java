package com.codepath.bibliophile.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("BookModel")
public class BookModel extends ParseObject {
    // Add a constructor that contains core properties

    public BookModel(){
    }


    public BookModel(String bookTitle, String author, String bookCover,
                     String description, String price, String rating){
        super();
        setTitle(bookTitle);
        setAuthor(author);
        setBookCover(bookCover);
        setDescription(description);
        setPrice(price);
        setRating(rating);

    }


    // Use getString and others to access fields
    public String getTitle() {
        return getString("bookTitle");
    }

    public String getAuthor() {
        return getString("Author");
    }

    public String getBookCover() {
        return getString("BookCover");
    }
    public String getDescription() {
        return getString("Description");
    }


    public String getPrice() {
        return getString("price");
    }
    public String getRating() {
        return getString("Rating");
    }







    // Use put to modify field values
    public void setTitle(String value) {
        put("bookTitle", value);
    }
    public void setAuthor(String value) {
        put("Author", value);
    }
    public void setBookCover(String value) {
        put("BookCover", value);
    }
    public void setDescription(String value) {
        put("Description", value);
    }
    public void setPrice(String value) {
        put("price", value);
    }
    public void setRating(String value) {
        put("Rating", value);
    }





    // Get the user for this item
    public ParseUser getUser()  {
        return getParseUser("owner");
    }

    // Associate each item with a user
    public void setOwner(ParseUser user) {
        put("owner", user);
    }



}



