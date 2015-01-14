package com.example.pdfviewer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.*;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.view.WindowManager;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Configuration config = getResources().getConfiguration();

	      FragmentManager fragmentManager = getFragmentManager();
	      FragmentTransaction fragmentTransaction = 
	      fragmentManager.beginTransaction();
	      if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
	      {
	    	  LM_Fragement ls_fragment = new LM_Fragement();
	          fragmentTransaction.replace(android.R.id.content, ls_fragment);
	      }
	      else
	      {
	    	  PM_Fragement pm_fragment = new PM_Fragement();
	          fragmentTransaction.replace(android.R.id.content, pm_fragment);
	      }
		
	      fragmentTransaction.commit();
		Log.d("First","By Vishal");
		Log.d("First","By Supriya");
		Log.d("Second","By Vishal");
		Log.d("Second","By Supriya");
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
