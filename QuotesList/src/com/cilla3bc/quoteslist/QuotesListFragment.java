package com.cilla3bc.quoteslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class QuotesListFragment extends Fragment{
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.quotes_list_fragment, container, false);
		return super.onCreateView(inflater, container, savedInstanceState);
		
		
	}

}
