package com.example.trackme;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.app.Activity;
import android.content.Context;
import android.util.Log;
//import android.view.Menu;
public class LocationAddress  {
	 private static final String TAG = "LocationAddress";

	    public static void getAddressFromLocation(final double latitude, final double longitude,
	                                              final Context context, final Handler handler) {
	        Thread thread = new Thread() {
	            @Override
	            public void run() {
	                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
	                String result = null;
	                try {
	                    List<Address> addressList = geocoder.getFromLocation(
	                            latitude, longitude, 1);
	                    if (addressList != null && addressList.size() > 0) {
	                        Address address = addressList.get(0);
	                        StringBuilder sb = new StringBuilder();
	                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
	                            
	                        }
	                        sb.append(address.getLocality()).append("\n");
	                        
	                        sb.append(address.getCountryName());
	                        result = sb.toString();
	                    }
	                } catch (IOException e) {
	                    Log.e(TAG, "Unable connect to Geocoder", e);
	                } finally {
	                    Message message = Message.obtain();
	                    message.setTarget(handler);
	                    if (result != null) {
	                        message.what = 1;
	                        Bundle bundle = new Bundle();
	                        result = "Latitude: " + latitude + " Longitude: " + longitude +
	                                "\n\nAddress:\n" + result;
	                        bundle.putString("address", result);
	                        message.setData(bundle);
	                    } else {
	                        message.what = 1;
	                        Bundle bundle = new Bundle();
	                        result = "Latitude: " + latitude + " Longitude: " + longitude +
	                                "\n Unable to get address for this lat-long.";
	                        bundle.putString("address", result);
	                        message.setData(bundle);
	                    }
	                    message.sendToTarget();
	                }
	            }
	        };
	        thread.start();
	    }
	

	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_address);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_location_address, menu);
		return true;
	}*/

	public <GeocoderHandler> void getAddressFromLocation(double latitude, double longitude,
			Context applicationContext, GeocoderHandler geocoderHandler) {
		// TODO Auto-generated method stub
		
	}
	
}



/*public class LocationAddress extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_address);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_location_address, menu);
		return true;
	}

	public static void getAddressFromLocation(double latitude,
			double longitude, Context applicationContext,
			GeocoderHandler geocoderHandler) {
		// TODO Auto-generated method stub
		
	}

}*/


/*public class LocationAddress extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_address);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_location_address, menu);
		return true;
	}

}*/
