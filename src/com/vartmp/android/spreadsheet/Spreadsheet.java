package com.vartmp.android.spreadsheet;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

//import javax.xml.namespace.QName;

public class Spreadsheet extends Activity {

	private GridView gridview;
	private TextView select;

	private int bigpos;
	private int rowsDown;
	private int columnsRight;
	private int rowsPulled;

	private int SCREEN_COLUMNS;
	private int SCREEN_ROWS;
	private int ROW_MARKER_WIDTH;
	private int CELL_WIDTH;
	private int CELL_HEIGHT;

	private int COLUMN_SIZE;
	private int ROW_SIZE;

	private final int AD_HEIGHT = 75;
	private final int COLUMN_MARKER_HEIGHT = 25;

	private String[] cellValue;
	private TextView rMarker[];
	private TextView cMarker[];

	private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
	private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
	private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;

	private FileInputStream fis;
	// private HSSFWorkbook wb;
	private Workbook wb;
	private Sheet sheet;
	// private HSSFSheet sheet;
	// private XSSFSheet sheet;

	private String data[][];

	private LinkedList<String> fileList = new LinkedList<String>();
	private LinearLayout ll;
	boolean hssf;
	String fileName;

	GestureDetector mGestureDetector;
	boolean mScrolled;
	int mTargetScrollX;

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
		Log.d("cell height is", CELL_HEIGHT + "");
		tempHeight = tempHeight - (CELL_HEIGHT * (SCREEN_ROWS + 2));
		int EXTRA_HEIGHT = tempHeight;
		int SELECT_HEIGHT = CELL_HEIGHT + EXTRA_HEIGHT;
		int MENU_HEIGHT = CELL_HEIGHT;

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

		// LinearLayout ll = new LinearLayout(this);
		ll = new LinearLayout(this);
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
				LinearLayout.LayoutParams.FILL_PARENT, MENU_HEIGHT);

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

				// onKey catches back button, so make sure it functions
				if (i == KeyEvent.KEYCODE_BACK && action == 0) {
					moveTaskToBack(true);
					return true;
				}

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
							Log.d(rowsDown + " " + SCREEN_ROWS, rowsPulled + "");
							if (rowsDown + SCREEN_ROWS >= rowsPulled)
								pullSomeRows();

							dataToScreen();
							gridview.invalidateViews();
							select.setText(cellValue[bigpos]);
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
								select.setText(cellValue[bigpos]);
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
							select.setText(cellValue[bigpos]);

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
								select.setText(cellValue[bigpos]);

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

		ImageView fileImage = new ImageView(this);

		int topPadding = 0;
		if (MENU_HEIGHT < 22) {
			fileImage.setImageResource(R.drawable.file16);
			if (MENU_HEIGHT > 16) {
				int temp = MENU_HEIGHT - 16;
				topPadding = temp / 2;
			}
			fileImage.setPadding(10, topPadding, 0, 0);
		} else if (MENU_HEIGHT >= 22) {
			fileImage.setImageResource(R.drawable.file22);
			int temp = MENU_HEIGHT - 22;
			topPadding = temp / 2;

			fileImage.setPadding(10, topPadding, 0, 0);
		}

		LinearLayout menu = new LinearLayout(this);

		// TextView menu = new TextView(this);
		TextView rowMarker = new TextView(this);
		select = new TextView(this);
		menu.addView(fileImage);
		setIconListener(fileImage, "open");

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

		setContentView(ll);

		// doExcelStuff();
		// dataToScreen();

		int firstPos = gridview.getSelectedItemPosition();
		// Log.d("firstpos is", firstPos + "");
		if (firstPos > 0)
			select.setText(cellValue[firstPos]);
		// ^^ kludgey, unneeded

		// setContentView(ll);

	}

	void setIconListener(final ImageView i, final String s) {
		i.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				File file = Environment.getExternalStorageDirectory();
				visitAllFiles(file);
				String al[] = new String[fileList.size()];
				for (int i = 0; fileList.size() > 0; i++) {
					al[i] = (String) fileList.remove();
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						Spreadsheet.this, android.R.layout.simple_list_item_1,
						al);

				ListView lv = new ListView(Spreadsheet.this);

				lv.setBackgroundColor(0xffcccccc); // light gray
				lv.setAdapter(adapter);

				TextView rv = new TextView(Spreadsheet.this);
				TextView sv = new TextView(Spreadsheet.this);
				if (al.length > 0) {
					rv.setText("We have found some Excel files.\nThey can take some time to load, especially larger ones.\n");
					sv.setText("Select the spreadsheet you want to use:");
					sv.setPadding(0, 0, 0, 50);
				} else {
					rv.setText("We did not find any Excel (xls, xlsx etc.) files"
							+ " on your SD drive.\n");
				}
				LinearLayout mll;
				mll = new LinearLayout(Spreadsheet.this);
				mll.setOrientation(LinearLayout.VERTICAL);
				mll.setGravity(Gravity.CENTER_HORIZONTAL);

				mll.addView(rv);
				mll.addView(sv);
				mll.addView(lv);

				setContentView(mll);

				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						CharSequence selectionCS = ((TextView) view).getText();
						String selection = selectionCS.toString();
						fileName = selection;

						// Toast.makeText(Grad.this, selection,
						// Toast.LENGTH_SHORT).show();

						// doExcelStuff(selection);
						doExcelStuff();
						dataToScreen();
						gridview.invalidateViews();
						setContentView(ll);

					}
				});

				// Toast.makeText(Grad.this, s, Toast.LENGTH_SHORT).show();
			}
		});
	}

	void visitAllFiles(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			if (dir.canRead()) {
				for (int i = 0; i < children.length; i++) {
					visitAllFiles(new File(dir, children[i]));
				}
			}
		} else {
			String ap = dir.getAbsolutePath();
			if (ap.endsWith(".xls") || ap.endsWith(".xlsx")) {
				fileList.add(ap);
			}
		}
	}

	void pullSomeRows() {
		int rp = rowsPulled;
		for (int r = rowsPulled; r < rp + 5; r++) {

			// HSSFRow row = sheet.getRow(r);
			Row row = sheet.getRow(r);
			if (row == null)
				continue;
			int cells = row.getPhysicalNumberOfCells();
			// Log.d("des", "14");

			for (int c = 0; c < cells; c++) {
				Cell cell = row.getCell(c);
				String value = null;

				switch (cell.getCellType()) {

				case Cell.CELL_TYPE_FORMULA:
					value = cell.getCellFormula();
					break;

				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell))
						value = "" + cell.getDateCellValue();
					else
						value = "" + cell.getNumericCellValue();
					break;

				case Cell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;

				default:
				}
				// Log.d("des","99");

				data[cell.getColumnIndex()][r] = value;
			}

			rowsPulled++;

		}

	}

	void doExcelStuff() {

		try {
			// Log.d("des", "1.25 ");
			File file = new File(fileName);
			long filelength = file.length();
			Log.d("File size is", filelength + "");

			fis = new FileInputStream(fileName);
			Log.d("grad", "before  workbook constructor ");

			if (fileName.endsWith(".xls"))
				hssf = true;
			else if (fileName.endsWith(".xlsx"))
				hssf = false;
			else
				hssf = true;

			if (hssf) {
				wb = new HSSFWorkbook(fis); // <-- this takes a while 20 seconds
				Log.d("grad", "hssf");

			} else {
				// wb = new XSSFWorkbook(fis); // <-- this takes a while 20
				// seconds
				// ^^ uncomment to allow xssf
				Log.d("grad", "xssf");
			}

			Log.d("grad", "after workbook constructor");

			sheet = wb.getSheetAt(0); // 0 means get first sheet
			Log.d("grad", "1");

			int rows = sheet.getPhysicalNumberOfRows();

			int mostcells = 0;

			for (int r = 0; r < rows; r++) {
				Row row = sheet.getRow(r);
				if (row == null)
					continue;
				int cells = row.getPhysicalNumberOfCells();
				if (cells > mostcells)
					mostcells = cells;
			}

			Log.d("grad", "2 ");

			COLUMN_SIZE = mostcells;
			ROW_SIZE = rows;
			// data = new String[mostcells][rows];
			data = new String[COLUMN_SIZE][ROW_SIZE];

			Log.d("grad", "3");

			for (int r = 0; r < SCREEN_ROWS + 3; r++) {
				Row row = sheet.getRow(r);
				if (row == null)
					continue;
				int cells = row.getPhysicalNumberOfCells();
				Log.d("grad", "4");

				for (int c = 0; c < cells; c++) {
					Cell cell = row.getCell(c);
					String value = null;

					switch (cell.getCellType()) {

					case Cell.CELL_TYPE_FORMULA:
						value = cell.getCellFormula();
						break;

					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell))
							value = "" + cell.getDateCellValue();
						else
							value = "" + cell.getNumericCellValue();
						break;

					case Cell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;

					default:
					}

					data[cell.getColumnIndex()][r] = value;
				}

			}
			rowsPulled = SCREEN_ROWS + 3;

		} catch (Exception e) {
			Log.d("error ", "" + e.getMessage());
		}

	}

	void dataToScreen() {
		int c = 0;
		int r = 0;
		for (int k = 0; k < cellValue.length; k++) {
			if (c + columnsRight < COLUMN_SIZE && r + rowsDown < ROW_SIZE) {
				cellValue[k] = data[c + columnsRight][r + rowsDown];
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