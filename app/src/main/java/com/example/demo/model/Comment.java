package com.example.demo.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/13.
 */

public class Comment {
    private int article;
    private String content;

    public Comment() {
    }

    public Comment(int article, String content) {
        this.content = content;
        this.article = article;
    }

    public String getContent() {
        return content;
    }

    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String toJson(){
        String jformat = null;
        try {
            jformat = new JSONObject()
                    .put("content", getContent())
                    .put("article", getArticle()).toString();
        }catch(Exception e){
            Log.e("commentToJson","can't transform comment to String");
        }
        return jformat;
    }
    static public ArrayList<Word> parseWordList(String content) throws Exception {
        ArrayList<Word> wordList = new ArrayList<>();
        JSONArray array = new JSONObject(content).getJSONArray("glossary");
        for (int i = 0; i < array.length(); i++) {
            JSONObject results = (JSONObject) array.get(i);
            Word w = new Word();
            w.setwName(results.getString("raw"));
            w.setwExplain(results.getString("meaning"));
            wordList.add(w);
        }
        return wordList;
    }

}
