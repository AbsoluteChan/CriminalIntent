package com.absolute.criminalintent;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {

	private static final String TAG = "CrimeListFragment";
	private static final int REQUEST_CRIME = 1;
	private ArrayList<Crime> mCrimes;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.crime_title);
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		
		/*ArrayAdapter<Crime> adapter = 
				new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);*/
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
		
		
		
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//通过一个外部的方法控制若适配器的内容改变，强制调用getView刷新Item的内容，实现动态刷新列表
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		/*Crime c = (Crime)(getListAdapter()).getItem(position);*/
		Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
		/*Log.d(TAG, c.getTitle() + "was clicked");*/
		Intent i = new Intent(getActivity(), CrimeActivity.class);
		
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
		startActivity(i);
				
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
/*		super.onActivityResult(requestCode, resultCode, data);
*/		
		if(requestCode == REQUEST_CRIME){
			//Handle result
			
		}
	}



	private class CrimeAdapter extends ArrayAdapter<Crime>{

		public CrimeAdapter(ArrayList<Crime> crimes)
		{
			super(getActivity(),0,crimes);
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//if we weren't given a view inflate one
			if(convertView == null){
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_crime, null);
			}
			//configure the view for this Crime
			
			Crime c = getItem(position);
			
			TextView titileTextView = 
					(TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
			titileTextView.setText(c.getTitle());
			
			TextView dateTextView = 
					(TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
			dateTextView.setText( c.getDate().toString());
			
			CheckBox solvedCheckBox = 
					(CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());
						
			
			return convertView;
		}
		
		
		
		
	}

	
}
