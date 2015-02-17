package com.example.pdfviewer;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class PdfDbHelper extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "pdfManager";
 
    // PDF table name
    public static String TABLE_PDFS = "";
 
    // PDF Table Columns names
    //private static final String KEY_ID = "id";
    public static final String KEY_NAME = "pdfname";
    public static final String KEY_FILE_KEY = "filekey";
 
    public PdfDbHelper(String user_id, Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_PDFS = user_id;
        //3rd argument to be passed is CursorFactory instance
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PDFS_TABLE = "CREATE TABLE " + TABLE_PDFS + "("
                + KEY_NAME + " TEXT,"
                + KEY_FILE_KEY + " TEXT" + ")";
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
 
 
           
}
 
