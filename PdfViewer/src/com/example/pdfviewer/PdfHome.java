package com.example.pdfviewer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class PdfHome extends Activity{
	ProgressDialog dialog=null;
	InputStream is;
	HttpPost httppost=null;
	HttpClient httpclient;
	HttpResponse response;
	HttpEntity entity;
	String result;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pdfhome);
	    dialog = ProgressDialog.show(PdfHome.this, "", 
                "Fetching Pdf Files from Server", true);
         new Thread(new Runnable() {
                public void run() {
                	pdfList();                          
                }
              }).start(); 
	    
	    
	}



    private void pdfList() {
		
		// TODO Auto-generated method stub
    	try {
    		int timeout= 7000;
    	    httpclient = new DefaultHttpClient();
    	    httpclient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeout);
    	    httppost = new HttpPost("http://10.0.3.2/pdfviewer/listPdf.php"); 
    	    response = httpclient.execute(httppost);
    	    entity = response.getEntity();
    	    is = entity.getContent();
    	}
    	catch(Exception e) {
    	    e.printStackTrace();
    	    dialog.dismiss();
    	}



    	
		try{
    	    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
    	    StringBuilder sb = new StringBuilder();
    	    sb.append(reader.readLine() + "\n");
    	    String line="0";

    	    while ((line = reader.readLine()) != null) {
    	        sb.append(line + "\n");
    	    }

    	    is.close();
    	    result=sb.toString();   
    	}
    	catch(Exception e){
    	    e.printStackTrace();
    	}

    	for (String getFile: result.split("-")){
    	    Log.d("abc","String is:" + getFile);
    	}
    	runOnUiThread(new Runnable() {
            public void run() {
                
                dialog.dismiss();
            }
        });
	}
    
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
    }
	
}