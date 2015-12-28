package azzaoui.sociadee;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends Activity {


    String mUsername;
    String mPassword;
    String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setErrorText(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in2, R.anim.fade_out2);
    }

    public void tryRegister(View view)
    {
        String username = ((TextView) findViewById(R.id.userNameRegisterEditText)).getText().toString();
        String email = ((TextView) findViewById(R.id.userEmailRegisterEditText)).getText().toString();
        String password = ((TextView) findViewById(R.id.passwordRegisterEditText)).getText().toString();

        if(username.isEmpty() || email.isEmpty() ||password.isEmpty())
        {
            setErrorText(R.string.error_login_nocredential);
        }
        else
        {
            spinAnimate();
            mUsername = username;
            mPassword = password;
            mEmail = email;
            RegisterTask registerAsync = new RegisterTask();
            registerAsync.execute();
        }
    }

    private void spinAnimate() {
        Animation spinAnim = AnimationUtils.loadAnimation(this, R.anim.spin);
        final ImageView spiner = (ImageView) findViewById(R.id.progressBarRegister);
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
        final ImageView spiner = (ImageView) findViewById(R.id.progressBarRegister);
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


        errorView = (TextView) findViewById(R.id.errorTextViewRegister);

        if (text != null) {
            errorView.setText(text);
        } else {
            errorView.setText("");
        }
    }

    private class RegisterTask extends AsyncTask<Void, Void, Void> {
        private Boolean mError = false;
        NetworkLogin netRegister;

        @Override
        protected Void doInBackground(Void... params) {
            netRegister = new NetworkLogin();
            try {
                mError = !netRegister.Register(mEmail,mUsername,mPassword);
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
                    lastError = netRegister.getLastError();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (lastError)
                {

                    case 1000:
                        setErrorText(R.string.error_network_1000);
                        break;
                    case 1001:
                        setErrorText(R.string.error_network_1001);
                        break;
                    case 1002:
                        setErrorText(R.string.error_network_1002);
                        break;
                    case 1003:
                        setErrorText(R.string.error_network_1003);
                        break;
                    case 1004:
                        setErrorText(R.string.error_network_1004);
                        break;
                    case 2000:
                        setErrorText(R.string.error_network_2000);
                        break;
                    case 9000:
                        setErrorText(R.string.error_network_9000);
                        break;

                    default:
                        setErrorText(R.string.error_network_9000);
                        break;
                }

            }
            else
            {
                Intent data = new Intent();
                data.putExtra("USERNAME",mUsername);
                data.putExtra("PASSWORD",mPassword);
                data.putExtra("EMAIL",mEmail);
                setResult(RESULT_OK, data);
                finish();
                overridePendingTransition(R.anim.fade_in2, R.anim.fade_out2);
            }


        }


    }
}
