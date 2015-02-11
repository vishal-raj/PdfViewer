package com.example.pdfviewer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PdfHome extends Activity{
	String pdflist;
	ProgressDialog dialog=null;
	InputStream is;
	HttpPost httppost=null;
	HttpClient httpclient;
	HttpResponse response;
	HttpEntity entity;
	String result,user_id,token;
	String pdfname[]={};
	String pdfimage[]={};
	int pdf_icon = (int)(R.drawable.pdf_icon);
	GridView gridView;      
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.home);
	    Intent i = getIntent();
	    Bundle extras = i.getExtras();
	    
        // Receiving the user_id and token from LoginActivity 
        user_id = extras.getString("user_id");
        token = extras.getString("token");
        
        //user_id on action bar
        ActionBar pdfHomeAb = getActionBar();
        pdfHomeAb.setTitle(user_id);
        //pdfHomeAb.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        //Toast.makeText(PdfHome.this,user_id, Toast.LENGTH_SHORT).show();
	    dialog = ProgressDialog.show(PdfHome.this, "", 
                "Fetching Pdf Files from Server", true);
	    fetchPdf fetchTask = new fetchPdf();
    	fetchTask.execute(new String[]{user_id, token});
    	
	}
	
	private class fetchPdf extends AsyncTask<String, Void, List<HashMap<String,String>>> {

		String pdfName;
		List<HashMap<String,String>> aList;
		@Override
		protected List<HashMap<String,String>> doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				PdfFromServer pdfObj= new PdfFromServer();
				pdflist=pdfObj.pdfList(user_id, token);
				aList = parseInfo(pdflist);
				dialog.dismiss();
			}
			catch(Exception e){
				Log.d("vishu","Exception in creating object");
				e.printStackTrace();
	    		dialog.dismiss();
			}
			
			
			Log.d("List 2",aList.toString());
        	
        	return aList; 
	}//End of doInBackground
		
		@Override
		   protected void onPostExecute(final List<HashMap<String,String>> aList) {
			
			//System.out.println(aList);
			
			String[] from = { "pdf_icon","pdf_name"};

    		// Ids of views in listview_layout
    		int[] to = { R.id.pdf_icon,R.id.pdf_name};

    		// Instantiating an adapter to store each items
    		// R.layout.listview_layout defines the layout of each item
        	SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.cloud_grid_item, from, to);        
        
        	// Getting a reference to gridview of MainActivity
        	gridView = (GridView) findViewById(R.id.gridviewcloud);
        
        	// Setting an adapter containing images to the gridview
        	gridView.setAdapter(adapter);
        	
        	gridView.setOnItemClickListener(new OnItemClickListener() { 
        		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        			pdfName=aList.get(position).get("pdf_name").toString();
       		
        			Toast.makeText(getBaseContext(), aList.get(position).get("pdf_name").toString(), Toast.LENGTH_LONG).show();
        			showViewDownloadDialog(pdfName);
       		 	}
        	});
		}//End of onPostExcute
				
	}//End of fetchPdf
	
	public void showViewDownloadDialog(final String pdfName){
		//dialog for view/download
        
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(PdfHome.this);
        myAlertDialog.setTitle("Dialog");
        myAlertDialog.setMessage("Download "+pdfName);
        myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

         public void onClick(DialogInterface arg0, int arg1) {
         // do something when the OK button is clicked
        	 Intent i = new Intent(getApplicationContext(), DownloadView.class);
        	 i.putExtra("pdf_name", pdfName);
        	 startActivity(i);
         }});
        myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              
         public void onClick(DialogInterface arg0, int arg1) {
         // do something when the Cancel button is clicked
        	 
         }});
        myAlertDialog.show();
	}//End of showViewDownloadDialog
	
	
	public void showAlert(){
        PdfHome.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(PdfHome.this);
                builder.setTitle("Error.");
                builder.setMessage("File not Found.")  
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
    }//End of showAlert
    
	public List<HashMap<String,String>> parseInfo(String pdflist){
		List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
		try {
			JSONObject jobj = new JSONObject(pdflist);
			String results = jobj.getString("results");
			JSONArray jsonArr = new JSONArray(results);
			
			//creating cache directory for pdf Image
			String dirPath = getFilesDir().getAbsolutePath() + File.separator + "image";
			File imageDir = new File(dirPath);
			if (!imageDir.exists()){
			    imageDir.mkdirs();
			    Log.d("Message","Directory Created");
			}
			
			for (int i=0; i<jsonArr.length(); i++){
				JSONObject jsonObject = jsonArr.getJSONObject(i);
				String fileName = jsonObject.getString("description").toString();
				String imageUrl = jsonObject.getString("image").toString();
				//Image name from image Url
				String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
				String imagePath = getFilesDir().getAbsolutePath() + File.separator + "image/"+imageName;
				Log.d("Image Path",imagePath);
				File imageFile = new File(imagePath);
				if(!imageFile.exists()){
					//download image
					downloadImage(imageUrl, imageName);
				}
				HashMap<String, String> hm = new HashMap<String,String>();
	    		hm.put("pdf_name", fileName);            
	    		hm.put("pdf_icon", imagePath);
	    		aList.add(hm);
	    	}
			
			Log.d("List 1",aList.toString());
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aList;
	}
	
	public void downloadImage(String imageUrl, String imageName){
		
		try{
			String storePath = getFilesDir().getAbsolutePath() + File.separator + "image/"+imageName;
			
			//File sPath = new File(storePath);
			URL url = new URL ("http://usgbc.org/"+ Uri.encode(imageUrl));
			URLConnection ucon = url.openConnection();

			//Define InputStreams to read from the URLConnection.
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			//Read bytes to the Buffer until there is nothing more to read(-1).
			ByteArrayBuffer baf = new ByteArrayBuffer(1024);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(storePath);
			fos.write(baf.toByteArray());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//End of downloadImage
	
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
	
}