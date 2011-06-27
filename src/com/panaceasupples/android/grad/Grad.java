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
	private GridView gridview;
	private TextView select;

	private int bigpos;
	private int rowsDown;
	private int columnsRight;

	private int SCREEN_COLUMNS;
	private int SCREEN_ROWS;
	private int ROW_MARKER_WIDTH;
	private int CELL_WIDTH;
	private int CELL_HEIGHT;

	private final int AD_HEIGHT = 75;
	private final int COLUMN_MARKER_HEIGHT = 25;
	private int DATA_SIZE = 20;

	private String[] cellValue;
	private TextView rMarker[];
	private TextView cMarker[];

	private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
	private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
	private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;

	// int data[][];
	String data[][];

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doStuff();
	}

	void doStuff() {

		// data = new int[20][20];
		data = new String[DATA_SIZE][DATA_SIZE];
		// data[0][0] = 1;
		data[0][0] = "1";
		data[7][0] = "7";

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

		if (dm.heightPixels < dm.widthPixels) { // landscape
			if (dm.heightPixels <= mdpiHeightMin) {
				ROW_MARKER_WIDTH = 34;
				// SCREEN_COLUMNS = 6;
				SCREEN_COLUMNS = 5;
				// SCREEN_ROWS = 6;
				SCREEN_ROWS = 7;
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
				// SCREEN_COLUMNS = 5;
				SCREEN_COLUMNS = 4;
				SCREEN_ROWS = 18;
			}
		}

		int tempHeight = dm.heightPixels;
		tempHeight = tempHeight - AD_HEIGHT;
		tempHeight = tempHeight - COLUMN_MARKER_HEIGHT;
		tempHeight = tempHeight - (statusBarHeight * 2);
		tempHeight = tempHeight - (SCREEN_ROWS - 1);

		CELL_HEIGHT = tempHeight / (SCREEN_ROWS + 2); // plus select and menu
		tempHeight = tempHeight - (CELL_HEIGHT * (SCREEN_ROWS + 2));
		int EXTRA_HEIGHT = tempHeight;
		int SELECT_HEIGHT = CELL_HEIGHT + EXTRA_HEIGHT;

		cellValue = new String[SCREEN_COLUMNS * SCREEN_ROWS];

		int tempWidth = dm.widthPixels;
		tempWidth = tempWidth - ROW_MARKER_WIDTH;
		tempWidth = tempWidth - (SCREEN_COLUMNS - 1);
		CELL_WIDTH = tempWidth / SCREEN_COLUMNS;
		tempWidth = tempWidth - (CELL_WIDTH * SCREEN_COLUMNS);
		ROW_MARKER_WIDTH = ROW_MARKER_WIDTH + tempWidth;

		TextView empty = new TextView(this);
		TextView emptyTwo = new TextView(this);
		// TextView cMarker[] = new TextView[SCREEN_COLUMNS];
		cMarker = new TextView[SCREEN_COLUMNS];
		rMarker = new TextView[SCREEN_ROWS];

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

		rowsDown = 0;
		columnsRight = 0;

		for (int j = 0; j < SCREEN_ROWS; j++) {
			rMarker[j] = new TextView(this);
			rMarker[j].setText(Integer.toString(j + rowsDown + 1));
			// / rMarker[j].setText(Integer.toString(j+101));
			rMarker[j].setTextColor(Color.rgb(0, 0, 0)); // turns it black
			rMarker[j].setGravity(Gravity.CENTER);
			rll.addView(rMarker[j], rMarkerLayoutParams);
		}

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
				if (pos < 0)
					pos = bigpos;

				int ac = 0; // off (1 is on)

				if (i == KeyEvent.KEYCODE_DPAD_DOWN) {
					if (pos < cellValue.length - SCREEN_COLUMNS) {
						if (action == ac) {
							bigpos = pos + SCREEN_COLUMNS;
							gridview.setSelection(bigpos);
							select.setText(cellValue[bigpos]);
						}
					} else {
						if (action == ac && rowsDown < 970) { // bottom downward
							rowsDown++;
							dataToScreen();
							gridview.invalidateViews();
							for (int j = 0; j < SCREEN_ROWS; j++) {
								rMarker[j].setText(Integer.toString(j + 1
										+ rowsDown));
							}
						}
					}
				}

				else if (i == KeyEvent.KEYCODE_DPAD_UP) {
					if (pos >= SCREEN_COLUMNS) {
						if (action == ac) {
							bigpos = pos - SCREEN_COLUMNS;
							gridview.setSelection(bigpos);
							select.setText(cellValue[bigpos]);
						}
					} else {
						if (rowsDown > 0) {
							if (action == ac) {
								rowsDown--;
								dataToScreen();
								gridview.invalidateViews();
								for (int j = 0; j < SCREEN_ROWS; j++) {
									rMarker[j].setText(Integer.toString(j + 1
											+ rowsDown));
								}
							}
						}
					}

				}

				else if (i == KeyEvent.KEYCODE_DPAD_RIGHT) {
					if ((pos + 1) % SCREEN_COLUMNS != 0 || pos == 0) {
						if (action == ac) {
							bigpos = pos + 1;
							gridview.setSelection(bigpos);
							select.setText(cellValue[bigpos]);
						}
					} else {
						if (action == ac) {
							columnsRight++;
							dataToScreen();
							gridview.invalidateViews();
							for (int j = 0; j < SCREEN_COLUMNS; j++) {
								cMarker[j]
										.setText(numToColumn(j + columnsRight));
							}
						}
					}
				}

				else if (i == KeyEvent.KEYCODE_DPAD_LEFT) {
					if ((pos) % SCREEN_COLUMNS != 0) {
						if (action == ac) {
							bigpos = pos - 1;
							gridview.setSelection(bigpos);
							select.setText(cellValue[bigpos]);
						}
					} else {

						if (columnsRight > 0) {
							if (action == ac) {
								columnsRight--;
								dataToScreen();
								gridview.invalidateViews();
								// deal with select as well
								for (int j = 0; j < SCREEN_COLUMNS; j++) {
									cMarker[j].setText(numToColumn(j
											+ columnsRight));
								}
							}
						}

					}
				}

				return true;
			}
		});

		columnMarkerLinearLayout.addView(empty, midMarkerParams);

		for (int i = 0; i < SCREEN_COLUMNS; i++) {
			cMarker[i] = new TextView(this);
			cMarker[i].setText(numToColumn(i));
			cMarker[i].setTextColor(Color.rgb(0, 0, 0)); // turns it black
			cMarker[i].setGravity(Gravity.CENTER_HORIZONTAL);
			columnMarkerLinearLayout.addView(cMarker[i], cmLayoutParams);
		}

		dataToScreen();

		TextView menu = new TextView(this);
		TextView rowMarker = new TextView(this);
		select = new TextView(this);

		menu.setBackgroundResource(R.color.ltblue);
		rowMarker.setBackgroundResource(R.color.thgrey);
		rll.setBackgroundResource(R.color.thgrey);
		select.setBackgroundResource(R.color.white);
		ll.setBackgroundResource(R.color.dgrey);
		columnMarkerLinearLayout.setBackgroundResource(R.color.thgrey);

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

		int firstPos = gridview.getSelectedItemPosition();
		select.setText(cellValue[firstPos]);

		setContentView(ll);
	}

	void dataToScreen() {
		int c = 0;
		int r = 0;
		for (int k = 0; k < cellValue.length; k++) {
			if (c + columnsRight < DATA_SIZE && r + rowsDown < DATA_SIZE) {
				cellValue[k] = data[c + columnsRight][r + rowsDown];
				// cellValue[k] = data[r+rowsDown][c+columnsRight];
			}
			if ((k + 1) % SCREEN_COLUMNS == 0) {
				c = 0;
				r++;
			} else
				c++;
		}

	}

	String numToColumn(int i) {
		boolean go = true;
		String t = "";
		while (go) {
			int x = i % 26;
			String s = "";
			switch (x) {
			case 0:
				s = "A";
				break;
			case 1:
				s = "B";
				break;
			case 2:
				s = "C";
				break;
			case 3:
				s = "D";
				break;
			case 4:
				s = "E";
				break;
			case 5:
				s = "F";
				break;
			case 6:
				s = "G";
				break;
			case 7:
				s = "H";
				break;
			case 8:
				s = "I";
				break;
			case 9:
				s = "J";
				break;
			case 10:
				s = "K";
				break;
			case 11:
				s = "L";
				break;
			case 12:
				s = "M";
				break;
			case 13:
				s = "N";
				break;
			case 14:
				s = "O";
				break;
			case 15:
				s = "P";
				break;
			case 16:
				s = "Q";
				break;
			case 17:
				s = "R";
				break;
			case 18:
				s = "S";
				break;
			case 19:
				s = "T";
				break;
			case 20:
				s = "U";
				break;
			case 21:
				s = "V";
				break;
			case 22:
				s = "W";
				break;
			case 23:
				s = "X";
				break;
			case 24:
				s = "Y";
				break;
			case 25:
				s = "Z";
				break;
			}

			t = s + t;
			if (i < 26)
				go = false;
			i = i / 26;
			i--;
		}
		return t;
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