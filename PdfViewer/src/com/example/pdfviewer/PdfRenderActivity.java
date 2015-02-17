/*
 * Author Vishal Raj
 * Intern@Groupten
 */
package com.example.pdfviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PdfRenderActivity extends Activity {
	private TextView tvPdfpath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdf_render);
		Intent i = getIntent();
		tvPdfpath = (TextView)findViewById(R.id.pdfpath);
		tvPdfpath.setText(i.getStringExtra("pdf_path"));
	}
	
}
