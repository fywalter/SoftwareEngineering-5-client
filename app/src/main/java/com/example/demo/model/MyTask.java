package com.example.demo.model;

import android.os.AsyncTask;
import android.util.Log;

import com.example.demo.utils.ShanbayAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 费  渝 on 2018/5/29.
 */

public class MyTask<T> extends AsyncTask<Void,Void,T> {
    private String taskType=null;
    private CallBack callBack;
    private T result = null;
    public MyTask(String type) { this.taskType=type;}

    @Override
    protected T doInBackground(Void... params) {
        if(taskType.equals("getNewsList")) {

            result = (T) Connection.getNewsList();
            return result;
        }
        return null;
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
