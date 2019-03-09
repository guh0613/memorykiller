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
	private TextInputLayout filelength;
	private TextInputLayout filename;
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


	//文件写入
	public boolean createFile(String targetFile, long fileLength, String unit) {
		//指定每次分配的块大小
		long KBSIZE = 1024;
		long MBSIZE1 = 1024 * 1024;
		long MBSIZE10 = 1024 * 1024 * 10;
		long MBSIZE100=1024*1024*100;
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
        if (unit=="TB")
		{
			fileLength=fileLength*1024*1024*1024*1024;
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
			if (fileLength >MBSIZE100){
				batchSize = MBSIZE100;
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
	static String fileUnit;
		//写入根目录
	public void genfile(View v)
	{
		
		Spinner Storage_unit = (Spinner) findViewById(R.id.Se);
		String[] mItems = getResources().getStringArray(R.array.unit);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Storage_unit.setAdapter(adapter);
		Storage_unit.setOnItemSelectedListener(new OnItemSelectedListener()
			{
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					String[] unit = getResources().getStringArray(R.array.unit);

					if(unit[pos].equals("KB"))
					{

						fileUnit="KB";
					}
					if(unit[pos].equals("MB"))
					{

						fileUnit="MB";
					}
					if(unit[pos].equals("GB"))
					{

						fileUnit="GB";
					}
					if(unit[pos].equals("TB"))
					{

						fileUnit="TB";
					}


				}
				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
				}
			});
		filelength=findViewById(R.id.genlength);
		EditText sfilelength=filelength.getEditText();
		filename=findViewById(R.id.genname);
		EditText sfilename=filename.getEditText();
		String strname=sfilename.getText().toString();
		String strlength=sfilelength.getText().toString();
		int fileLength=Integer.parseInt(strlength);
		if (strlength.trim().length()==0)
		{
			filelength.setErrorEnabled(true);
			filelength.setError("你至少给个大小吧！");
		}
		
		
			if(strname.trim().length()==0)
			{
				filename.setErrorEnabled(true);
				filename.setError("你至少给个名字吧!");
			}
		if (strname.trim().length()!=0 & strlength.trim().length()!=0)
		{
			String fileall="/data/data/com.huaji.memorykiller/files/"+strname;
			createFile(fileall,fileLength,fileUnit);
		}
	}
}
