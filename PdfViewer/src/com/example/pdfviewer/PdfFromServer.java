package com.example.pdfviewer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class PdfFromServer {
	private InputStream is;
	private HttpGet httpGet=null;
	private HttpClient httpclient;
	private HttpResponse response;
	private HttpEntity entity;
	private String result;
	private BufferedReader reader;
	private StringBuilder sb;
	
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
			//Adding Authorization header
			httpGet.addHeader("Authorization", "Basic "+token);
			response = httpclient.execute(httpGet);
			entity = response.getEntity();
			is = entity.getContent();
			reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line="0";

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result=sb.toString();  
			//Log.d("book list",result);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<HashMap<String,String>> parseInfo(String pdflist,String user_id, Context context){
		
		String dirpath = context.getFilesDir().getAbsolutePath() + File.separator + user_id;
		List<HashMap<String,String>> tempList = new ArrayList<HashMap<String,String>>();
		try {
			JSONObject jobj = new JSONObject(pdflist);
			String results = jobj.getString("results");
			JSONArray jsonArr = new JSONArray(results);
			
			
			for (int i=0; i<jsonArr.length(); i++){
				JSONObject jsonObject = jsonArr.getJSONObject(i);
				
				String filename = jsonObject.getString("description").toString();
				String imageurl = jsonObject.getString("image").toString();
				String filekey = jsonObject.getString("fileKey").toString();
				String imageName = imageurl.substring(imageurl.lastIndexOf("/") + 1);
				String imagepath = downloadIcon(imageurl, imageName, dirpath);
				//Log.d("imp1",imagepath);
				//If PDF already exists no need to add 
				if(pdfExists(filename, dirpath)){
					
					HashMap<String, String> hm = new HashMap<String,String>();
		    		hm.put("pdf_name", filename);            
		    		hm.put("pdf_icon", imagepath);
		    		hm.put("filekey", filekey);
		    		
		    		tempList.add(hm);
				}
				
				
	    	}
			
			//Log.d("List 1",aList.toString());
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return tempList;
	}//End of parseInfo
	
	//To download pdf icons if needed
	private String downloadIcon(String imageurl, String imagename, String dirpath){
		
		//Checking image directory exists or not
		File userdir = new File(dirpath);
		if (!userdir.exists()){
		    userdir.mkdirs();
		   // Log.d("Message","User Directory Created");
		}
		dirpath = dirpath+"/image";
		//Log.d("imagedir", dirpath);
		File imageDir = new File(dirpath);
		if (!imageDir.exists()){
		    imageDir.mkdirs();
		    //Log.d("Message","Inside User Directory Image Directory Created");
		}
		
		String imagepath = dirpath + "/"+imagename;
		//Log.d("Image Path",imagepath);
		File imageFile = new File(imagepath);
		if(!imageFile.exists()){
			//download image if image not exists
			try{
				//String storePath = getFilesDir().getAbsolutePath() + File.separator + "image/"+imageName;
				
				//File sPath = new File(storePath);
				URL url = new URL ("http://usgbc.org/"+ Uri.encode(imageurl));
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
				FileOutputStream fos = new FileOutputStream(imagepath);
				fos.write(baf.toByteArray());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//Log.d("ip2",imagepath);
		return imagepath;
	}//End of downloadIcon
	
	private boolean pdfExists(String filename, String dirpath){
		
		String pdfpath = dirpath + "/pdf";
		File pdfdir = new File(pdfpath);
		if(!pdfdir.exists()){
			pdfdir.mkdir();
			//Log.d("pdfdir", "Directory Created");
		}
		pdfpath = pdfpath + "/" + filename + ".pdf";
		//Log.d("pdfpath",pdfpath);
		File pdffile = new File(pdfpath);
		if (!pdffile.exists()){
		    return true;
		}
		return false;
	}//End of pdfExists
}

