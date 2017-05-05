package com.zhbit.administrator.chat.bean;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class Result
{
    private int code;//返回code的状态码
    private String text;//内容

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

}