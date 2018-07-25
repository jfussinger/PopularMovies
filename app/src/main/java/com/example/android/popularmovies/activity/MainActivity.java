package com.example.android.popularmovies.activity;

//https://developer.android.com/guide/topics/ui/layout/gridview.html
//https://github.com/Mayur-007/iSearch
//https://www.androidhive.info/2016/01/android-working-with-recycler-view/

//https://stackoverflow.com/questions/47393110/how-to-add-onclicklistener-for-grid-view-which-opens-new-activity

//https://www.androidhive.info/2015/04/android-getting-started-with-material-design/
//https://www.androidhive.info/2013/11/android-sliding-menu-using-navigation-drawer/

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.fragments.FavoriteMovieFragment;
import com.example.android.popularmovies.fragments.PopularMovieFragment;
import com.example.android.popularmovies.fragments.TopRatedMovieFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "mainactivity";

    private Toolbar mToolbar;

    //https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://stackoverflow.com/questions/22557780/first-fragment-to-be-added-to-the-main-activity-when-application-starts-up
        //https://github.com/codepath/android-fragment-basics/blob/master/app/src/main/java/com/codepath/mypizza/MainActivity.java

        if(savedInstanceState == null) {

            PopularMovieFragment firstFragment = new PopularMovieFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.flContent, firstFragment).commit();
        }

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Navigation View
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass;

        switch (item.getItemId()) {
            case R.id.nav_popularMovie:
                Log.i(TAG, "popularMovie selected");
                fragmentClass = PopularMovieFragment.class;
                setTitle(R.string.nav_popularMovie);
                break;
            case R.id.nav_topRatedMovie:
                Log.i(TAG, "topRatedMovie selected");
                fragmentClass = TopRatedMovieFragment.class;
                setTitle(R.string.nav_topRatedMovie);
                break;
            case R.id.nav_favoriteMovie:
                Log.i(TAG, "favoriteMovie selected");
                fragmentClass = FavoriteMovieFragment.class;
                setTitle(R.string.nav_favoriteMovie);
                break;
            default:
                fragmentClass = PopularMovieFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //https://guides.codepath.com/android/Fragment-Navigation-Drawer

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
    }

}

