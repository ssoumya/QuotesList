package com.cilla3bc.quoteslist;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class QuotesListFragment extends Fragment{

	private ListView mQuotesListView;
	private QuotesListAdapter mAdapter;
	private Bundle mBundle;
	private QuoteFragment mQuoteFragment;
	private FragmentOnClickListener mCallback;
	public static TextToSpeech mQuoteToSpeech;
	private ArrayList<String> mQuotesList;
	private int mIndex;
	private String mQuote;


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
		inflater.inflate(R.menu.quoteslist_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_list_speaker) {
			onSpeak();
			return true;
		} 
		else if(itemId == R.id.action_list_mute_speaker){
			stopSpeaker();
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.quotes_list_fragment, container, false);
		((QuotesListActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.quotelist_title));

		mQuotesListView = (ListView)rootView.findViewById(R.id.quotesListView);
		mQuotesListView.setBackgroundColor(getResources().getColor(R.color.black));

		mQuotesList = QuotesListActivity.mQuotesList;

		mAdapter = new QuotesListAdapter(getActivity(), mQuotesList);

		mQuotesListView.setAdapter(mAdapter);

		unLockScreenOrientation();

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

		mQuotesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				mIndex = position;
				mQuote = mQuotesList.get(position).toString();
				onClickListView();

			}
		});

		return rootView;	

	}
	private void onClickListView(){

		mBundle = new Bundle();
		mBundle.putInt("INDEX", mIndex);
		mBundle.putString("QUOTE", mQuote);
		mQuoteFragment = new QuoteFragment();
		mQuoteFragment.setArguments(mBundle);
		mCallback.fragmentOnClickCallBack(mQuoteFragment);

	}	

	@SuppressWarnings("deprecation")
	private void onSpeak(){
		// lock screen orientation to avoid canceling the text to speech(audio)
		lockScreenOrietation();		
		for(String quote: mQuotesList)			
		{
			mQuoteToSpeech.speak(quote, TextToSpeech.QUEUE_ADD, null);
			//play silence the Text To Speech for 3 seconds for every quote
			mQuoteToSpeech.playSilence(3000, TextToSpeech.QUEUE_ADD, null);
		}	
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

