package com.example.demo.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by xiaowei on 2018/5/25.
 */

public class News {
    public String title;
    public String source;
    public String content;
    public String date;
    public String imgUrl;
    public Boolean ifFavored;
    News(){title=null;source=null;content=null;date=null;ifFavored=false;}
    static public News parseNews(String content) throws Exception {
        JSONObject results = new JSONObject(content);
        News news = new News();
        news.title=(results.getString("title"));
        news.source=(results.getString("source"));
        news.date=(results.getString("pub_date"));
        news.content=(results.getString("content"));
        news.imgUrl=(results.getString("img_url"));
        return news;
    }
}
