package com.huaji.memorykiller;
import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import android.support.design.widget.*;
import android.widget.*;
import android.text.*;
import android.util.*;
import android.support.v7.widget.*;
import java.util.*;
import com.huaji.memorykiller.*;
import org.litepal.*;

public class DelFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view=inflater.inflate(R.layout.delete_frag,container,false);
		// TODO: Implement this method
		//Something();
		
		List<HistoryPath> mList= LitePal.findAll(HistoryPath.class);
		
		
		
		
		RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(mList);
        recyclerView.setAdapter(adapter);
		
    
	/*private void Something()
	{

		AlarmTime one= new AlarmTime( "13:00");
		mList.add(one);


	}*/
	
		return view;

	}


}
