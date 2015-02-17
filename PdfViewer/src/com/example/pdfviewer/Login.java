/*
 * Author Vishal Raj
 * Intern@Groupten
 */
package com.example.pdfviewer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

public class Login {
	
	private HttpPost httppost;
	private HttpResponse response;
	private HttpClient httpclient;
	private HttpEntity httpEntity;
	private InputStream is = null;
	private int timeout= 30000;
	
	public String validate(String username, String password ){
		
		httpclient=new DefaultHttpClient();
		httpclient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeout);
		//URL to validate the user
		httppost= new HttpPost("http://identity.usgbc.org:80/Api/v1/authenticate.json");
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("username", username ));
		postParameters.add(new BasicNameValuePair("password", password ));

		String jsonRes = null;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(postParameters));
			//Sending POST data to server for validation and get the response
			response = httpclient.execute(httppost);
			//Converting response to human readable format i-e string :)
			httpEntity = response.getEntity();
			is = httpEntity.getContent();
			//res= res.replaceAll("\\s+","");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			jsonRes = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		//Return the response.
		System.out.println(jsonRes);
		return jsonRes;
	
		
	}//End of validate

	
}
