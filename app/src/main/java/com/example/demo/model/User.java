package com.example.demo.model;

/**
 * Created by Xiaowei on 2018/5/25.
 */
import android.content.Context;

import com.example.demo.MainActivity;
import com.example.demo.utils.Uuid;

import java.util.ArrayList;
import android.util.Log;

public class User {
    private static User instance = new User();
    private String userID;
    private String token;
    private String devID;  //00000000-1b1f-d7e0-0000-000031b8a7c0  in my device
    private ArrayList<Word> wordList;
    private ArrayList<News> newsList;
    private ArrayList<NewsTitle> newsTitleList;
    private boolean isLoggedIn;
    private String userName;

    public User(){
        wordList = new ArrayList<Word>();
        newsList = new ArrayList<News>();
        newsTitleList = new ArrayList<NewsTitle>();
        devID = Uuid.getDeviceUUID(MainActivity.mcontext);
        isLoggedIn = false;
        userName = "Anonymous";
        setTokenAndUserID();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    fetchWordList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static synchronized User getInstance(){
        return instance;
    }
    public ArrayList<Word> getWordList(){
        return wordList;
    }
    public void addWord(Word w){
        wordList.add(w);
        ArrayList<String> param = new ArrayList<>();
        param.add(w.toJson());
        new MyTask<String>("sendNewWord",param).execute();
    }
    public  void delWord(Word w){
        wordList.remove(w);
        ArrayList<String> param = new ArrayList<>();
        param.add(w.toJson());
        new MyTask<String>("deleteWord",param).execute();

    }
    public ArrayList<News> getNewsList(){
        return newsList;
    }
    public ArrayList<NewsTitle> getNewsTitleList(){
        return newsTitleList;
    }
    public void setNewsTitleList( ArrayList<NewsTitle>  ntl){
        newsTitleList = ntl;
        Log.i("set newsList",newsTitleList.toString());
    }
    public String getDevID(){
        return devID;
    }
    public void setUserID(String userID){
        this.userID = userID;
        Log.i("set userID",userID);
    }
    public String getUserID(){
        return this.userID;
    }
    public void setToken(String token){
        this.token = token;
        Log.i("set token",token);
    }
    public String getToken(){
        return this.token;
    }
    public void setTokenAndUserID(){
        MyTask<DevTokenBean> dkTask = new MyTask<DevTokenBean>("getDevAuthToken");
        dkTask.setCallBack(dkTask.new CallBack() {
            @Override
            public void setSomeThing(DevTokenBean dkb) {
                setToken(dkb.getToken());
                setUserID(dkb.getUserID());
            }
        });
        dkTask.execute();
    }
    private void fetchWordList(){
        MyTask<ArrayList<Word>> wlTask = new MyTask<>("getWordList");
        wlTask.setCallBack(wlTask.new CallBack() {
            @Override
            public void setSomeThing(ArrayList<Word> wordlist) {
                setWordList(wordlist);
            }
        });
        wlTask.execute();
    }
    public void setWordList(ArrayList<Word> wordlist){
        this.wordList = wordlist;
    }
    public void setLoggedIn(boolean status){this.isLoggedIn = status;}
    public boolean getLoggedIn(){return this.isLoggedIn;}
    public void setUserName(String usrname){this.userName = usrname;}
    public String getUserName() {return this.userName;}
}
