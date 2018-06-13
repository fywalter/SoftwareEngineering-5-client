package com.example.demo.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NewsTitle {
    private int newsID;
    private String title;
    private String source;
    private String newsDate;
    private String url;
    private String imgUrl;
    public NewsTitle(){
    };
    public NewsTitle(String title, String source, String newsDate,String url,String imgUrl){
        this.title = title;
        this.source = source;
        this.newsDate = newsDate;
        this.url=url;
        this.imgUrl=imgUrl;
    }
    public int getNewsID(){ return this.newsID; }
    public String getTitle(){
        return this.title;
    }
    public String getSource(){
        return this.source;
    }
    public String getNewsdate(){
        return this.newsDate;
    }
    public String getUrl(){
        return this.url;
    }
    public String getImgUrl(){
        return this.imgUrl;
    }
    public void setNewsID(int id){
        this.newsID = id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setSource(String src){
        this.source = src;
    }
    public void setNewsdate(String newsDate){
        this.newsDate = newsDate;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }
    /**
     * 解析返回Json数据的方法
     */
    static public ArrayList<NewsTitle> parseNewsTitleList(String content) throws Exception {
        ArrayList<NewsTitle> newsList = new ArrayList<>();
        JSONArray array = new JSONObject(content).getJSONArray("results");
        for (int i = 0; i < array.length(); i++) {
            JSONObject results = (JSONObject) array.get(i);
            NewsTitle nt = new NewsTitle();
            nt.setNewsID(results.getInt("id"));
            nt.setTitle(results.getString("title"));
            nt.setSource(results.getString("from_media") + " " + results.getString("author"));
            nt.setNewsdate(results.getString("pub_date"));
            nt.setUrl(results.getString("url"));
            nt.setImgUrl(results.getString("img_url"));
            newsList.add(nt);
        }
       // Collections.reverse(newsList);
        return newsList;
    }
}
