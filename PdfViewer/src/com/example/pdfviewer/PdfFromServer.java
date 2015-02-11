package com.example.pdfviewer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.net.Uri;

public class PdfFromServer {
	InputStream is;
	HttpGet httpGet=null;
	HttpClient httpclient;
	HttpResponse response;
	HttpEntity entity;
	String result;
	
public String pdfList(String user_id, String token) {
		
		// TODO Auto-generated method stub
    	try {
    		int timeout= 30000;
    	    httpclient = new DefaultHttpClient();
    	    httpclient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeout);
    	    //fetching list of pdf files that are stored on server
    	    String url="http://identity.usgbc.org:80/Api/v1/Education/getPublications.json?email="+Uri.encode(user_id);
    	    //Log.d("URL",url);
    	    httpGet = new HttpGet(url); 
    	    httpGet.addHeader("Authorization", "Basic "+token);
    	    response = httpclient.execute(httpGet);
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
    	    //Log.d("book list",result);
    	}
    	catch(Exception e){
    	    e.printStackTrace();
    	}
    	
    	//for (String getFile: result.split("-")){
    		//list of pdf files in logcat obtained from server
    	 //   Log.d("abc","File :" + getFile);
    	//}
    	return result;
        
	}
    
    
    }

