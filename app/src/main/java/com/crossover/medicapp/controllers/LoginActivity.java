package com.crossover.medicapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crossover.business.services.api.ISessionService;
import com.crossover.medicapp.R;
import com.crossover.medicapp.controllers.create_account.CreateAccountActivity;
import com.google.inject.Inject;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * This is the Login activity. Here a user can login or create a Medicapp account
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends RoboActionBarActivity implements View.OnClickListener {

    /** Login Button **/
    @InjectView(R.id.login_btn)
    private Button mLoginBtn;

    /** Create account Button **/
    @InjectView(R.id.create_account_btn)
    private TextView mCreateAccountBtn;

    /** Session service **/
    @Inject
    private ISessionService mSessionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginBtn.setOnClickListener(this);
        mCreateAccountBtn.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v
     *         The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.login_btn:

                break;
            case R.id.create_account_btn:
                Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
                startActivity(createAccountIntent);
                break;
        }
    }
}
