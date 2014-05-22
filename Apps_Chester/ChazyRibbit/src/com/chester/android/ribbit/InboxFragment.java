/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */

package com.chester.android.ribbit;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * @author CHazyhabiT
 * 
 */
public class InboxFragment extends ListFragment {
	
	public static final String TAG = InboxFragment.class.getSimpleName();
	
	protected List<ParseObject> mMessages;
	
	private TextView mEmptyTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate(int resource, ViewGroup root, boolean attachToRoot)
		// Inflate a new view hierarchy from the specified xml resource.
		View rootView = inflater.inflate(R.layout.fragment_inbox,
				container, false);

		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().setProgressBarIndeterminateVisibility(true);
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
		query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
		// new message will be retrieve first, consequently it will be displayed on top
		query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> messages, ParseException e) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				if(e==null) {
					// success
					mMessages = messages;
					String[] usernames = new String[mMessages.size()];
					int i = 0;
					for(ParseObject message : mMessages) {
						usernames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
						i++;
					}
					
					// maintain the scroll position when you fresh
					// instead of jumping to the top when return back from other activities at each time,
					// we want to stay at the previous position
					if(getListView().getAdapter()==null) {
						MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);
//						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), 
//							android.R.layout.simple_list_item_1, usernames);
						setListAdapter(adapter);
					} else {
						// refill the adapter
						((MessageAdapter) getListView().getAdapter()).refill(messages);
					}
				} else {
					// Incorrect
//					Log.e(TAG, e.getMessage());
//					AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
//					builder.setTitle(getString(R.string.error_title))
//						.setMessage(e.getMessage())
//						.setPositiveButton(android.R.string.ok, null);
//					AlertDialog dialog = builder.create();
//					dialog.show();
					mEmptyTextView = (TextView) getListView().findViewById(android.R.id.empty);
					mEmptyTextView.setText(R.string.empty_inbox_label);
					
				}
				
			}
		});
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ParseObject message = mMessages.get(position);
		String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
		ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
		Uri fileUri = Uri.parse(file.getUrl());
		if(messageType.equals(ParseConstants.TYPE_IMAGE)) {
			// view the image
			Intent intent = new Intent(getActivity(), ViewImageActivity.class);
			intent.setData(fileUri);
			startActivity(intent);
			
		} else {
			// view the video
			Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
			intent.setDataAndType(fileUri, "video/*");
			startActivity(Intent.createChooser(intent, getActivity().getString(R.string.choose_player)));
		}
		
		// delete it
		List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);
		if(ids.size()==1) {
			// last recipient, delete the message!
			message.deleteInBackground();
		} else {
			// remove the current recipient and save
			// remove locally
			ids.remove(ParseUser.getCurrentUser().getObjectId());
			// remove in the back end server
			ArrayList<String> idToRemove = new ArrayList<String>();
			idToRemove.add(ParseUser.getCurrentUser().getObjectId());
			message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idToRemove);
			message.saveInBackground();
			
		}
		
		
	}

	
}
