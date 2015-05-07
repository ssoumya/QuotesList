package com.cilla3bc.quoteslist;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class QuoteFragment extends Fragment{

	private TextView mQuoteText;
	private FragmentOnClickListener mCallback;
	private Bundle mBundle;
	private String mQuote;
	private int mIndex;
	protected ImageButton mNextBtn;
	protected ImageButton mPreviousBtn;
	protected ImageButton mHomeBtn;
	protected QuotesListFragment mListFragment;
	private QuoteFragment mQuoteFragment;
	private TextToSpeech mQuoteToSpeech;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallback = (FragmentOnClickListener)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.quote_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_speaker) {
			onSpeak();
			return true;
		} 
		else if(itemId == R.id.action_mute_speaker){
			stopSpeaker();
			return true;
		}
		else if(itemId == R.id.action_share){
			shareQuote();
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.quote_fragment,
				container, false);

		((QuotesListActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.quote_title));

		mQuoteText = (TextView)rootView.findViewById(R.id.quoteTv);
		mNextBtn = (ImageButton)rootView.findViewById(R.id.nextBtn);
		mHomeBtn = (ImageButton)rootView.findViewById(R.id.homeBtn);
		mPreviousBtn = (ImageButton)rootView.findViewById(R.id.previousBtn);

		unLockScreenOrientation();

		Typeface myTypeface = Typeface.createFromAsset(
				getActivity().getAssets(),
				"font/BLKCHCRY.TTF"); 

		mQuoteText.setTypeface(myTypeface);

		//Initialize the text to speech listener
		mQuoteToSpeech = new TextToSpeech(getActivity(), 
				new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR){
					mQuoteToSpeech.setLanguage(Locale.US);
				}				
			}
		});

		mBundle = getArguments();

		if(mBundle != null){
			try{
				mQuote = (String)mBundle.getString("QUOTE");
				mIndex = (Integer)mBundle.getInt("INDEX");
			}
			catch(Exception e)
			{

			}			

		}
		if(mIndex >= 1){
			mPreviousBtn.setVisibility(View.VISIBLE);		
		}
		else{
			mPreviousBtn.setVisibility(View.GONE);
		}
		if(mIndex == (QuotesListActivity.mQuotesList.size())-1){
			mNextBtn.setVisibility(View.GONE);			
		}
		else
		{
			mNextBtn.setVisibility(View.VISIBLE);
		}
		mQuoteText.setText(mQuote);

		mHomeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(mListFragment != null)
				{
					mCallback.fragmentOnClickCallBack(mListFragment);
				}
				else
				{
					mListFragment = new QuotesListFragment();
					mCallback.fragmentOnClickCallBack(mListFragment);
				}
			}
		});

		mPreviousBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mBundle = new Bundle();
				mBundle.putInt("INDEX", mIndex-1);
				mBundle.putString("QUOTE", QuotesListActivity.mQuotesList.get(mIndex-1).toString());
				mQuoteFragment = new QuoteFragment();
				mQuoteFragment.setArguments(mBundle);
				mCallback.fragmentOnClickCallBack(mQuoteFragment);
			}
		});

		mNextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mBundle = new Bundle();
				mBundle.putInt("INDEX", mIndex+1);
				mBundle.putString("QUOTE", QuotesListActivity.mQuotesList.get(mIndex+1).toString());
				mQuoteFragment = new QuoteFragment();
				mQuoteFragment.setArguments(mBundle);
				mCallback.fragmentOnClickCallBack(mQuoteFragment);

			}
		});

		return rootView;
	}

	private void stopSpeaker(){
		if(mQuoteToSpeech != null){
			mQuoteToSpeech.stop();
		}
		// unlock the screen orientation on onStop audio
		unLockScreenOrientation();
	}

	@Override
	public void onPause() {
		super.onPause();
		mQuoteToSpeech.stop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mQuoteToSpeech.stop();
	}

	private void shareQuote(){
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, mQuote);
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}

	@SuppressWarnings("deprecation")
	public void onSpeak(){
		// lock screen orientation to avoid cancelling the text to speech(audio)
		lockScreenOrietation();
		mQuoteToSpeech.speak(mQuote, TextToSpeech.QUEUE_FLUSH, null);
	}

	private void lockScreenOrietation(){

		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} 
		else { 
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		} 		
	}

	private void unLockScreenOrientation(){
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}

}
