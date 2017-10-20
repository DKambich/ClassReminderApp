package edu.purdue.dkambich.classreminderapp.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.purdue.dkambich.classreminderapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final LatLng PURDUE_CENTER = new LatLng(40.4237, -86.9212);

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private MapView mapView;
    private View mView;
    private Location lastUserLocation;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    Geocoder test;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        test = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> location = test.getFromLocationName("HAAS", 5);
            System.out.println(location.get(0).getLatitude() + ", " + location.get(0).getLongitude());
        }
        catch (IOException e){
            e.printStackTrace();
        }


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            //if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            //} else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                System.out.println("Request Permission");
        }
        else {
            System.out.println("Permission already granted");
            mGoogleApiClient.connect();
        }
        return mView;
    }

    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        mGoogleApiClient.disconnect();
        super.onPause();
    }

    @Override
    public void onResume() {
        mGoogleApiClient.connect();
        super.onResume();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Enable the map user location
                    mGoogleApiClient.connect();
                } else {
                    lastUserLocation = new Location("");
                    lastUserLocation.setLatitude(PURDUE_CENTER.latitude);
                    lastUserLocation.setLongitude(PURDUE_CENTER.longitude);
                    Toast.makeText(getContext(), "Error Retrieving Location", Toast.LENGTH_LONG);
                    System.out.println("Failed to retrieve location");
                }
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                return;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) mView.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        //Default map type
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //Enabling 3D Buildings
        mGoogleMap.setBuildingsEnabled(true);
        //Setting minimum Zoom value
        mGoogleMap.setMinZoomPreference(15.5f);
        //Turn off extra UI settings
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        //Setting Bounds of Map
        LatLngBounds purdue = new LatLngBounds(new LatLng(40.40905, -86.93604), new LatLng(40.43896, -86.90725));
        mGoogleMap.setLatLngBoundsForCameraTarget(purdue);

        //Creating starting map view
        CameraPosition fixed = CameraPosition.builder().target(PURDUE_CENTER).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(fixed));
    }

    public void centerLocation(){
        float currentZoom = mGoogleMap.getCameraPosition().zoom;
        CameraPosition fixed = CameraPosition.builder().target(
                new LatLng(lastUserLocation.getLatitude(), lastUserLocation.getLongitude()))
                .zoom(currentZoom).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(fixed));
    }

    public void resetView(){
        CameraPosition fixed = CameraPosition.builder().target(new LatLng(lastUserLocation.getLatitude(), lastUserLocation.getLongitude())).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(fixed));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("Connected");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastUserLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            System.out.println("Got last user location");
            centerLocation();
        }
        MarkerOptions location = new MarkerOptions().position(new LatLng(lastUserLocation.getLatitude(), lastUserLocation.getLongitude())).title("Current Location");
        mGoogleMap.addMarker(location);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        lastUserLocation = new Location("");
        lastUserLocation.setLatitude(PURDUE_CENTER.latitude);
        lastUserLocation.setLongitude(PURDUE_CENTER.longitude);
        Toast.makeText(getContext(), "Error Retrieving Location", Toast.LENGTH_LONG);
        System.out.println("Failed to retrieve location");
        MarkerOptions location = new MarkerOptions().position(new LatLng(lastUserLocation.getLatitude(), lastUserLocation.getLongitude())).title("Current Location");
        Marker currentLocation = mGoogleMap.addMarker(location);
    }
}
