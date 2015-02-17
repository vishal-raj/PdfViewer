/* 
 * Author Vishal Raj
 * Intern@Groupten
 */
package com.example.pdfviewer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class HomeActivity extends Activity{
	
	private String pdfname, filekey, pdflist, imageurl;
	private ProgressDialog dialog=null;
	private String user_id,token;
	int pdf_icon = (int)(R.drawable.pdf_icon);
	CustomGridview gridView;   
	ImageAdapterCloud cloudadapter;
	ImageAdapterDevice deviceadapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pdfhome);
	    
	    // Receiving the user_id and token from LoginActivity
	    Intent i = getIntent();
	    Bundle extras = i.getExtras();
	    user_id = extras.getString("user_id");
        token = extras.getString("token");
        
        //user_id on action bar
        ActionBar pdfHomeAb = getActionBar();
        pdfHomeAb.setTitle(user_id);
        
	    dialog = ProgressDialog.show(HomeActivity.this, "", 
                "Loading...", true);
	    
	    //loading from PDF from local using AsyncTask class FetchPdfLocal
	    FetchPdfLocal fetchlocal = new FetchPdfLocal();
	    fetchlocal.execute();
	    
	    //Checking for Internet Connection
	    if(isNetworkAvailable()){
	    	//loading PDF files from server AsyncTask class FetchPdfServer
	    	FetchPdfServer fetchTask = new FetchPdfServer();
	    	fetchTask.execute(new String[]{user_id, token});
	    }
    	
	}//End of onCreate
	
	
	private class FetchPdfLocal extends AsyncTask<String, Void, List<HashMap<String,String>>> {
		
		//For Store Pdf name and image path in SQLite DB
		private PdfDetailsSource datasource;
		
		@Override
		protected List<HashMap<String, String>> doInBackground(String... params) {
			List<HashMap<String, String>> aList;
			// TODO Auto-generated method stub
			String user = user_id.split("\\@")[0];
			datasource = new PdfDetailsSource(getBaseContext());
		    datasource.open();
			aList =  datasource.getPdfDetails(user);
			
			System.out.println(aList.toString());
			ArrayList<String> temp1 = new ArrayList<String>();
			ArrayList<String> temp2 = new ArrayList<String>();
			for (int i = 0; i < aList.size(); i++) {
				temp1.add((String) aList.get(i).get("pdf_name"));
				temp2.add((String) aList.get(i).get("pdf_icon"));
				//Log.d("temp1",  temp1.toString());
				//Log.d("temp2",  temp2.toString());
			}
			
			//Converting List to Array for creating image adapter
			String [] pname = temp1.toArray(new String[temp1.size()]);
			String [] pimage = temp2.toArray(new String[temp2.size()]);

			//Creating Custom gridview object using the above created Arrays
			deviceadapter = new ImageAdapterDevice(HomeActivity.this, pname, pimage);
			return aList;
		}//End of doInBackground
		
		@Override
		protected void onPostExecute(final List<HashMap<String,String>> aList) {

			gridView = (CustomGridview) findViewById(R.id.griddevice);

			// Setting an adapter containing images to the gridview
			gridView.setAdapter(deviceadapter);
			//For this only we created the custom gridview
			//For screen scrollable
			gridView.setExpanded(true);
    	
			//Listener for grid items
			gridView.setOnItemClickListener(new OnItemClickListener() { 
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					
					pdfname=aList.get(position).get("pdf_name").toString();
					
					//Log.d("name",pdfname);
					//Toast.makeText(getBaseContext(), aList.get(position).get("pdf_name").toString(), Toast.LENGTH_LONG).show();

					//Open Pdfrender Activity to render the pdf file
					String pdfpath = getFilesDir().getAbsolutePath()+"/pdf/"+pdfname+".pdf";
					Intent i = new Intent(HomeActivity.this, PdfRenderActivity.class);
					i.putExtra("pdf_path", pdfpath);
					startActivity(i);
					
				}
			});
		
	}
}
	
	
	//Inner AsyncTask class for loading pdf from server
	private class FetchPdfServer extends AsyncTask<String, Void, List<HashMap<String,String>>> {

		List<HashMap<String,String>> aList;
		@Override
		protected List<HashMap<String,String>> doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				//To get JSON object from server which contain all the details.
				PdfFromServer pdfObj= new PdfFromServer();
				pdflist=pdfObj.pdfList(user_id, token);
				//String dirpath = getFilesDir().getAbsolutePath() + File.separator;
				aList = pdfObj.parseInfo(pdflist, user_id, getBaseContext());
				dialog.dismiss();
			}
			catch(Exception e){
				//Log.d("vishu","Exception in creating object");
				e.printStackTrace();
	    		dialog.dismiss();
			}
			
			ArrayList<String> temp1 = new ArrayList<String>();
			ArrayList<String> temp2 = new ArrayList<String>();
			for (int i = 0; i < aList.size(); i++) {
			    temp1.add((String) aList.get(i).get("pdf_name"));
			    temp2.add((String) aList.get(i).get("pdf_icon"));
			    //Log.d("temp1",  temp1.toString());
			    //Log.d("temp2",  temp2.toString());
			}
		
			String [] pname = temp1.toArray(new String[temp1.size()]);
			String [] pimage = temp2.toArray(new String[temp2.size()]);
			
			//Creating image adapter with pdfname and pdficon
			cloudadapter = new ImageAdapterCloud(HomeActivity.this, pname, pimage);
        	return aList; 
		}//End of doInBackground
		
		@Override
		   protected void onPostExecute(final List<HashMap<String,String>> aList) {
			
	    	//Creating Custom gridview object*/
			gridView = (CustomGridview) findViewById(R.id.gridcloud);
	    
	    	// Setting an adapter containing images to the gridview
	    	gridView.setAdapter(cloudadapter);
	    	
	    	//For this only we created the custom gridview
	    	//For screen scrollable
	    	gridView.setExpanded(true);
			
	    	//Listener for grid items
        	gridView.setOnItemClickListener(new OnItemClickListener() { 
        		
        		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        			
        			pdfname=aList.get(position).get("pdf_name").toString();
        			filekey=aList.get(position).get("filekey").toString();
        			imageurl = aList.get(position).get("pdf_icon").toString();
        			
        			//View viewItem = gridView.getChildAt(position);
        			dialog = ProgressDialog.show(HomeActivity.this, "", 
        	                "Downloading...", true);
        			dialog.setCancelable(false);
        			DownloadPdfTask dtask = new DownloadPdfTask();
        			dtask.execute(new String[]{pdfname, filekey, imageurl});
        			
        		}
        	}); 
		}//End of onPostExcute
				
	}//End of fetchPdf
	
	
	//Inner AsyncTask class for download and store pdf file on data/data/pkg/files/pdf directory
	private class DownloadPdfTask extends AsyncTask<String, Void, String> {
		
		

		private PdfDetailsSource datasource;
		String dirpath = getFilesDir().getAbsolutePath() + File.separator + user_id;
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//Log.d("p1",params[0]);
			//Log.d("p1",params[1]);
			//Log.d("p1",params[2]);
			
			Download dpdf = new Download();
			dpdf.download(params[0], params[1], dirpath);
			
			String storePath = dirpath + "/pdf/"+params[0]+".pdf";
			File pdfFile = new File(storePath);
			
			//if pdf file is downloaded successfully then creating a new row in User Table
			if(pdfFile.exists()){
				String user = user_id.split("\\@")[0];
				datasource = new PdfDetailsSource(getBaseContext());
			    datasource.open();
				datasource.createDetails(user, params[0], params[2]);
				datasource.close();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			dialog.dismiss();
			

		}
		
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
		if (id == R.id.logout) {
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			//Preventing from going back 
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.putExtra("LoginMessage", "Logged Out");
            startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Function to check Internet Connection	
		private boolean isNetworkAvailable() {
		    ConnectivityManager connectivityManager 
		          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
		}//end of isNetworkAvailable
	
}