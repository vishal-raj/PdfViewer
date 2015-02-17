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
	private String[] allColumns = { PdfDbHelper.KEY_NAME,
	      PdfDbHelper.KEY_FILE_KEY };

	public PdfDetailsSource(String user_id, Context context) {
	    dbHelper = new PdfDbHelper(user_id, context);
	}

	public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	}

	public void close() {
	    dbHelper.close();
	}

	public void createDetails(String filename, String imageurl) {
	    ContentValues values = new ContentValues();
	    values.put(PdfDbHelper.KEY_NAME, filename);
	    values.put(PdfDbHelper.KEY_FILE_KEY, imageurl);
	    database.insert(PdfDbHelper.TABLE_PDFS, null,
	    				values);
	    
	}


	  public List<HashMap<String, String>> getPdfDetails() {
		  List<HashMap<String, String>> templist = new ArrayList<HashMap<String,String>>();
		  Cursor cursor = database.query(PdfDbHelper.TABLE_PDFS,
	    				allColumns, null, null, null, null, null);
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
