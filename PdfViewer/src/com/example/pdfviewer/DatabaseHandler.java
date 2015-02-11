package com.example.pdfviewer;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "pdfManager";
 
    // Pdf table name
    private static final String TABLE_PDFS = "pdfs";
 
    // Pdf Table Columns names
    //private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PDF_FILE = "pdf_file";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PDFS_TABLE = "CREATE TABLE " + TABLE_PDFS + "("
                + KEY_NAME + " TEXT,"
                + KEY_PDF_FILE + " BLOB" + ")";
        db.execSQL(CREATE_PDFS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PDFS);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    void addPdf(String name, byte[] buffer) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Pdf Name
        values.put(KEY_PDF_FILE, buffer); // Pdf File
 
        // Inserting Row
        db.insert(TABLE_PDFS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }
 
 
    // Getting All Contacts
    public List<String> getAllPdf_Names() {
        List<String> pdfList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  KEY_NAME FROM " + TABLE_PDFS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                
                String pdfName=cursor.getString(0);
                pdfList.add(pdfName);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return pdfList;
    }
 
    public void getPdf(String name){
    	//System.out.println(name);
    	SQLiteDatabase db = this.getReadableDatabase();
    	//Cursor cursor = db.rawQuery("select * from pdfs where name='progit en 280.pdf'",null);
    	Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_PDFS+" WHERE "+KEY_NAME+"='"+name+"'", null);
    	//System.out.println("below query");
    	if( cursor != null ){
    		cursor.moveToFirst();
    		//System.out.println("hi");
    		//System.out.println(cursor.getColumnIndex(KEY_NAME)+" "+cursor.getColumnIndex(KEY_PDF_FILE));
    		//byte[] contentByte = cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_PDF_FILE));
    		cursor.close(); 
    	
        /*	
        
    	//return contentByte;
        try{
        	String fileSavePath="/data/data/com.example.pdfviewer/";
        	fileSavePath=fileSavePath+name;
        	File saveFile=new File(fileSavePath);
        	DataOutputStream DOStream=new DataOutputStream(new FileOutputStream(saveFile));
        	DOStream.write(contentByte);
        	DOStream.flush();
        	DOStream.close();
        }catch(Exception e){
        	
        }*/
        }
    }
 
}