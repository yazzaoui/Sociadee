package azzaoui.sociadee;



import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;


public class MainActivity extends AppCompatActivity {

    private GoogleMap mMap;

    private ProfileFragment mProfileFragment;
    private MapWrapperFragment mMapFragment;
    private GroupChatFragment mGroupChatFragment;
    private PeopleGridFragment mPeopleGridFragment;
    private AddPicFragment mAddPicFragment;


    private View mProfileView;
    private View mMapView;
    private View mGroupChatView;
    private View mPeopleGridView;
    private View mAddPicView;

    private View mCurrentView = null;
    private View mLastView =  null;


    private ListView mMenuDrawerList;
    private ArrayAdapter<String> mMenuAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(mCurrentView == null || mLastView == null)
        {
            mCurrentView =  findViewById(R.id.profileFragment);
            mLastView =  findViewById(R.id.profileFragment);
        }
        mMenuDrawerList = (ListView) findViewById(R.id.menu_list);
        addMenuDrawerItems();
        setupActionBar();

        mProfileFragment = (ProfileFragment)
                getSupportFragmentManager().findFragmentById(R.id.profileFragment);
        mMapFragment = (MapWrapperFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mGroupChatFragment = (GroupChatFragment)
                getSupportFragmentManager().findFragmentById(R.id.groupChatFragment);
        mPeopleGridFragment = (PeopleGridFragment)
                getSupportFragmentManager().findFragmentById(R.id.peopleGridFragment);
        mAddPicFragment = (AddPicFragment)
                getSupportFragmentManager().findFragmentById(R.id.addPicFragment);


        mProfileView = findViewById(R.id.profileFragment);
        mProfileView.setVisibility(View.VISIBLE);

        mMapView = findViewById(R.id.mapFragment);
        mMapView.setVisibility(View.INVISIBLE);

        mGroupChatView = findViewById(R.id.groupChatFragment);
        mGroupChatView.setVisibility(View.INVISIBLE);

        mPeopleGridView = findViewById(R.id.peopleGridFragment);
        mPeopleGridView.setVisibility(View.INVISIBLE);

        mAddPicView = findViewById(R.id.addPicFragment);
        mAddPicView.setVisibility(View.INVISIBLE);

        ((TextView)findViewById(R.id.profilMenuName)).setText(Parameters.getFirstname());

        BitmapDrawable bd=(BitmapDrawable)Parameters.getProfilePicture();
        ((ImageView)findViewById(R.id.profilMenuPicture)).setImageBitmap(bd.getBitmap());
        ((ImageView)findViewById(R.id.profilMenuPicture)).setScaleType(ImageView.ScaleType.FIT_XY);
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
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        // your code.
        switchFragment(mLastView);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private void setupActionBar()
    {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.top_toolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        getSupportActionBar().setTitle("Sociadee");
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setScrimColor(0x30000000);

    }

    private void addMenuDrawerItems() {
        String[] osArray = { "Discussions", "Carte", "Réseau", "Mon Profil", "Se déconnecter" };
        mMenuAdapter = new ArrayAdapter<String>(this, R.layout.menu_item, osArray);
        mMenuDrawerList.setAdapter(mMenuAdapter);

        mMenuDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        switchFragment(mGroupChatView);
                        break;
                    case 1:
                        switchFragment(mMapView);
                        break;
                    case 2:
                        switchFragment(mPeopleGridView);
                        break;
                    case 3:
                        switchFragment(mProfileView);
                        break;
                    case 4:
                        finish();
                        break;
                }
                mDrawerLayout.closeDrawer(Gravity.START);
            }
        });
    }
    private void switchFragment(final View nextView)
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

            }
        });
        //fadeIn.setFillBefore(true);
        nextView.startAnimation(fadeIn);
        mLastView = mCurrentView;
        mCurrentView = nextView;
    }

    public void addPicProfile(View v)
    {
        switchFragment(mAddPicView);
    }
}
