package com.codewell4.DictionaryExample;

import android.util.Log;

public class Common
{
	public final static String fileEnding				= ".db3";
	public final static String dictionaryName 			= "dictionary";
	public static String dictionaryPath 				= "";
	
	public final static String EXTRA_SEARCH_DICTIONARY 	= "searchDictionary";
	public final static String EXTRA_SEARCH_TERM 		= "searchTerm";
	public final static String EXTRA_SEARCH_MODE 		= "searchMode";
	public final static String EXTRA_SEARCH_ID 			= "searchID";

	public final static String SORT_LIST 				= "listSort";
	public final static String CB_FULLSCREEN			= "cbFullscreen";
	public final static String LBL_VERSION_NUMBER		= "lblVersionNumber";

	public static enum LogLevelCustom
	{
		Verbose,
		Debug,
		Info,
		Warning,
		Error,
		Assert
	}

	public static Boolean IsAppFullscreen()
	{
		return Search.settings.getBoolean(Common.CB_FULLSCREEN, false);
	}
	
	public static void LogFunctionVerbose(String OutString)
	{
		Log.v("Log: " , OutString);
	}
	
	public static void LogFunction(LogLevelCustom LogLevel, String FunctionName, String LogVariable)
	{
		if (LogLevel == LogLevelCustom.Verbose)
			Log.v(FunctionName, "Log: " + LogVariable);
		else if(LogLevel == LogLevelCustom.Debug)
			Log.d(FunctionName, "Log: " + LogVariable);
		else if (LogLevel == LogLevelCustom.Info)
			Log.i(FunctionName, "Log: " + LogVariable);
		else if (LogLevel == LogLevelCustom.Warning)
			Log.w(FunctionName, "Log: " + LogVariable);
		else if (LogLevel == LogLevelCustom.Error)
			Log.e(FunctionName, "Log: " + LogVariable);
		else if (LogLevel == LogLevelCustom.Assert)
			Log.wtf(FunctionName, "Log: " + LogVariable);
	}
}
