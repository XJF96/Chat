package com.zhbit.administrator.chat.utils;

import com.google.gson.Gson;  //gson包里的，该jar包可用于将Java对象和json字符串之间进行转换
import com.zhbit.administrator.chat.bean.ChatMessage;
import com.zhbit.administrator.chat.bean.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;
/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class HttpUtils
{
    //图灵账号的APIKEY
    private static final String URL = "http://www.tuling123.com/openapi/api";
    private static final String API_KEY = "05e0c620380f48ff97af27b3fb0fef65";

    /**
     * 发送一个消息，得到返回的消息
     * @param msg
     * @return
     */
    public static ChatMessage sendMessage(String msg)
    {
        ChatMessage chatMessage = new ChatMessage();
        String jsonRes = doGet(msg);

        //初始化，用于将Java对象和json字符串之间进行转换
        Gson gson = new Gson();
        Result result = null;
        try
        {
            //通过jsonRes转换成Result
            result = gson.fromJson(jsonRes, Result.class);
            chatMessage.setMsg(result.getText());//
        } catch (Exception e)
        {
            chatMessage.setMsg("服务器繁忙，请稍候再试"); //断网时抛出异常
        }
        chatMessage.setDate(new Date());
        chatMessage.setType(ChatMessage.Type.INCOMING);//传入
        return chatMessage;
    }

    public static String doGet(String msg)
    {
        String result = "";
        String url = setParams(msg);

        //可以捕获内存缓冲区的数据，转换成字节数组。
        ByteArrayOutputStream baos = null;
        InputStream is = null;
        try
        {
            java.net.URL urlNet = new java.net.URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlNet.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            is = conn.getInputStream();
            int len = -1;
            byte[] buf = new byte[128];
            baos = new ByteArrayOutputStream();

            while ((len = is.read(buf)) != -1)
            {
                baos.write(buf, 0, len);
            }
            baos.flush();
            result = new String(baos.toByteArray());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (baos != null)
                    baos.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                if (is != null)
                {
                    is.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static String setParams(String msg)
    {
        String url = "";//
        try
        {
            url = URL + "?key=" + API_KEY + "&info="
                    + URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return url;
    }

}
