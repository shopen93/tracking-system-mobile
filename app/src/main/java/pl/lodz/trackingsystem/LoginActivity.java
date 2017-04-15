package pl.lodz.trackingsystem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import pl.lodz.trackingsystem.utils.AppUtils;
import pl.lodz.trackingsystem.utils.ServerRequests;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        settings = getSharedPreferences(AppUtils.PREFS_NAME, 0);
        if(!"".equals(settings.getString("LOGIN", ""))) {
            // already logged
            nextAction();
        }

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        // stop if task in progress
        if (mAuthTask != null) {
            return;
        }

        // if fields valid try login
        if (validFields() ) {
            showProgress(true);
            mAuthTask = new UserLoginTask(mEmailView.getText().toString(), mPasswordView.getText().toString());
            mAuthTask.execute((Void) null);
        }

    }

    private boolean validFields() {

        // Reset errors
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Check for a valid password.
        if (TextUtils.isEmpty(mPasswordView.getText().toString()) ) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            return false;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmailView.getText().toString()) ) {
            mEmailView.setError(getString(R.string.error_field_required));
            return false;
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void nextAction() {
        // TODO redirect for next action (MODE)
        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
                param.add("login", mEmail);
                param.add("password", mPassword);
                String response = restTemplate.postForObject(ServerRequests.getLoginUrl(), param, String.class); // TODO repair this
                return new Boolean(response);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Boolean login) {
            mAuthTask = null;
            showProgress(false);

            if (login) {
                saveLogin(mEmail);
                nextAction();
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private void saveLogin(String email) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("LOGIN", email);
            editor.commit();
        }
    }

}

