package com.ayaz.instamart.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.ayaz.instamart.marttool.handler.ConnectHandler;
import com.ayaz.instamart.marttool.Feedback;
import com.ayaz.instamart.marttool.session.Login;

import com.ayaz.instamart.marttool.R;
import com.ayaz.instamart.sdk.Preferences;
import com.ayaz.instamart.sdk.Util;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private Context context = this;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private TextView emailText;
    private TextView passwordText;
    private Button loginButton;
    private Drawable errorIcon;
    private Boolean running = false;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView registerText;

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.textInputLayout4);
        populateAutoComplete();

        registerText = (TextView) findViewById(R.id.textView10);
        emailText = (TextView) findViewById(R.id.textView8);
        passwordText = (TextView) findViewById(R.id.textView9);
        loginButton = (Button) findViewById(R.id.email_login_button);

        mPasswordView = (EditText) findViewById(R.id.textInputLayout5);
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout_login);


        registerText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(context, SignActivity.class);
                startActivity(intentRegister);
                finish();
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_login_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!running){
                    running = true;
                    attemptLogin();
                    running = false;
                }
            }
        });

        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        errorIcon = getResources().getDrawable(R.drawable.ic_error_outline_black_24dp);
        if(errorIcon != null){
            errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());
        }

        // Reset errors.
        Util.setError(mEmailView);
        Util.setError(mPasswordView);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!Util.isNullOrEmpty(password) && !isPasswordValid(password)) {
            Util.setError(mPasswordView, getString(R.string.error_invalid_password), errorIcon);
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (Util.isNullOrEmpty(email)) {
            Util.setError(mEmailView, getString(R.string.error_field_required), errorIcon);
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            Util.setError(mEmailView, getString(R.string.error_invalid_email), errorIcon);
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);

            if(!Util.isInternetConnected(context)){
                Snackbar.make(findViewById(R.id.activitylogin),
                        getString(R.string.network_connection_problem), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else{
                Snackbar snackbarConnect = Snackbar.make(findViewById(R.id.activitylogin),
                        getString(R.string.logging_in), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null);
                snackbarConnect.show();
            }

            ConnectHandler handler = new ConnectHandler() {
                @Override
                public void onSucceed(JSONObject result) {
                    try {
                        Preferences.saveApiyKey(getApplicationContext(), result.getString("apikey"));
                        Preferences.saveName(getApplicationContext(), result.getString("name"));
                        Preferences.saveEmail(getApplicationContext(), result.getString("email"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Preferences.saveIsFirstConnection(context, false);
                    Preferences.saveLoggedIn(context, true);
                    Preferences.saveEmail(context, mEmailView.getText().toString());
                    Intent intent = new Intent(context, ProductListActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailed(JSONObject result) {
                    try {
                        String feedback = result.getString("feedback");

                        if(feedback
                                .equals(Feedback.EMAIL_OR_PASSWORD_INCORRECT)){
                            Util.setError(mEmailView,
                                    getString(R.string.email_or_password_incorrect), errorIcon);
                            mEmailView.requestFocus();
                        }else if(feedback.equals(Feedback.INVALID_ACCOUNT)){
                            Snackbar.make(findViewById(R.id.activitylogin),
                                    getString(R.string.invalid_account), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }else if(feedback.equals(Feedback.ALREADY_SIGN_ON)){
                            Snackbar.make(findViewById(R.id.activitylogin),
                                    getString(R.string.already_sign_on), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }else if(feedback.equals(Feedback.PLEASE_TRY_AGAIN)){
                            Snackbar.make(findViewById(R.id.activitylogin),
                                    getString(R.string.please_try_again), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }else if(feedback.equals(Feedback.LOGIN_DENIED)){
                            Snackbar.make(findViewById(R.id.activitylogin),
                                    getString(R.string.login_denied), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }else if(feedback.equals(Feedback.DATABASE_CONNECTION_PROBLEM)){
                            Snackbar.make(findViewById(R.id.activitylogin),
                                    getString(R.string.database_connection_problem), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }else if(feedback.equals(Feedback.DATABASE_PROBLEM)){
                            Snackbar.make(findViewById(R.id.activitylogin),
                                    getString(R.string.database_problem), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }else if(feedback.equals(Feedback.EMAIL_AND_PASSWORD_REQUIRED)){
                            Snackbar.make(findViewById(R.id.activitylogin),
                                    getString(R.string.email_and_password_required), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }else {
                            Snackbar.make(findViewById(R.id.activitylogin),
                                    getString(R.string.unknown_problem), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onProblem() {
                    Snackbar.make(findViewById(R.id.activitylogin),
                            getString(R.string.serverside_problem), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            };
            Login login = new Login(mEmailView.getText().toString(), mPasswordView.getText().toString());
            login.setHandler(handler);
            login.start();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.substring(email.indexOf("@")).contains(".");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            emailText.setVisibility(show ? View.GONE : View.VISIBLE);
            emailText.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    emailText.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mEmailView.setVisibility(show ? View.GONE : View.VISIBLE);
            mEmailView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEmailView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            passwordText.setVisibility(show ? View.GONE : View.VISIBLE);
            passwordText.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    passwordText.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mPasswordView.setVisibility(show ? View.GONE : View.VISIBLE);
            mPasswordView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mPasswordView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            loginButton.setVisibility(show ? View.GONE : View.VISIBLE);
            loginButton.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginButton.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            emailText.setVisibility(show ? View.GONE : View.VISIBLE);
            mEmailView.setVisibility(show ? View.GONE : View.VISIBLE);
            passwordText.setVisibility(show ? View.GONE : View.VISIBLE);
            mPasswordView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginButton.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}