package com.example.demo.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsTitle {
    private String title;
    private String source;
    private String newsDate;
    public NewsTitle(){
    };
    public NewsTitle(String title, String source, String newsDate){
        this.title = title;
        this.source = source;
        this.newsDate = newsDate;
    }
    public String getTitle(){
        return this.title;
    }
    public String getSource(){
        return this.source;
    }
    public String getNewsdate(){
        return this.newsDate;
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
    /**
     * 解析返回Json数据的方法
     */
    static public ArrayList<NewsTitle> parseNewsTitleList(String content) throws Exception {
        ArrayList<NewsTitle> newsList = new ArrayList<>();
        JSONArray array = new JSONArray(content);
        for (int i = 0; i < array.length(); i++) {
            JSONObject results = (JSONObject) array.get(i);
            NewsTitle nt = new NewsTitle();
            nt.setTitle(results.getString("title"));
            nt.setSource(results.getString("source"));
            nt.setNewsdate(results.getString("pub_date"));
            newsList.add(nt);
        }
        return newsList;
    }
}
