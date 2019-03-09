package com.huaji.memorykiller;

import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import com.pgyersdk.crash.*;
import com.pgyersdk.update.*;
import android.util.*;
import com.pgyersdk.update.javabean.*;
import android.content.*;
import java.net.*;
import android.net.*;
import android.view.*;
import android.support.v4.widget.*;
import android.support.v4.view.*;
import android.support.design.widget.*;
import android.text.*;
import android.widget.*;
import java.io.*;
import java.nio.channels.*;
import java.nio.*;
import android.support.v4.app.*;
import android.widget.AdapterView.*;

public class MainActivity extends AppCompatActivity
{
	private DrawerLayout mdraw1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		
        super.onCreate(savedInstanceState);
		PgyCrashManager.register(); 
		setContentView(R.layout.main);
        repalceFragment(new QuickFregment());
		android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		mdraw1=(DrawerLayout) findViewById(R.id.draw1);
		
		ActionBar actionbar=getSupportActionBar();
		android.support.v7.app.ActionBarDrawerToggle mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mdraw1, toolbar,R.string.draw_open,R.string.draw_close);
																		
		mDrawerToggle.syncState();//初始化状态
        mdraw1.setDrawerListener(mDrawerToggle);
		NavigationView mNavigationView=(NavigationView) findViewById(R.id.nav_view);
		mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem) {
					switch (menuItem.getItemId()) 
					{
						case R.id.nav_gen:
							repalceFragment(new GenFragment());
							break;
						case R.id.nav_quickstart:
							repalceFragment(new QuickFregment());
							break;
						case R.id.nav_external:
							repalceFragment(new ExtFragment());
							break;
						case R.id.aboutnav:
							break;
							default:
							break;
					}
					menuItem.setChecked(false);//点击了把它设为选中状态
					mdraw1.closeDrawers();//关闭抽屉
					return true;
					}
					});
		checkupdate();
		

	}
	
	

		
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		getMenuInflater().inflate(R.menu.main,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.about:
			break;
			case R.id.updata:
			checkupdate();
			break;
					
		}
		// TODO: Implement this method
		return true;
	}
	private void repalceFragment(Fragment fregment)
	{
		FragmentManager fragmentmanager=getSupportFragmentManager();
		FragmentTransaction transaction=fragmentmanager.beginTransaction();
		transaction.replace(R.id.mainframe,fregment);
		transaction.commit();
	}
	//文件单位
	public static String fileUnit ="MB";

	//文件写入
	public boolean createFile(String targetFile, long fileLength, String unit) {
		//指定每次分配的块大小
		long KBSIZE = 1024;
		long MBSIZE1 = 1024 * 1024;
		long MBSIZE10 = 1024 * 1024 * 10;
		if(unit=="KB")
		{
			fileLength = fileLength * 1024;
		}
		if(unit=="MB")
		{
			fileLength = fileLength * 1024*1024;
		}
		if(unit=="GB")
		{
			fileLength = fileLength * 1024*1024*1024;
		}

			
		
		FileOutputStream fos = null;
		File file = new File(targetFile);
		try {
//如果文件存在
			if (!file.exists()) {
				file.createNewFile();
			}

			long batchSize = 0;
			batchSize = fileLength;
			if (fileLength > KBSIZE) {
				batchSize = KBSIZE;
			}
			if (fileLength > MBSIZE1) {
				batchSize = MBSIZE1;
			}
			if (fileLength > MBSIZE10) {
				batchSize = MBSIZE10;
			}
			long count = fileLength / batchSize;
			long last = fileLength % batchSize;

			fos = new FileOutputStream(file);
			FileChannel fileChannel = fos.getChannel();
			for (int i = 0; i < count; i++) {
				ByteBuffer buffer = ByteBuffer.allocate((int) batchSize);
				fileChannel.write(buffer);

			}
			if (last != 0) {
				ByteBuffer buffer = ByteBuffer.allocate((int) last);
				fileChannel.write(buffer);
			}
			fos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			PgyCrashManager.reportCaughtException(e);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				PgyCrashManager.reportCaughtException(e);
			}
		}
		return false;
	}
	public void b1(View view)
	{
		Toast.makeText(MainActivity.this,"haha",Toast.LENGTH_SHORT).show();
	}

	public void checkupdate()
	{
		new PgyUpdateManager.Builder()
			.setForced(false)
			.setUserCanRetry(true)
			.setDeleteHistroyApk(true)
			.register();
	}
		//OnItemSelected监听器
	private class  UnitOnItemSelectedListener implements OnItemSelectedListener
	{		
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
		{
			String[] unit = getResources().getStringArray(R.array.unit);
			
			if(unit[pos].equals("KB"))
			{
				Toast.makeText(MainActivity.this,"娘炮才选KB", 2000).show();
				fileUnit="KB";
			}
			if(unit[pos].equals("MB"))
			{
				Toast.makeText(MainActivity.this,"你选的是MB", 2000).show();
				fileUnit="MB";
			}
			if(unit[pos].equals("GB"))
			{
				Toast.makeText(MainActivity.this, "GB，还行吧", 2000).show();
				fileUnit="GB";
			}
			if(unit[pos].equals("TB"))
			{
				Toast.makeText(MainActivity.this, "真男人都选TB",2000).show();
				fileUnit="无";
			}
				
				
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent)
		{
		}
	}			
	public void onNothingSelected(AdapterView<?> arg0) 
	{
		
	}
		//写入外部储存
	public void b2(View view)
	{
		EditText edit =(EditText) findViewById(R.id.extlength2);//获取文件大小
		String text=edit.getText().toString();
		EditText editname =(EditText) findViewById(R.id.extname2);
		String filename=editname.getText().toString();
		//由于写入文件时应用处理无响应，toast会在处理完成后弹出
		Toast.makeText(MainActivity.this,"写入完成!",Toast.LENGTH_LONG).show();
		//获得路径
		EditText editpath=(EditText) findViewById(R.id.extlocate3);
		String filepath=editpath.getText().toString();
		edit=(EditText) findViewById(R.id.extlength2);//获取文件大小
		String input=edit.getText().toString();
		int filelength = Integer.parseInt(input);//转换成int型
		//写入文件
		String filePath="/sdcard/"+filepath+filename;
		int fileSize=filelength;
		createFile(filePath,fileSize,fileUnit);
	}
}
