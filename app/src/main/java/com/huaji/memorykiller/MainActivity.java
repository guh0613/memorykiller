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

public class MainActivity extends AppCompatActivity
{
	private DrawerLayout mdraw1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		
        super.onCreate(savedInstanceState);
		PgyCrashManager.register(); 
        setContentView(R.layout.main);
		android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		mdraw1=(DrawerLayout) findViewById(R.id.draw1);
		
		ActionBar actionbar=getSupportActionBar();
		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mdraw1, toolbar,R.string.draw_open,R.string.draw_close);
																		
		mDrawerToggle.syncState();//初始化状态
        mdraw1.setDrawerListener(mDrawerToggle);
		NavigationView mNavigationView=(NavigationView) findViewById(R.id.nav_view);
		mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem) {
					menuItem.setChecked(false);//点击了把它设为选中状态
					mdraw1.closeDrawers();//关闭抽屉
					return true;
					}
					});
		checkupdate();
		final TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.ET1);
		inputLayout.setHint("输入要占用的内存：");

        final EditText editText = inputLayout.getEditText();
        editText.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					Log.e("TAG", s.length() + "");
					if (s.length()==0 )
					{//字符超过5个时，出现EditText提示
						inputLayout.setError("你起码给个大小吧！");
						inputLayout.setErrorEnabled(true);
					}
					else
					{
						inputLayout.setErrorEnabled(false);
					}
				}

				@Override
				public void afterTextChanged(Editable s)
				{

				}
			});
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
	

	public void checkupdate()
	{
		new PgyUpdateManager.Builder()
			.setForced(false)
			.setUserCanRetry(true)
			.setDeleteHistroyApk(true)
			.register();
	}
}
