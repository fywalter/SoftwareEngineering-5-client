package com.example.demo.model;

/**
 * Created by Xiaowei on 2018/5/25.
 */
import java.util.ArrayList;

public class User {
    private static User instance = new User();
    private String userID;
    private String devID;
    private ArrayList<Word> wordList;

    public User(){
        wordList = new ArrayList<Word>();
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
    public String getNews(){
        return null;
    }
    public String translate(Word w){
        return null;
    }
}
