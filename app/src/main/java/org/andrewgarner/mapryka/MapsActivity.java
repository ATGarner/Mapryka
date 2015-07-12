package org.andrewgarner.mapryka;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static String TAG="Mapryka";
    LocationManager mLocationManager;

    private long LOCATION_REFRESH_TIME = 5000;
    private float LOCATION_REFRESH_DISTANCE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
        setUpMapIfNeeded();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mLocationManager.removeUpdates(mLocationListener);
    }


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){
            //Log.v(TAG,"LocationListener / onStatusChanged / provider= "+provider+" "+status);
        }
        @Override
        public void onProviderEnabled(String provider){
            Log.v(TAG,"LocationListener / onProviderEnabled / "+provider);
        }
        @Override
        public void onProviderDisabled(String provider){
            Log.v(TAG,"LocationListener / onProviderDisabled / "+provider);

        }
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            Log.e(TAG, "onLocationChanged!");
            Log.i(TAG, "Location changed/ " + location.getAccuracy()
                    + " , " + location.getLatitude() + "," + location.getLongitude());
            TextView latlong = (TextView) findViewById(R.id.latlong);

            latlong.setText(String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(),location.getLongitude()))
                    .zoom(19)                   // Sets the zoom
                    .bearing(location.getBearing())                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    };
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    LatLng spaceNeedle = new LatLng(47.620823, -122.347570);
    //LatLngBounds bounds = new LatLngBounds(new LatLng(47.656240, -122.411196), new LatLng(47.572477, -122.275652));
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //mMap.setBuildingsEnabled(true);
        mMap.addMarker(new MarkerOptions().position(spaceNeedle).title("Space Needle").snippet("This place is cool"));
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spaceNeedle,16));
    }
}
