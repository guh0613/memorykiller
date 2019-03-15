package com.huaji.memorykiller;
import android.app.*;
import android.content.*;

public class MemApplication extends Application
{
	private  static Context context;

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		context=getApplicationContext();
	}
	public static Context getContext()
	{
		return context;
	}
}
