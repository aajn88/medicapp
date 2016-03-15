package com.crossover.medicapp.controllers.create_account;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.crossover.business.services.api.ISessionService;
import com.crossover.common.model.common.User;
import com.crossover.common.model.constants.Role;
import com.crossover.medicapp.R;
import com.crossover.medicapp.custom_views.progress_bars.ProgressWheel;
import com.crossover.medicapp.utils.ViewUtils;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.inject.Inject;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_create_account)
public class CreateAccountActivity extends RoboActionBarActivity implements View.OnClickListener {

    /** Toolbar **/
    @InjectView(R.id.toolbar)
    private Toolbar mToolbar;

    /** First Action Button **/
    @InjectView(R.id.fst_action_mditv)
    private TextView mFstActionBtn;

    /** Second Action Button **/
    @InjectView(R.id.snd_action_btn)
    private TextView mSndActionBtn;

    /** Create Account LinearLayout **/
    @InjectView(R.id.create_account_ll)
    private View mCreateAccountLl;

    /** Name EditText **/
    @InjectView(R.id.user_name_et)
    private EditText mNameEt;

    /** Username EditText **/
    @InjectView(R.id.username_et)
    private EditText mUsernameEt;

    /** Password EditText **/
    @InjectView(R.id.password_et)
    private EditText mPasswordEt;

    /** Repeat password EditText **/
    @InjectView(R.id.repeat_password_et)
    private EditText mRepeatPasswordEt;

    /** Role Spinner **/
    @InjectView(R.id.roles_sp)
    private Spinner mRoleSp;

    /** Loading Progress Wheel **/
    @InjectView(R.id.loading_pw)
    private ProgressWheel mLoadingPw;

    /** Session Service **/
    @Inject
    private ISessionService mSessionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSndActionBtn.setVisibility(View.GONE);
        mFstActionBtn.setOnClickListener(this);

        RolesAdapter adapter = new RolesAdapter(this, Role.values());
        mRoleSp.setAdapter(adapter);
    }

    /**
     * This method enables/disables the loading progress bars and hides/show all related views
     *
     * @param enable
     *         Enable/Disable
     */
    private void enableLoading(boolean enable) {
        mLoadingPw.setVisibility(enable ? View.VISIBLE : View.GONE);
        mCreateAccountLl.setVisibility(enable ? View.GONE : View.VISIBLE);

        if (mLoadingPw.isSpinning()) {
            mLoadingPw.stopSpinning();
        }

        if (enable) {
            mLoadingPw.spin();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v
     *         The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fst_action_mditv:
                createAccount();
                break;
        }
    }

    /**
     * This method validates and creates an account
     */
    private void createAccount() {
        if (validateFields()) {
            User user = buildUser();
            new CreateUserAsyncTask().execute(user);
        }
    }

    /**
     * This method builds the user given all the fields
     *
     * @return The User
     */
    private User buildUser() {
        User user = new User();

        user.setName(mNameEt.getText().toString());
        user.setUsername(mUsernameEt.getText().toString());
        user.setPassword(mPasswordEt.getText().toString());
        user.setRole((Role) mRoleSp.getSelectedItem());

        return user;
    }

    /**
     * This method validates if the fields are correct
     */
    private boolean validateFields() {
        if (mNameEt.getText().length() == 0) {
            mNameEt.setError(getString(R.string.error_name));
            return false;
        }
        if (mUsernameEt.getText().length() < 4) {
            mUsernameEt.setError(getString(R.string.error_username));
            return false;
        }
        if (!mSessionService.checkAvailableUsername(mUsernameEt.getText().toString())) {
            mUsernameEt.setError(getString(R.string.error_username_exists));
            return false;
        }
        if (mPasswordEt.getText().length() < 6) {
            mPasswordEt.setError(getString(R.string.error_username));
            return false;
        } else if (!mPasswordEt.getText().toString()
                .equals(mRepeatPasswordEt.getText().toString())) {
            mPasswordEt.setError(getString(R.string.error_passwords_not_match));
            mRepeatPasswordEt.setError("");
            return false;
        }
        return true;
    }

    private class CreateUserAsyncTask extends AsyncTask<User, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            enableLoading(true);
        }

        @Override
        protected Boolean doInBackground(User... params) {
            try {
                // Simulates server connection
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
            return mSessionService.createAccount(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            enableLoading(false);

            if (!success) {
                ViewUtils.makeToast(CreateAccountActivity.this, R.string.connection_error,
                        SuperToast.Duration.EXTRA_LONG, Style.RED).show();
            } else {
                ViewUtils.makeToast(CreateAccountActivity.this, R.string.account_created,
                        SuperToast.Duration.EXTRA_LONG, Style.BLUE).show();
                finish();
            }
        }
    }
}
