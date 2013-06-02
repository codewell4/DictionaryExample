package com.codewell4.DictionaryExample;

import java.io.File;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.WindowManager;
import android.widget.TextView;

public class DictionaryDetails extends Activity
{
	String htmlBreak	="<br>";
	int searchID		= 0;
	String searchTerm	= "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.results_detail);

			if (Common.IsAppFullscreen())
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);			
			
			if(getIntent().hasExtra(Common.EXTRA_SEARCH_ID))
			{
				searchID = getIntent().getIntExtra(Common.EXTRA_SEARCH_ID,0);
				searchTerm = getIntent().getStringExtra(Common.EXTRA_SEARCH_TERM);
				getResult(searchID,searchTerm);
			}
			
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);			
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}

	private String applyBold(String element)
	{
		element = "<font color='red'><b>"  + element + "</b></font>";
		return element;
	}

	private void getResult(int searchID, String searchTerm)
	{
		try
		{
			SQLiteDatabase db;
			String dictionaryPath = Common.dictionaryPath + Common.dictionaryName + Common.fileEnding;
			File dictionaryFile = new File(dictionaryPath);
			
			if (dictionaryFile.exists() && dictionaryFile.isFile())
				db = SQLiteDatabase.openOrCreateDatabase(dictionaryPath, null);
			else
				db = null;

			if (db != null)
			{
				String selectQuery = " SELECT * FROM dictionary WHERE _id=" + String.valueOf(searchID) + " LIMIT 1 ";
				Cursor cursor = db.rawQuery(selectQuery, null);

				if (cursor.moveToFirst())
				{
					String term			= cursor.getString(cursor.getColumnIndex("term"));
					String description	= cursor.getString(cursor.getColumnIndex("description"));

					TextView tvTitle = (TextView)findViewById(R.id.textDetailTitle);
					TextView tv = (TextView)findViewById(R.id.textDetail);
					
					tvTitle.setText(Html.fromHtml(term));

					searchTerm = searchTerm.replace("*", "");
					searchTerm = searchTerm.replace("?", "");
					searchTerm = searchTerm.replace("+", "");
					searchTerm = searchTerm.replace("-", "");
					searchTerm = searchTerm.replace("\"", "");
					searchTerm = searchTerm.replace("  ", " ");
					searchTerm = searchTerm.trim();

					String listOfWord[] = searchTerm.split(" ");
					String searchTermFistLetterUpper = "";
					String searchTermLowercase = "";
					for (int cnt=0; cnt < listOfWord.length; cnt++)
					{
						searchTermLowercase = listOfWord[cnt].toLowerCase();
						description = description.replace(searchTermLowercase, applyBold(searchTermLowercase));
						searchTermFistLetterUpper = searchTermLowercase.substring(0,1).toUpperCase() + searchTermLowercase.substring(1);
						description = description.replace(searchTermFistLetterUpper, applyBold(searchTermFistLetterUpper));
					}

					tv.setText(Html.fromHtml(description));
				}
			}
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}
}
