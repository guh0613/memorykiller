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
import org.litepal.*;
import java.util.*;
import java.io.*;
import android.support.v7.app.*;
import android.content.*;


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
				public void onClick(final View v) {
					int position=holder.getAdapterPosition();
					final HistoryPath Path = mList.get(position);
					//List<HistoryPath> L = LitePal.where("name = ?", Path.getName()).find(HistoryPath.class); 
					//String[] s = L.toArray(new String[L.size()]);
					//LitePal.delete(HistoryPath.class,);
					final int i = position;
					final File file=new File(Path.getName());
					try 
					{
						
						AlertDialog.Builder dequ=new AlertDialog.Builder(v.getContext());
						dequ.setTitle("删除");
						dequ.setMessage("确定要删除所占内存文件"+Path.getName()+"吗？");
						dequ.setPositiveButton("老子确定了", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog,int which)
								{
									if (file.exists())
									{
										file.delete();
										Toast.makeText(v.getContext(),"删除成功",Toast.LENGTH_SHORT).show();
										LitePal.deleteAll(HistoryPath.class, "name = ?" ,Path.getName() ); 
										mList.remove(i);
										notifyItemRemoved(i);

										notifyItemRangeChanged(i, getItemCount());

									}
									else
									{
										Toast.makeText(v.getContext(),"老子没找到，你是不是偷偷删了",Toast.LENGTH_SHORT).show();
										LitePal.deleteAll(HistoryPath.class, "name = ?" ,Path.getName() ); 
										mList.remove(i);
										notifyItemRemoved(i);

										notifyItemRangeChanged(i, getItemCount());
									}
								}
							});
						dequ.setNegativeButton("老子后悔了", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog,int which)
								{
									Toast.makeText(v.getContext(),"(눈_눈)",Toast.LENGTH_SHORT).show();
								}
							});
						dequ.show();


					}catch(Exception e)
					{
						e.printStackTrace();
						Toast.makeText(v.getContext(),"是不是......发生了什么错误",2000).show();
					}
					
					//Toast.makeText(v.getContext(),Path.getName() + "当前位置" + position,Toast.LENGTH_SHORT).show();
					//LitePal.deleteAll(HistoryPath.class, "name = ?" ,Path.getName() ); 
					

				}
			});
    }
    //获取RecyclerView有多少子项
    @Override
    public int getItemCount() {
        return mList.size();
    }
}

