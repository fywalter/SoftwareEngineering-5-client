package com.example.demo.model;

/**
 * Created by Administrator on 2018/5/27.
 */

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;
import android.os.AsyncTask;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.text.TextUtils;
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
    static public ArrayList<NewsTitle> getRecommendNewsList() {
        String urlString = backendAddress+"recommend_article/";
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
    static public ArrayList<String> getFavoriteNewsList() {
        String urlString = backendAddress+"favourite/?userprofile="+User.getInstance().getUserID();
        ArrayList<String> newsIdList = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            Log.v(TAG, "get favorite: Server response：" + code);
            if (code == 200) {
                InputStream in = conn.getInputStream();
                byte[] data = readFromStream(in);
                String j = new String(data, "UTF-8");
                JSONArray ja = new JSONArray(j);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject results = (JSONObject) ja.get(i);
                    newsIdList.add(results.getString("article"));
                 }
            } else {
                Log.e(TAG,"请求失败：" + urlString + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsIdList;
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
    static public DevTokenBean getDevAuthToken(){
        DevTokenBean dkb = null;
        try {
            String urlString = backendAddress+"dev-token-auth/";
            Map<String,String> params= new HashMap<>();
            params.put("method","POST");
            String result = queryJson(urlString,
                    new JSONObject()
                            .put("username",User.getInstance().getDevID()).toString(),params);
            dkb = DevTokenBean.parseFromJson(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dkb;
    }
    static public String register(String userName, String password){
        String result = "";
        try {
            String urlString = backendAddress+"register/";
            Map<String,String> params = new HashMap<>();
            params.put("method","POST");
            result = queryJson(urlString,
                    new JSONObject().put("username",userName)
                            .put("password", password).toString(), params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    static public ArrayList<String> login(String userName, String password){
        ArrayList<String> resultList = new ArrayList<>();
        try {
            String urlString = backendAddress+"api-token-auth/";
            Map<String,String> params = new HashMap<>();
            params.put("method","POST");
            String JSONresult = queryJson(urlString,
                    new JSONObject().put("username",userName)
                            .put("password", password).toString(), params);
            JSONObject j = new JSONObject(JSONresult);
            resultList.add(j.getString("token"));
            resultList.add(j.getString("user_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
    static public void sendNewWord( String wordJson){
        try {
            String urlString = backendAddress+"words/";
            Map<String,String> params = new HashMap<>();
            params.put("method","POST");
            params.put("Authorization","Token "+User.getInstance().getToken());
            queryJson(urlString,wordJson,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static public void addWord( String wordJson){
        try {
            String urlString = backendAddress+"wordlist/";
            Map<String,String> params = new HashMap<>();
            params.put("method","POST");
            params.put("Authorization","Token "+User.getInstance().getToken());
            queryJson(urlString,
                    new JSONObject().put("userprofile",User.getInstance().getUserID())
                    .put("word",new JSONObject(wordJson).get("raw")).toString(),
                    params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static public void deleteWord( String wordJson){
        try {
            String urlString = backendAddress+"wordlist/delete_post/";
            Map<String,String> params = new HashMap<>();
            params.put("method","POST");
            params.put("Authorization","Token "+User.getInstance().getToken());
            queryJson(urlString,
                    new JSONObject().put("userprofile",User.getInstance().getUserID())
                            .put("word",new JSONObject(wordJson).get("raw")).toString(),
                    params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static public void addComment( String commentJson){
        try {
            JSONObject comment = new JSONObject(commentJson);
            String urlString = backendAddress+"comment/";
            Map<String,String> params = new HashMap<>();
            params.put("method","POST");
            params.put("Authorization","Token "+User.getInstance().getToken());
            queryJson(urlString,
                    new JSONObject()
                            .put("userprofile",User.getInstance().getUserID())
                            .put("article",comment.get("article"))
                            .put("content",comment.get("content")).toString(),
                    params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static public ArrayList<Word> getWordList() {
        String urlString = backendAddress+"userwordlist/"+User.getInstance().getUserID();
        ArrayList<Word> wordlist = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            Log.i(TAG,urlString);
            Log.v(TAG, "Server response：" + code);
            if (code == 200) {
                InputStream in = conn.getInputStream();
                byte[] data = readFromStream(in);
                String result = new String(data, "UTF-8");
                wordlist = Word.parseWordList(result);
            } else {
                Log.e(TAG,"请求失败：" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wordlist;
    }
    private static String queryJson(String urlPath, String Json, Map<String,String> params) {
        String result = "";
        try {
            URL url = new URL(urlPath);
            Log.i("postJson",urlPath+Json);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(params.get("method"));
            params.remove("method");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            // 设置接收类型否则返回415错误
            //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
            conn.setRequestProperty("accept","application/json");
            for(String key:params.keySet()){
                conn.setRequestProperty(key,params.get(key));
            }

            // 往服务器里面发送数据
            if (Json != null && !TextUtils.isEmpty(Json)) {
                byte[] writebytes = Json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(Json.getBytes());
                outwritestream.flush();
                outwritestream.close();
                Log.i("upload", "doJsonPost: return code "+conn.getResponseCode());
            }
            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 201) {
                InputStream in = conn.getInputStream();
                byte[] data = readFromStream(in);
                result = new String(data, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 读取流中数据的方法
     */
    static private byte[] readFromStream(InputStream inputStream) throws Exception {
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
