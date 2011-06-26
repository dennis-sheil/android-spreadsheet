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
	int CELL_WIDTH;
	int CELL_HEIGHT;
	// final int CELL_HEIGHT = 51;

	final int AD_HEIGHT = 75;
	final int COLUMN_MARKER_HEIGHT = 25;

	private String[] cellValue;

	private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
	private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
	private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;

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

		int statusBarHeight;

		switch (dm.densityDpi) {
		case DisplayMetrics.DENSITY_HIGH:
			statusBarHeight = HIGH_DPI_STATUS_BAR_HEIGHT;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
			break;
		case DisplayMetrics.DENSITY_LOW:
			statusBarHeight = LOW_DPI_STATUS_BAR_HEIGHT;
			break;
		default:
			statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
		}

		int mdpiHeightMin = 620;

		CELL_HEIGHT = 51;

		if (dm.heightPixels < dm.widthPixels) { // landscape
			if (dm.heightPixels <= mdpiHeightMin) {
				ROW_MARKER_WIDTH = 34;
				SCREEN_COLUMNS = 6;
				SCREEN_ROWS = 6;
			} else {
				ROW_MARKER_WIDTH = 38;
				SCREEN_COLUMNS = 8;
				SCREEN_ROWS = 8;
			}
		} else { // portrait
			if (dm.heightPixels <= mdpiHeightMin) {
				ROW_MARKER_WIDTH = 38;
				SCREEN_COLUMNS = 4;
				SCREEN_ROWS = 12;
			} else {
				ROW_MARKER_WIDTH = 38;
				SCREEN_COLUMNS = 5;
				SCREEN_ROWS = 18;
			}
		}

		int tempHeight = dm.heightPixels;
		// tempHeight = tempHeight - SELECT_HEIGHT;
		tempHeight = tempHeight - AD_HEIGHT;
		tempHeight = tempHeight - COLUMN_MARKER_HEIGHT;
		tempHeight = tempHeight - (statusBarHeight * 2);
		tempHeight = tempHeight - (SCREEN_ROWS - 1);
		CELL_HEIGHT = tempHeight / (SCREEN_ROWS + 2); // plus select and menu
		// Log.d("cell height", Integer.toString(CELL_HEIGHT));
		tempHeight = tempHeight - (CELL_HEIGHT * (SCREEN_ROWS + 2));

		// Log.d("precell height", Integer.toString(tempHeight));
		// int EXTRA_HEIGHT = (tempHeight % SCREEN_ROWS);
		int EXTRA_HEIGHT = tempHeight;
		Log.d("extraa", Integer.toString(EXTRA_HEIGHT));

		// int SELECT_HEIGHT = CELL_HEIGHT + EXTRA_HEIGHT;
		int SELECT_HEIGHT = CELL_HEIGHT + EXTRA_HEIGHT;

		cellValue = new String[SCREEN_COLUMNS * SCREEN_ROWS];

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
					if (pos < cellValue.length - SCREEN_COLUMNS)
						if (action == ac) {
							bigpos = pos + SCREEN_COLUMNS;
							gridview.setSelection(bigpos);
							select.setText(cellValue[bigpos]);
						}
				} else if (i == KeyEvent.KEYCODE_DPAD_UP) {
					// Log.d("gradkey", "dpad-down " + pos);
					if (pos >= SCREEN_COLUMNS)
						if (action == ac) {
							bigpos = pos - SCREEN_COLUMNS;
							gridview.setSelection(bigpos);
							select.setText(cellValue[bigpos]);
						}
				} else if (i == KeyEvent.KEYCODE_DPAD_RIGHT) {
					// Log.d("gradkey", "dpad-right " + pos);
					if ((pos + 1) % SCREEN_COLUMNS != 0 || pos == 0)
						if (action == ac) {
							bigpos = pos + 1;
							gridview.setSelection(bigpos);
							select.setText(cellValue[bigpos]);
						}
				} else if (i == KeyEvent.KEYCODE_DPAD_LEFT) {
					// Log.d("gradkey", "dpad-left " + pos);
					if ((pos) % SCREEN_COLUMNS != 0)
						if (action == ac) {
							bigpos = pos - 1;
							gridview.setSelection(bigpos);
							select.setText(cellValue[bigpos]);
						}
				}

				return true;
			}
		});

		LinearLayout ll = new LinearLayout(this);
		LinearLayout columnMarkerLinearLayout = new LinearLayout(this);
		LinearLayout bottomll = new LinearLayout(this);
		LinearLayout rll = new LinearLayout(this);

		LinearLayout.LayoutParams cmLayoutParams = new LinearLayout.LayoutParams(
				CELL_WIDTH + 1, COLUMN_MARKER_HEIGHT);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, AD_HEIGHT);

		LinearLayout.LayoutParams selectLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, SELECT_HEIGHT);

		LinearLayout.LayoutParams menuLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, CELL_HEIGHT);

		LinearLayout.LayoutParams columnMarkerLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, COLUMN_MARKER_HEIGHT);

		LinearLayout.LayoutParams rowMarkerLayoutParams = new LinearLayout.LayoutParams(
				ROW_MARKER_WIDTH, LinearLayout.LayoutParams.FILL_PARENT);

		LinearLayout.LayoutParams rMarkerLayoutParams = new LinearLayout.LayoutParams(
				ROW_MARKER_WIDTH, CELL_HEIGHT + 1);

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
			rll.addView(rMarker[j], rMarkerLayoutParams);
		}

		TextView menu = new TextView(this);
		TextView rowMarker = new TextView(this);
		select = new TextView(this);

		menu.setBackgroundResource(R.color.ltblue);
		rowMarker.setBackgroundResource(R.color.thgrey);
		rll.setBackgroundResource(R.color.thgrey);
		select.setBackgroundResource(R.color.white);
		ll.setBackgroundResource(R.color.dgrey);
		columnMarkerLinearLayout.setBackgroundResource(R.color.thgrey);

		// columnHeader.setTextColor(Color.rgb(0, 0, 0));
		rowMarker.setTextColor(Color.rgb(0, 0, 0));
		select.setTextColor(Color.rgb(0, 0, 0));

		rll.setOrientation(LinearLayout.VERTICAL);
		ll.setOrientation(LinearLayout.VERTICAL);

		bottomll.addView(rll, rowMarkerLayoutParams);
		bottomll.addView(gridview);

		ll.addView(emptyTwo, layoutParams);
		ll.addView(menu, menuLayoutParams);
		ll.addView(select, selectLayoutParams);
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
			return cellValue.length;
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

			tv.setText(cellValue[position]);
			final int pos = position;

			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					bigpos = pos;
					tv.setBackgroundResource(R.drawable.click);
					select.setText(cellValue[pos]);
				}

			});

			return tv;
		}

	}

}