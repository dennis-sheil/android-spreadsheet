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
	int CELL_WIDTH;
	int CELL_HEIGHT;
	int SELECT_HEIGHT;

	final int AD_HEIGHT = 75;
	final int ROW_MARKER_WIDTH = 38;
	final int COLUMN_MARKER_HEIGHT = 25;

	private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
	private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
	private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;

	private String[] cellValue; /*
								 * = { "a1", "b1", "c1", "d1", "e1", "a2", "b2",
								 * "c2", "d2", "e2", "a3", "b3", "c3", "d3",
								 * "e3", "a4", "b4", "c4", "dddddd dddddd",
								 * "e4", "a5", "b5", "c5", "d5", "e5", "a6",
								 * "b6", "c6", "d6", "e6", "a7", "b7",
								 * "ccccc cccz", "d7", "e7", "a8", "b8", "c8",
								 * "d8", "e8", "a9", "b9", "c9", "d9", "e9",
								 * "a10", "b10", "c10", "d10", "e10" };
								 */

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// int topHeight=getTop();

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
		// Log.d("sbh",Integer.toString(statusBarHeight));

		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// Log.d("height",Integer.toString(dm.heightPixels));//
		// Log.d("width",Integer.toString(dm.widthPixels));
		// boolean portrait;
		if (dm.heightPixels < dm.widthPixels) { // landscape
			SCREEN_COLUMNS = 8;
			// SCREEN_ROWS = 4;
			// SCREEN_ROWS = 5;
			SCREEN_ROWS = 8;
			// CELL_HEIGHT = 50;

			// portrait=false;
		} else { // portrait
			SCREEN_COLUMNS = 5;
			// SCREEN_ROWS = 10;
			// SCREEN_ROWS = 15;
			SCREEN_ROWS = 18;
			// CELL_HEIGHT = 51;
			// portrait=true;
		}

		int tempWidth = dm.widthPixels;
		tempWidth = tempWidth - ROW_MARKER_WIDTH;
		tempWidth = tempWidth - (SCREEN_COLUMNS - 1);
		CELL_WIDTH = tempWidth / SCREEN_COLUMNS;

		LinearLayout ll = new LinearLayout(this);

		setContentView(ll);

		// int topHeight=getTop();

		int tempHeight = dm.heightPixels;
		// tempHeight = tempHeight - SELECT_HEIGHT;
		tempHeight = tempHeight - AD_HEIGHT;
		tempHeight = tempHeight - COLUMN_MARKER_HEIGHT;
		tempHeight = tempHeight - (statusBarHeight * 2);
		tempHeight = tempHeight - (SCREEN_ROWS - 1);
		// Log.d("precell height", Integer.toString(tempHeight));

		CELL_HEIGHT = tempHeight / (SCREEN_ROWS + 2); // plus select and menu
		// Log.d("cell height", Integer.toString(CELL_HEIGHT));

		SELECT_HEIGHT = CELL_HEIGHT;

		cellValue = new String[SCREEN_COLUMNS * SCREEN_ROWS];

		ll.setOrientation(LinearLayout.VERTICAL);

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

				// Log.d("gradkey", "" + pos + " " + action);

				if (pos < 0) {
					// Log.d("gradkey", "" + clickpos + " " + action);
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

		LinearLayout cll = new LinearLayout(this);
		LinearLayout oll = new LinearLayout(this);
		LinearLayout rll = new LinearLayout(this);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, AD_HEIGHT);

		LinearLayout.LayoutParams selectLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, SELECT_HEIGHT);

		LinearLayout.LayoutParams columnHeaderLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, COLUMN_MARKER_HEIGHT);

		LinearLayout.LayoutParams rowMarkerLayoutParams = new LinearLayout.LayoutParams(
				ROW_MARKER_WIDTH, LinearLayout.LayoutParams.FILL_PARENT);

		LinearLayout.LayoutParams rllLP = new LinearLayout.LayoutParams(
				ROW_MARKER_WIDTH, CELL_HEIGHT + 1);

		LinearLayout.LayoutParams iclllp = new LinearLayout.LayoutParams(
				CELL_WIDTH + 1, COLUMN_MARKER_HEIGHT);

		LinearLayout.LayoutParams leftMostColumnTopLP = new LinearLayout.LayoutParams(
				ROW_MARKER_WIDTH, COLUMN_MARKER_HEIGHT);

		oll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		TextView cHeader[] = new TextView[SCREEN_COLUMNS];
		TextView rMarker[] = new TextView[SCREEN_ROWS];

		TextView lmct = new TextView(this);
		TextView empty = new TextView(this);
		TextView menu = new TextView(this);
		select = new TextView(this);

		rll.setOrientation(LinearLayout.VERTICAL);

		rll.setBackgroundResource(R.color.thgrey);
		cll.setBackgroundResource(R.color.thgrey);
		menu.setBackgroundResource(R.color.ltblue);
		select.setBackgroundResource(R.color.white);
		ll.setBackgroundResource(R.color.dgrey); // grey background

		select.setTextColor(Color.rgb(0, 0, 0)); // turns it black

		cll.addView(lmct, leftMostColumnTopLP);
		oll.addView(rll, rowMarkerLayoutParams);
		oll.addView(gridview);

		for (int i = 0; i < SCREEN_COLUMNS; i++) {
			cHeader[i] = new TextView(this);
			// cHeader[i].setText(Integer.toString(i));
			if (i == 0)
				cHeader[i].setText("A");
			else if (i == 1)
				cHeader[i].setText("B");
			else if (i == 2)
				cHeader[i].setText("C");
			else if (i == 3)
				cHeader[i].setText("D");
			else if (i == 4)
				cHeader[i].setText("E");
			else if (i == 5)
				cHeader[i].setText("F");
			else if (i == 6)
				cHeader[i].setText("G");
			else if (i == 7)
				cHeader[i].setText("H");

			cHeader[i].setTextColor(Color.rgb(0, 0, 0)); // turns it black
			cHeader[i].setGravity(Gravity.CENTER_HORIZONTAL);
			cll.addView(cHeader[i], iclllp);
		}

		for (int j = 0; j < SCREEN_ROWS; j++) {
			rMarker[j] = new TextView(this);
			rMarker[j].setText(Integer.toString(j + 1));
			// rMarker[j].setText(Integer.toString(j+101));
			rMarker[j].setTextColor(Color.rgb(0, 0, 0)); // turns it black
			rMarker[j].setGravity(Gravity.CENTER);
			rll.addView(rMarker[j], rllLP);
		}

		for (int k = 0; k < 16; k++)
			cellValue[k] = Integer.toString(k + 1);

		int firstPos = gridview.getSelectedItemPosition();
		// Log.d("firstpos", Integer.toString(firstPos));
		select.setText(cellValue[firstPos]);

		ll.addView(empty, layoutParams);
		ll.addView(menu, selectLayoutParams);
		ll.addView(select, selectLayoutParams);
		ll.addView(cll, columnHeaderLayoutParams);
		ll.addView(oll);

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

				// tv.setLayoutParams(new GridView.LayoutParams(95,
				// CELL_HEIGHT));
				tv.setLayoutParams(new GridView.LayoutParams(CELL_WIDTH,
						CELL_HEIGHT));
			} else {
				tv = (TextView) convertView;
			}

			tv.setBackgroundResource(R.color.cwhite); // cells are white

			tv.setTextColor(Color.rgb(0, 0, 0)); // turns it black

			tv.setText(cellValue[position]);
			final int pos = position;

			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					bigpos = pos;
					// Log.d("onClick", "position " + pos);
					tv.setBackgroundResource(R.drawable.click);
					select.setText(cellValue[pos]);
				}

			});

			return tv;
		}

	}

}