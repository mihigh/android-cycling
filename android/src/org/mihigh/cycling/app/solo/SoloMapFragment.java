package org.mihigh.cycling.app.solo;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import org.mihigh.cycling.app.R;

public class SoloMapFragment extends Fragment {

    public static final MyLocationListener LOCATION_CHANGE_LISTENER = new MyLocationListener();

    MapView mapView;


    GoogleMap map;
    private ViewPager viewPager;

    public SoloMapFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.solo_map, container, false);

        Button backButton = (Button) v.findViewById(R.id.mapback);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });


        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            Log.e("Address Map", "Could not initialize google play", e);
        }

        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity())) {
            case ConnectionResult.SUCCESS:
                mapView = (MapView) v.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                // Gets to GoogleMap from the MapView and does initialization stuff
                if (mapView != null) {
                    map = mapView.getMap();
                    map.setMyLocationEnabled(true);
                    map.setOnMyLocationChangeListener(LOCATION_CHANGE_LISTENER);
                }
                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    static class MyLocationListener implements GoogleMap.OnMyLocationChangeListener {

        @Override
        public void onMyLocationChange(Location location) {
            Tracking.instance.addLocation(location);
        }
    }

}
