package com.example.demo.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by xiaowei on 2018/5/30.
 */

public class DevTokenBean {
    private String token;
    private String userID;
    public String getToken(){
        return token;
    }
    public String getUserID(){
        return userID;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public static DevTokenBean parseFromJson(String content) throws Exception{
        JSONObject results = new JSONObject(content);
        DevTokenBean dk = new DevTokenBean();
        dk.setToken(results.getString("token"));
        dk.setUserID(results.getString("user_id"));
        return dk;
    }
}
