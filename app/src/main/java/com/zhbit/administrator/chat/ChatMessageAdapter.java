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
    private LayoutInflater mInflater;   //程序变量
    private List<ChatMessage> mDatas;  //数据集列表聊天消息

    public ChatMessageAdapter(Context context, List<ChatMessage> mDatas) {
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount() {//计数
        return this.mDatas.size();//数组长度
    }

    public Object getItem(int position) {
        return this.mDatas.get(position);//获取数据位置
    }

    public long getItemId(int position) {
        return (long)position;//得到子项ID
    }

    //接收和发送是不同的位置item
    public int getItemViewType(int position) {
        ChatMessage chatMessage = (ChatMessage)this.mDatas.get(position);//通过位置拿到chatmessage
        return chatMessage.getType() == ChatMessage.Type.INCOMING?0:1;//若接收消息return 0，发送为1
    }

    public int getViewTypeCount() {
        return 2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = (ChatMessage)this.mDatas.get(position);
        ChatMessageAdapter.ViewHolder viewHolder = null;

        if(convertView == null) {//空时判断
            //通过TypeItem设置不同的布局
            if(this.getItemViewType(position) == 0) {
                //开始，如果为0，为接收的布局
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
        viewHolder.mDate.setText(df.format(chatMessage.getDate()));//获取时间

        viewHolder.mMsg.setText(chatMessage.getMsg());
        return convertView;
    }

    private final class ViewHolder {//提高效率
        TextView mDate;
        TextView mMsg;

        private ViewHolder() {
        }
    }
}

