package com.huaji.memorykiller;
import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.support.annotation.*;
import android.widget.AdapterView.*;

public class ExtFragment extends Fragment
{
	
	@Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
   {
	   View v=inflater.inflate(R.layout.ext_frag, container, false);
	   Spinner Storage_unit = (Spinner) v.findViewById(R.id.Se);
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
