package com.chester.android.ribbit;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewImageActivity extends Activity {

	private static final int TIMER = 3*1000;
	
	private ImageView mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);

		mImageView = (ImageView) findViewById(R.id.imageView);
		Uri imageUri = getIntent().getData();
		// mImageView.setImageURI(Uri uri) can only retrieve image from the local directory
		// Picasso will get over that condition
		Picasso.with(this).load(imageUri.toString()).into(mImageView);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				finish();
			}
		}, TIMER);
		
	}


}
