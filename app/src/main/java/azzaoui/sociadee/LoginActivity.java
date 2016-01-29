package azzaoui.sociadee;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends Activity {


    private String mToken = "";
    private LoginButton loginButton;
    private long mFacebookId = 0;
    private String mPictureUrl;
    private Boolean mNoBug = true;
    private NetworkLogin networkLogin;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();

        networkLogin = new NetworkLogin();

        ((TextView) findViewById(R.id.errorTextView1)).setVisibility(View.INVISIBLE);
        callbackManager = CallbackManager.Factory.create();

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends,user_photos,email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                spinAnimate();
                Parameters.setFBToken(loginResult.getAccessToken().getToken());
                getFacebookStuff gFs = new getFacebookStuff();
                gFs.execute();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                setErrorText(exception.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void setErrorText(String text)
    {
        TextView errorTextView =(TextView) findViewById(R.id.errorTextView1);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(text);
    }

    private void goMainActivity()
    {

        Parameters.setFBToken(mToken);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in2, R.anim.fade_out2);
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


    private class getFacebookStuff extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Void doInBackground(Void... params) {
           // fetchFacebookProfilePicture();

            try {
                networkLogin.Login(Parameters.getFBToken());
                Parameters.setFirstname(networkLogin.getFirstName());
                Parameters.setSociadeeToken(networkLogin.getSociadeeToken());
                Parameters.setFacebookId(networkLogin.getFaceBookid());
                Parameters.setProfilePicture(new BitmapDrawable(getResources(), networkLogin.getProfilePicture()));
                Parameters.setAboutme(networkLogin.getAboutme());
                Parameters.setAvailable(networkLogin.isAvailable());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            spinStopAnimation();
            goMainActivity();
        }


    }
    /*
    private void fetchFacebookData()
    {

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,picture.type(large)");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject firstResponse = response.getJSONObject();
                        try {
                            mFacebookId = firstResponse.getLong("id");
                            Parameters.setFacebookId(mFacebookId);
                            String mFirstname = firstResponse.getString("first_name");
                            Parameters.setFirstname(mFirstname);
                            mPictureUrl= firstResponse.getJSONObject("picture").getJSONObject("data").getString("url");

                        } catch (JSONException e) {
                            setErrorText("Error getting facebook id :(");
                            mNoBug = false;

                        }

                    }
                }
        ).executeAsync();
    }

    //google map : AIzaSyBGSNFjfGpm5eAVvAIeV_lu4GkVV46w08Y
    //Depreciated no more used
    private void fetchFacebookProfilePicture()
    {
        try {
            URL image_value = new URL(mPictureUrl);
            Drawable profilePic = null;
            try {

                profilePic = Drawable.createFromStream(image_value.openConnection().getInputStream(),"blah");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parameters.setProfilePicture(profilePic);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    } */
}
