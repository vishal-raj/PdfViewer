package com.example.pdfviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapterDevice extends BaseAdapter{
	  private Context mContext;
	  private final String[] web;
	  private final String[] Imageid; 

	    public ImageAdapterDevice(Context c,String[] web,String[] Imageid ) {
	        mContext = c;
	        this.Imageid = Imageid;
	        this.web = web;
	    }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return web.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View grid;
			LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
	        if (convertView == null) {  
	        	
	        	grid = new View(mContext);
				grid = inflater.inflate(R.layout.device_grid_item, null);
	        	TextView textView = (TextView) grid.findViewById(R.id.pdf_name_device);
	        	ImageView imageView = (ImageView)grid.findViewById(R.id.pdf_icon_device);
	        	textView.setText(web[position]);
	        	Bitmap myBitmap = BitmapFactory.decodeFile(Imageid[position]);
	        	imageView.setImageBitmap(myBitmap);
	        } else {
	        	grid = (View) convertView;
	        }
			
			return grid;
		}
}
