/**
 * 
 */
package com.chester.android.ribbit;

/**
 * @author CHazyhabiT
 *
 */

import android.app.Application;

import com.parse.Parse;

public class RibbitApplication extends Application {
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Parse.initialize(this, "wRqXcCJPpfwYBa9lpikSkXwD1F8aEwsfRmgMHXc6", "j80h8P03BEKgx9doXxdnz2ZpFFXc8VCzqOspUFRL");
	
//		ParseObject testObject = new ParseObject("TestObject");
//		testObject.put("foo", "bar");
//		testObject.saveInBackground();
	}
}
