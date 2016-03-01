package azzaoui.sociadee;



import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private GoogleMap mMap;


    private ImageButton mTopMenuButton;

    private ProfileFragment mProfileFragment;
    private MapWrapperFragment mMapFragment;
    private GroupChatFragment mGroupChatFragment;
    private PeopleGridFragment mPeopleGridFragment;
    private AddPicFragment mAddPicFragment;
    private PeopleProfileFragment mPeopleProfileFragment;

    Map<View, SociadeeFragment> viewFragmmentMap ;

    private View mProfileView;
    private View mMapView;
    private View mGroupChatView;
    private View mPeopleGridView;
    private View mAddPicView;
    private View mPeopleProfileView;

    private View mCurrentView = null;
    private View mLastView =  null;

    //used by picture activty
    // yes ugly but picture activity is not modifying the pictures
    public static PictureList pictureList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewFragmmentMap = new HashMap<View,SociadeeFragment>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        if(mCurrentView == null || mLastView == null)
        {
            mCurrentView =  findViewById(R.id.profileFragment);
            mLastView =  findViewById(R.id.profileFragment);
        }

        mTopMenuButton = (ImageButton)findViewById(R.id.topMenuButton1);
        CallBackTopButton callBackTopButton = new CallBackTopButton() {
            @Override
            public void fadeIn(int imageRes) {
                fadeInTopMenuButton(imageRes);
            }

            @Override
            public void fadeOut() {
                fadeOutTopMenuButton();
            }
        };


        mProfileFragment = (ProfileFragment)
                getSupportFragmentManager().findFragmentById(R.id.profileFragment);
        mProfileFragment.setButtonCallback(callBackTopButton);
        mMapFragment = (MapWrapperFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mMapFragment.setButtonCallback(callBackTopButton);
        mGroupChatFragment = (GroupChatFragment)
                getSupportFragmentManager().findFragmentById(R.id.groupChatFragment);
        mGroupChatFragment.setButtonCallback(callBackTopButton);
        mPeopleGridFragment = (PeopleGridFragment)
                getSupportFragmentManager().findFragmentById(R.id.peopleGridFragment);
        mPeopleGridFragment.setButtonCallback(callBackTopButton);
        mAddPicFragment = (AddPicFragment)
                getSupportFragmentManager().findFragmentById(R.id.addPicFragment);
        mAddPicFragment.setButtonCallback(callBackTopButton);
        mAddPicFragment.goBackCallback = new AddPicFragment.CallBackSwitch() {
            @Override
            public void goBack() {
                mProfileFragment.setImages();
                switchView(mProfileView);
            }
        };
        mPeopleProfileFragment = (PeopleProfileFragment)
                getSupportFragmentManager().findFragmentById(R.id.peopleProfileFragment);
        mProfileFragment.setButtonCallback(callBackTopButton);

        mProfileView = findViewById(R.id.profileFragment);
        mProfileView.setVisibility(View.VISIBLE);
        viewFragmmentMap.put(mProfileView, mProfileFragment);

        mMapFragment.myLocationCallback = mMapFragment.new MyLocationCallback()
        {
          public void newLocation(String city){
              mProfileFragment.setNewCity(city);
          }
        };

        mMapView = findViewById(R.id.mapFragment);
        mMapView.setVisibility(View.INVISIBLE);
        viewFragmmentMap.put(mMapView, mMapFragment);

        mGroupChatView = findViewById(R.id.groupChatFragment);
        mGroupChatView.setVisibility(View.INVISIBLE);
        viewFragmmentMap.put(mGroupChatView, mGroupChatFragment);

        mPeopleGridView = findViewById(R.id.peopleGridFragment);
        mPeopleGridView.setVisibility(View.INVISIBLE);
        viewFragmmentMap.put(mPeopleGridView, mPeopleGridFragment);

        mAddPicView = findViewById(R.id.addPicFragment);
        mAddPicView.setVisibility(View.INVISIBLE);
        viewFragmmentMap.put(mAddPicView, mAddPicFragment);

        mPeopleProfileView = findViewById(R.id.peopleProfileFragment);
        mPeopleProfileView.setVisibility(View.INVISIBLE);
        viewFragmmentMap.put(mPeopleProfileView, mPeopleProfileFragment);


        /*Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.Evening));*/
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }





    @Override
    public void onBackPressed() {
        // your code.
        switchView(mLastView);
    }

    private void switchView(final View nextView)
    {
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        /** To avoid race conditions **/
        final View fadingOutView = mCurrentView;

        fadeOutAnimation.setRepeatCount(0);

        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation anim) {
            }

            @Override
            public void onAnimationEnd(Animation anim) {
                fadingOutView.setVisibility(View.GONE);
                viewFragmmentMap.get(fadingOutView).onFragmentLeave();
                fadingOutView.setAlpha(1.0f);
            }
        });

        mCurrentView.startAnimation(fadeOutAnimation);

        /** Fade In the the textboxes **/

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeIn.setStartTime(600);


        fadeIn.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
                nextView.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation anim) {
            }

            public void onAnimationEnd(Animation anim) {
                viewFragmmentMap.get(nextView).onFragmentEnter();
            }
        });
        //fadeIn.setFillBefore(true);
        nextView.startAnimation(fadeIn);
        mLastView = mCurrentView;
        mCurrentView = nextView;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //FB tracking
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    private void fadeInTopMenuButton(int id)
    {
        mTopMenuButton.setImageDrawable(getResources().getDrawable(id));

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
                mTopMenuButton.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation anim) {
            }

            public void onAnimationEnd(Animation anim) {

            }
        });
        mTopMenuButton.startAnimation(fadeIn);
    }
    private void fadeOutTopMenuButton()
    {
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);


        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation anim) {
            }

            @Override
            public void onAnimationEnd(Animation anim) {
                mTopMenuButton.setVisibility(View.INVISIBLE);
                mTopMenuButton.setAlpha(1.0f);
            }
        });

        mTopMenuButton.startAnimation(fadeOutAnimation);
    }

    public void addPicProfile(View v)
    {
        mAddPicFragment.retrievePictures();
        switchView(mAddPicView);
    }

    public void FragmentSaveClick(View v)
    {

    }

    public void showPic(View v)
    {
        int num = (int)v.getTag(R.integer.ITEM_NUM);
        Intent intent = new Intent(this, PictureActivity.class);
        pictureList = new PictureList(Parameters.getFacebookImages());
        intent.putExtra("ITEM_NUM",num);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in2, R.anim.fade_out2);

    }

    public void switchMyProfile(View v)
    {
        switchView(mProfileView);
    }
    public void switchMap(View v)
    {
        switchView(mMapView);
    }
    public void validatePic(View v)
    {
        mAddPicFragment.validatePic(v);
    }

    public void marMarkerClick(View v)
    {
        long userId = mMapFragment.getLastUserClikedId();
        mPeopleProfileFragment.setUserID(userId);
        switchView(mPeopleProfileView);
    }

    public void topButtonClick(View v)
    {
        viewFragmmentMap.get(mCurrentView).onTopMenuMenuButtonClick();
    }
    public interface CallBackTopButton
    {
        public void fadeIn(int imageRes);
        public void fadeOut();
    }
}
