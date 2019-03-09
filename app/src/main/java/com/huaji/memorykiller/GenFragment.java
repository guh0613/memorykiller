package com.huaji.memorykiller;
import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import android.support.design.widget.*;
import android.widget.*;
import android.util.*;
import android.text.*;
import android.view.View.*;

public class GenFragment extends Fragment implements View.OnClickListener
{
private TextInputLayout filelength;
private TextInputLayout filename;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view=inflater.inflate(R.layout.gen_frag,container,false);
		// TODO: Implement this method
		return view;
	}

	@Override
	public void onClick(View p1)
	{
		
		}
		// TODO: Implement this method
	

	
}

