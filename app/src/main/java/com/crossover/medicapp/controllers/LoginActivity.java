package com.crossover.medicapp.controllers;

import android.os.Bundle;

import com.crossover.medicapp.R;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;

/**
 * This is the Login activity. Here a user can login or create a Medicapp account
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends RoboActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
