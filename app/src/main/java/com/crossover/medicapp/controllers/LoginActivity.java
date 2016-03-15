package com.crossover.medicapp.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.crossover.business.services.api.ISessionService;
import com.crossover.common.model.common.User;
import com.crossover.common.model.utils.StringUtils;
import com.crossover.medicapp.R;
import com.crossover.medicapp.controllers.create_account.CreateAccountActivity;
import com.crossover.medicapp.custom_views.progress_bars.ProgressWheel;
import com.crossover.medicapp.utils.ViewUtils;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.inject.Inject;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

/**
 * This is the Login activity. Here a user can login or create a Medicapp account
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends RoboActionBarActivity implements View.OnClickListener,
        TextView.OnEditorActionListener {

    /** Main container LinearLayout **/
    @InjectView(R.id.container_ll)
    private View mContainerLl;

    /** Login Button **/
    @InjectView(R.id.login_btn)
    private Button mLoginBtn;

    /** Create account Button **/
    @InjectView(R.id.create_account_btn)
    private TextView mCreateAccountBtn;

    /** Username EditText **/
    @InjectView(R.id.username_et)
    private EditText mUsernameEt;

    /** Password EditText **/
    @InjectView(R.id.password_et)
    private EditText mPasswordEt;

    /** Welcome msg **/
    @InjectResource(R.string.welcome_user)
    private String mWelcomeUserMsg;

    /** Invalid username/password msg **/
    @InjectResource(R.string.incorrect_username_password)
    private String mIncorrectUserPassMsg;

    /** Keep session **/
    @InjectView(R.id.remember_password_cb)
    private CheckBox mRememberPassCb;

    /** Loading Progress Wheel **/
    @InjectView(R.id.loading_pw)
    private ProgressWheel mLoadingPw;

    /** Session service **/
    @Inject
    private ISessionService mSessionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginBtn.setOnClickListener(this);
        mCreateAccountBtn.setOnClickListener(this);

        mPasswordEt.setOnEditorActionListener(this);
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
                doLogin();
                break;
            case R.id.create_account_btn:
                Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
                startActivity(createAccountIntent);
                break;
        }
    }

    /**
     * This method does the login process, which includes fields validations
     */
    private void doLogin() {
        if (validateFields()) {
            new LoginAsyncTask(mUsernameEt.getText().toString(), mPasswordEt.getText().toString(),
                    mRememberPassCb.isChecked()).execute();
        }
    }

    /**
     * This method validates the fields
     *
     * @return True if fields are valid. False otherwise
     */
    private boolean validateFields() {
        if (mUsernameEt.getText().length() == 0) {
            mUsernameEt.setError(getString(R.string.required_field));
            return false;
        }
        if (mPasswordEt.getText().length() == 0) {
            mPasswordEt.setError(getString(R.string.required_field));
            return false;
        }

        return true;
    }

    /**
     * This method enables the Loading Progress Wheel and its related views
     *
     * @param enable
     *         Enables/Disables progress wheel and its related views
     */
    private void enableLoading(boolean enable) {
        mLoadingPw.setVisibility(enable ? View.VISIBLE : View.GONE);
        mContainerLl.setVisibility(enable ? View.GONE : View.VISIBLE);

        mLoadingPw.stopSpinning();
        if (enable) {
            mLoadingPw.spin();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    /**
     * Called when an action is being performed.
     *
     * @param v
     *         The view that was clicked.
     * @param actionId
     *         Identifier of the action.  This will be either the
     *         identifier you supplied, or {@link EditorInfo#IME_NULL
     *         EditorInfo.IME_NULL} if being called due to the enter key
     *         being pressed.
     * @param event
     *         If triggered by an enter key, this is the event;
     *         otherwise, this is null.
     *
     * @return Return true if you have consumed the action, else false.
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            ViewUtils.hideKeyboard(LoginActivity.this);
            mLoginBtn.performClick();
            return true;
        }
        return false;
    }

    /**
     * This AsyncTask does the login connection
     */
    private class LoginAsyncTask extends AsyncTask<Void, Void, Boolean> {

        /** Username **/
        String username;
        /** Password **/
        String password;
        /** Keep session **/
        boolean keepSession;

        public LoginAsyncTask(String username, String password, boolean keepSession) {
            this.username = username;
            this.password = password;
            this.keepSession = keepSession;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            enableLoading(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulates server connection
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return mSessionService.login(username, password, keepSession);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            String message;
            int color;

            if (success) {
                User logedInUser = mSessionService.getCurrentSession();
                message = StringUtils.format(mWelcomeUserMsg, logedInUser.getName());
                color = Style.BLUE;
                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finishAffinity();
            } else {
                message = mIncorrectUserPassMsg;
                color = Style.RED;
            }

            ViewUtils.makeToast(LoginActivity.this, message, SuperToast.Duration.EXTRA_LONG, color)
                    .show();
            
            enableLoading(false);
        }
    }

}
