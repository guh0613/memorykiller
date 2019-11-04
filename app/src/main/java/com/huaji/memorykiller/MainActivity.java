package com.huaji.memorykiller;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class MainActivity extends AppCompatActivity
{
	private DrawerLayout mdraw1;
	private TextInputLayout filelength;
	private TextInputLayout filename;
	private TextInputLayout filepath;
	private long[] counts= new long[5];
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		
        super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		//当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
		StatusBarUtil.setRootViewFitsSystemWindows(this,true);
		//设置状态栏透明
		StatusBarUtil.setTranslucentStatus(this);
		//一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
		//所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
		
			//如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
			//这样半透明+白=灰, 状态栏的文字能看得清
			StatusBarUtil.setStatusBarColor(this,0xFF3F51B5);

        repalceFragment(new QuickFregment());
		androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
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
						Toast.makeText(MainActivity.this,"(눈_눈)",Toast.LENGTH_SHORT).show();
						
					}
						
					
				});
			dialog.show();
		}
		mdraw1 = findViewById(R.id.draw1);
		
		ActionBar actionbar=getSupportActionBar();

		androidx.appcompat.app.ActionBarDrawerToggle mDrawerToggle = new androidx.appcompat.app.ActionBarDrawerToggle(this, mdraw1, toolbar, R.string.draw_open, R.string.draw_close);
																		
		mDrawerToggle.syncState();//初始化状态
        mdraw1.setDrawerListener(mDrawerToggle);
		NavigationView mNavigationView = findViewById(R.id.nav_view);
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
						
						case R.id.nav_del:
							if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
							{
								Toast.makeText(MainActivity.this, "你是不是没给老子读写权限", Toast.LENGTH_SHORT).show();
							}
							else
							{
								repalceFragment(new DelFragment());
							}
							break;
						
						case R.id.nav_about:
							repalceFragment(new AboFragment());
							
							break;
							
						
							default:
							break;
					}
					menuItem.setChecked(false);//点击了把它设为选中状态
					mdraw1.closeDrawers();//关闭抽屉
					return true;
					}
					});


		//LitePal.deleteAll(HistoryPath.class);
			
			
			
			
		
			
			
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
			if 
			(!file.exists()) {
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

		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		return false;
	}
	


            
               
                
			
	
	static String fileUnit="KB";
		//写入根目录
	public void genfile(View v)
	{
		/*
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
			*/
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
			 
			int intfilelength=Integer.parseInt(strlength);
			if(intfilelength==0)
			{
				filelength.setErrorEnabled(true);
				filelength.setError("你想让老子占个寂寞？");
			}
			else{
				filelength.setErrorEnabled(false);
			String fileall="/data/data/com.huaji.memorykiller/files/"+strname;
			
				
				
				String[] p1={fileall,strlength,fileUnit};
				try{
			
			new CreateFileTask().execute(p1);
			HistoryPath HP = new HistoryPath();
			/*long i = (int)(Math.random()*10086110);
			HP.setId(i);*/
			HP.setName(fileall);
			HP.save();
			
			}catch(Exception e)
			{


			}
		}
	}
	}
	//写入外部储存
	public void extfile(View v)
	{
		/*
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
			*/
		filelength=findViewById(R.id.extlength);
		EditText sfilelength=filelength.getEditText();
		filename=findViewById(R.id.extname);
		EditText sfilename=filename.getEditText();
		String strname=sfilename.getText().toString();
		String strlength=sfilelength.getText().toString();
		
		filepath =findViewById(R.id.extpath);
		EditText sfilepath=filepath.getEditText();
		String strpath = sfilepath.getText().toString();
		String nstrpath = "/sdcard/"+strpath;
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
			if(strpath.trim().length()!=0)
			{
			if(!strpath.endsWith("/"))
			{
				filepath.setErrorEnabled(true);

				filepath.setError("你必须以/为结尾啊!");
			}
			else
			{
				filepath.setErrorEnabled(false);
			}
			}
		if (strname.trim().length()!=0 & strlength.trim().length()!=0 )
		{
			if (strpath.endsWith("/") | strpath.trim().length()==0)
			{
				int intfilelength=Integer.parseInt(strlength);
				if(intfilelength==0)
				{
					filelength.setErrorEnabled(true);
					filelength.setError("你想让老子占个寂寞？");
				}
				else{
					filelength.setErrorEnabled(false);
				try{
			
			String fileall=nstrpath+strname;
			String[] p1={fileall,strlength,fileUnit};
			new CreateFileTask().execute(p1);
			HistoryPath HP = new HistoryPath();
			//HP.setId((int)(Math.random()*10086110));
			HP.setName(fileall);
			HP.save();
			}catch(Exception e)
			{
				e.printStackTrace();

			}
		}
		}
	}
	}
	String quickfileall= "/sdcard/Android/data/com.huaji.memorykiller/quickfile";
	public void quickfile(View v)
	{

		TextInputLayout quickfileet = findViewById(R.id.quickfileet);
		EditText quickfileleng=quickfileet.getEditText();
		final String strqfileleng=quickfileleng.getText().toString();

		RadioGroup rdquick = findViewById(R.id.rg_quickfile);

		switch (rdquick.getCheckedRadioButtonId())
		{
			case R.id.splarge:
				try{
				
				String[] p1={quickfileall,"8888","MB"};
				new CreateFileTask().execute(p1);
					
				}catch(Exception e)
				{
					e.printStackTrace();

				}
				break;
			case R.id.large:
				
				try{
					
					String[] p1={quickfileall,"888","MB"};
					new CreateFileTask().execute(p1);
				}catch(Exception e)
				{
					e.printStackTrace();

				}
				break;
			case R.id.med:
				try{
					String[] p1={quickfileall,"88","MB"};
					new CreateFileTask().execute(p1);
				}catch(Exception e)
				{
					e.printStackTrace();

				}
				break;
			case R.id.small:
				try{
					String[] p1={quickfileall,"8","MB"};
					new CreateFileTask().execute(p1);
				}catch(Exception e)
				{
					e.printStackTrace();

				}
				break;
			case R.id.custom:
				if (strqfileleng.trim().length()==0|Integer.parseInt(strqfileleng)==0)
				{
					quickfileet.setErrorEnabled(true);
					quickfileet.setError("必须给大小才能塞东西啊!");

				}
				else
				{
					
					try{
						String[] p1={quickfileall,strqfileleng,"MB"};
						new CreateFileTask().execute(p1);
					}catch(Exception e)
					{
						e.printStackTrace();

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
		androidx.appcompat.app.AlertDialog alert_progress = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this).create();
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
		ImageView img = window.findViewById(R.id.progress_bar);  // 获取布局文件中的ImageView控件
		img.setBackgroundResource(R.drawable.huaji); // 设置图片，也可在布局文件中设置
		// 设置旋转动画
		Animation tranfrom = new RotateAnimation(0,359,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		tranfrom.setDuration(3000); // 旋转速度
		tranfrom.setFillAfter(true); 
		tranfrom.setRepeatCount(-1); // －1为一只旋转，若10，则旋转10次设定的角度后停止
		// tranfrom.cancel();  // 取消动画
		img.setAnimation(tranfrom);
    }
	public void about(View v)
	{		
		System.arraycopy(counts, 1, counts, 0, counts.length - 1);//源数组  源数组要复制的起始位置 目的数组  目的数组放置的起始位置  复制的长度
		counts[counts.length - 1] = SystemClock.uptimeMillis();
		if (counts[0] > SystemClock.uptimeMillis() - 1500)
		{
			Toast.makeText(MainActivity.this, "你好", Toast.LENGTH_SHORT).show();
			counts= new long[5];
		}
	}
	
	public void genfilede(View v)
	{
		
		final File quickfile=new File("/data/data/com.huaji.memorykiller/files");
		try 
		{
			AlertDialog.Builder dequ=new AlertDialog.Builder(MainActivity.this);
			dequ.setTitle("删除");
			dequ.setMessage("确定要删除已创建的所有内存吗？");
			dequ.setPositiveButton("老子确定了", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,int which)
					{
						File[] files = quickfile.listFiles();
						if(files.length!=0)
						{
							Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
						
						for (int i = 0; i < files.length; i++) {
							File f = files[i];
							f.delete();
						}
						}
						else
						{
							Toast.makeText(MainActivity.this,"没有找到内存文件，删除失败",Toast.LENGTH_SHORT).show();
						}
						
					}
				});
			dequ.setNegativeButton("老子后悔了", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,int which)
					{
						Toast.makeText(MainActivity.this,"(눈_눈)",Toast.LENGTH_SHORT).show();
					}
				});
			dequ.show();


		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	public void quickfilede(View v)
	{
		final File quickfile=new File("/storage/emulated/0/Android/data/com.huaji.memorykiller","quickfile");
		try 
		{
			AlertDialog.Builder dequ=new AlertDialog.Builder(MainActivity.this);
			dequ.setTitle("删除");
			dequ.setMessage("确定要删除已创建的内存吗？");
			dequ.setPositiveButton("老子确定了", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,int which)
					{
						if (quickfile.exists())
						{
							quickfile.delete();
							Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();

						}
						else
						{
							Toast.makeText(MainActivity.this,"没有找到内存文件，删除失败",Toast.LENGTH_SHORT).show();

						}
					}
				});
			dequ.setNegativeButton("老子后悔了", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,int which)
					{
						Toast.makeText(MainActivity.this,"(눈_눈)",Toast.LENGTH_SHORT).show();
					}
				});
			dequ.show();


		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	public void oldquickfilede(View v)
	{
		final File quickfile1=new File("/storage/emulated/0","quickfile");
		try 
		{
			AlertDialog.Builder dequ=new AlertDialog.Builder(MainActivity.this);
			dequ.setTitle("删除旧版文件");
			dequ.setMessage("如果你曾经使用1.17及更早版本创建过内存，可以在这里删除。");
			dequ.setPositiveButton("老子确定了", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,int which)
					{
						if (quickfile1.exists())
						{
							quickfile1.delete();
							Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();

						}
						else
						{
							Toast.makeText(MainActivity.this,"没有找到内存文件，删除失败",Toast.LENGTH_SHORT).show();

						}
					}
				});
			dequ.setNegativeButton("老子后悔了", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,int which)
					{
						Toast.makeText(MainActivity.this,"(눈_눈)",Toast.LENGTH_SHORT).show();
					}
				});
			dequ.show();


		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	 class CreateFileTask extends AsyncTask<String,Integer,Boolean>
	{


		android.app.ProgressDialog progress=new android.app.ProgressDialog(MainActivity.this);


		@Override
		protected void onPreExecute()
		{

			progress.setTitle("占用内存");
			progress.setMessage("占用内存中...");
			progress.setCancelable(false);
			progress.show();

			// TODO: Implement this method

		}
		public String lastpath;
		@Override
		protected Boolean doInBackground(String[] p1)
		{

			String fileall1=p1[0];
			lastpath=fileall1;
			String filelength=p1[1];
			int filelength1=Integer.parseInt(filelength);
			String fileunit1=p1[2];
			publishProgress(filelength1);

			return createFile(fileall1, filelength1, fileunit1);
			// TODO: Implement this method

		}

		@Override
		protected void onProgressUpdate(Integer ... values)
		{
			// TODO: Implement this method
			progress.setMessage("正在写入文件...");
		}



		@Override
		protected void onPostExecute(Boolean result)
		{
			progress.dismiss();
			if(result)
			{
				Toast.makeText(MainActivity.this,"写入完成！",Toast.LENGTH_SHORT).show();
				
				
			}
			else
			{
				Toast.makeText(MainActivity.this,"写入时出现问题",Toast.LENGTH_SHORT).show();
			}
			// TODO: Implement this method

		}




	}
	
}
