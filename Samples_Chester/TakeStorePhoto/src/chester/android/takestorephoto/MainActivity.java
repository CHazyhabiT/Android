/**
 * 
 */
package chester.android.takestorephoto;

/**
 * @author CHazyhabiT
 *
 */


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import chester.android.takephoto.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	
	final static String DEBUG_TAG = "mainActivity";
	// layout component
	private ImageView imageView;

	// request
	private static final int CAMERA_TAKE = 1;

	// info. of photos
	private String photoName;
	private Date date;
	private String dateString;
	// storage directory
	private String photoPath;
	// compressed configuration
	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	private static final int quality = 40;
			
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// get components
		imageView = (ImageView) findViewById(R.id.imageView1);
		final Button takeButton = (Button) findViewById(R.id.button1);
		final Button saveButton = (Button) findViewById(R.id.button2);
		
		// set listeners
		takeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				takePhoto();
			}
		});
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				storePhoto();
				
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK||requestCode==CAMERA_TAKE) {
//			Bitmap photo = BitmapFactory.decodeFile(PATH+"/"+photoName);
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			// Write a compressed version of the bitmap to the specified outputstream.
			photo.compress(Bitmap.CompressFormat.JPEG, quality, bytes);
			
			Toast.makeText(this, photoName, Toast.LENGTH_LONG).show();
			System.out.println(photo.getHeight()+"==="+photo.getWidth());
			
			imageView.setImageBitmap(photo);
			
		}
	}
	
	private void takePhoto() {
		// get the date and time
		date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmdd_hhmmss",Locale.US);
		dateString = dateFormat.format(date);
		photoName = "Pic_"+dateString+".jpg";
		
		// call for the system camera
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAMERA_TAKE);

	}
	
	private void storePhoto() {
		photoPath = getDir().getPath();
//		Uri imageUri = Uri.fromFile(new File(photoPath, photoName));
		
//		photoPath = Environment.getExternalStorageDirectory() + "/TakePhoto";
		  
		
		String fileName = photoPath+File.separator+photoName;
		File photoFile = new File(fileName);
		
		try {
			FileOutputStream fos = new FileOutputStream(photoFile);
			fos.write(bytes.toByteArray());
			fos.close();
			Toast.makeText(this, "New Image saved: "+fileName, Toast.LENGTH_LONG).show();
		}catch(Exception e) {
			Toast.makeText(this, "Image could not be saved.", Toast.LENGTH_LONG).show();
			
		}	
	}
	
	private File getDir() {
		File sdDir = Environment
			      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		// File(File parent, String child)
		// Creates a new File instance from a parent abstract pathname and a child pathname string.
		return new File(sdDir, "TakePhoto");
		
	}
	
}
