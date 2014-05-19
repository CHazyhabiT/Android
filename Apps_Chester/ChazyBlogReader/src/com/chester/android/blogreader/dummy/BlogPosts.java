/**
 * 
 */
package com.chester.android.blogreader.dummy;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.util.Log;

/**
 * @author CHazyhabiT
 *
 */
public class BlogPosts {
	
	public static final String KEY_POSTS = "posts";
	public static final String KEY_TITLE = "title";
	public static final String KEY_AUTHOR = "author";
	public static final String KEY_URL = "url";
	private static final String TAG = BlogPosts.class.getSimpleName();
	
	private JSONObject mBlogData;
	private ArrayList<HashMap<String, String>> mBlogPosts;
	
	public BlogPosts(JSONObject jsonResponse) {
		mBlogData = jsonResponse;
		mBlogPosts = new ArrayList<HashMap<String,String>>();
		jsonParse();
	}
	
	public void jsonParse() {
		try {
			JSONArray jsonPosts = mBlogData.getJSONArray(KEY_POSTS);
			for(int i=0;i<jsonPosts.length();i++) {
				JSONObject post = jsonPosts.getJSONObject(i);
				String title = post.getString(KEY_TITLE);
				title = Html.fromHtml(title).toString();
				String author = post.getString(KEY_AUTHOR);
				author = Html.fromHtml(author).toString();
				
				HashMap<String, String> blogPost = new HashMap<String, String>();
				blogPost.put(KEY_TITLE, title);
				blogPost.put(KEY_AUTHOR, author);
				
				mBlogPosts.add(blogPost);

				
			}
		} catch (JSONException e) {
			Log.e(TAG, "Exception caught!", e);
		}
		
		
	}
	
	public String getClickUrl(int position) {
		String clickUrl = null;
		try {
			JSONArray jsonPosts = mBlogData.getJSONArray(KEY_POSTS);
			JSONObject jsonPost = jsonPosts.getJSONObject(position);
			clickUrl = jsonPost.getString(KEY_URL);
			
		} catch (JSONException e) {
			Log.e(TAG, "Exception caught!", e);
		}
		return clickUrl;
		
	}
	
	public JSONObject getMBlogData() {
		return mBlogData;
	}
	
	public ArrayList<HashMap<String, String>> getMBlogPosts() {
		return mBlogPosts;
	}
	
	
	

}
