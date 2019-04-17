package com.huaji.memorykiller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import android.os.*;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<HistoryPath> mList;

    public class ViewHolder extends RecyclerView.ViewHolder{
        
        TextView titleTextView;
        LinearLayout linearLayout;

        public ViewHolder(View view){
            super(view);
            titleTextView=(TextView)view.findViewById(R.id.file);
			
            linearLayout=(LinearLayout)view.findViewById(R.id.layout);

        }
    }

    //传入展示数据,并且赋值给全局变量
    public RecyclerViewAdapter(List<HistoryPath> List){
        mList=List;
    }

    //创建ViewHolder实例,将item布局加载出来
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    //对RecyclerView的子项数据进行赋值
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        HistoryPath Path = mList.get(position);
        holder.titleTextView.setText(Path.getName());
		
        //点击事件
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int position=holder.getAdapterPosition();
					HistoryPath Path=mList.get(position);
					Toast.makeText(v.getContext(),Path.getName() + "当前位置" + position,Toast.LENGTH_SHORT).show();
					
					

				}
			});
    }
    //获取RecyclerView有多少子项
    @Override
    public int getItemCount() {
        return mList.size();
    }
}

