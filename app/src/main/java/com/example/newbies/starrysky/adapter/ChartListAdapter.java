package com.example.newbies.starrysky.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newbies.starrysky.R;
import com.example.newbies.starrysky.entity.FriendSampleInfo;

import java.util.List;

/**
 *
 * @author NewBies
 * @date 2018/6/18
 */
public class ChartListAdapter extends RecyclerView.Adapter<ChartListAdapter.ChartHolder>{

    private Context context;

    /**
     * 数据
     */
    private List<FriendSampleInfo> infos;

    /**
     * 加载的布局
     */
    private int resId;

    private RecyclerAdaptListener listener;

    public ChartListAdapter(List<FriendSampleInfo> infos, int resId){
        this.infos = infos;
        this.resId = resId;
    }

    public class ChartHolder extends RecyclerView.ViewHolder{

        /**
         * 头像
         */
        private ImageView headImg;
        /**
         * 名字
         */
        private TextView friendName;

        public ChartHolder(View itemView) {
            super(itemView);
            headImg = itemView.findViewById(R.id.headImg);
            friendName = itemView.findViewById(R.id.friendName);
        }
    }

    public void setItemListener(RecyclerAdaptListener listener){
        this.listener = listener;
    }

    @Override
    public ChartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(resId,parent,false);
        return new ChartHolder(view);
    }

    @Override
    public void onBindViewHolder(ChartHolder holder, final int position) {
        holder.friendName.setText(infos.get(position).getName());
        holder.friendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClick(v,position,1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }
}
