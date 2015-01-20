package com.example.pdfviewer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity {
	EditText editUser;
	EditText editPass;
	Button loginButton;
	//TextView php_res;
	HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Object of User_name, Password and Sign in button
		
		editUser = (EditText)findViewById(R.id.editEmail);
		editPass = (EditText)findViewById(R.id.editPass);
		loginButton = (Button)findViewById(R.id.btnSingIn);
		//php_res =(TextView)findViewById(R.id.php_res) ;
		loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	//validate dialog box
                dialog = ProgressDialog.show(MainActivity.this, "", 
                        "Validating user...", true);
                 new Thread(new Runnable() {
                        public void run() {
                            login();                          
                        }
                      }).start();                
            }
        });
		
		
	}

	void login(){
        try{            
              
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://10.0.3.2/pdfviewer/check.php"); //  url of check.php.
            
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Sending POST data to PHP script to validate
            nameValuePairs.add(new BasicNameValuePair("username",editUser.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("password",editPass.getText().toString().trim())); 
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);
            
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response); 
            runOnUiThread(new Runnable() {
                public void run() {
                    //php_res.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });
             
            if(response.equalsIgnoreCase("User Found")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this,"Login Success", Toast.LENGTH_SHORT).show();
                    }
                });
                 
                startActivity(new Intent(MainActivity.this, PdfHome.class));
                
            }else{
                showAlert();                
            }
             
        }catch(Exception e){
            dialog.dismiss();
            //System.out.println("Exception : " + e.getMessage());
        }
    }
	//Show alert dialog if User not found
    public void showAlert(){
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")  
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
