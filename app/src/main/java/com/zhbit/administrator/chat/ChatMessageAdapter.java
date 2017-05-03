package com.zhbit.administrator.chat;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhbit.administrator.chat.bean.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;   //
    private List<ChatMessage> mDatas;  //列表聊天消息

    public ChatMessageAdapter(Context context, List<ChatMessage> mDatas) {
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount() {//计数
        return this.mDatas.size();//数据大小
    }

    public Object getItem(int position) {
        return this.mDatas.get(position);//获取数据位置
    }

    public long getItemId(int position) {
        return (long)position;//返回位置长度？
    }

    public int getItemViewType(int position) {
        ChatMessage chatMessage = (ChatMessage)this.mDatas.get(position);
        return chatMessage.getType() == ChatMessage.Type.INCOMING?0:1;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = (ChatMessage)this.mDatas.get(position);
        ChatMessageAdapter.ViewHolder viewHolder = null;
        if(convertView == null) {
            if(this.getItemViewType(position) == 0) {
                convertView = this.mInflater.inflate(R.layout.item_from_msg , parent, false);
                viewHolder = new ViewHolder();
               // viewHolder = new ChatMessageAdapter.ViewHolder((ChatMessageAdapter.ViewHolder)null);
                viewHolder.mDate = (TextView)convertView.findViewById(R.id.id_form_msg_date);
                viewHolder.mMsg = (TextView)convertView.findViewById(R.id.id_from_msg_info);
            } else {
                convertView = this.mInflater.inflate(R.layout.item_to_msg, parent, false);
                viewHolder = new ViewHolder();
                //viewHolder = new ChatMessageAdapter.ViewHolder((ChatMessageAdapter.ViewHolder)null);
                viewHolder.mDate = (TextView)convertView.findViewById(R.id.id_to_msg_date);
                viewHolder.mMsg = (TextView)convertView.findViewById(R.id.id_to_msg_info);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChatMessageAdapter.ViewHolder)convertView.getTag();
        }
        //设置数据
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.mDate.setText(df.format(chatMessage.getDate()));
        viewHolder.mMsg.setText(chatMessage.getMsg());
        return convertView;
    }

    private final class ViewHolder {
        TextView mDate;
        TextView mMsg;

        private ViewHolder() {
        }
    }
}

