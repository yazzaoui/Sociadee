package azzaoui.sociadee;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapWrapperFragment extends Fragment {
    GoogleMap map;

    private SupportMapFragment mapFragment;
    public MapWrapperFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_mapwrapper, container, false);
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
            // Enable/disable my location button
            map.getUiSettings().setMyLocationButtonEnabled(true);
            // Enable/disable compass icon
            map.getUiSettings().setCompassEnabled(false);
            // Enable/disable rotate gesture
            map.getUiSettings().setRotateGesturesEnabled(false);
            // Enable/disable zooming functionality
            map.getUiSettings().setZoomGesturesEnabled(true);
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
    private void updateWithNewLocation(Location location) {

        if(location != null){
        // Update the map location
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(17).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            map.clear();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(latitude, longitude));
            markerOptions.title("Here I am");
            markerOptions.snippet("");
            Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());
            try
            {
                List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        sb.append("\n").append(address.getAddressLine(i));
                    }
                    Toast.makeText(getActivity(),  sb.toString(), Toast.LENGTH_LONG).show();

                }
            }
            catch (IOException e){}
            map.addMarker(markerOptions).showInfoWindow();
        }
    }

}
