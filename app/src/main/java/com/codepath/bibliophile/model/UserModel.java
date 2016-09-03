package com.codepath.bibliophile.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by supriya on 8/28/16.
 */
public class UserModel {
    String name;
    String emailAddress;
    LatLng coord;
    String address;
    String profilePicUrl;
    String coverPicUrl;


    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String getCoverPicUrl() {
        return coverPicUrl;
    }

    public void setCoverPicUrl(String coverPicUrl) {
        this.coverPicUrl = coverPicUrl;
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

    public LatLng getCoord() {
        return coord;
    }

    public void setCoord(LatLng coord) {
        this.coord = coord;
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


}
