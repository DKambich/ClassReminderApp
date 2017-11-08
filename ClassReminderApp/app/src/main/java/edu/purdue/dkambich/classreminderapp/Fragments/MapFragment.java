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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.purdue.dkambich.classreminderapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //Constant Variables
    private final LatLng PURDUE_CENTER = new LatLng(40.4237, -86.9212);
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;

    //Google Maps and Services Variables
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;

    //User Information Variables
    private Location lastUserLocation;

    //Fragment Variables
    private MapView mapView;
    private View mView;

    //Test Variables
    Geocoder test;

    public MapFragment() {
        // Required empty public constructor
    }

    //Central Fragment Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        //If our ApiClient is null, build it
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //Check to see if we have location permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            //if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

            //} else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            //Permission is already granted so connect our mapclient
            mGoogleApiClient.connect();
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) mView.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        //Check to see if we got location permissions
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Enable the map user location
                    mGoogleApiClient.connect();
                } else { //Permission was denied so functionality breaks down
                    Toast.makeText(getContext(), "Error Retrieving Location", Toast.LENGTH_LONG);
                }
                break;
        }
    }

    //Google Maps and Services Methods

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Check if we have access to fine location and if so, get the user info
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastUserLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            centerLocation();
        }
        //Add a marker at the user location
        addMarker(new LatLng(lastUserLocation.getLatitude(), lastUserLocation.getLongitude()), "User Location");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Error Retrieving Location", Toast.LENGTH_LONG);
    }

    //Overridden Android lifecycle methods

    @Override
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

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    //Custom Interaction Methods

    public void addMarker(LatLng newLatLng, String locationName) {
        //Add a marker to the map using the inputted LatLng and name
        mGoogleMap.addMarker(new MarkerOptions().position(newLatLng).title(locationName));
    }

    public void centerLocation() {
        //Save the current zoom location
        float currentZoom = mGoogleMap.getCameraPosition().zoom;
        //Create a camera position at the user location
        CameraPosition centerLocation = CameraPosition.builder().target(new LatLng(lastUserLocation.getLatitude(), lastUserLocation.getLongitude())).zoom(currentZoom).build();
        //Move the camera to the position
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(centerLocation));
    }

    public void resetView() {
        //Create a reset camera position at the user location
        CameraPosition centerLocation = CameraPosition.builder().target(new LatLng(lastUserLocation.getLatitude(), lastUserLocation.getLongitude())).build();
        //Move the camera to the position
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(centerLocation));
    }

}
