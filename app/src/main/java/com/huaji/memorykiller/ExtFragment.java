package com.huaji.memorykiller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

public class ExtFragment extends Fragment
{
	
	@Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
   {
	   View v=inflater.inflate(R.layout.ext_frag, container, false);
	   Spinner Storage_unit = v.findViewById(R.id.Se);
	   String[] mItems = getResources().getStringArray(R.array.unit);
	   ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, mItems);
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
					   MainActivity.fileUnit="KB";
				} 
					if(unit[pos].equals("MB"))
					{

						MainActivity.fileUnit="MB";
					}
					if(unit[pos].equals("GB"))
					{

						MainActivity.fileUnit="GB";
					}
					if(unit[pos].equals("TB"))
					{

						MainActivity.fileUnit="TB";
					}


				}
				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
				}
			});
			return v;
   }
 

}
