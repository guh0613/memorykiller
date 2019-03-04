package com.huaji.memorykiller;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import java.io.*;
import android.content.*;
import java.nio.channels.*;
import java.nio.*;
import android.provider.*;
import android.*;
import java.security.*;
import com.pgyersdk.crash.*;
import com.pgyersdk.update.*;
import android.util.*;
import com.pgyersdk.update.javabean.*;
import android.net.*;
import android.widget.AdapterView.*;

public class MainActivity extends Activity 
{
	private EditText edit;
	private EditText editname;
	private EditText editafter;
	private EditText editpath;
	
    
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		PgyCrashManager.register();
		/** 新版本 **/
		new PgyUpdateManager.Builder()
			.setForced(false)                //设置是否强制提示更新,非自定义回调更新接口此方法有用
			.setUserCanRetry(true)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
			.setDeleteHistroyApk(true)     // 检查更新前是否删除本地历史 Apk， 默认为true
			.setUpdateManagerListener(new UpdateManagerListener() {
				@Override
				public void onNoUpdateAvailable() {
					//没有更新是回调此方法
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
			
			
		//这里是选择单位的地方
		Spinner Storage_unit = (Spinner) findViewById(R.id.S1);
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
		});
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
		edit=(EditText) findViewById(R.id.ET1);//获取文件大小
		String text=edit.getText().toString();
		if(text.equals(""))//我要检测空格了
		{
			Toast.makeText(MainActivity.this,"嗯?你想让老子占个寂寞?",Toast.LENGTH_LONG).show();
		}
		else
		{
		//检测文件名是否为空
		editname =(EditText) findViewById(R.id.ETname);
		String filename=editname.getText().toString();
		if(filename.trim().length()==0)
		{
			Toast.makeText(MainActivity.this,"你至少给个名字吧！",Toast.LENGTH_SHORT).show();
		}
		else
		{
		//获得后缀名
		editafter=(EditText) findViewById(R.id.ETafter);
		String fileafter=editafter.getText().toString();
			//由于写入文件时应用处理无响应，toast会在处理完成后弹出
			Toast.makeText(MainActivity.this,"写入完成!",Toast.LENGTH_LONG).show();
			edit=(EditText) findViewById(R.id.ET1);//获取文件大小
			String input=edit.getText().toString();
			int filelength = Integer.parseInt(input);//转换成int型
			//写入文件
			String data="1";
			FileOutputStream out=null;
			BufferedWriter writer=null;
			try{
				out=openFileOutput(filename+"."+fileafter,Context.MODE_APPEND);
				writer=new BufferedWriter(new OutputStreamWriter(out));
				//通过for循环写入1
				for(int a=0;a<filelength*1024*1024;a++)
				{
					writer.write(data);
				}
			
			} catch(IOException e){
				e.printStackTrace();
				PgyCrashManager.reportCaughtException(e);
			}
		
	    }
	}
	}
	//写入外部储存
	public void b2(View view)
	{
		
		edit=(EditText) findViewById(R.id.ET1);//获取文件大小
		String text=edit.getText().toString();
		//同样的啥都没写就
		if(text.equals(""))
		{
			Toast.makeText(MainActivity.this,"你想让老子占个寂寞吗?",Toast.LENGTH_LONG).show();
		}
		else
		{
			editname =(EditText) findViewById(R.id.ETname);
			String filename=editname.getText().toString();
			if(filename.trim().length()==0)
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
						editafter=(EditText) findViewById(R.id.ETafter);
						String fileafter=editafter.getText().toString();
						//获得路径
						editpath=(EditText) findViewById(R.id.ETpath);
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
	public boolean onCreateOptionsMenu(Menu menu)
	
{
	getMenuInflater().inflate(R.menu.main , menu);
	return true;
	}

	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		{
			switch (item.getItemId())
			{
				case R.id.about:
					showNormalDialog();
					break;
				case R.id.updata:
					Toast.makeText(this , "我不知道怎么更新啊" ,Toast.LENGTH_SHORT).show();
					finish();
					break;
				default:
			}
			return true;
		}
	}
	private void showNormalDialog(){
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
      public void onClick(DialogInterface dialog, int which) {
        //...To-do
      }
    });
    // 显示
    normalDialog.show();
  }
}
