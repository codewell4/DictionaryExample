package com.codewell4.DictionaryExample;

import java.util.List;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.WindowManager;

public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	private static ListPreference chosenSort;
	private static String version;
	private static Preference versionNumber;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);

			if (Common.IsAppFullscreen())
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName.toString();
			
			if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB)
			{
				addPreferencesFromResource(R.xml.settings);

				versionNumber		= (Preference) findPreference(Common.LBL_VERSION_NUMBER);
				chosenSort 			= (ListPreference) findPreference(Common.SORT_LIST);
				
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				if (settings.getString(Common.SORT_LIST, "").trim().length()>0)
				{
					chosenSort.setSummary(chosenSort.getSummary() + "\n" + getString(R.string.SortedBy) + ": " + chosenSort.getEntry());
				}
				versionNumber.setSummary(version);
			}
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}

	@Override
	@TargetApi(11)
	public void onBuildHeaders(List<Header> target)
	{
		try
		{
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
				loadHeadersFromResource(R.xml.headers, target);
		}
		catch (Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}

	@TargetApi(11)
	public static class SettingsFragment extends PreferenceFragment
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			try
			{
				super.onCreate(savedInstanceState);
				addPreferencesFromResource(R.xml.settings);

				SharedPreferences settings = getPreferenceScreen().getSharedPreferences();
				chosenSort = (ListPreference) findPreference(Common.SORT_LIST);
				if (settings.getString(Common.SORT_LIST, "").trim().length()>0)
				{
					chosenSort.setSummary(getString(R.string.SettingsSortBySummary) + "\n" + getString(R.string.SortedBy) + ": " + chosenSort.getEntry());
				}
			}
			catch (Exception e)
			{
				Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
			}
		}
	}

	@TargetApi(11)
	public static class GeneralInfoFragment extends PreferenceFragment
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			try
			{
				super.onCreate(savedInstanceState);
				addPreferencesFromResource(R.xml.settings_info);
				versionNumber = (Preference) findPreference(Common.LBL_VERSION_NUMBER);
				versionNumber.setSummary(version);
			}
			catch (Exception e)
			{
				Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume()
	{
		super.onResume();
		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB)
			getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		else
			PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause()
	{
		super.onPause();
		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB)
			getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		else
			PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
	}

	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		try
		{
			if (key.equals(Common.SORT_LIST))
			{
				if (sharedPreferences.getString(key, "").trim().length()>0)
				{
					chosenSort.setSummary(getString(R.string.SettingsSortBySummary) + "\n" + getString(R.string.SortedBy) + ": " + chosenSort.getEntry());
				}
			}
		}
		catch(Exception e)
		{
			Common.LogFunction(Common.LogLevelCustom.Error, e.getStackTrace()[0].getMethodName(), e.getMessage());
		}
	}
}