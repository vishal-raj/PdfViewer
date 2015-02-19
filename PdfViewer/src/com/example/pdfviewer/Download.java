/*
 * Author Vishal Raj
 * Intern@Groupten
 */
package com.example.pdfviewer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class Download {
	
	private URL url;
	private URLConnection ucon;
	private InputStream is;
	private BufferedInputStream bis;
	private ByteArrayBuffer baf;
	private FileOutputStream fos;
	
	public void download(String filename, String filekey, String dirpath){
		
		dirpath = dirpath + "/pdf";
		//Log.d("path", dirpath);
		File pdfDir = new File(dirpath);
		if (!pdfDir.exists()){
			pdfDir.mkdirs();
			Log.d("Message","Directory Created");
		}
		
		try{
			filename = filename.replace(":", "");
			String storePath = dirpath + "/"+filename+".pdf";
			//Log.d("path", storePath);
			File pdfFile = new File(storePath);
			if(!pdfFile.exists()){
				//File sPath = new File(storePath);
				url = new URL ("http://www.usgbc.org/uberdownload.php?key=" + filekey); 
				ucon = url.openConnection();
				
				//Define InputStreams to read from the URLConnection.
				is = ucon.getInputStream();
				bis = new BufferedInputStream(is);

				//Read bytes to the Buffer until there is nothing more to read(-1).
				baf = new ByteArrayBuffer(1024);
				int current = 0;
				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}

				/* Convert the Bytes read to a String. */
				fos = new FileOutputStream(storePath);
				fos.write(baf.toByteArray());
				fos.close();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
