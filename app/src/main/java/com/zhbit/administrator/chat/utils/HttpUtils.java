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
 *        util时存放工具类的地方
 *        该类是发送http请求的一个工具类
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
        String jsonRes = doGet(msg);//用户发送后返回的结果给jsonRes

        //初始化，用于将Java对象和json字符串之间进行转换
        Gson gson = new Gson();
        Result result = null;//返回的数据给result对象
        try
        {
            //将接收的jsonRes传入给Result.class,result进行接收
            result = gson.fromJson(jsonRes, Result.class);
            chatMessage.setMsg(result.getText());//转化成功，则这就是最终的聊天消息
        } catch (Exception e)
        {
            chatMessage.setMsg("服务器繁忙，请稍候再试"); //断网时抛出异常
        }
        chatMessage.setDate(new Date());//当前时间
        chatMessage.setType(ChatMessage.Type.INCOMING);//类型，收到
        return chatMessage;
    }

    public static String doGet(String msg)//调用函数，用户传出的消息
    {
        String result = "";

        //调用setParams将问题：msg拼接成完整的url
        String url = setParams(msg);

        ByteArrayOutputStream baos = null;
        InputStream is = null;
        try
        {
            java.net.URL urlNet = new java.net.URL(url);//将完整的url发送请求

            //只是建立了一个与服务器的tcp连接，并没有实际发送http请求。
            HttpURLConnection conn = (HttpURLConnection) urlNet.openConnection();

            conn.setReadTimeout(5 * 1000);//传输数据超时时间
            conn.setConnectTimeout(5 * 1000);//建立连接超时时间
            conn.setRequestMethod("GET");//请求方式为get

            //正式发送http请求后，并将服务器回传的数据流给is
            is = conn.getInputStream();
            int len = -1;
            byte[] buf = new byte[128];//缓冲区为128字节
            baos = new ByteArrayOutputStream(); //可以捕获内存缓冲区的数据，转换成字节数组。

            while ((len = is.read(buf)) != -1)//流的读操作，缓冲区不到结尾
            {
                baos.write(buf, 0, len);//一直读到流的结束等于-1时，循环
            }
            baos.flush();//清除缓冲区
            result = new String(baos.toByteArray());//本地流转化成字符串
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

    private static String setParams(String msg)//你所问的问题msg
    {
        String url = "";
        try
        {
            //调用示例：URL+?key+KEY&info=所问的问题
            url = URL + "?key=" + API_KEY + "&info="
                    + URLEncoder.encode(msg, "UTF-8");//图灵调用规范
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return url;
    }

}
