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
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;
import java.nio.channels.*;
import java.nio.*;

public class MainActivity extends AppCompatActivity
{
	private DrawerLayout mdraw1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);	
		mdraw1=(DrawerLayout) findViewById(R.id.draw1);
		ActionBar actionbar=getSupportActionBar();
		if (actionbar!=null)
		{
			actionbar.setDisplayHomeAsUpEnabled(true);
			actionbar.setHomeAsUpIndicator(R.drawable.__ic_menu);
		}
		checkupdate();
		
		//这里是选择单位的地方
		Spinner Storage_unit = (Spinner) findViewById(R.id.S1);
		String[] mItems = getResources().getStringArray(R.array.unit);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Storage_unit.setAdapter(adapter);
		Storage_unit.setOnItemSelectedListener(new UnitOnItemSelectedListener());
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
			showAboutDialog();
			break;
			case R.id.updata:
			checkupdate();
			break;
			case R.id.home:
			mdraw1.openDrawer(GravityCompat.START);
			break;
		}
		// TODO: Implement this method
		return true;
	}
	

	public void checkupdate()
	{
		/** 新版本 **/
		new PgyUpdateManager.Builder()
			.setForced(false)                //设置是否强制提示更新,非自定义回调更新接口此方法有用
			.setUserCanRetry(true)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
			.setDeleteHistroyApk(true)     // 检查更新前是否删除本地历史 Apk， 默认为true
			.setUpdateManagerListener(new UpdateManagerListener() {
				@Override
				public void onNoUpdateAvailable() {
					//没有更新是回调此方法
					Toast.makeText(MainActivity.this,"老子懒，没更新",Toast.LENGTH_LONG).show();
					Log.d("pgyer", "there is no new version");
				}
				@Override
				public void onUpdateAvailable(final AppBean appBean) {
					//有更新回调此方法
					Log.d("pgyer", "there is new version can update"
						  + "new versionCode is " + appBean.getVersionCode());
					AlertDialog.Builder dialog = new AlertDialog.Builder (MainActivity.this);
					dialog.setTitle("更新提醒");
					dialog.setMessage("应用有新版本，要不要更新？");
					dialog.setCancelable(false);
					dialog.setPositiveButton("好，更新",new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,int which)
							{
								PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
							}
						});
					dialog.setNegativeButton("老子才不更新",new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,int which)
							{

							}
						});
					dialog.show();
					//调用以下方法，DownloadFileListener 才有效；
					//如果完全使用自己的下载方法，不需要设置DownloadFileListener

				}

				@Override
				public void checkUpdateFailed(Exception e) {
					//更新检测失败回调
					//更新拒绝（应用被下架，过期，不在安装有效期，下载次数用尽）以及无网络情况会调用此接口
					Log.e("pgyer", "check update failed ", e);
                }
            })
            //注意 ：
            //下载方法调用 PgyUpdateManager.downLoadApk(appBean.getDownloadURL()); 此回调才有效
            //此方法是方便用户自己实现下载进度和状态的 UI 提供的回调
            //想要使用蒲公英的默认下载进度的UI则不设置此方法
            .setDownloadFileListener(new DownloadFileListener() {   
                @Override
                public void downloadFailed() {
                    //下载失败
                    Log.e("pgyer", "download apk failed");
                }

                @Override
                public void downloadSuccessful(Uri uri) {
                    Log.e("pgyer", "download apk failed");
                    // 使用蒲公英提供的安装方法提示用户 安装apk
                    PgyUpdateManager.installApk(uri);  
				}

                @Override
                public void onProgressUpdate(Integer... integers) {
                    Log.e("pgyer", "update download apk progress" + integers);
                }})
			.register();
	

	}
		public static String fileUnit ="";
		
		
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
		
	
	//写入内部储存
	public void b1(View view)
	{
		//如果这个东西啥都没写
		EditText edit=(EditText) findViewById(R.id.ET1);//获取文件大小
		String text=edit.getText().toString();
		if(text.equals(""))//我要检测空格了
		{
			Toast.makeText(MainActivity.this,"嗯?你想让老子占个寂寞?",Toast.LENGTH_LONG).show();
		}
		else
		{
			//检测文件名是否为空
			EditText editname =(EditText) findViewById(R.id.ETname);
			String filename=editname.getText().toString();
			if(filename.equals(""))
			{
				Toast.makeText(MainActivity.this,"你至少给个名字吧！",Toast.LENGTH_SHORT).show();
			}
			else
			{
				if(fileUnit.equals("无"))
				{
					Toast.makeText(MainActivity.this,"但是我拒绝!",Toast.LENGTH_SHORT).show();
				}
				else
				{
					//获得后缀名
					EditText editafter=(EditText) findViewById(R.id.ETafter);
					String fileafter=editafter.getText().toString();
					//由于写入文件时应用处理无响应，toast会在处理完成后弹出
					Toast.makeText(MainActivity.this,"写入完成!",Toast.LENGTH_LONG).show();
					EditText editmuch=(EditText) findViewById(R.id.ET1);//获取文件大小
					String input=editmuch.getText().toString();
					int filelength = Integer.parseInt(input);//转换成int型
					//写入文件
					String data="1";
					
					FileOutputStream out=null;
					BufferedWriter writer=null;
					
					if(fileUnit=="KB")
					{
						filelength = (filelength+8) * 1024;
					}
					if(fileUnit=="MB")
					{
						filelength = ((filelength * 1024) +8)* 1024;
					}
					if(fileUnit=="GB")
					{
						filelength = ((filelength * 1024 * 1024)+8) * 1024;
					}
					try
					{
						
						out=openFileOutput(filename+"."+fileafter,Context.MODE_APPEND);
						writer=new BufferedWriter(new OutputStreamWriter(out));
						//通过for循环写入1
						for(int a=0;a<=filelength;a++)
						{
							writer.write(data);
						}
			
					}
					    
					catch(IOException e)
					{
					e.printStackTrace();
					PgyCrashManager.reportCaughtException(e);
					
					}
					
				}
			}
		}
	}
	//写入外部储存
	public void b2(View view)
	{
		
		EditText edit =(EditText) findViewById(R.id.ET1);//获取文件大小
		String text=edit.getText().toString();
		//同样的啥都没写就
		if(text.equals(""))
		{
			Toast.makeText(MainActivity.this,"你想让老子占个寂寞吗?",Toast.LENGTH_LONG).show();
		}
		else
		{
			EditText editname =(EditText) findViewById(R.id.ETname);
			String filename=editname.getText().toString();
			if(filename.equals(""))
			{
				Toast.makeText(MainActivity.this,"你至少给个名字吧！",Toast.LENGTH_SHORT).show();
			}
			else
			{
				if(fileUnit.equals("无"))
				{
					Toast.makeText(MainActivity.this,"但是我拒绝!",Toast.LENGTH_SHORT).show();
				}
				else
				{
					//由于写入文件时应用处理无响应，toast会在处理完成后弹出
					Toast.makeText(MainActivity.this,"写入完成!",Toast.LENGTH_LONG).show();
						//获得后缀名
						EditText editafter=(EditText) findViewById(R.id.ETafter);
						String fileafter=editafter.getText().toString();
						//获得路径
						EditText editpath=(EditText) findViewById(R.id.ETpath);
						String filepath=editpath.getText().toString();
					edit=(EditText) findViewById(R.id.ET1);//获取文件大小
					String input=edit.getText().toString();
					int filelength = Integer.parseInt(input);//转换成int型
					//写入文件
					String filePath="/sdcard/"+filepath+filename+"."+fileafter;
					int fileSize=filelength;
					createFile(filePath, fileSize,fileUnit);
				}
			}
		}
	}

	
	private void showAboutDialog()
	{
    	/* @setIcon 设置对话框图标
     	* @setTitle 设置对话框标题
     	* @setMessage 设置对话框消息提示
     	* setXXX方法返回Dialog对象，因此可以链式设置属性
    	*/
   		final AlertDialog.Builder normalDialog = 
    	new AlertDialog.Builder(MainActivity.this);
    	normalDialog.setTitle("没想好");
    	normalDialog.setMessage("我还没想好");
    	normalDialog.setPositiveButton("老子知道了", 
      	new DialogInterface.OnClickListener() {
      		@Override
      		public void onClick(DialogInterface dialog, int which)
			{
        
      		}
    	});
    	normalDialog.show();
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
			if(unit[pos].equals("TB(有种就试试)"))
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
	
}
