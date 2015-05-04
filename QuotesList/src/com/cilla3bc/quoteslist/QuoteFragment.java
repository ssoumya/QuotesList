package com.cilla3bc.quoteslist;

import java.util.Locale;

import android.app.Activity;
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
	private boolean mIsSpeakerInMenu = false;

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
		((QuotesListActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.quote_title));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (mIsSpeakerInMenu == false) {
			inflater.inflate(R.menu.quote_menu, menu);
			mIsSpeakerInMenu = true;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_speaker) {
			onSpeak();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.quote_fragment,
				container, false);

		mQuoteText = (TextView)rootView.findViewById(R.id.quoteTv);
		mNextBtn = (ImageButton)rootView.findViewById(R.id.nextBtn);
		mHomeBtn = (ImageButton)rootView.findViewById(R.id.homeBtn);
		mPreviousBtn = (ImageButton)rootView.findViewById(R.id.previousBtn);

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

	public void onSpeak(){

		mQuoteToSpeech.speak(mQuote, TextToSpeech.QUEUE_FLUSH, null);

	}

}
