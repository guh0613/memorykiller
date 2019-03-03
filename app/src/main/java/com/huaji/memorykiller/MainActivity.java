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
				public void onUpdateAvailable(AppBean appBean) {
					//有更新回调此方法
					Log.d("pgyer", "there is new version can update"
						  + "new versionCode is " + appBean.getVersionCode());
					//调用以下方法，DownloadFileListener 才有效；
					//如果完全使用自己的下载方法，不需要设置DownloadFileListener
					PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
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

//文件单位
		public enum FileUnit {
			KB, MB, GB
			}

		//文件写入
		public boolean createFile(String targetFile, long fileLength, FileUnit unit) {
			//指定每次分配的块大小
			long KBSIZE = 1024;
			long MBSIZE1 = 1024 * 1024;
			long MBSIZE10 = 1024 * 1024 * 10;
			switch (unit) {
				case KB:
					fileLength = fileLength * 1024;
					break;
				case MB:
					fileLength = fileLength * 1024*1024;
					break;
				case GB:
					fileLength = fileLength * 1024*1024*1024;
					break;

				default:
					break;
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
			createFile(filePath, fileSize, FileUnit.MB);
		}
	}
}
}
