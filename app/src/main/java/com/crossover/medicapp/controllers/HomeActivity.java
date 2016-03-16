package com.crossover.medicapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crossover.business.services.api.ISessionService;
import com.crossover.medicapp.R;
import com.crossover.medicapp.controllers.calendar.MainCalendarFragment;
import com.google.inject.Inject;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * This is the home activity where menu can be accessed
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
@ContentView(R.layout.activity_home)
public class HomeActivity extends RoboActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /** Fst action btn **/
    @InjectView(R.id.fst_action_mditv)
    protected TextView mFstActionBtn;

    /** Snd action btn **/
    @InjectView(R.id.snd_action_mditv)
    protected TextView mSndActionBtn;

    /** Drawer Layout **/
    @InjectView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    /** Navigation View **/
    @InjectView(R.id.nav_view)
    protected NavigationView mNavigationView;

    /** Toolbar **/
    @InjectView(R.id.toolbar)
    protected Toolbar mToolbar;

    /** Drawer List **/
    protected ActionBarDrawerToggle mDrawerToggle;

    /** Session Service **/
    @Inject
    private ISessionService mSessionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFstActionBtn.setVisibility(View.GONE);
        mSndActionBtn.setVisibility(View.GONE);

        initDrawer();
    }

    /**
     * This method initializes the Drawer
     */
    private void initDrawer() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ViewCompat.setElevation(mToolbar, getResources().getDimension(R.dimen.toolbar_elevation));

        mNavigationView.inflateMenu(R.menu.menu_admin);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item
     *         The selected item
     *
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        switch (id) {
            case R.id.home_item:
                break;
            case R.id.calendar_item:
                fragment = MainCalendarFragment.newInstance();
                break;
            case R.id.log_out_item:
                logOut();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content_rl, fragment).commit();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Log out
     */
    private void logOut() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSessionService.logout();
            }
        }).start();
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (fm.getBackStackEntryCount() != 0) {
            fm.popBackStack();
        } else {
            finishAffinity();
        }
    }

}
