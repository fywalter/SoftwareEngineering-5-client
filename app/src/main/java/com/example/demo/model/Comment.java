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
    private int userID;
    private String username;
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

    public String getUsername(){
        return username;
    }
    public int getUserID(){
        return userID;
    }
    public void setArticle(int article) {
        this.article = article;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setUserID(int userID){
        this.userID = userID;
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
    static public ArrayList<Comment> parseCommentList(String content) throws Exception {
        ArrayList<Comment> commentList = new ArrayList<>();
        JSONArray array = new JSONArray(content);
        for (int i = 0; i < array.length(); i++) {
            JSONObject results = (JSONObject) array.get(i);
            Comment cmt = new Comment();
            cmt.setUserID(results.getJSONObject("userprofile").getInt("user"));
            cmt.setUsername(results.getJSONObject("userprofile").getString("nickname"));
            cmt.setContent(results.getString("content"));
            commentList.add(cmt);
        }
        return commentList;
    }

}
