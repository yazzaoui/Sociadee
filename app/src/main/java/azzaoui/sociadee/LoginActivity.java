package azzaoui.sociadee;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;


public class LoginActivity extends Activity {

    static final int REGISTER_REQUEST = 0;

    private boolean mLoginFirstLayout = false;
    private String mLoginEmail;
    private String mUsername;
    private String mPassword;
    private String mToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        /*
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.Hex));*/
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setErrorText(0);

    }




    public void goRegister(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivityForResult(registerIntent,REGISTER_REQUEST);
        overridePendingTransition(R.anim.fade_in2, R.anim.fade_out2);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REGISTER_REQUEST) {
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                mLoginEmail = data.getStringExtra("EMAIL");
                mPassword = data.getStringExtra("PASSWORD");
                LoginTask loginAsync = new LoginTask();
                loginAsync.execute();
            }
        }
    }


    private void startContestListActivity() {

        // TODO: Exception to code
        if(!mToken.isEmpty())
        {
            /*
            Intent intent = new Intent(this, ContestListActivity.class);
            intent.putExtra("EMAIL", mLoginEmail);
            intent.putExtra("TOKEN", mToken);

            final View logoView = findViewById(R.id.logoImage);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, logoView, "logo");
            startActivity(intent, options.toBundle());
            */
            Parameters.setToken(mToken);
            Parameters.setUsername(mUsername);
          //  Intent intent = new Intent(this, MainActivity.class);
           // startActivity(intent);
        }

    }

    public void goConnect(View view) {
        String username = ((TextView) findViewById(R.id.userNameEditText)).getText().toString();
        String password = ((TextView) findViewById(R.id.passwordEditText)).getText().toString();

        setErrorText(0);

        if (username.isEmpty() && password.isEmpty()) {
            setErrorText(R.string.error_login_nocredential);
        } else if (username.isEmpty()) {
            setErrorText(R.string.error_login_nousername);
        } else if (password.isEmpty()) {
            setErrorText(R.string.error_login_nopassword);
        } else {

            mLoginEmail = username;
            mPassword = password;
            LoginTask loginAsync = new LoginTask();
            loginAsync.execute();
        }
    }


    public void switchLoginLayout(View view) {
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.down_fade_out);

        final LinearLayout firstLayout;
        final LinearLayout secondLayout;

        if (!mLoginFirstLayout) {
            firstLayout = (LinearLayout) findViewById(R.id.signupChoiceLayout);
            secondLayout = (LinearLayout) findViewById(R.id.loginLayout);
        } else {
            secondLayout = (LinearLayout) findViewById(R.id.signupChoiceLayout);
            firstLayout = (LinearLayout) findViewById(R.id.loginLayout);

        }
        /** Fade Out the three button **/
        hyperspaceJumpAnimation.setRepeatCount(0);
        final float Y = firstLayout.getY();
        hyperspaceJumpAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                //secondLayout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation anim) {
            }

            @Override
            public void onAnimationEnd(Animation anim) {
                firstLayout.setVisibility(View.GONE);
                firstLayout.setAlpha(1.0f);
                firstLayout.setY(Y - 0.0001f);// Ugly hack- One beer if you can find something better
            }
        });

        firstLayout.startAnimation(hyperspaceJumpAnimation);

        /** Fade In the the textboxes **/

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeIn.setStartTime(600);


        fadeIn.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {

                mLoginFirstLayout = !mLoginFirstLayout;
                setErrorText(0);
                secondLayout.setVisibility(View.VISIBLE);

            }

            public void onAnimationRepeat(Animation anim) {
            }

            public void onAnimationEnd(Animation anim) {

            }
        });
        //fadeIn.setFillBefore(true);
        secondLayout.startAnimation(fadeIn);

    }

    public void goFacebook(View view) {
        setErrorText(R.string.error_notImplemented);
    }

    /**
     * Set the error text depending on the layout state
     *
     * @param resInt : if 0 no text
     */
    private void setErrorText(int resInt) {
        TextView errorView;
        String text;

        if (resInt != 0)
            text = getResources().getString(resInt);
        else
            text = "";

        if (mLoginFirstLayout)
            errorView = (TextView) findViewById(R.id.errorTextView2);
        else
            errorView = (TextView) findViewById(R.id.errorTextView1);

        if (text != null) {
            errorView.setText(text);
        } else {
            errorView.setText("");
        }
    }

    private void spinAnimate() {
        Animation spinAnim = AnimationUtils.loadAnimation(this, R.anim.spin);
        final ImageView spiner = (ImageView) findViewById(R.id.progressBar);
        spinAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                spiner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation anim) {
            }

            @Override
            public void onAnimationEnd(Animation anim) {
            }
        });
        spiner.startAnimation(spinAnim);

    }


    private void spinStopAnimation() {
        Animation spinAnim = AnimationUtils.loadAnimation(this, R.anim.spin_stop);
        final ImageView spiner = (ImageView) findViewById(R.id.progressBar);
        spiner.clearAnimation();
        spinAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                spiner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation anim) {
            }

            @Override
            public void onAnimationEnd(Animation anim) {

                spiner.setVisibility(View.INVISIBLE);

            }
        });
        spiner.startAnimation(spinAnim);
    }

    private class LoginTask extends AsyncTask<Void, Void, Void> {
        private Boolean mError = false;
        NetworkLogin netLogin;

        @Override
        protected void onPreExecute()
        {
            spinAnimate();
        }

        @Override
        protected Void doInBackground(Void... params) {
            netLogin = new NetworkLogin();
            try {
                mError = !netLogin.Login(mLoginEmail, mPassword);
            } catch (JSONException e) {
                mError = true;
            } catch (IOException e) {
                mError = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            spinStopAnimation();
            if (mError) {
                int lastError=0;
                try {
                    lastError = netLogin.getLastError();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(lastError == 1004)
                {
                    setErrorText(R.string.error_network_1004);
                }
                else
                {
                    setErrorText(R.string.error_network_9000);
                }
            }
            else
            {
                try {
                    mToken = netLogin.getToken();
                    mUsername = netLogin.getUserName();
                    startContestListActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
