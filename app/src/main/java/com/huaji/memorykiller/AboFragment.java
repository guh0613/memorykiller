
package com.huaji.memorykiller;
import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.support.annotation.*;
import android.widget.AdapterView.*;

public class AboFragment extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View v=inflater.inflate(R.layout.about_frag, container, false);
		
		return v;
	}


}
