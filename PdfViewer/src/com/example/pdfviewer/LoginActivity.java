/*
 * Author Vishal Raj
 * Intern@Groupten
 */
package com.example.pdfviewer;

import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class LoginActivity extends Activity {
	private EditText editUser;
	private EditText editPass;
	private Button loginButton;
	HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    HttpEntity httpEntity;
    InputStream is = null;
    List<NameValuePair> nameValuePairs;
	private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Object of User_name, Password and Sign in button
		editUser = (EditText)findViewById(R.id.editEmail);
		editPass = (EditText)findViewById(R.id.editPass);
		final TextView loginMsg = (TextView)findViewById(R.id.UserNotFound);
		loginButton = (Button)findViewById(R.id.btnSingIn);
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
                	//Check for Internet Connection
                	if(isNetworkAvailable()){
                	//Start User validation using AsyncTask class ValidateUser 
                	ValidateUser task = new ValidateUser();
                	//Passing username and password for user validation
                	task.execute(new String[]{user_name, user_pass});
                	}
                	else{
                		dialog.dismiss();
                		loginMsg.setText("No Internet Connection");
                		loginMsg.setTextColor(Color.RED);
						loginMsg.setGravity(Gravity.CENTER); 
						editUser.requestFocus();
                	}
                }
            }
        });
	}//close onCreate
	
	private class ValidateUser extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			
			Login lobj = new Login();
			String jsonres = lobj.validate(params[0], params[1]);
			
			return jsonres;
			
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
							//System.out.println("token"+token);
						}catch(JSONException e){
							e.printStackTrace();
						}
						if(token!=null){
							//navigate to PdfHome
							String user_id=editUser.getText().toString();
							Intent i = new Intent(LoginActivity.this, HomeActivity.class);
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
				
				
		   }//close onPostExecute
		    
	}// close validateUser
		

	//Function to check Internet Connection	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}//end of isNetworkAvailable
	
	
	
}//close LoginActivity
