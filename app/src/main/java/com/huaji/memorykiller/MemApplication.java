package com.huaji.memorykiller;
import android.app.*;
import android.content.*;
import com.pgyersdk.crash.*;

public class MemApplication extends Application
{
	private  static Context context;

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		context=getApplicationContext();
		PgyCrashManager.register();
	}
	public static Context getContext()
	{
		return context;
	}
}
