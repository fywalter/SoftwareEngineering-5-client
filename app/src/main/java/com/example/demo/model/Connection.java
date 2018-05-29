package com.example.demo.model;

/**
 * Created by Administrator on 2018/5/27.
 */

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.lang.StringBuffer;
import java.util.ArrayList;
import android.util.Log;
import android.os.AsyncTask;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONArray;
import com.example.demo.model.NewsTitle;

public class Connection{
    private static final String TAG = "Connection";
    static final String backendAddress = "https://api.li-positive-one.com:4433/";

    static public ArrayList<NewsTitle> getNewsList() {
        String urlString = backendAddress+"articles/";
        ArrayList<NewsTitle> newsList = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            Log.v(TAG, "Server response：" + code);
            if (code == 200) {
                InputStream in = conn.getInputStream();
                byte[] data = readFromStream(in);
                String result = new String(data, "UTF-8");
                newsList = NewsTitle.parseNewsTitleList(result);
            } else {
                Log.e(TAG,"请求失败：" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }
    static public News getNews(String urlString){
        News news=new News();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            Log.v(TAG, "Server response：" + code);
            if (code == 200) {
                InputStream in = conn.getInputStream();
                byte[] data = readFromStream(in);
                String result = new String(data, "UTF-8");
                news = News.parseNews(result);
            } else {
                Log.e(TAG,"请求失败：" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }
    /**
     * 读取流中数据的方法
     */
    static public byte[] readFromStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len ;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }


}
