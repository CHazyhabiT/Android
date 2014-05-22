package com.chester.android.ribbit;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class RecipientsActivity extends ListActivity {

	public static final String TAG = RecipientsActivity.class.getSimpleName();

	protected ParseUser mCurrentUser;
	protected List<ParseUser> mFriends;
	protected ParseRelation<ParseUser> mFriendsRelation;
	
	protected  MenuItem mSenDMenuItem;
	protected Uri mMediaUri;
	protected String mFileType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_recipients);
		
		// setupActionBar
		getActionBar();
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		mMediaUri = getIntent().getData();
		mFileType = getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE);

	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setProgressBarIndeterminateVisibility(true);
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

		ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		query.orderByAscending(ParseConstants.KEY_USERNAME);
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					// success, return a list of ParseUser
					mFriends = friends;
					String[] usernames = new String[mFriends.size()];
					int i = 0;
					for (ParseUser user : mFriends) {
						usernames[i] = user.getUsername();
						i++;
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						// since Fragment doesn't extend from activity
						// so the first parameter shouldn't be
						// FriendsFragment.this
						getListView().getContext(),
						android.R.layout.simple_list_item_checked,
						usernames);
					setListAdapter(adapter);
				} else {
					// Incorrect
					Log.e(TAG, e.getMessage());
					AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
					builder.setTitle(getString(R.string.error_title))
						.setMessage(e.getMessage())
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
					
					
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipients, menu);
		
		// the id of the send button in the menu is 0
		mSenDMenuItem = menu.getItem(0);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_send) {
			ParseObject message = createMessage();
			if(message==null) {
				// error
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.error_selecting_file_title);
				builder.setMessage(R.string.error_selecting_file_message);
				builder.setPositiveButton(android.R.string.ok, null);
				AlertDialog dialog = builder.create();
				dialog.show();
				
			} else {
				send(message);
				finish();
			}
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	protected ParseObject createMessage() {
		ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
		message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
		message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
		message.put(ParseConstants.KEY_RECIPIENT_IDS, getRecipientIds());
		message.put(ParseConstants.KEY_FILE_TYPE, mFileType);
		
		byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);
		
		if(fileBytes==null) {
			return null;
		} else {
			if(mFileType.equals(ParseConstants.TYPE_IMAGE)) {
				fileBytes = FileHelper.reduceImageForUpload(fileBytes);
			}
			
			String fileName = FileHelper.getFileName(this, mMediaUri, mFileType);
			ParseFile file = new ParseFile(fileName, fileBytes);
			message.put(ParseConstants.KEY_FILE, file);
			return message;
		}
	}
	
	protected ArrayList<String> getRecipientIds() {
		ArrayList<String> recipientsIds = new ArrayList<String>();
		for(int i=0;i<getListView().getCount();i++) {
			if(getListView().isItemChecked(i)) {
				recipientsIds.add(mFriends.get(i).getObjectId());
			}	
		}
		return recipientsIds;
	}
	
	protected void send(ParseObject message) {
		message.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if(e==null) {
					// success
					Toast.makeText(RecipientsActivity.this, R.string.success_message, Toast.LENGTH_LONG).show();
				} else {
					// error
					AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
					builder.setTitle(R.string.error_selecting_file_title);
					builder.setMessage(R.string.error_sending_message);
					builder.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				
			}
		});
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if(l.getCheckedItemCount()>0) {
			mSenDMenuItem.setVisible(true);
		} else {
			mSenDMenuItem.setVisible(false);
		}
	}



}
