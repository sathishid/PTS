package com.ara.approvalshipment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ara.approvalshipment.models.User;
import com.ara.approvalshipment.utils.AppService;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ara.approvalshipment.utils.Helper.CurrentUser;
import static com.ara.approvalshipment.utils.Helper.LOGIN_ACTION;
import static com.ara.approvalshipment.utils.Helper.getAppService;
import static com.ara.approvalshipment.utils.Helper.isAnyNetworkAvailable;
import static com.ara.approvalshipment.utils.Helper.log;
import static com.ara.approvalshipment.utils.Helper.showSnackbar;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.user_name)
    EditText mLoginId;

    @BindView(R.id.password)
    EditText mPasswordView;

    @BindView(R.id.login_progress)
    View mProgressView;

    @BindView(R.id.login_form)
    View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnEditorAction(R.id.password)
    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.email_sign_in_button)
    public void onSignIn(View view) {
        attemptLogin();
    }

    private void attemptLogin() {
        if (!isAnyNetworkAvailable(this)) {
            showSnackbar(mLoginFormView, R.string.internet_not_available);
            return;
        }
        if (!validate()) {
            return;
        }
        String loginId = mLoginId.getText().toString();
        String password = mPasswordView.getText().toString();
        AppService appService = getAppService();
        Call<ResponseBody> httpCall = appService.validateUser(LOGIN_ACTION, loginId, password);
        showProgress(true);
        httpCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showProgress(false);
                try {
                    String jsonResult = response.body().string();

                    doLogin(jsonResult);

                } catch (IOException exception) {
                    showSnackbar(mLoginFormView, R.string.something_went_wrong);
                    log("On Login", exception.getMessage());
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgress(false);
                showSnackbar(mLoginFormView, R.string.error_server);
            }
        });
    }


    private void doLogin(String jsonResult) {
        CurrentUser = User.fromJson(jsonResult);

        if (CurrentUser != null) {
            setResult(RESULT_OK);
            finish();
        } else {
            log("On Login", jsonResult);
            showSnackbar(mLoginFormView, R.string.login_failed);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean validate() {

        // Reset errors.
        mLoginId.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mLoginId.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (userName.isEmpty()) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        if (TextUtils.isEmpty(userName)) {
            mLoginId.setError(getString(R.string.error_field_required));
            focusView = mLoginId;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}

