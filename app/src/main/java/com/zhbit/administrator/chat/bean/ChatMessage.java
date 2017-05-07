package com.zhbit.administrator.chat.bean;

import java.util.Date;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class ChatMessage//聊天的内容
{
    private String msg;
    private Type type;//发送还是接收
    private Date date;

    public enum Type//枚举
    {
        INCOMING, OUTCOMING
    }

    public ChatMessage() {
    }
    public ChatMessage(String msg, Type type, Date date)
    {
        super();
        this.msg = msg;
        this.type = type;
        this.date = date;
    }
    //三个get方法和三个set方法
    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

}