package com.codewell4.DictionaryExample;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;

public class Search extends Activity
{
	boolean fileExists = false;
	public static SharedPreferences settings;
	Boolean fullscreenApp					= false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.search);

			settings = PreferenceManager.getDefaultSharedPreferences(this);
			settings.registerOnSharedPreferenceChangeListener(settingsListener);
			fullscreenApp = settings.getBoolean(Common.CB_FULLSCREEN, false);

			Common.dictionaryPath = this.getFilesDir().getParent() + "/databases/";			
			
			DatabaseHelperClass myDbHelper = new DatabaseHelperClass(getApplicationContext());
			myDbHelper = new DatabaseHelperClass(this);
			try
			{
				 myDbHelper.createDataBase();
			}
			catch (Exception ioe)
			{
				 throw new Error("Unable to create database");
			}
			
			fileExists = true;
			try
			{
				 myDbHelper.openDataBase();
			}
			catch(SQLException sqle)
			{
				 fileExists = false;
				 throw sqle;
			}

			if (Common.IsAppFullscreen())
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
			addListenerOnEditAction();
			addListenerOnRunButton();
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}

	public void addListenerOnEditAction()
	{
		try
		{
			EditText editText = (EditText) findViewById(R.id.editText1);
			editText.setOnEditorActionListener(new OnEditorActionListener()
			{
				public boolean onEditorAction(TextView view, int keycode, KeyEvent event)
				{
					if(keycode == KeyEvent.KEYCODE_ENDCALL)
					{
						onSearch();
						return true;
					}
					return false;
			}});
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}
	
	public void addListenerOnRunButton()
	{
		try
		{
			Button btnSearch = (Button) findViewById(R.id.btnSearch);
			btnSearch.setOnClickListener
			(
					new OnClickListener()
					{
						public void onClick(View v)
						{
							onSearch();
						}
					}
			);
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}

	private void onSearch()
	{
		try
		{
			TextView tv = (TextView)findViewById(R.id.textView1);
			tv.setText("");

			if (fileExists)
			{
				EditText edit = (EditText)findViewById(R.id.editText1);
				RadioButton rbAll = (RadioButton) findViewById(R.id.rbAll);
				RadioButton rbTerm = (RadioButton) findViewById(R.id.rbTerm);
				RadioButton rbDescription = (RadioButton) findViewById(R.id.rbDescription);

				String searchTerm = edit.getText().toString().trim();

				if (searchTerm.length() > 1)
				{
					Intent intent = new Intent(getApplicationContext(),DictionaryResults.class);
					intent.putExtra(Common.EXTRA_SEARCH_TERM, searchTerm);
					if (rbAll.isChecked())
						intent.putExtra(Common.EXTRA_SEARCH_MODE, 0);
					else if (rbTerm.isChecked())
						intent.putExtra(Common.EXTRA_SEARCH_MODE, 1);
					else if (rbDescription.isChecked())
						intent.putExtra(Common.EXTRA_SEARCH_MODE, 2);
					else
						intent.putExtra(Common.EXTRA_SEARCH_MODE, 0);
					startActivity(intent);
				}
				else
				{
					tv.setText(getString(R.string.ErrorSearchTermTooShort));
				}
			}
			else
			{
				tv.setText(getString(R.string.ErrorDictionaryNotExist));
			}
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		try
		{
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu, menu);
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		try
		{
			if(item.getItemId() == R.id.btnSettings)
			{
				startActivity(new Intent(this, Settings.class));
				return true;
			}
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
		return false;
	}

	SharedPreferences.OnSharedPreferenceChangeListener settingsListener = new SharedPreferences.OnSharedPreferenceChangeListener()
	{
		public void onSharedPreferenceChanged(SharedPreferences prefs,String key)
		{
			try
			{

			}
			catch (Exception e)
			{
				Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
			}
		}
	};
}
