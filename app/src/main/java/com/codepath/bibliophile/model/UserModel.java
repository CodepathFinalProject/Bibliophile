package com.codepath.bibliophile.model;

import com.parse.ParseGeoPoint;

/**
 * Created by supriya on 8/28/16.
 */
public class UserModel {
    String profilePicUrl;

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ParseGeoPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ParseGeoPoint coordinates) {
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String emailAddress;
    ParseGeoPoint coordinates;
    String address;
    String name;

}
