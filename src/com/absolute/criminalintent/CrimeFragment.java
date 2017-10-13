package com.absolute.criminalintent;

import java.util.Date;
import java.util.UUID;
import java.util.zip.Inflater;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CrimeFragment extends Fragment {
	
	public static final String EXTRA_CRIME_ID = "com.absolute.criminalintent.id";
	private static final String TAG = "CrimeFragment";

	protected static final String DIALOG_DATE = "date";
	private static final String DIALOG_IMAGE = "image";

	protected static final int REQUEST_DATE = 0;
	protected static final int REQUEST_PHOTO = 1;

	private Crime mCrime;
	private ImageButton mPhotoButton;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	private ImageView mPhotoView;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		UUID crimeiId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
		
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeiId);
	}
	
	public void returnResult()
	{
		getActivity().setResult(Activity.RESULT_OK, null);
	}
	
	public void updateDate(){
		String date = (String)DateFormat.format("EE, MMMM,dd日,yyyy",mCrime.getDate());
		mDateButton.setText(date);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub	
		super.onStart();
		showPhoto();
	}

	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		PictureUtils.cleanImageView(mPhotoView);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//Toast.makeText(getActivity(), "onPause(", Toast.LENGTH_SHORT).show();
		CrimeLab.get(getActivity()).saveCrimes();
	}
	
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if(resultCode != Activity.RESULT_OK) 
			return ;
		//该处没执行；
		if(requestCode == REQUEST_DATE){
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATA);
			mCrime.setDate(date);
			updateDate();
			
		}else if (requestCode == REQUEST_PHOTO){
			String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
			if(filename != null){
				
				Photo p = new Photo(filename);
				mCrime.setPhoto(p);
				Log.i(TAG, "Crime---> " + mCrime.getTitle() + "has a photo");
				showPhoto();
				
			}
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		case android.R.id.home:
			if(NavUtils.getParentActivityName(getActivity()) != null){
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			
			return true;
			 

		default:
			return super.onOptionsItemSelected(item);
		}
		
		
		
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_crime, parent, false);
		
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			if(NavUtils.getParentActivityName(getActivity())!= null ){
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);				
			}
		}
		
		mPhotoView = (ImageView)v.findViewById(R.id.crime_imageView);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Photo p = mCrime.getPhoto();
				if(p == null) return ;
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
				
				
			}
		});
		
		
		mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
		
		//if camera is not availabel disbale camera functionality
		PackageManager pm = getActivity().getPackageManager();
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)|| 
							pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)||
							Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD ||
							Camera.getNumberOfCameras() > 0 ;
		if(!hasACamera){
			mPhotoButton.setEnabled(false);
		}
		
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);				
				
				//startActivity(intent);
				startActivityForResult(intent, REQUEST_PHOTO);
				
				
			}
		});		
		
		
		mTitleField = (EditText)v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				// TODO Auto-generated method stub
				 
				mCrime.setTitle(c.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence c, int start, int count, int before) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable c) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mDateButton = (Button)v.findViewById(R.id.crime_date);
		
		updateDate();
		mDateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getActivity().getSupportFragmentManager();
				
				/*DatePickerFragment dialog = new DatePickerFragment();*/
				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				Log.e(getTag(), "gotoDatePickerFragment--request_date"+REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});

		
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				mCrime.setSolved(isChecked);
			}
		});
		
		
		return v;
	}
	
	private void showPhoto(){
		Photo p = mCrime.getPhoto();
		BitmapDrawable b = null;
		if(p != null){
			String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaleDrawable(getActivity(), path);			
		}
		mPhotoView.setImageDrawable(b);
		
	}
	private String getCrimeReport(){
		String solvedString = null;
		if(mCrime.isSolved()){
			solvedString = getString(R.string.crime_report_solved);
		}else {
			solvedString = getString(R.string.crime_report_unsolved);
		}
		
		String dateFormate = "EEE, MMM dd";
		String dateString = (String) DateFormat.format(dateFormate, mCrime.getDate());
		
		String suspect = mCrime.getSuspect();
		if(suspect == null){
			suspect = getString(R.string.crime_report_no_suspect);
		}else {
			suspect = getString(R.string.crime_report_suspect,suspect);
		}
		
		String report = getString(R.string.crime_report,mCrime.getTitle(),dateString,solvedString,suspect);
		
		return report;
		
	}
	
	
	public static CrimeFragment newInstance(UUID crimeId){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	
}
