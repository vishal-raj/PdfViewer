package com.example.pdfviewer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class PdfFromLocal{
	
	public List<String> getPath(String dirpath){
		
		List<String> pdfpaths = new ArrayList<String>();
		File pdfDir = new File(dirpath);
		File[] files = pdfDir.listFiles();
		for (int i = 0; i < files.length; ++i) {
		    pdfpaths.add(files[i].getAbsolutePath());
		}
		Log.d("path",pdfpaths.toString());
		return pdfpaths;
		
	}
}
