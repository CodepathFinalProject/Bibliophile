package com.codepath.bibliophile;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.codepath.bibliophile.model.BookModel;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

public class ParseApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "myBibliophileApp";
    public static final String YOUR_CLIENT_KEY = "BibliophileAndroidApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(BookModel.class);

        // set applicationId, and server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myBibliophileApp") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://bibliophile-android-app.herokuapp.com/parse/").build());

        ParseFacebookUtils.initialize(getBaseContext()); // not sure if this is the right context
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
