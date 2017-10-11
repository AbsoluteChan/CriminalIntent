package com.absolute.criminalintent;


import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCameraFragment extends Fragment {

	private static final String TAG = "CrimeCameraFragment";

	public static final String EXTRA_PHOTO_FILENAME = "com.bingnerdranch.android.criminalintent.photo_filename";
	
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private View mProgressContainer;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_crime_camera, parent, false);
		
		Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//getActivity().finish();
				if(mCamera != null){
					mCamera.takePicture(mShutterCallback, null, mJpegCallback);
				}
			}
		});
		
		mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);
				
		mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfacrView);
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		holder.addCallback(new  SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if(mCamera != null){
					mCamera.stopPreview();
				}								
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				try {
					if(mCamera != null){
						mCamera.setPreviewDisplay(holder);	
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, "Error setting up preview display",e);
				}
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
				// TODO Auto-generated method stub
				if(mCamera == null) return;
				
				Camera.Parameters parameters = mCamera.getParameters();
				Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
				parameters.setPreviewSize(s.width, s.height);
				 
				
				mCamera.setParameters(parameters);
				try {
					mCamera.startPreview();		
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, "Could not start preview", e);
					mCamera.release();
					mCamera = null;
				}
				
			}
		});
		
		return v;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@SuppressLint("NewApi")
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
			mCamera = Camera.open(0);
		}
		else {
			mCamera = Camera.open();
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(mCamera != null)
		{
			mCamera.release();
			mCamera = null;
		}
	}

	private Size getBestSupportedSize(List<Size> sizes ,int width ,int height){
		
		Size bestSize = sizes.get(0);
		int largestArea = bestSize.width*bestSize.height;
		for(Size s : sizes){
			int area = s.width*s.height;
			if(area > largestArea){
				bestSize = s;
				largestArea = area;
			}
		}
		
		return bestSize;
		
	}
	
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			mProgressContainer.setVisibility(View.VISIBLE);
			
		}
	};
	
	private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			String filename = UUID.randomUUID().toString() + ".jpg";
			
			FileOutputStream os = null;
			boolean success = true;
			try {
				os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				os.write(data);
			
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, "Error writing to file " + filename);
				success = false;
			}finally{
				try {
					if(os != null){
						os.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, "Error closing file " + filename);
					success = false;
				}
			}
			if(success == true){
				Log.i(TAG, "JPEG saved at " + filename );
				Intent i = new Intent();
				i.putExtra(EXTRA_PHOTO_FILENAME, filename);
				getActivity().setResult(Activity.RESULT_OK, i);
			}else {
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			
			
			
			
			getActivity().finish();
			
		}
	};
	
	
	
	
	
}
