/**
 * Pick photos from the gallery
 * - using an intent
 */
package chester.android.chooseimage;

/**
 * @author CHazyhabiT
 *
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private static final int REQUEST_CODE = 1;
	private Bitmap bitmap;
	private ImageView imageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// get the contents
		imageView = (ImageView) findViewById(R.id.imageView1);
		final Button button = (Button) findViewById(R.id.button1);

		// set OnClickListener
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// create an intent that open the gallery
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		InputStream stream = null;
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
			try {
				// recycle unused bitmaps
				if (bitmap != null) {
					bitmap.recycle();
				}
				stream = getContentResolver().openInputStream(data.getData());
				bitmap = BitmapFactory.decodeStream(stream);

				imageView.setImageBitmap(bitmap);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} // finally ch (IOException e) {
				// e{
				// if (stream != null)
				// try {
				// stream.close();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// }
				// }

			finally {
				if (stream != null)
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
	}

}