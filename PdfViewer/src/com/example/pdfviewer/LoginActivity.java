package com.example.pdfviewer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	TextView tvForgotPass;
	TextView tvNeedAccount;
	EditText editUser;
	EditText editPass;
	Button loginButton;
	HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    HttpEntity httpEntity;
    InputStream is = null;
    List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Object of User_name, Password and Sign in button
		editUser = (EditText)findViewById(R.id.editEmail);
		editPass = (EditText)findViewById(R.id.editPass);
		tvForgotPass = (TextView)findViewById(R.id.forgotPassword);
		tvNeedAccount = (TextView)findViewById(R.id.needAccount);
		//login button color #4EA2B8
		loginButton = (Button)findViewById(R.id.btnSingIn);
		
		//Start forgot password Activity
		tvForgotPass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), ForgotPassword.class);
				startActivity(i);
				
			}
		});
		
		//Start need account Activity
		tvNeedAccount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent i = new Intent(getBaseContext(), NeedAccount.class);
				//startActivity(i);
				Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("http://usgbc.name/registration/create-user​"));
		        startActivity(intent);
			}
		});
		
		
		
		//php_res =(TextView)findViewById(R.id.php_res) ;
		loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	//validate dialog box
                dialog = ProgressDialog.show(LoginActivity.this, "", 
                        "Validating User...", true);
                String user_name=editUser.getText().toString().trim();
                String user_pass=editPass.getText().toString().trim();
                
                //validation for empty EditText
                if(TextUtils.isEmpty(user_name)) {
                	dialog.dismiss();
                    editUser.setError("Can't be blank");
                    return;
                 }
                else if(TextUtils.isEmpty(user_pass)) {
                	dialog.dismiss();
                    editPass.setError("Can't be blank");
                    return;
                 }
                else{
                	validateUser task = new validateUser();
                	task.execute(new String[]{user_name, user_pass});
                }
            }
        });
	}//close onCreate
	
	private class validateUser extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//timeout in milliseconds
			int timeout= 30000;
			//boolean flag=false;
			httpclient=new DefaultHttpClient();
			httpclient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeout);
			//PHP to validate the user
			httppost= new HttpPost("http://identity.usgbc.org:80/Api/v1/authenticate.json");
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("username", params[0] ));
            postParameters.add(new BasicNameValuePair("password", params[1] ));
            String jsonRes = null;
            try {
            		httppost.setEntity(new UrlEncodedFormEntity(postParameters));
            		//Sending POST data to server for validation and get the response
            		response = httpclient.execute(httppost);
            		//Converting response to human readable format i-e string :)
            		/*ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String responses = httpclient.execute(httppost, responseHandler);
            		jsonRes=responses.toString();*/
            		httpEntity = response.getEntity();
                    is = httpEntity.getContent();
            		
            		
            		Log.d("vishu",jsonRes);
            		//res= res.replaceAll("\\s+","");
            }catch (Exception e) {
            	//Toast.makeText(LoginActivity.this,"Error occured during validation of user", Toast.LENGTH_SHORT).show();
            }
            
            try {
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
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            
            //Return the response.
            System.out.println(jsonRes);
            return jsonRes;
		}//close doInBackground
		
		@Override
		   protected void onPostExecute(String result) {
				//Stop the validation dialog
				TextView loginMsg = (TextView)findViewById(R.id.UserNotFound);
				dialog.dismiss();
				try {
					if(result != null){
						JSONObject jsonObj = new JSONObject(result);
						String token = null;
						try{
							token=jsonObj.getString("token");
							System.out.println("token"+token);
						}catch(JSONException e){
							e.printStackTrace();
						}
						if(token!=null){
							//navigate to PdfHome
							String user_id=editUser.getText().toString();
							Intent i = new Intent(LoginActivity.this, PdfHome.class);
							//Sending user_id and token to Home activity
							Bundle extras = new Bundle();
							extras.putString("user_id", user_id);
							extras.putString("token", token);
							i.putExtras(extras);
							startActivity(i);
							
						}//End of if block
						else{
							JSONObject jsonObj1 = new JSONObject(result);
							String msg=jsonObj1.getString("errorMessage");
							loginMsg.setText(msg);
							loginMsg.setTextColor(Color.RED);
							loginMsg.setGravity(Gravity.CENTER); 
							editUser.requestFocus();
						}//End of else block
					}//End of if block
					else{
						Log.d("vishu","empty result");
					}
						//}
					
					//}//System.out.println(jsonObj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				/*
				//If response is "User Found" then start the PdfHome activity 
				//and also pass the user_id(for fetching the list of pdf file 
				//purchased by the user.
				if(result.equals("User Found")){
					//navigate to PdfHome
					String user_id=editUser.getText().toString();
					Intent i = new Intent(LoginActivity.this, PdfHome.class);
					//Sending user_id to PdfHome activity
					i.putExtra("user_id", user_id);
					startActivity(i);
				}
				else{
					//If response is other than "User Found" then show the message 
					//"IIncorrect User Name or Password" 
					TextView loginMsg = (TextView)findViewById(R.id.UserNotFound);
					loginMsg.setText("Incorrect User Name or Password");
					loginMsg.setTextColor(Color.RED);
					loginMsg.setGravity(Gravity.CENTER); 
					editUser.requestFocus();
					//editUser.setSelectAllOnFocus(true);
				}   */
		   }//close onPostExecute
		    
	}// close validateUser
		

		

	
	
	
}//close LoginActivity
