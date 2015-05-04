package com.cilla3bc.quoteslist;


import java.util.ArrayList;
import java.util.Arrays;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class QuotesListActivity extends ActionBarActivity implements FragmentOnClickListener {

	protected FrameLayout mContentFrame;
	protected Button mNextBtn;
	protected Button mPreviousBtn;
	protected Button mHomeBtn;
	private String[] mQuotesArray;
	public static ArrayList mQuotesList;
	
	protected QuotesListFragment mListFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quotes_list);
		
		mQuotesArray = getResources().getStringArray(R.array.testArray);
		mQuotesList = new ArrayList(Arrays.asList(mQuotesArray));
		
		mContentFrame = (FrameLayout)findViewById(R.id.content_frame);

		if(mListFragment == null)
		commitListFragment();		
	}
	
	protected void setActionBarTitle(String title){
		getSupportActionBar().setTitle(title);
	}
	private void commitListFragment(){
		
		mListFragment = new QuotesListFragment();
		commitFragment(mListFragment);
		
	}
	private boolean commitFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment currentFragment = fragmentManager.findFragmentByTag("Content");
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (currentFragment != null) {
			fragmentTransaction
			.replace(R.id.content_frame, fragment, "Content")
			.addToBackStack("Content").commit();
		} else {
			fragmentManager.beginTransaction()
			.add(R.id.content_frame, fragment, "Content").commit();
		}
		return true;

	}	
	public void onBackPressed(View view) {

		FragmentManager fragmentManager = getSupportFragmentManager();

		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		int backStackCount = fragmentManager.getBackStackEntryCount();
		if (backStackCount > 0) {
			fragmentManager.popBackStack();
			fragmentTransaction.commit();	
		} else {
			super.onBackPressed();
		}

	}
	@Override
	public boolean fragmentOnClickCallBack(Fragment fragment) {
		return commitFragment(fragment);
	}

}
