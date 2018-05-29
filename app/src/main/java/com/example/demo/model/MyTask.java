package com.example.demo.model;

import android.os.AsyncTask;
/**
 * Created by 费  渝 on 2018/5/29.
 * 异步任务类，用于前后端交互
 */

public class MyTask<T> extends AsyncTask<Void,Void,T> {
    private String taskType=null;
    private CallBack callBack;
    private T result = null;
    public MyTask(String type) { this.taskType=type;}

    @Override
    protected T doInBackground(Void... params) {
        //在此处类似根据任务名执行Connection里中自己写好的函数
        switch (taskType){
            case("getNewsList"):{
                result = (T) Connection.getNewsList();
                return result;
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
