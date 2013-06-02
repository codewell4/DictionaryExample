package com.codewell4.DictionaryExample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DictionaryResults extends ListActivity
{
	String searchTerm = "";
	int searchMode = 0;
	private static final String wildCardSQLAll = "%";
	private static final String wildCardAll = "*";
	private static final String wildCardSQLOne = "_";
	private static final String wildCardOne = "?";
	private static final String fullTextNOT = " -";
	private static final String fullTextAND = " +";
	private static final String fullTextSQLAND = " ";
	private static final String fullTextEXP = "\"";
	private static final String fullTextOR = " ";
	private static final String fullTextSQLOR = " OR ";

	protected ArrayList<DictionaryItem> mItems;
	private DictionaryItemListAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.results);

			if (Common.IsAppFullscreen())
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
			mItems = new ArrayList<DictionaryItem>();
			listAdapter = new DictionaryItemListAdapter(this, mItems);
			setListAdapter(listAdapter);

			LayoutInflater inflator = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View emptyView = inflator.inflate(R.layout.results_empty, null);
			((ViewGroup)getListView().getParent()).addView(emptyView);
			getListView().setEmptyView(emptyView);

			if(getIntent().hasExtra(Common.EXTRA_SEARCH_MODE))
			{
				searchMode = getIntent().getIntExtra(Common.EXTRA_SEARCH_MODE,0);
			}

			if(getIntent().hasExtra(Common.EXTRA_SEARCH_TERM))
			{
				searchTerm = getIntent().getStringExtra(Common.EXTRA_SEARCH_TERM);
				getResultList(searchMode, searchTerm);
			}
			
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}

	private void getResultList(int searchMode, String searchTerm)
	{
		try
		{
			SQLiteDatabase db;
			File dictionaryFile = new File(Common.dictionaryPath+Common.dictionaryName+Common.fileEnding);

			if (dictionaryFile.exists() && dictionaryFile.isFile())
			{
				db = SQLiteDatabase.openDatabase(dictionaryFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
			}
			else
				db = null;

			if (db != null)
			{
				String selectQuery = "SELECT _id, term, description FROM dictionary WHERE ";

				if (searchMode == 0)
				{
					if ((searchTerm.contains(wildCardAll)) || (searchTerm.contains(wildCardOne)))
					{
						searchTerm = searchTerm.replace(wildCardSQLAll, "");
						searchTerm = searchTerm.replace(wildCardSQLOne, "");
						searchTerm = searchTerm.replace(wildCardAll, wildCardSQLAll);
						searchTerm = searchTerm.replace(wildCardOne, wildCardSQLOne);
						searchTerm = searchTerm.replace(fullTextAND, "");
						searchTerm = searchTerm.replace(fullTextOR, "");
						searchTerm = searchTerm.replace(fullTextEXP, "");
						
						selectQuery += " term LIKE '" + searchTerm  + "' OR ";
						selectQuery += " description LIKE '" + wildCardSQLAll + searchTerm + wildCardSQLAll + "' COLLATE NOCASE";
					}
					else
					{
						searchTerm = searchTerm.replace("  ", " ");
						searchTerm = searchTerm.replace(fullTextAND, "+");
						searchTerm = searchTerm.replace(fullTextNOT, "-");
						searchTerm = searchTerm.replace(fullTextOR, fullTextSQLOR);
						searchTerm = searchTerm.replace("+", fullTextSQLAND);
						searchTerm = searchTerm.replace("-", fullTextNOT);
						selectQuery += " dictionary MATCH '" + searchTerm  + "' ";
					}
				}
				else if (searchMode == 1)
				{
					if ((searchTerm.contains(wildCardAll)) || (searchTerm.contains(wildCardOne)))
					{
						searchTerm = searchTerm.replace(wildCardSQLAll, "");
						searchTerm = searchTerm.replace(wildCardSQLOne, "");
						searchTerm = searchTerm.replace(wildCardAll, wildCardSQLAll);
						searchTerm = searchTerm.replace(wildCardOne, wildCardSQLOne);
						searchTerm = searchTerm.replace(fullTextAND, "");
						searchTerm = searchTerm.replace(fullTextOR, "");
						searchTerm = searchTerm.replace(fullTextEXP, "");
						selectQuery += " term LIKE '" + searchTerm  + "' ";
					}
					else
					{
						searchTerm = searchTerm.replace("  ", " ");
						searchTerm = searchTerm.replace(fullTextAND, "+");
						searchTerm = searchTerm.replace(fullTextNOT, "-");
						searchTerm = searchTerm.replace(fullTextOR, fullTextSQLOR);
						searchTerm = searchTerm.replace("+", fullTextSQLAND);
						searchTerm = searchTerm.replace("-", fullTextNOT);
						selectQuery += " jaslo MATCH 'term:" + searchTerm  + " OR description:" + searchTerm  + "' ";
					}
				}
				else if (searchMode == 2)
				{
					if ((searchTerm.contains(wildCardAll)) || (searchTerm.contains(wildCardOne)))
					{
						searchTerm = searchTerm.replace(wildCardSQLAll, "");
						searchTerm = searchTerm.replace(wildCardSQLOne, "");
						searchTerm = searchTerm.replace(wildCardAll, wildCardSQLAll);
						searchTerm = searchTerm.replace(wildCardOne, wildCardSQLOne);
						searchTerm = searchTerm.replace(fullTextAND, "");
						searchTerm = searchTerm.replace(fullTextOR, "");
						searchTerm = searchTerm.replace(fullTextEXP, "");
						selectQuery += " description LIKE '" + wildCardSQLAll + searchTerm  + wildCardSQLAll + "' COLLATE NOCASE";
					}
					else
					{
						searchTerm = searchTerm.replace("  ", " ");
						searchTerm = searchTerm.replace(fullTextAND, "+");
						searchTerm = searchTerm.replace(fullTextNOT, "-");
						searchTerm = searchTerm.replace(fullTextOR, fullTextSQLOR);
						searchTerm = searchTerm.replace("+", fullTextSQLAND);
						searchTerm = searchTerm.replace("-", fullTextNOT);
						selectQuery += " description MATCH '" + searchTerm  + "' ";
					}
				}

				String sorting = Search.settings.getString(Common.SORT_LIST, "0").trim();
				if (sorting.equals("0"))
					selectQuery += " ORDER BY term ASC ";
				else
					selectQuery += " ORDER BY _id ASC ";
				selectQuery += " LIMIT 100 ";

				Cursor cursor = db.rawQuery(selectQuery, null);

				mItems.clear();
				if (cursor.moveToFirst())
				{
					do
					{
						DictionaryItem item		= new DictionaryItem();
						item.id					= Integer.parseInt(cursor.getString(0));
						item.term				= cursor.getString(cursor.getColumnIndex("term"));
						item.description		= cursor.getString(cursor.getColumnIndex("description"));
						
						mItems.add(item);
					} while (cursor.moveToNext());
				}
				if (mItems.size() > 0)
				{
					TextView tvResults = (TextView)findViewById(R.id.textViewResults);
					tvResults.setText(getString(R.string.ResultsTitle) + " (" + String.valueOf(mItems.size()) + ")");
					listAdapter.notifyDataSetChanged();
				}
			}
			else
			{
				TextView tv = (TextView) findViewById(R.id.rowEmpty);
				tv.setText(getString(R.string.ErrorDictionaryNotExist));
			}
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		try
		{
			DictionaryItem item = (DictionaryItem)l.getItemAtPosition(position);

			Intent intent = new Intent(v.getContext(),DictionaryDetails.class);
			intent.putExtra(Common.EXTRA_SEARCH_ID, item.id);
			intent.putExtra(Common.EXTRA_SEARCH_TERM, searchTerm);
			startActivity(intent);

			super.onListItemClick(l, v, position, id);
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}

	private class DictionaryItemListAdapter extends ArrayAdapter<DictionaryItem>
	{
		private List<DictionaryItem> mObjects;

		public DictionaryItemListAdapter(Context context, List<DictionaryItem> objects)
		{
			super(context, R.layout.results_item, android.R.id.text1, objects);
			mObjects = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View row = null;
			try
			{
				if(convertView == null)
				{ 
					LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					row = inflater.inflate(R.layout.results_item, parent, false);
				}
				else
				{
					row = convertView;
				}
				DictionaryItem object = mObjects.get(position);

				TextView textViewItem = (TextView)row.findViewById(R.id.rowTextView);
				String textOut = "";
				textOut = object.term;
				textViewItem.setText(textOut);


				TextView textViewItem1 = (TextView)row.findViewById(R.id.rowTextView1);
				textViewItem1.setText(object.description);
				textViewItem1.setTypeface(null, Typeface.NORMAL);
			}
			catch (Exception e)
			{
				Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
			}
			return row;
		}
	}
}