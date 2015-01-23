package com.example.pdfviewer;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

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
                	try{
                		PdfFromServer pdfObj= new PdfFromServer();
                		pdfObj.pdfList();
                		dialog.dismiss();
                	}catch(Exception e){
                		Log.d("abc","Exception in creating object");
                		dialog.dismiss();
                		}
                	
                }
              }).start(); 
	    
	    
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