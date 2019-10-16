package com.huaji.memorykiller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.List;

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
		recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
		
        recyclerView.setAdapter(adapter);
		/*
		DefaultItemAnimator animator = new DefaultItemAnimator();
		animator.setAddDuration(500);
        animator.setRemoveDuration(500);
		recyclerView.setItemAnimator(animator);
    */
	
	
		return view;

	}


}

