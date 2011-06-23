package com.panaceasupples.android.grad;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Grad extends Activity {
	GridView gridview;
	TextView select;
	int bigpos;

	int SCREEN_COLUMNS;
	int SCREEN_ROWS;
	int ROW_MARKER_WIDTH;

	final int AD_HEIGHT = 75;

	final int CELL_HEIGHT = 51;
	int CELL_WIDTH;
	final int COLUMN_MARKER_HEIGHT = 25;

	private String[] texts = { "a1", "b1", "c1", "d1", "e1", "a2", "b2", "c2",
			"d2", "e2", "a3", "b3", "c3", "d3", "e3", "a4", "b4", "c4",
			"dddddd dddddd", "e4", "a5", "b5", "c5", "d5", "e5", "a6", "b6",
			"c6", "d6", "e6", "a7", "b7", "ccccc cccz", "d7", "e7", "a8", "b8",
			"c8", "d8", "e8", "a9", "b9", "c9", "d9", "e9", "a10", "b10",
			"c10", "d10", "e10" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doStuff();
	}

	void doStuff() {

		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(dm);

		int mdpiHeightMin = 620;

		if (dm.heightPixels < dm.widthPixels) { // landscape
			if (dm.heightPixels <= mdpiHeightMin) {
				ROW_MARKER_WIDTH = 34;
				SCREEN_ROWS = 6;
				SCREEN_COLUMNS = 6;
			} else {
				ROW_MARKER_WIDTH = 38;
				SCREEN_COLUMNS = 8;
				SCREEN_ROWS = 8;
			}
		} else { // portrait
			if (dm.heightPixels <= mdpiHeightMin) {
				SCREEN_ROWS = 12;
				ROW_MARKER_WIDTH = 38;
				SCREEN_COLUMNS = 4;

			} else {
				SCREEN_ROWS = 18;
				ROW_MARKER_WIDTH = 38;
				SCREEN_COLUMNS = 5;
			}
		}

		int tempWidth = dm.widthPixels;
		tempWidth = tempWidth - ROW_MARKER_WIDTH;
		tempWidth = tempWidth - (SCREEN_COLUMNS - 1);
		CELL_WIDTH = tempWidth / SCREEN_COLUMNS;

		gridview = new GridView(this);
		gridview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		gridview.setColumnWidth(CELL_WIDTH);
		gridview.setHorizontalSpacing(1);
		gridview.setVerticalSpacing(1);
		gridview.setGravity(Gravity.CENTER);
		gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gridview.setNumColumns(-1);
		gridview.setAdapter(new TextAdapter(this));
		gridview.setBackgroundResource(R.color.dgrey);
		gridview.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int i, KeyEvent k) {
				int action = k.getAction();
				// action 0 - off position
				// action 1 - on position
				int pos = gridview.getSelectedItemPosition();

				if (pos < 0) {
					pos = bigpos;
				}

				int ac = 0; // off (1 is on)

				if (i == KeyEvent.KEYCODE_DPAD_DOWN) {
					// Log.d("gradkey", "dpad-down " + pos);
					if (pos < texts.length - SCREEN_COLUMNS)
						if (action == ac) {
							bigpos = pos + SCREEN_COLUMNS;
							gridview.setSelection(bigpos);
							select.setText(texts[bigpos]);
						}
				} else if (i == KeyEvent.KEYCODE_DPAD_UP) {
					// Log.d("gradkey", "dpad-down " + pos);
					if (pos >= SCREEN_COLUMNS)
						if (action == ac) {
							bigpos = pos - SCREEN_COLUMNS;
							gridview.setSelection(bigpos);
							select.setText(texts[bigpos]);
						}
				} else if (i == KeyEvent.KEYCODE_DPAD_RIGHT) {
					// Log.d("gradkey", "dpad-right " + pos);
					if ((pos + 1) % SCREEN_COLUMNS != 0 || pos == 0)
						if (action == ac) {
							bigpos = pos + 1;
							gridview.setSelection(bigpos);
							select.setText(texts[bigpos]);
						}
				} else if (i == KeyEvent.KEYCODE_DPAD_LEFT) {
					// Log.d("gradkey", "dpad-left " + pos);
					if ((pos) % SCREEN_COLUMNS != 0)
						if (action == ac) {
							bigpos = pos - 1;
							gridview.setSelection(bigpos);
							select.setText(texts[bigpos]);
						}
				}

				return true;
			}
		});

		LinearLayout ll = new LinearLayout(this);

		LinearLayout columnMarkerLinearLayout = new LinearLayout(this);

		LinearLayout bottomll = new LinearLayout(this);

		LinearLayout.LayoutParams cmLayoutParams = new LinearLayout.LayoutParams(
				CELL_WIDTH + 1, COLUMN_MARKER_HEIGHT);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, AD_HEIGHT);

		LinearLayout.LayoutParams selectLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, CELL_HEIGHT);

		LinearLayout.LayoutParams columnMarkerLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, COLUMN_MARKER_HEIGHT);

		LinearLayout.LayoutParams rowMarkerLayoutParams = new LinearLayout.LayoutParams(
				ROW_MARKER_WIDTH, LinearLayout.LayoutParams.FILL_PARENT);

		LinearLayout.LayoutParams midMarkerParams = new LinearLayout.LayoutParams(
				ROW_MARKER_WIDTH, COLUMN_MARKER_HEIGHT);

		bottomll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		TextView empty = new TextView(this);
		TextView emptyTwo = new TextView(this);
		TextView cMarker[] = new TextView[SCREEN_COLUMNS];
		TextView rMarker[] = new TextView[SCREEN_ROWS];

		columnMarkerLinearLayout.addView(empty, midMarkerParams);

		for (int i = 0; i < SCREEN_COLUMNS; i++) {
			cMarker[i] = new TextView(this);
			if (i == 0)
				cMarker[i].setText("A");
			else if (i == 1)
				cMarker[i].setText("B");
			else if (i == 2)
				cMarker[i].setText("C");
			else if (i == 3)
				cMarker[i].setText("D");
			else if (i == 4)
				cMarker[i].setText("E");
			else if (i == 5)
				cMarker[i].setText("F");
			else if (i == 6)
				cMarker[i].setText("G");
			else if (i == 7)
				cMarker[i].setText("H");

			cMarker[i].setTextColor(Color.rgb(0, 0, 0)); // turns it black
			cMarker[i].setGravity(Gravity.CENTER_HORIZONTAL);
			columnMarkerLinearLayout.addView(cMarker[i], cmLayoutParams);
			Log.d("another", "column header");
		}

		for (int j = 0; j < SCREEN_ROWS; j++) {
			rMarker[j] = new TextView(this);
			rMarker[j].setText(Integer.toString(j + 1));
			// rMarker[j].setText(Integer.toString(j+101));
			rMarker[j].setTextColor(Color.rgb(0, 0, 0)); // turns it black
			rMarker[j].setGravity(Gravity.CENTER);
			// rll.addView(rMarker[j], rllLP);
		}

		TextView menu = new TextView(this);
		// TextView columnHeader = new TextView(this);
		TextView rowMarker = new TextView(this);
		select = new TextView(this);

		menu.setBackgroundResource(R.color.ltblue);
		// columnHeader.setBackgroundResource(R.color.thgrey);
		rowMarker.setBackgroundResource(R.color.thgrey);
		select.setBackgroundResource(R.color.white);
		ll.setBackgroundResource(R.color.dgrey);
		columnMarkerLinearLayout.setBackgroundResource(R.color.thgrey);

		// columnHeader.setTextColor(Color.rgb(0, 0, 0));
		rowMarker.setTextColor(Color.rgb(0, 0, 0));
		select.setTextColor(Color.rgb(0, 0, 0));

		ll.setOrientation(LinearLayout.VERTICAL);

		bottomll.addView(rowMarker, rowMarkerLayoutParams);
		bottomll.addView(gridview);

		ll.addView(emptyTwo, layoutParams);
		ll.addView(menu, selectLayoutParams);
		ll.addView(select, selectLayoutParams);
		// ll.addView(columnHeader, columnHeaderLayoutParams);
		ll.addView(columnMarkerLinearLayout, columnMarkerLayoutParams);

		ll.addView(bottomll);

		setContentView(ll);
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
			final TextView tv;

			if (convertView == null) {
				tv = new TextView(context);
				tv.setLayoutParams(new GridView.LayoutParams(CELL_WIDTH,
						CELL_HEIGHT));
			} else {
				tv = (TextView) convertView;
			}

			tv.setBackgroundResource(R.color.cwhite);

			tv.setTextColor(Color.rgb(0, 0, 0));

			tv.setText(texts[position]);
			final int pos = position;

			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					bigpos = pos;
					tv.setBackgroundResource(R.drawable.click);
					select.setText(texts[pos]);
				}

			});

			return tv;
		}

	}

}