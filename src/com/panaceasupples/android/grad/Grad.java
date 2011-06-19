package com.panaceasupples.android.grad;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Grad extends Activity {
	GridView gridview;

	private String[] texts = { "a1", "b1", "c1", "d1", "e1", "a2", "b2", "c2",
			"d2", "e2", "a3", "b3", "c3", "d3", "e3", "a4", "b4", "c4",
			"dddddd dddddd", "e4", "a5", "b5", "c5", "d5", "e5", "a6", "b6",
			"c6", "d6", "e6", "a7", "b7", "cccccccccccccccccccccccccz", "d7",
			"e7", "a8", "b8", "c8", "d8", "e8", "a9", "b9", "c9", "d9", "e9",
			"a10", "b10", "c10", "d10", "e10", "a11", "b11", "c11", "d11",
			"e11", "a12", "b12", "c12", "d12", "e12", };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doStuff();
		// doMoreStuff();
	}

	/*
	void doMoreStuff() {
		MyGridView gridview = new MyGridView(this);
		gridview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		gridview.setColumnWidth(94);
		// gridview.setColumnWidth(95);
		gridview.setHorizontalSpacing(1); // was 10dp
		gridview.setVerticalSpacing(1); // was 10 dp
		gridview.setGravity(Gravity.CENTER); // android:gravity="center"
		gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gridview.setNumColumns(-1);
		gridview.setAdapter(new TextAdapter(this));
		gridview.setBackgroundResource(R.color.dgrey); // grey background
		MyView myview = new MyView(this);

		// gridview.setOnKeyListener(new MyView.OnKeyListener());

		setContentView(gridview);

	}
	*/


	void doStuff() {
		// LinearLayout ll = new LinearLayout(this);
		// ll.setOrientation(LinearLayout.VERTICAL);

		// GridView gridview = new GridView(this);
		gridview = new GridView(this);
		gridview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		gridview.setColumnWidth(94);
		// gridview.setColumnWidth(95);
		gridview.setHorizontalSpacing(1); // was 10dp
		gridview.setVerticalSpacing(1); // was 10 dp
		gridview.setGravity(Gravity.CENTER); // android:gravity="center"
		gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gridview.setNumColumns(-1);
		gridview.setAdapter(new TextAdapter(this));

		// standard cgrey
		// better cursor darker - dgrey

		// gridview.setBackgroundResource(R.color.cgrey); // grey background
		gridview.setBackgroundResource(R.color.dgrey); // grey background

		// gridview.setDrawSelectorOnTop(true); // hides cell
		// gridview.setOnKeyListener(new MyView.OnKeyListener());

		// int cip;
		// int cip = gridview.getSelectedItemPosition();

		gridview.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int i, KeyEvent k) {
				// Log.d("gradkey",i + " " + k.toString());
				// int h = v.getHeight();
				// int down = v.getNextFocusDownId();
				// int right = v.getNextFocusRightId();
				int action = k.getAction();

				// Log.d("gradkey",i + " " + h + " " + down + " " + right + " "
				// + action);

				if (i == KeyEvent.KEYCODE_DPAD_DOWN) {
					int cip = gridview.getSelectedItemPosition();
					// Log.d("gradkey", "dpad-down " + cip);
					if (cip < 55)
						if (action == 0)
							gridview.setSelection(cip + 5);
				} else if (i == KeyEvent.KEYCODE_DPAD_UP) {
					int cip = gridview.getSelectedItemPosition();
					// Log.d("gradkey", "dpad-down " + cip);
					if (cip > 4)
						if (action == 0)
							gridview.setSelection(cip - 5);
				}

				else if (i == KeyEvent.KEYCODE_DPAD_RIGHT) {
					int cip = gridview.getSelectedItemPosition();
					// Log.d("gradkey", "dpad-right " + cip);

					if ((cip + 1) % 5 != 0 || cip == 0)
						if (action == 0)
							gridview.setSelection(cip + 1);
				} else if (i == KeyEvent.KEYCODE_DPAD_LEFT) {
					int cip = gridview.getSelectedItemPosition();
					// Log.d("gradkey", "dpad-left " + cip);

					if ((cip) % 5 != 0)
						if (action == 0)
							gridview.setSelection(cip - 1);
				}

				return true;
			}
		});

		// gridview.setDuplicateParentStateEnabled(true);

		// LinearLayout.LayoutParams layoutParams = new
		// LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.FILL_PARENT, 75);

		// TextView empty = new TextView(this);
		// ll.setBackgroundResource(R.color.dgrey); // grey background

		// ll.addView(empty, layoutParams);

		// ll.addView(gridview);

		setContentView(gridview);
		// setContentView(ll);
	}

	public class TextAdapter extends BaseAdapter {

		private Context context;

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

				tv.setLayoutParams(new GridView.LayoutParams(95, 53));
			} else {
				tv = (TextView) convertView;
			}
			// standard dwhite
			// better cursor darker cwhite
			// tv.setBackgroundResource(R.color.dwhite); // cells are white
			tv.setBackgroundResource(R.color.cwhite); // cells are white

			tv.setTextColor(Color.rgb(0, 0, 0)); // turns it black

			tv.setText(texts[position]);

/*
			tv.setDuplicateParentStateEnabled(true);
			final int pos = position;


			tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				public void onFocusChange(View v, boolean b) {
					String t;
					if (b)
						t = "true";
					else
						t = "false";
					Log.d("grid", texts[pos] + " " + pos + " " + b);
				}
			});
*/
			return tv;
		}

	}

}