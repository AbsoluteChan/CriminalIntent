package com.absolute.criminalintent;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class CrimeListFragment extends ListFragment {

	private static final String TAG = "CrimeListFragment";
	private static final int REQUEST_CRIME = 1;
	private boolean mSubtitleVisible;
	private ArrayList<Crime> mCrimes;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		setRetainInstance(true);
		mSubtitleVisible = false;
		
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
		//废弃使用原来的crimeActivity
		/*Intent i = new Intent(getActivity(), CrimeActivity.class);*/
		//Start CrimePagerActivity with this crime
		Intent i = new Intent(getActivity(),CrimePagerActivity.class);
		
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



	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_lis, menu);
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if(mSubtitleVisible && showSubtitle != null){
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
		
	}
	
	



	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			 Crime crime = new Crime();
			 CrimeLab.get(getActivity()).addCrime(crime);
			 Intent i = new Intent(getActivity(), CrimePagerActivity.class);
			 i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			 startActivityForResult(i, 0);
			 
			 return true;			
		case R.id.menu_item_show_subtitle:
			 if(getActivity().getActionBar().getSubtitle() == null){
				 getActivity().getActionBar().setSubtitle(R.string.subtitle);
				 mSubtitleVisible = true;
			 }
			 else{
				 getActivity().getActionBar().setSubtitle(null);
				 item.setTitle(R.string.show_subtitile);
				 mSubtitleVisible = false;
			 }
			 return true;
		default:			
			return super.onOptionsItemSelected(item);
			
		}		 
		
					
		
		
	}








	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}

	
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
		Crime crime = adapter.getItem(position);
		
		switch (item.getItemId()) {
		case R.id.menu_item_delete_crime:
			CrimeLab.get(getActivity()).deleteCrime(crime);
			adapter.notifyDataSetChanged();
			return true;
			

		default:
			break;
		}
		
		return super.onContextItemSelected(item);
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			if(mSubtitleVisible ){
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}			
		}
		
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
				registerForContextMenu(listView);
		}else {
			
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				
				@Override
				public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public void onDestroyActionMode(ActionMode arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}
				
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					// TODO Auto-generated method stub
					switch (item.getItemId()) {
					case R.id.menu_item_delete_crime:
						CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
						CrimeLab crimeLab = CrimeLab.get(getActivity());
						for (int i = adapter.getCount() - 1; i >= 0 ; i--) {
							if(getListView().isItemChecked(i))
							{
								crimeLab.deleteCrime(adapter.getItem(i));
							}
						}
						mode.finish();
						adapter.notifyDataSetChanged();
						return true;
					default:
						return false;
					}
					
				}
				
				@Override
				public void onItemCheckedStateChanged(ActionMode arg0, int arg1, long arg2,
						boolean arg3) {
					// TODO Auto-generated method stub
					
				}
			});
			
		}
		
		
		
		return v;
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
