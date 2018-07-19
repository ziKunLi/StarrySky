package com.example.newbies.starrysky.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newbies.starrysky.R;
import com.example.newbies.starrysky.StaticDataPool;
import com.example.newbies.starrysky.entity.Message;

import java.util.List;

/**
 *
 * @author NewBies
 * @date 2018/6/27
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder>{

    private List<Message> messages;

    private Context context;

    private int resId;

    private RecyclerAdaptListener listener;

    public MessageListAdapter(List<Message> messages, int resId){
        this.messages = messages;
        this.resId = resId;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView senderName;

        ImageView friendHeadImage;

        TextView message;

        ImageView senderHeadImage;

        public ViewHolder(View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.senderName);
            friendHeadImage = itemView.findViewById(R.id.friendHeadImg);
            message = itemView.findViewById(R.id.message);
            senderHeadImage = itemView.findViewById(R.id.senderHeadImg);
        }
    }

    /**
     * 设置item点击监听
     * @param listener
     */
    public void setListener(RecyclerAdaptListener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(resId,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //如果发送者是用户本人
        if(messages.get(position).getSenderId().equals(StaticDataPool.userId)){
            holder.senderName.setGravity(Gravity.RIGHT);
            holder.message.setGravity(Gravity.RIGHT);
            holder.friendHeadImage.setBackgroundResource(R.drawable.head_bg);
        }
        else{
            holder.senderName.setGravity(Gravity.LEFT);
            holder.message.setGravity(Gravity.LEFT);
            holder.senderHeadImage.setBackgroundResource(R.drawable.head_bg);
        }
        holder.senderName.setText(messages.get(position).getSenderName());
        holder.message.setText(messages.get(position).getMessage());
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }
}
