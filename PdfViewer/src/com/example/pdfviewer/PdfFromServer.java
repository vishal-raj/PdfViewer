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
import android.util.Log;

public class PdfFromServer {
	InputStream is;
	HttpPost httppost=null;
	HttpClient httpclient;
	HttpResponse response;
	HttpEntity entity;
	String result;
	
public void pdfList() {
		
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
    	
        
	}
    
    
    }

