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
        setBody(bookTitle);
        setBody(author);
        setBody(bookCover);
        setBody(description);
        setBody(price);
        setBody(rating);

    }


    // Use getString and others to access fields
    public String getBody() {
        return getString("bookTitle");
    }



    // Use put to modify field values
    public void setBody(String value) {
        put("bookTitle", value);
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



