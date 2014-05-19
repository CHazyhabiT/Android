package com.chester.android.blogreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chester.android.blogreader.dummy.BlogPosts;
import com.chester.android.blogreader.R;

public class MainBlogListActivity extends ListActivity {

	public static final int NUMBER_OF_POSTS = 20;
	public static final String TAG = MainBlogListActivity.class.getSimpleName();
	
	// UI components
	protected ProgressBar mProgressBar;
	protected TextView mEmptyTextView;
	// an instance of BlogPosts
	protected BlogPosts mBlogPosts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_blog_list);
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		if(isNetworkAvailable()) {
			mProgressBar.setVisibility(View.VISIBLE);
			GetBlogPostsTask getBlogPostsTask = new GetBlogPostsTask();
			getBlogPostsTask.execute();
			
		} else {
			Toast.makeText(this, "Network is unavailable!", Toast.LENGTH_LONG).show();
		}


	}



	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		try {
			String clickUrl = mBlogPosts.getClickUrl(position);
    		
			if(clickUrl!=null) {
    		// intent for opening an webView activity
    		Intent intent = new Intent(this, BlogWebViewActivity.class);
    		intent.setData(Uri.parse(clickUrl));
    		startActivity(intent);
			} else {
				Toast.makeText(this, "Url is null!", Toast.LENGTH_LONG).show();
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
    /**
     * check whether the network is available
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     * @return boolean
     */
	private boolean isNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		
		boolean isAvailable = false;
		if (networkInfo != null && networkInfo.isConnected()) {
			isAvailable = true;
		}
		
		return isAvailable;
	}
	
	/**
	 * handle blog response once the download is done
	 */
	public void handleBlogResponse(JSONObject jsonResponse) {
		mProgressBar.setVisibility(View.INVISIBLE);
		if(jsonResponse==null) {
			updateDisplayForError();
		} else {
			mBlogPosts = new BlogPosts(jsonResponse);
			ArrayList<HashMap<String, String>> blogPosts = mBlogPosts.getMBlogPosts();
			String[] keys = {BlogPosts.KEY_TITLE, BlogPosts.KEY_AUTHOR};
			int[] ids = {android.R.id.text1, android.R.id.text2};
			SimpleAdapter adapter = new SimpleAdapter(this, blogPosts,
					android.R.layout.simple_list_item_2,
					keys, ids);
			setListAdapter(adapter);	
		}
	}

	/**
	 * when data download from blog website is null,
	 * display AlertDialog
	 * display empty TextView
	 */
    private void updateDisplayForError() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(getString(R.string.dialog_error_title));
    	builder.setMessage(getString(R.string.dialog_error_message));
    	builder.setPositiveButton(android.R.string.ok, null);
    	AlertDialog dialog = builder.create();
    	dialog.show();
    	
    	mEmptyTextView = (TextView) getListView().getEmptyView();
    	mEmptyTextView.setText(getString(R.string.no_item));
	}



	private void logException(Exception e) {
    	Log.e(TAG, "Exception caught!", e);
	}
	
	private class GetBlogPostsTask extends AsyncTask<Object, Void, JSONObject> {		
		@Override
		protected JSONObject doInBackground(Object... params) {
			int responseCode = -1;
			JSONObject jsonResponse = null;
			try {
				URL blogFeedUrl = new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count=" + NUMBER_OF_POSTS);
				HttpURLConnection connection = (HttpURLConnection) blogFeedUrl.openConnection();
				connection.connect();
				
				responseCode = connection.getResponseCode();
				
				if(responseCode==HttpsURLConnection.HTTP_OK) {
					InputStream inputStream = connection.getInputStream();
					Reader reader = new InputStreamReader(inputStream);
					int contentLength = connection.getContentLength();
					char[] charArray = new char[contentLength];
					reader.read(charArray);
					String responseData = charArray.toString();
					jsonResponse = new JSONObject(responseData);
					
					
				} else {
					Log.i(TAG, "Unsuccessful HTTP Response Code: " + responseCode);
				}
				
			} catch (MalformedURLException e) {
				logException(e);
			} catch (IOException e) {
				logException(e);
			} catch (Exception e) {
				logException(e);
			}
			return jsonResponse;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			// super.onPostExecute(result);
			handleBlogResponse(result);
			
		}

	}




}
