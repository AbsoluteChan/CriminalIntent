package com.absolute.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class CrimeActivity extends SingleFragmentActivity {



	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new CrimeFragment();
	}


}
