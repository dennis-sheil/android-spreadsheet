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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Grad extends Activity {
	GridView gridview;
	TextView select;
	int bigpos;

	final int CELL_HEIGHT = 53;

	private String[] texts = {
			"a1",
			"b1",
			"c1",
			"d1",
			"e1",
			"a2",
			"b2",
			"c2",
			"d2",
			"e2",
			"a3",
			"b3",
			"c3",
			"d3",
			"e3",
			"a4",
			"b4",
			"c4",
			"dddddd dddddd",
			"e4",
			"a5",
			"b5",
			"c5",
			"d5",
			"e5",
			"a6",
			"b6",
			"c6",
			"d6",
			"e6",
			"a7",
			"b7",
			"ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccz",
			"d7", "e7", "a8", "b8", "c8", "d8", "e8", "a9", "b9", "c9", "d9",
			"e9", "a10", "b10", "c10", "d10", "e10" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doStuff();
		// doMoreStuff();
	}

	void doStuff() {
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);

		gridview = new GridView(this);
		gridview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		gridview.setColumnWidth(94);
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

				if (i == KeyEvent.KEYCODE_DPAD_DOWN) {
					// Log.d("gradkey", "dpad-down " + pos);
					if (pos < texts.length - 5)
						if (action == 1) {
							bigpos = pos + 5;
							gridview.setSelection(bigpos);
							select.setText(texts[bigpos]);
						}
				} else if (i == KeyEvent.KEYCODE_DPAD_UP) {
					// Log.d("gradkey", "dpad-down " + pos);
					if (pos > 4)
						if (action == 1) {
							bigpos = pos - 5;
							gridview.setSelection(bigpos);
							select.setText(texts[bigpos]);
						}
				} else if (i == KeyEvent.KEYCODE_DPAD_RIGHT) {
					// Log.d("gradkey", "dpad-right " + pos);
					if ((pos + 1) % 5 != 0 || pos == 0)
						if (action == 1) {
							bigpos = pos + 1;
							gridview.setSelection(bigpos);
							select.setText(texts[bigpos]);
						}
				} else if (i == KeyEvent.KEYCODE_DPAD_LEFT) {
					// Log.d("gradkey", "dpad-left " + pos);
					if ((pos) % 5 != 0)
						if (action == 1) {
							bigpos = pos - 1;
							gridview.setSelection(bigpos);
							select.setText(texts[bigpos]);
						}
				}

				return true;
			}
		});

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 75);

		TextView empty = new TextView(this);

		LinearLayout.LayoutParams selectLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, CELL_HEIGHT);

		// TextView select = new TextView(this);
		TextView menu = new TextView(this);
		menu.setBackgroundResource(R.color.ltblue);

		select = new TextView(this);
		select.setBackgroundResource(R.color.white);
		select.setTextColor(Color.rgb(0, 0, 0)); // turns it black

		ll.setBackgroundResource(R.color.dgrey); // grey background

		ll.addView(empty, layoutParams);
		ll.addView(menu, selectLayoutParams);
		ll.addView(select, selectLayoutParams);

		ll.addView(gridview);

		// setContentView(gridview);
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

				tv.setLayoutParams(new GridView.LayoutParams(95, CELL_HEIGHT));
			} else {
				tv = (TextView) convertView;
			}
			// standard dwhite
			// better cursor darker cwhite
			// tv.setBackgroundResource(R.color.dwhite); // cells are white
			tv.setBackgroundResource(R.color.cwhite); // cells are white

			tv.setTextColor(Color.rgb(0, 0, 0)); // turns it black

			tv.setText(texts[position]);
			final int pos = position;

			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					bigpos = pos;
					Log.d("onClick", "position " + pos);
					// tv.setBackgroundResource(R.color.green); // cells are
					// white
					tv.setBackgroundResource(R.drawable.click); // cells are
					// white
					select.setText(texts[pos]);
				}

			});

			return tv;
		}

	}

}