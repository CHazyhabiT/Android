package com.chester.android.googlemap;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {

	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	private ArrayList<LatLng> locaions = new ArrayList<LatLng>();
	private GoogleMap map;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		Intent intent = getIntent();
		ArrayList<Double> latitudes = new ArrayList<Double>();
		ArrayList<Double> longitudes = new ArrayList<Double>();
		temp(latitudes, longitudes);
		
		initLocations(latitudes, longitudes);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		

		for(int i=0;i<locaions.size();i++) {
			map.addMarker(new MarkerOptions().position(locaions.get(i)));
			
		}
		
//		Marker kiel = map.addMarker(new MarkerOptions()
//				.position(KIEL)
//				.title("Kiel")
//				.snippet("Kiel is cool")
//				.icon(BitmapDescriptorFactory
//						.fromResource(R.drawable.ic_launcher)));

		// Move the camera instantly to hamburg with a zoom of 10.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(locaions.get(1), 10));
		// Zoom in, animating the camera.
		// Zoom to level 14 (0~18 the lower level, the less details)
		// animateCamera(CameraUpdate update, int durationMs, GoogleMap.CancelableCallback callback)
		// Moves the map according to the update with an animation over a specified duration,
		// and calls an optional callback on completion.
		map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void initLocations(ArrayList<Double> latitudes, ArrayList<Double> longitudes) {
		int len = latitudes.size();
		for(int i=0;i<len;i++) {
			LatLng temp = new LatLng(latitudes.get(i), longitudes.get(i));
			locaions.add(temp);	
		}
	}
	
	
	private void temp(ArrayList<Double> latitudes, ArrayList<Double> longitudes) {
		latitudes.add(33.644051);
		latitudes.add(33.650714);
		latitudes.add(33.645146);
		latitudes.add(33.650951);
		
		longitudes.add(-117.845431);
		longitudes.add(-117.837782);
		longitudes.add(-117.837776);
		longitudes.add(-117.847996);
		
	}
	
	
	

}
