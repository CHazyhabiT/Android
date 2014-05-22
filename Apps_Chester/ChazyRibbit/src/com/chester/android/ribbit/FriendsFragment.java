/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */

package com.chester.android.ribbit;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * @author CHazyhabiT
 * 
 */
public class FriendsFragment extends ListFragment {

	public static final String TAG = FriendsFragment.class.getSimpleName();

	protected ParseUser mCurrentUser;
	protected List<ParseUser> mFriends;
	protected ParseRelation<ParseUser> mFriendsRelation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends, container,
				false);

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().setProgressBarIndeterminateVisibility(true);
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

		ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		query.orderByAscending(ParseConstants.KEY_USERNAME);
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				getActivity().setProgressBarIndeterminateVisibility(false);
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
						android.R.layout.simple_list_item_1,
						usernames);
					setListAdapter(adapter);
				} else {
					// Incorrect
					Log.e(TAG, e.getMessage());
					AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
					builder.setTitle(getString(R.string.error_title))
						.setMessage(e.getMessage())
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
					
					
				}
			}

		});
	}
}
