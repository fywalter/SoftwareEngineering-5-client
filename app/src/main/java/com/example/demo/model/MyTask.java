package com.example.demo.model;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by 费  渝 on 2018/5/29.
 * 异步任务类，用于前后端交互
 */

public class MyTask<T> extends AsyncTask<Void,Void,T> {
    private String taskType=null;
    private CallBack callBack;
    private ArrayList<String> params;
    private T result = null;
    public MyTask(String type) { this.taskType=type;this.params=null;}
    public MyTask(String type,ArrayList<String> paramlist) { this.taskType=type;this.params=paramlist;}

    @Override
    protected T doInBackground(Void... params) {
        //在此处类似根据任务名执行Connection里中自己写好的函数
        switch (taskType){
            case("getNewsList"):{
                result = (T) Connection.getNewsList();
                return result;
            }
            case("getNews"):{
                result=(T)Connection.getNews(this.params.get(0));
                return result;
            }
            case("getRecommendNewsList"):{
                result=(T)Connection.getRecommendNewsList();
                return  result;
            }
            case("getDevAuthToken"):{
                result = (T)Connection.getDevAuthToken();
                return result;
            }
            case("register"):{
                result = (T)Connection.register(this.params.get(0), this.params.get(1));
                return result;
            }
            case("login"):{
                result = (T)Connection.login(this.params.get(0), this.params.get(1));
                return result;
            }
            case("sendNewWord"):{
                Connection.sendNewWord(this.params.get(0));
                Connection.addWord(this.params.get(0));
            }
            case("getWordList"):{
                result = (T)Connection.getWordList();
                return result;
            }
            case("deleteWord"):{
                Connection.deleteWord(this.params.get(0));
            }
            default: return null;
        }
    }
    @Override
    protected void onPostExecute(T rslt) {
        try{
            //如果callBack不为空,调用回调函数
            if (callBack != null){
                callBack.setSomeThing(result);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setCallBack(CallBack callback){
        this.callBack=callback;
    }
    public abstract class CallBack{
        public CallBack(){}
        public abstract void setSomeThing(T result);
    }
}
