package com.codewell4.DictionaryExample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperClass extends SQLiteOpenHelper
{
	private static String DB_PATH = Common.dictionaryPath;
	private static String DB_NAME = Common.dictionaryName + Common.fileEnding;
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelperClass(Context context)
	{
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}	
	 
	public void createDataBase() throws IOException
	{
		try
		{
		 	boolean dbExist = checkDataBase();
			 
			if(dbExist)
			{
				this.getWritableDatabase();
			}
			else
			{
				this.getReadableDatabase();
				try
				{
					copyDataBase();
				}
				catch (Exception e)
				{
					Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
				}
			}
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}

	public void openDataBase() throws SQLException
	{
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}	
	
	private boolean checkDataBase()
	{
		SQLiteDatabase checkDB = null;
		String myPath = DB_PATH + DB_NAME;
		try
		{
			File checkFile = new File(myPath);
			if (checkFile.exists())
				checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		}
		catch(SQLiteException e)
		{
			//database does't exist yet.
		}

		if(checkDB != null)
		{
			checkDB.close();
		}
		 
		return checkDB != null ? true : false;
	}
	 
	private void copyDataBase() throws IOException
	{
		try
		{
			String outFileName = DB_PATH + DB_NAME;
			InputStream myInput = myContext.getAssets().open(DB_NAME);
			OutputStream myOutput = new FileOutputStream(outFileName);
		
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer))>0)
			{
				myOutput.write(buffer, 0, length);
			}
		
			myOutput.flush();
			myOutput.close();
			myInput.close();
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}
	 
	@Override
	public synchronized void close()
	{
		if(myDataBase != null)
			myDataBase.close();
		super.close();
	}
	 
	@Override
	public void onCreate(SQLiteDatabase db) {}
	 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (newVersion > oldVersion)
		{
			try
			{
				copyDataBase();
			}
			catch (IOException e)
			{
				throw new Error("Error copying database");
			}
		}
	}
}