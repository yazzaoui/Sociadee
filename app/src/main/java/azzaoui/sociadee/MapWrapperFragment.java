package azzaoui.sociadee;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapWrapperFragment extends Fragment implements SociadeeFragment {
    GoogleMap map;

    private SupportMapFragment mapFragment;
    private ImageButton mUserButton,mChatButton;
    private boolean mMapAnimating = false;
    private boolean mButtonVisible =false;
    private String myCity = null;
    private double myLatitude = 0.;
    private double myLongitude = 0.;
    private ScheduledExecutorService scheduler;
    private NetworkGPS networkGPS;
    public MyLocationCallback myLocationCallback;

    private long lastUserClikedId = 0;
    private boolean firstAnimate = true;

    private Map<Long,UserMap> userList;
    private Map<Marker,Long> markerLongMap;

    public MapWrapperFragment() {
        // Required empty public constructor
        userList = new HashMap<Long, UserMap>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_mapwrapper, container, false);
        mUserButton = (ImageButton)v.findViewById(R.id.mapUserButton);
        mChatButton = (ImageButton)v.findViewById(R.id.mapChatButton);
        scheduler = Executors.newScheduledThreadPool(1);
        networkGPS = new NetworkGPS();

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(myCity != null )
                    try {
                        networkGPS.UpdateLoc(myLatitude, myLongitude, myCity);
                        compareSavedUser(networkGPS.getFetchedUser());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateMap();
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }, 2, 60, TimeUnit.SECONDS); // Every 60 sec

        return v;
    }

    //TODO : remove if do not ocntains
    private void compareSavedUser( ArrayList<NetworkGPS.UserFetchedGPS> fetched) throws IOException, JSONException {
        for(int i=0; i< fetched.size(); i++)
        {
            NetworkGPS.UserFetchedGPS curFetched = fetched.get(i);
            if(userList.containsKey(curFetched.facebookId))
            {
                UserMap curUser =  userList.get(curFetched.facebookId);
                curUser.position = new LatLng(curFetched.latitude,curFetched.longitude);
            }
            else
            {
                networkGPS.fetchUserPicture(curFetched.facebookId);
                UserMap curUser = new UserMap();
                curUser.position =  new LatLng(curFetched.latitude,curFetched.longitude);
                curUser.facebookId = curFetched.facebookId;
                curUser.icon = createMarker(new BitmapDrawable(getResources(), networkGPS.getLastPicture()));
                userList.put(curFetched.facebookId,curUser);
            }
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm =  getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.mapBlock);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapBlock, mapFragment).commit();
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        if (map == null) {

            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

//        boolean network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // Showing status
            if (status != ConnectionResult.SUCCESS) // Google Play Services are not available
                throw new RuntimeException("Bug no google play");


            map = mapFragment.getMap();
            // Changing map type
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // Showing/hiding your current location
            map.setMyLocationEnabled(false);
            // Enable/disable zooming controls
            map.getUiSettings().setZoomControlsEnabled(false);
            map.getUiSettings().setMapToolbarEnabled(false);
            // Enable/disable my location button
            map.getUiSettings().setMyLocationButtonEnabled(false);
            // Enable/disable compass icon
            map.getUiSettings().setCompassEnabled(false);
            // Enable/disable rotate gesture
            map.getUiSettings().setRotateGesturesEnabled(false);
            // Enable/disable zooming functionality
            map.getUiSettings().setZoomGesturesEnabled(true);

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    Log.d("animation", "marker click ");
                    if(!mButtonVisible && !mMapAnimating) {
                        lastUserClikedId = markerLongMap.get(arg0);
                        showButtons(arg0);
                    }
                    return true;
                }

            });

            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    Log.d("animation", "Camera change called ");
                    if(!mMapAnimating && mButtonVisible)
                        hideButtons();
                    else
                     Log.d("animation", "refused ");
                }
            });

            LocationManager locationManager;
            String context = Context.LOCATION_SERVICE;
            locationManager = (LocationManager) getActivity().getSystemService(context);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.ACCURACY_FINE);
            String provider = locationManager.getBestProvider(criteria, true);
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
              //  Location location = locationManager.getLastKnownLocation(provider);
                Location location = this.getLastKnownLocation(locationManager);
                if (location != null) {
                    updateWithNewLocation(location);
                }
            }

            final LocationListener locationListener = new LocationListener(){
                public void onLocationChanged(Location location){
                    updateWithNewLocation(location);
                }
                public void onProviderDisabled(String provider){
                    updateWithNewLocation(null);
                }
                public void onProviderEnabled(String provider){}
                public void onStatusChanged(String provider, int status, Bundle extras){}
            };

            locationManager.requestLocationUpdates(provider, 2000, 10,locationListener);

        }
    }

    private void showButtons(Marker arg)
    {
        Animation showUserButtonAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.map_usericon_fadein);
        Animation showChatButtonAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.map_chaticon_fadein);
        showChatButtonAnim.setStartTime(400);
        showUserButtonAnim.setStartTime(400);
        mMapAnimating = true;
        mButtonVisible = false;
         final Marker currentMarker = arg;
        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(currentMarker.getPosition().latitude, currentMarker.getPosition().longitude)), 200, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                mMapAnimating = false;
            }

            @Override
            public void onCancel() {

            }
        });
        showUserButtonAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mUserButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation anim) {
            }

            @Override
            public void onAnimationEnd(Animation anim) {
            }
        });
        showChatButtonAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mChatButton.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation anim) {
            }

            @Override
            public void onAnimationEnd(Animation anim) {
                mButtonVisible=true;
            }
        });
       mChatButton.startAnimation(showChatButtonAnim);
       mUserButton.startAnimation(showUserButtonAnim);
    }
    private void hideButtons()
    {
        Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.map_icon_fade_out);

        mMapAnimating = true;
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation anim) {
            }

            @Override
            public void onAnimationEnd(Animation anim) {
                mChatButton.setVisibility(View.INVISIBLE);
                mUserButton.setVisibility(View.INVISIBLE);
                mButtonVisible = false;
                mMapAnimating = false;
            }
        });
        mChatButton.startAnimation(fadeOut);
        mUserButton.startAnimation(fadeOut);
    }
    //hash code and marker
    private Location getLastKnownLocation(LocationManager mLocationManager) {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
    private void updateMap()
    {
        map.clear();
        markerLongMap = new HashMap<>();
        Iterator it = userList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            UserMap curUser = (UserMap)pair.getValue();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(curUser.position.latitude, curUser.position.longitude));
            markerOptions.icon(curUser.icon);

            Marker newMarker =  map.addMarker(markerOptions);
            markerLongMap.put(newMarker,curUser.facebookId);
        }


    }
    private void updateWithNewLocation(Location location) {

        if(location != null){
        // Update the map location
            double latitude = location.getLatitude();
            myLatitude =latitude;

            double longitude = location.getLongitude();
            myLongitude = longitude;

            if(firstAnimate) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(17).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                firstAnimate = false;
            }



            try
            {
                Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    /*
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        sb.append("\n").append(address.getAddressLine(i));
                    }
                    Toast.makeText(getActivity(),  sb.toString(), Toast.LENGTH_LONG).show();
                    */
                    myCity = address.getLocality();
                    if(myLocationCallback != null)
                        myLocationCallback.newLocation(address.getLocality());
                }
            }
            catch (IOException e){}

        }
    }


    private BitmapDescriptor createMarker(Drawable profilePic)
    {
        Drawable[] layers = new Drawable[2];
        layers[0] = getResources().getDrawable(R.drawable.blue_marker);
        layers[1] = profilePic;
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

       // layerDrawable.setLayerInset(0, 0, 0, 0, 0);
        layerDrawable.setLayerInset(1, 15, 13, 16, 18);
        layerDrawable.setBounds(0, 0, 100, 100);
        layerDrawable.draw(new Canvas(b));
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(b);

        return icon;
    }

    @Override
    public void setButtonCallback(MainActivity.CallBackTopButton myCallback) {

    }

    @Override
    public void onFragmentEnter() {

    }

    @Override
    public void onFragmentLeave() {

    }

    @Override
    public void onTopMenuMenuButtonClick() {

    }

    public long getLastUserClikedId() {
        return lastUserClikedId;
    }


    public static class UserMap
    {
        public long facebookId;
        public BitmapDescriptor icon;
        public LatLng position;

    }
    public class MyLocationCallback
    {
        public void newLocation(String city){};
    }
}
