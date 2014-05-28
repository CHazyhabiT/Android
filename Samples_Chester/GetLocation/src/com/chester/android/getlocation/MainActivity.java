package com.chester.android.getlocation;

import java.util.List;

import com.chester.android.getlocation.R;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity{
	private static final int TWO_MINUTES = 30 * 1000;
	
	LocationManager mLocationManager;
	LocationListener mLocationListener;
	Location currentBestLocation;
	//	private boolean mFirstUpdate = true;
	
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		
		
		
		// Acquire reference to the LocationManager
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (mLocationManager==null) finish();
		
		currentBestLocation = getInitalLocation();
		
		if(currentBestLocation!=null) {
			updateDisplay(currentBestLocation);
		} else {
			textView1.setText("try again!!!");
		}
		
		
		
		mLocationListener = new LocationListener() {
			
			@Override
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.				
				if(isBetterLocation(location, currentBestLocation)) {
					// if new retrieved location better than currentBest
					currentBestLocation = location;
					updateDisplay(currentBestLocation);
				}
			}
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			
		};
		
		
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		// Register the listener with the Location Manager to receive location updates
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
	
	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(mLocationListener);
	}
	
	
	private Location getInitalLocation() {
		Location bestResult = null;
		float bestAccuracy = Float.MAX_VALUE;
		
		List<String> matchingProviderStrings = mLocationManager.getAllProviders();
		for(String provider : matchingProviderStrings) {
			Location location = mLocationManager.getLastKnownLocation(provider);
			if(location!=null) {
				float accuracy = location.getAccuracy();
				if(accuracy < bestAccuracy) {
					bestResult = location;
					bestAccuracy = accuracy;
				}	
			}
		}		
		return bestResult;
	
	}
	
	
	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}

	private void updateDisplay(Location location) {
		textView1.setText("Accuracy:" + location.getAccuracy());
		textView2.setText("Longitude:" + location.getLongitude());
		textView3.setText("Latitude:" + location.getLatitude());
	}
	

}
