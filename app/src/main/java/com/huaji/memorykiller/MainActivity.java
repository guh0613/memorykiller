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
import android.support.v4.content.*;
import android.*;
import android.content.pm.*;
import android.view.animation.*;

public class MainActivity extends AppCompatActivity
{
	private DrawerLayout mdraw1;
	private TextInputLayout filelength;
	private TextInputLayout filename;
	private TextInputLayout filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		
        super.onCreate(savedInstanceState);
		PgyCrashManager.register(); 
		setContentView(R.layout.main);
		
        repalceFragment(new QuickFregment());
		android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
		{
			AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
			dialog.setTitle("权限授予");
			dialog.setMessage("应用检测到你没有授予基本的储存权限。如果没有授予储存权限，将不能愉快地占内存。");
			dialog.setCancelable(false);
			dialog.setPositiveButton("明白了，授予", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog,int which)
				{
					ActivityCompat.requestPermissions(MainActivity.this,new 
					String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
				}
			});
			dialog.setNegativeButton("老子就是不给", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,int which)
					{

					}
				});
			dialog.show();
		}
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
							Toast.makeText(MainActivity.this,"老子太忙了，没时间写关于页",Toast.LENGTH_SHORT).show();
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
				Toast.makeText(MainActivity.this,"老子太忙了，没时间写关于页",Toast.LENGTH_SHORT).show();
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
	

	public void checkupdate()
	{
		new PgyUpdateManager.Builder()
			.setForced(false)
			.setUserCanRetry(true)
			.setDeleteHistroyApk(true)
			.register();
	}
	static String fileUnit="KB";
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
		
		if(strlength.trim().length()==0)
		{
			filelength.setErrorEnabled(true);
			filelength.setError("你至少给个大小吧!");
		}
		else
		{
			filelength.setErrorEnabled(false);
		}
		
		
			if(strname.trim().length()==0)
			{
				
				filename.setErrorEnabled(true);
				filename.setError("你至少给个名字吧!");
			}
			else
			{
				filename.setErrorEnabled(false);
			}
		if (strname.trim().length()!=0 & strlength.trim().length()!=0)
		{
			int fileLength=Integer.parseInt(strlength);
			try{
			String fileall="/data/data/com.huaji.memorykiller/files/"+strname;
			createFile(fileall,fileLength,fileUnit);
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally
			{
				Toast.makeText(MainActivity.this,"写入完成！",Toast.LENGTH_SHORT).show();
			}
		}
	}
	//写入外部储存
	public void extfile(View v)
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
		filelength=findViewById(R.id.extlength);
		EditText sfilelength=filelength.getEditText();
		filename=findViewById(R.id.extname);
		EditText sfilename=filename.getEditText();
		String strname=sfilename.getText().toString();
		String strlength=sfilelength.getText().toString();
		
		filepath =findViewById(R.id.extpath);
		EditText sfilepath=filepath.getEditText();
		String strpath = sfilepath.getText().toString();
		strpath = "/sdcard/"+strpath;
		if (strlength.trim().length()==0)
		{
			filelength.setErrorEnabled(true);
			filelength.setError("你至少给个大小吧！");
		}
		else
		{
			filelength.setErrorEnabled(false);
		}
		
		
			if(strname.trim().length()==0)
			{
				
				filename.setErrorEnabled(true);
				filename.setError("你至少给个名字吧!");
			}
			else
			{
				filename.setErrorEnabled(false);
			}
			if(!strpath.endsWith("/"))
			{
				filepath.setErrorEnabled(true);

				filepath.setError("你必须以/为结尾啊!");
			}
			else
			{
				filepath.setErrorEnabled(false);
			}
		if (strname.trim().length()!=0 & strlength.trim().length()!=0 & strpath.endsWith("/"))
		{
			
			try{
			showGifDialog1();
			int fileLength=Integer.parseInt(strlength);
			String fileall=strpath+strname;
			createFile(fileall,fileLength,fileUnit);
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally
			{
				Toast.makeText(MainActivity.this,"写入完成！",Toast.LENGTH_SHORT).show();
				
			}
		}
	}
	
	String quickfileall="/sdcard/quickfile";
	public void quickfile(View v)
	{

		TextInputLayout quickfileet=(TextInputLayout) findViewById(R.id.quickfileet);
		EditText quickfileleng=quickfileet.getEditText();
		final String strqfileleng=quickfileleng.getText().toString();

		RadioGroup rdquick=(RadioGroup) findViewById(R.id.rg_quickfile);

		switch (rdquick.getCheckedRadioButtonId())
		{
			case R.id.splarge:
				try{
				int Lfileleng=8888;
				createFile(quickfileall,Lfileleng,"MB");
				}catch(Exception e)
				{
					e.printStackTrace();
				}finally
				{
					Toast.makeText(MainActivity.this,"写入完成！",Toast.LENGTH_SHORT).show();
					
				}
				break;
			case R.id.large:
				try{
				int lfileleng=888;
				createFile(quickfileall,lfileleng,"MB");
				}catch(Exception e)
				{
					e.printStackTrace();
			
				}finally
				{
					Toast.makeText(MainActivity.this,"写入完成！",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.med:
				try{
				int mfileleng=88;
				createFile(quickfileall,mfileleng,"MB");
				}catch(Exception e)
				{
					e.printStackTrace();
				}finally
				{
					Toast.makeText(MainActivity.this,"写入完成！",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.small:
				try{
				int sfileleng=8;
				createFile(quickfileall,sfileleng,"MB");
				}catch(Exception e)
				{
					e.printStackTrace();
				}finally
				{
					Toast.makeText(MainActivity.this,"写入完成！",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.custom:
				if (strqfileleng.trim().length()==0)
				{
					quickfileet.setErrorEnabled(true);
					quickfileet.setError("必须给大小才能塞东西啊!");

				}
				else
				{
					try{
					final int qfileleng=Integer.parseInt(strqfileleng);
					createFile(quickfileall,qfileleng,"MB");
					}catch(Exception e)
					{
						e.printStackTrace();
					}finally
					{
						Toast.makeText(MainActivity.this,"写入完成！",Toast.LENGTH_SHORT).show();
					}
				}
				break;
			default:
				Toast.makeText(MainActivity.this,"请选择一个大小",Toast.LENGTH_SHORT).show();
				break;
		}
	}
	public void showGifDialog1() 
	{
        AlertDialog alert_progress = new AlertDialog.Builder(MainActivity.this).create();
		alert_progress.show(); 
		alert_progress.setCancelable(false); // 点击背景时对话框不会消失
		// alert_progress.dismiss(); // 取消对话框
		Window window = alert_progress.getWindow();
		window.setContentView(R.layout.dialog_gif); //加载自定义的布局文件
		WindowManager.LayoutParams wm = window.getAttributes();
		wm.width = 666; // 设置对话框的宽
		wm.height = 666; // 设置对话框的高
		wm.alpha = 1f;   // 对话框背景透明度
		wm.dimAmount = 0.5f; // 遮罩层亮度
		window.setAttributes(wm); 
		ImageView img = (ImageView)window.findViewById(R.id.progress_bar);  // 获取布局文件中的ImageView控件
		img.setBackgroundResource(R.drawable.huaji); // 设置图片，也可在布局文件中设置
		// 设置旋转动画
		Animation tranfrom = new RotateAnimation(0,359,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		tranfrom.setDuration(3000); // 旋转速度
		tranfrom.setFillAfter(true); 
		tranfrom.setRepeatCount(-1); // －1为一只旋转，若10，则旋转10次设定的角度后停止
		// tranfrom.cancel();  // 取消动画
		img.setAnimation(tranfrom);
    }
}
