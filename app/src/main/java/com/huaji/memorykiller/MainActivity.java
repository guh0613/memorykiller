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

public class MainActivity extends AppCompatActivity
{
	private DrawerLayout mdraw1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		mdraw1=(DrawerLayout) findViewById(R.id.draw1);
		ActionBar actionbar=getSupportActionBar();
		if (actionbar!=null)
		{
			actionbar.setDisplayHomeAsUpEnabled(true);
			actionbar.setHomeAsUpIndicator(R.drawable.__ic_menu);
		}
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
}
