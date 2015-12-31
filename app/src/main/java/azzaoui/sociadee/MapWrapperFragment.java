package azzaoui.sociadee;



import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.MapFragment;

public class MapWrapperFragment extends Fragment {
    GoogleMap map;

    private MapFragment mapFragment;
    public MapWrapperFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       FragmentManager fm =  getChildFragmentManager();
        mapFragment = (MapFragment) fm.findFragmentById(R.id.mapContainerBis);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapContainerBis, new PeopleGridFragment()).commit();
        }
    }

    @Override
    public void onResume() {

        super.onResume();
/*
        if (map == null) {
            map = mapFragment.getMap();
            map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        }*/
    }

}
