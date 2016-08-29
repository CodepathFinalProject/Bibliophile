package com.codepath.bibliophile.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.fragment.AddBookFragment;
import com.codepath.bibliophile.fragment.BookShelfFragment;
import com.codepath.bibliophile.fragment.HomeFragment;
import com.codepath.bibliophile.fragment.PostFragment;
import com.codepath.bibliophile.fragment.ProfileFragment;
import com.codepath.bibliophile.fragment.TransactionFragment;
import com.codepath.bibliophile.model.Book;
import com.codepath.bibliophile.model.BookModel;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class HomeMainActivity extends AppCompatActivity implements PostFragment.OnSearchBookListener, AddBookFragment.OnPostBookListener {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView rvHomePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);


        // Setup drawer view
        setupDrawerContent(nvDrawer);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();


        }
//
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599

                FragmentTransaction frameManager = getSupportFragmentManager().beginTransaction();

                // Add a book as a fragment argument
                HomeFragment homeFragment = new HomeFragment();
                Bundle args = new Bundle();
                args.putString("query", query);
                Log.d("searchQuery", "onQueryTextSubmit: " + query);
                homeFragment.setArguments(args);

                frameManager.replace(R.id.flContent, homeFragment).commit();
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_bookshelf:
                Log.d("bookshelf", "selectDrawerItem: ");
                fragmentClass = BookShelfFragment.class;  // TODO replace with other fragments
                break;
            case R.id.nav_transaction:
                fragmentClass = TransactionFragment.class;
                break;
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                break;
            case R.id.nav_logout:
                ParseUser.logOut();
                Intent intent = new Intent(HomeMainActivity.this, LoginActivity.class);
                startActivity(intent);

            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    public void addBook(View view) {
        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.fabAdd);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // TODO figure out why this doesn't work the first time you press the button
                Toast.makeText(getBaseContext(), "pressed FAB", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new PostFragment()).commit();
            }
        });
    }

    @Override
    public void onSearchBookClicked(Book book) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Add a book as a fragment argument
        AddBookFragment addBookFragment = new AddBookFragment();
        Bundle args = new Bundle();
        args.putParcelable("book", Parcels.wrap(book));
        addBookFragment.setArguments(args);

        ft.replace(R.id.flContent, addBookFragment);
        ft.addToBackStack("post");
        ft.commit();
    }

    @Override
    public void onPostClicked(Book book) {
        // TODO save book to database
        Toast.makeText(this, "Saving " + book.getTitle() + " to the database", Toast.LENGTH_SHORT).show(); // TODO remove

        BookModel parseBook = new BookModel(book);
        parseBook.setOwner(ParseUser.getCurrentUser());
        parseBook.setBookOwner(ParseUser.getCurrentUser().getString("username"));
        parseBook.setContactEmail(ParseUser.getCurrentUser().getString("email"));
        parseBook.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //  Toast.makeText(getApplicationContext(),"saved record " + parseBook.getContactEmail() ,Toast.LENGTH_SHORT).show();
                } else {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "error saving : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HomeFragment()).commit(); // TODO would like to use Bookshelf Fragment but the POST call does not finish before the GET call
    }

    @Override
    public void onCancelClicked() {
        Toast.makeText(this, "Cancelled this post", Toast.LENGTH_SHORT).show(); // TODO remove

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

}

