package com.infotech.sfparking;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.infotech.sfparking.db.ParkingDataSource;
import com.infotech.sfparking.model.AvailableParking;
import com.infotech.sfparking.parser.ParkingJSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

	List<MyTask> tasks;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
	

    List<AvailableParking> parkingList;

    ParkingDataSource datasource;

    private GoogleApiClient mGoogleApiClient;
    final float DEFAULT_ZOOM = 15;
    LatLng mCurrentLatLng;
    String sfParkingURI = "http://api.sfpark.org/sfpark/rest/availabilityservice?response=json&pricing=yes";
    float radius = 0.5f;
    boolean mLocationSensor = true;
    LocationRequest request;
    final int TIME_INTERVAL = 60000;
    final int FAST_TIME_INTERVAL = 1000;

    


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        setUpMapIfNeeded();
        buildGoogleApiClient();


		tasks = new ArrayList<>();

        datasource = new ParkingDataSource(this);
        datasource.open();
        parkingList = datasource.findAll();

        if(!datasource.findAll().isEmpty()){
            datasource.deleteAll();
        }
        mLocationSensor = true;
        initLocationRequest();

	}
    private void initLocationRequest(){

        request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(TIME_INTERVAL);
        request.setFastestInterval(FAST_TIME_INTERVAL);

    }
    public void geoLocate(View v) throws IOException {
        hideSoftKeyboard(v);
        EditText et = (EditText) findViewById(R.id.locationEdit);
        String location = et.getText().toString();
        Geocoder gc = new Geocoder(this);
        List<Address> addressList = gc.getFromLocationName(location, 1);
        Address address = addressList.get(0);
        double lat = address.getLatitude();
        double lng = address.getLongitude();
        et.setText("");
        gotoLocation(lat,lng);



    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void gotoCurrentLocation() {
        Location currentlocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(currentlocation == null){
            Toast.makeText(this,"current location is not available", Toast.LENGTH_LONG).show();
        }else{
            double lat = currentlocation.getLatitude();
            double lng = currentlocation.getLongitude();
            mCurrentLatLng = new LatLng(lat,lng);
            gotoLocation(lat,lng);

        }

    }

    private void gotoLocation(double lat, double lng){
        mCurrentLatLng = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mCurrentLatLng, DEFAULT_ZOOM);
        mMap.moveCamera(update);
        mMap.addMarker(new MarkerOptions().position(mCurrentLatLng).title(""));

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();



    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
//            if (mMap != null) {
//                setUpMap();
//
//
//            }
        }
    }

//    private void setUpMap() {
////        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
//    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    private String getSfParkingURI(){

        String sLat = Double.toString(mCurrentLatLng.latitude);
        String sLng = Double.toString(mCurrentLatLng.longitude);
        String sRadius = Float.toString(radius);

        String uri = sfParkingURI + "&lat=" + sLat + "&long=" + sLng + "&radius=" + sRadius + "&uom=mile";
        Log.i("SFPARK", uri);
        return uri;
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_get_data) {
			if (isOnline()) {

                requestData(getSfParkingURI());

			} else {
				Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
			}
		}
        if(item.getItemId() == R.id.action_current){
            gotoCurrentLocation();
        }
        if(item.getItemId() == R.id.action_market){
            gotoLocation(37.7919, -122.3975);
        }
        if(item.getItemId() == R.id.action_toggle_sensor){

                toggleSensor();

        }
		return false;
	}



    private void requestData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
	}

	protected void updateDisplay() {

        parkingList = datasource.findAll();
        LatLng ll;

        	if (parkingList != null) {
			for (AvailableParking parking: parkingList) {
                	ll = new LatLng(parking.getLatitude(), parking.getLongitude());
                mMap.addMarker(new MarkerOptions().position(ll).title(parking.getName()));
			}
		}
	}
	
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

    private void toggleSensor(){
        if(mLocationSensor){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request,this);
            mLocationSensor = false;
        }else{
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            mLocationSensor = true;
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "The connection service is available", Toast.LENGTH_SHORT).show();


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request,this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        mCurrentLatLng = ll;
        gotoCurrentLocation();

    }


    private class MyTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {

			tasks.add(this);
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			String content = HttpManager.getData(params[0]);
			return content;
		}
		
		@Override
		protected void onPostExecute(String result) {

            //output.append(result + "\n");
			parkingList = ParkingJSONParser.parseFeed(result);

            updateDatabase();
            updateDisplay();
			tasks.remove(this);


		}
		
		@Override
		protected void onProgressUpdate(String... values) {
//			updateDisplay(values[0]);
		}


	}

    private void updateDatabase() {
        datasource.open();
        AvailableParking parking;

        if((parkingList != null)&&(!parkingList.isEmpty())){
            Iterator it = parkingList.iterator();

            while(it.hasNext()){
                parking = (AvailableParking)it.next();
                datasource.create(parking);
                Log.i("SFPARK", "Parking Info: " + parking);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        datasource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        datasource.close();
    }
}