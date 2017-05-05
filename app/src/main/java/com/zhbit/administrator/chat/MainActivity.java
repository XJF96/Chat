package com.zhbit.administrator.chat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.zhbit.administrator.chat.bean.ChatMessage;
import com.zhbit.administrator.chat.utils.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    private ListView mMsgs;//聊天记录
    private ChatMessageAdapter mAdapter;//适配器
    private List<ChatMessage> mDatas;//数据集源

    private EditText mInputMsg;
    private Button mSendMsg;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //等待接收，子线程完成数据的返回
            ChatMessage fromMessge = (ChatMessage)msg.obj;
            MainActivity.this.mDatas.add(fromMessge);//加入数据集中
            MainActivity.this.mAdapter.notifyDataSetChanged();//数据源发生改变
            MainActivity.this.mMsgs.setSelection(MainActivity.this.mDatas.size() - 1);
        }
    };

    public MainActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(1);//no title
        this.setContentView(R.layout.activity_main);

        this.initView();
        this.initDatas();
        this.initListener();
    }

    private void initListener() {//初始化事件
        this.mSendMsg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {//匿名内部类
                final String toMsg = MainActivity.this.mInputMsg.getText().toString();

                if(TextUtils.isEmpty(toMsg)) {
                    Toast.makeText(MainActivity.this, "发送消息不能为空！", Toast.LENGTH_SHORT).show();//窗口提示
                } else {
                    ChatMessage toMessage = new ChatMessage();
                    toMessage.setDate(new Date());
                    toMessage.setMsg(toMsg);
                    toMessage.setType(ChatMessage.Type.OUTCOMING);//发送类型

                    MainActivity.this.mDatas.add(toMessage);
                    MainActivity.this.mAdapter.notifyDataSetChanged();
                    MainActivity.this.mMsgs.setSelection(MainActivity.this.mDatas.size() - 1);
                    MainActivity.this.mInputMsg.setText("");//清空输入框

                    (new Thread() {
                        public void run() {
                            ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);//网络请求
                            Message m = Message.obtain();//当子线程拿到from_masg返回的数据后,给message--m
                            m.obj = fromMessage;
                            MainActivity.this.mHandler.sendMessage(m);//Handle将消息发送给mHandler
                        }
                    }).start();

                }
            }
        });
    }

    private void initDatas() {
        this.mDatas = new ArrayList();
        this.mDatas.add(new ChatMessage("您好，小白为您服务", ChatMessage.Type.INCOMING, new Date()));
        //this.mDatas.add(new ChatMessage("Hello,小白",ChatMessage.Type.OUTCOMING,new Date()));

        this.mAdapter = new ChatMessageAdapter(this, this.mDatas);//数据集
        this.mMsgs.setAdapter(this.mAdapter);//适配器
    }

    private void initView() {
        this.mMsgs = (ListView)this.findViewById(R.id.id_listview_msgs);
        this.mInputMsg = (EditText)this.findViewById(R.id.id_input_msg);
        this.mSendMsg = (Button)this.findViewById(R.id.id_send_msg);
    }
}
