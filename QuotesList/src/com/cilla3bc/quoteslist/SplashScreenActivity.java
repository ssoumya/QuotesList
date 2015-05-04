package com.cilla3bc.quoteslist;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class SplashScreenActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		TextView title = (TextView)findViewById(R.id.titleTv);

		Typeface myTypeface = Typeface.createFromAsset(
				this.getAssets(),
				"font/BLKCHCRY.TTF"); 

		title.setTypeface(myTypeface);

		Thread splashThread = new Thread(){

			@Override
			public void run() {
				try
				{
					sleep(2000);
					Intent intent = new Intent(getBaseContext(), QuotesListActivity.class);
					startActivity(intent);
					finish();

				}
				catch(Exception e){

				}
			}
		};
		splashThread.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
