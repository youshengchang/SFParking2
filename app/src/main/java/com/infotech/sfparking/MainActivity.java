package com.infotech.sfparking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.infotech.sfparking.db.ParkingDataSource;
import com.infotech.sfparking.model.AvailableParking;
import com.infotech.sfparking.parser.ParkingJSONParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends FragmentActivity {

	TextView output;
	ProgressBar pb;
	List<MyTask> tasks;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
	
	//List<Flower> flowerList;
    List<AvailableParking> parkingList;

    ParkingDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        setUpMapIfNeeded();
		
//		Initialize the TextView for vertical scrolling
		output = (TextView) findViewById(R.id.textView);
		output.setMovementMethod(new ScrollingMovementMethod());
		
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);
		
		tasks = new ArrayList<>();

        datasource = new ParkingDataSource(this);
        datasource.open();
        parkingList = datasource.findAll();

//        ArrayAdapter<AvailableParking> adapter = new ArrayAdapter<AvailableParking>(this,
//                android.R.layout.simple_list_item_1, parkingList);
//        setListAdapter(adapter);

        if(!datasource.findAll().isEmpty()){
            datasource.deleteAll();
        }
        updateDisplay(37.7919, -122.3975);
	}

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

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_get_data) {
			if (isOnline()) {
				//requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                requestData("http://api.sfpark.org/sfpark/rest/availabilityservice?response=json&lat=37.7919&long=-122.3975&radius=0.05&uom=mile");
                //updateDisplay(37.7919, -122.3975);
			} else {
				Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
			}
		}
		return false;
	}

    private void updateDisplay(double lat, double lng) {
        final float DEFAULT_ZOOM = 15;
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, DEFAULT_ZOOM);
        mMap.moveCamera(update);
    }

    private void requestData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
	}

	protected void updateDisplay() {

        parkingList = datasource.findAll();

//        ArrayAdapter<AvailableParking> adapter = new ArrayAdapter<AvailableParking>(this,
//                android.R.layout.simple_list_item_1, parkingList);
//        setListAdapter(adapter);

//		if (parkingList != null) {
//			for (AvailableParking parking: parkingList) {
//				output.append("Parking Address: " + parking.getName() + " Latitude: " + parking.getLatitude() + " Longitude: " + parking.getLongitude() + "\n");
//
//			}
//		}
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
	
	private class MyTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
//			updateDisplay("Starting task");
			
			if (tasks.size() == 0) {
				pb.setVisibility(View.VISIBLE);
			}
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
			if (tasks.size() == 0) {
				pb.setVisibility(View.INVISIBLE);
			}

		}
		
		@Override
		protected void onProgressUpdate(String... values) {
//			updateDisplay(values[0]);
		}


	}

    private void updateDatabase() {
        AvailableParking parking;

        if(!parkingList.isEmpty()){
            Iterator it = parkingList.iterator();

            while(it.hasNext()){
                parking = (AvailableParking)it.next();
                datasource.create(parking);
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