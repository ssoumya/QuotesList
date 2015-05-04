package com.cilla3bc.quoteslist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
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
	private boolean mQuotesListSpeaker;
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
		((QuotesListActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.quotelist_title));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (mQuotesListSpeaker == false) {
			inflater.inflate(R.menu.quoteslist_menu, menu);
			mQuotesListSpeaker = true;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_list_speaker) {
			onSpeak();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.quotes_list_fragment, container, false);

		mQuotesListView = (ListView)rootView.findViewById(R.id.quotesListView);
		mQuotesListView.setBackgroundColor(getResources().getColor(R.color.black));

		mQuotesList = QuotesListActivity.mQuotesList;

		mAdapter = new QuotesListAdapter(getActivity(), mQuotesList);

		mQuotesListView.setAdapter(mAdapter);

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
		 HashMap<String, String> myHash = new HashMap<String, String>();		 
		 myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "done");
		 
		for(String quote: mQuotesList)			
		{
			mQuoteToSpeech.speak(quote, TextToSpeech.QUEUE_ADD, null);
			//play silence the Text To Speech for 3 seconds for every quote
			mQuoteToSpeech.playSilence(3000, TextToSpeech.QUEUE_ADD, null);
		}		
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

}
