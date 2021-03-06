package com.chester.android.blogreader;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class BlogWebViewActivity extends Activity {

	protected String mUrl;
	protected WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog_web_view);

		Intent intent = getIntent();
		Uri blogUri = intent.getData();
		mUrl = blogUri.toString();
		
		mWebView = (WebView) findViewById(R.id.webView1);
		mWebView.loadUrl(mUrl);
		
		// ActionBar actionBar = getSupportActionBar();
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blog_web_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_share) {
			sharePost();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void sharePost() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, mUrl);
		startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)));
	}


}
