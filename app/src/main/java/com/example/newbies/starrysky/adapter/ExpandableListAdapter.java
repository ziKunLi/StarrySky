package com.example.newbies.starrysky.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newbies.starrysky.R;
import com.example.newbies.starrysky.entity.FriendSampleInfo;

import java.util.List;

/**
 *
 * @author NewBies
 * @date 2018/4/14
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private  List<String> groupNames;
    private List<List<FriendSampleInfo>> info;
    private Context context;

    public ExpandableListAdapter(Context context, List<String> groupNames, List<List<FriendSampleInfo>> info){
        this.context = context;
        this.groupNames = groupNames;
        this.info = info;
    }

    @Override
    public int getGroupCount() {
        return info.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return info.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupNames.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return info.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定分组的视图
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        //类似于listView的优化
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.friends_title_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.title =  convertView.findViewById(R.id.title);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.title.setText(groupNames.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.friends_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.name =  convertView.findViewById(R.id.friendName);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.name.setText(info.get(groupPosition).get(childPosition).getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class ChildViewHolder{
        ImageView headImage;
        TextView name;
    }
    static class GroupViewHolder  {
        TextView title;
    }
}
