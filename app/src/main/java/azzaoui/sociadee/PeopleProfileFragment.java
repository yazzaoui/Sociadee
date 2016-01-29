package azzaoui.sociadee;


import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleProfileFragment extends Fragment implements SociadeeFragment {

    private NetworkUserInfo mNetworkUserInfo;

    private TextView mAvailability;
    private TextView mCity;
    private TextView mAboutme;
    private TextView mFirstname;
    private ImageView mProfilePicture;

    private RelativeLayout infoLayout;

    private  ImageView spiner;
    private long userID = 0;


    public PeopleProfileFragment() {
        // Required empty public constructor
        mNetworkUserInfo = new NetworkUserInfo();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_people_profile, container, false);
        mAvailability = (TextView)v.findViewById(R.id.availability);
        mCity = (TextView)v.findViewById(R.id.locationText);
        mAboutme = (TextView)v.findViewById(R.id.peopleAboutMe);
        mFirstname = (TextView)v.findViewById(R.id.textName);
        mProfilePicture = (ImageView)v.findViewById(R.id.profilePicture);
        spiner  = (ImageView) v.findViewById(R.id.progressBar);
        infoLayout = (RelativeLayout) v.findViewById(R.id.infoGroup);

        return v;
    }

    private void spinAnimate() {
        Animation spinAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.spin);

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
        Animation spinAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.spin_stop);
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



        Animation spinAnim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in2);

        spinAnim2.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                infoLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation anim) {
            }

            @Override
            public void onAnimationEnd(Animation anim) {
            }
        });
        infoLayout.startAnimation(spinAnim2);


    }

    @Override
    public void setButtonCallback(MainActivity.CallBackTopButton myCallback) {

    }

    @Override
    public void onFragmentEnter() {


        if(userID != 0) {
            getUserInfo gUI = new getUserInfo();
            gUI.execute();
        }
    }

    @Override
    public void onFragmentLeave() {
        infoLayout.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onTopMenuMenuButtonClick() {

    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    private class getUserInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute()
        {
            spinAnimate();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // fetchFacebookProfilePicture();
            int i = 4 +8;
            i = 2*i;
            try {
                mNetworkUserInfo.fetchUserInfo(userID);

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
            if(mNetworkUserInfo.ismAvailable()) {
                mAvailability.setText("Disponible!");
            }
            else
                mAvailability.setText("Indisponible!");

            mAboutme.setText(mNetworkUserInfo.getmAboutme());
            mFirstname.setText(mNetworkUserInfo.getmFirstname());
            mCity.setText(mNetworkUserInfo.getmCity());
            mProfilePicture.setImageBitmap(mNetworkUserInfo.getmUserpicture());
            spinStopAnimation();
           // fadeStuffIn();
        }


    }

}
