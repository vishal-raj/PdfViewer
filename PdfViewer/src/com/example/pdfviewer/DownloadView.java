package com.example.pdfviewer;

import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DownloadView extends Activity {
    /** Called when the activity is first created. */
	ProgressDialog dialog;
	String name;
	String fileDownloadPath="http://10.0.3.2/pdfviewer/pdf/";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloadview);
 
        TextView txtpdfName = (TextView) findViewById(R.id.tvpdf_name);
        
 
        Intent i = getIntent();
        // Receiving the Data
        name = i.getStringExtra("pdf_name");
        
        //Log.e("Second Screen", name);
        fileDownloadPath=fileDownloadPath+name;
        fileDownloadPath=fileDownloadPath.replace(" ", "%20");
        //System.out.println(fileDownloadPath);
        // Displaying Received data
        txtpdfName.setText(name);
        dialog = ProgressDialog.show(DownloadView.this, "", 
                "Loading "+name+"....", true);
 
        new Thread(new Runnable(){
			
			public void run(){
				byte[] temp=downloadFile(fileDownloadPath);
				DatabaseHandler db = new DatabaseHandler(DownloadView.this);
				//db.addPdf(name, temp);
				db.getPdf(name);
				hideProgressDialog();
			}
		}).start();
        
 
    }
    public byte[] downloadFile(String fileDownloadPath) {
		// TODO Auto-generated method stub
    	byte[] buffer = null;
    	try{
			//File saveFile=new File(fileSavePath);
			URL u=new URL(fileDownloadPath);
			URLConnection con=u.openConnection();
			int lengthOfContent=con.getContentLength();
			DataInputStream DIStream=new DataInputStream(u.openStream());
			buffer = new byte[lengthOfContent];
			DIStream.readFully(buffer);
			DIStream.close();
			//System.out.println(buffer);
			
		}catch(Exception e){ 
			
		}
    	return buffer;
	}
    
    public String getName(){
    	return name;
    }
    
    private void hideProgressDialog() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
} 
	

