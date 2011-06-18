package com.panaceasupples.android.grad;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Grad extends Activity {
	/** Called when the activity is first created. */
	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doStuff();
	}

	void doStuff() {

		
		GridView gridview = new GridView(this);
		gridview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		gridview.setColumnWidth(94); // was 90dp 
		gridview.setHorizontalSpacing(1); // was 10dp
		gridview.setVerticalSpacing(1); // was 10 dp
		gridview.setGravity(Gravity.CENTER); // android:gravity="center"
		gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gridview.setNumColumns(-1);
		gridview.setAdapter(new TextAdapter(this));
//		float f=(float)0.1;
//		gridview.setAlpha(f);
		//gridview.setBackgroundColor(R.color.grey); // grey background
		//gridview.setBackgroundResource(R.color.grey); // grey background
		// ^^ no effect on text color
		//gridview.setDrawSelectorOnTop(true);
		

		setContentView(gridview);
	}

	
	public class TextAdapter extends BaseAdapter {

		private Context context;
		private String[] texts = { "aaa", "bbb", "ccc", "ddd", "eee", "fff",
				"gg", "hhh", "iii", "j", "k", "l", "m", "n", "o", "p", "q",
				"r", "s", "t", "u", "v", "w", "x", "y", "z", "", "", "", "",
				"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
				"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
				"x", "", "", "z" };

		public TextAdapter(Context context) {
			this.context = context;
		}

		public int getCount() {
			return texts.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv;

			if (convertView == null) {
				tv = new TextView(context);

				tv.setLayoutParams(new GridView.LayoutParams(95, 54));
			} else {
				tv = (TextView) convertView;
			}

			
			//tv.setBackgroundResource(R.color.white); // cells are white
			tv.setBackgroundResource(R.color.hwhite); // cells are white

//			tv.setBackgroundResource(Color.rgb(254,254,254)); // cells are white
			
			
			//tv.setBackgroundResource(R.drawable.text_color); // cells are white

	//		tv.setBackgroundColor(R.color.white); // cells are white
			//tv.setBackgroundDrawable(R.drawable.thecolor); // cells are white
			
			 //tv.setTextColor(Color.rgb(0,0,0)); // turns it black, but then no shift
			 //tv.setTextColor(Color.rgb(254,254,254)); // turns it white

			tv.setText(texts[position]);
			

			
			
			
			
			return tv;
		}

	}

}