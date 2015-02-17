/*
 * Author Vishal Raj
 * Intern@Groupten
 */
package com.example.pdfviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PdfDetailsSource {
	
	private SQLiteDatabase database;
	private PdfDbHelper dbHelper;
	private String[] allColumns = { PdfDbHelper.KEY_USER, PdfDbHelper.KEY_NAME,
	      PdfDbHelper.KEY_FILE_KEY };

	public PdfDetailsSource(Context context) {
	    dbHelper = new PdfDbHelper(context);
	}

	public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	}

	public void close() {
	    dbHelper.close();
	}

	public void createDetails(String user, String filename, String imageurl) {
	    ContentValues values = new ContentValues();
	    values.put(PdfDbHelper.KEY_USER, user);
	    values.put(PdfDbHelper.KEY_NAME, filename);
	    values.put(PdfDbHelper.KEY_FILE_KEY, imageurl);
	    database.insert(PdfDbHelper.TABLE_PDFS, null,
	    				values);
	    
	}


	  public List<HashMap<String, String>> getPdfDetails(String user) {
		  List<HashMap<String, String>> templist = new ArrayList<HashMap<String,String>>();
		  /*Cursor cursor = database.query(PdfDbHelper.TABLE_PDFS,
	    				PdfDbHelper.KEY_NAME, PdfDbHelper.KEY_FILE_KEY, PdfDbHelper.KEY_NAME=user, null, null, null, null);*/
		  Cursor cursor = database.rawQuery("SELECT pdfname, imagepath FROM pdfs WHERE userid='" + user +"'", null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
	      HashMap<String, String> hm = new HashMap<String,String>();
  		  hm.put("pdf_name", cursor.getString(0));            
  		  hm.put("pdf_icon", cursor.getString(1));
  		  templist.add(hm);
	      cursor.moveToNext();
	  }
	    // make sure to close the cursor
	    cursor.close();
	    System.out.println(templist.toString());
	    return templist;
	  }
	
}
