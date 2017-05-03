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
    private ListView mMsgs;
    private ChatMessageAdapter mAdapter;
    private List<ChatMessage> mDatas;
    private EditText mInputMsg;
    private Button mSendMsg;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            ChatMessage fromMessge = (ChatMessage)msg.obj;
            MainActivity.this.mDatas.add(fromMessge);
            MainActivity.this.mAdapter.notifyDataSetChanged();
            MainActivity.this.mMsgs.setSelection(MainActivity.this.mDatas.size() - 1);
        }
    };

    public MainActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.activity_main);
        this.initView();
        this.initDatas();
        this.initListener();
    }

    private void initListener() {
        this.mSendMsg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final String toMsg = MainActivity.this.mInputMsg.getText().toString();
                if(TextUtils.isEmpty(toMsg)) {
                    Toast.makeText(MainActivity.this, "发送消息不能为空！", Toast.LENGTH_SHORT).show();//窗口提示
                } else {
                    ChatMessage toMessage = new ChatMessage();
                    toMessage.setDate(new Date());
                    toMessage.setMsg(toMsg);
                    toMessage.setType(ChatMessage.Type.OUTCOMING);
                    MainActivity.this.mDatas.add(toMessage);
                    MainActivity.this.mAdapter.notifyDataSetChanged();
                    MainActivity.this.mMsgs.setSelection(MainActivity.this.mDatas.size() - 1);
                    MainActivity.this.mInputMsg.setText("");
                    (new Thread() {
                        public void run() {
                            ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
                            Message m = Message.obtain();
                            m.obj = fromMessage;
                            MainActivity.this.mHandler.sendMessage(m);
                        }
                    }).start();
                }
            }
        });
    }

    private void initDatas() {
        this.mDatas = new ArrayList();
        this.mDatas.add(new ChatMessage("你好，小白为您服务", ChatMessage.Type.INCOMING, new Date()));
        this.mAdapter = new ChatMessageAdapter(this, this.mDatas);
        this.mMsgs.setAdapter(this.mAdapter);
    }

    private void initView() {
        this.mMsgs = (ListView)this.findViewById(R.id.id_listview_msgs);
        this.mInputMsg = (EditText)this.findViewById(R.id.id_input_msg);
        this.mSendMsg = (Button)this.findViewById(R.id.id_send_msg);
    }
}
