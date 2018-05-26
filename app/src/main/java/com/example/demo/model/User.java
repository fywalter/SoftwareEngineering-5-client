package com.example.demo.model;

/**
 * Created by Xiaowei on 2018/5/25.
 */
import android.content.Context;

import com.example.demo.MainActivity;
import com.example.demo.utils.Uuid;

import java.util.ArrayList;

public class User {
    private static User instance = new User();
    private String userID;
    private String devID;  //00000000-1b1f-d7e0-0000-000031b8a7c0  in my device
    private ArrayList<Word> wordList;
    private ArrayList<News> newsList;

    public User(){
        wordList = new ArrayList<Word>();
        newsList = new ArrayList<News>();
        devID = Uuid.getDeviceUUID(MainActivity.mcontext);
    }

    public static synchronized User getInstance(){
        return instance;
    }

    public ArrayList<Word> getWordList(){
        return wordList;
    }
    public void addWord(Word w){
        wordList.add(w);
    }
    public  void delWord(Word w){
        wordList.remove(w);
    }
    public String getNews(){
        return null;
    }
    public String getDevID(){
        return devID;
    }
    public String translate(Word w){
        return null;
    }
}
