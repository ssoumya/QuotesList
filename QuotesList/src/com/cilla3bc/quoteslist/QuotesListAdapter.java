package com.cilla3bc.quoteslist;

import java.util.ArrayList;
import java.util.zip.Inflater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuotesListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<String> mQuotesList;
	private LayoutInflater mInflater;

	public QuotesListAdapter(Context context, ArrayList<String> quotesList){
		mContext = context;
		mQuotesList = quotesList;

		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mQuotesList.size();
	}

	@Override
	public Object getItem(int position) {
		return mQuotesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder;
		
		if(convertView ==  null){
			
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.list_row_layout, null);
			holder.quoteRow = (TextView)view.findViewById(R.id.quoteRow); 
			
			view.setTag(holder);
			
		}
		else
		{
			holder = (ViewHolder)view.getTag();
		}
		//handle the null data
		if(mQuotesList.size() <= 0){
			holder.quoteRow.setText("No Quotes are found");
		}
		else
		{
			holder.quoteRow.setText(mQuotesList.get(position));
		}
		
		
		return view;
	}

	public static class ViewHolder {

		public TextView quoteRow;
	}

}
