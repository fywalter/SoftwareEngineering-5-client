package com.example.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.demo.adapter.MyAdapter;
import com.example.demo.model.User;
import com.example.demo.model.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiaowei on 2018/5/25.
 */

public class WordListActivity extends AppCompatActivity {

    private ListView wordListView;
    private MyAdapter<Word> myAdapter = null;
    private List<Word> mData = null;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist);
        setStatusBarColor(this,R.color.colorPrimaryDark);
        init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
            actionBar.setTitle(R.string.wordlist_actionbar_title);
        }


    }

    private void init() {

        wordListView = (ListView) findViewById(R.id.word_list);

        mData = User.getInstance().getWordList();

        myAdapter = new MyAdapter<Word>((ArrayList)mData,R.layout.item_word) {
            @Override
            public void bindView(ViewHolder holder, Word obj) {
                final String wordName = obj.getwName();
                final String wordExplain = obj.getwExplain();
                holder.setText(R.id.txt_wname,wordName);
                holder.setText(R.id.txt_wexplain,wordExplain);
                holder.getItemView().setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        alert = null;
                        builder = new AlertDialog.Builder(WordListActivity.this);
                        alert = builder
                                .setTitle(wordName)
                                .setMessage(wordExplain)
                                .setPositiveButton("删除单词", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        myAdapter.remove(new Word(wordName,wordExplain));
                                        Toast.makeText(WordListActivity.this, "删除单词~", Toast.LENGTH_SHORT).show();
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();                    //显示对话框
                    }
                });
            }
        };
        //ListView设置下Adapter：
        wordListView.setAdapter(myAdapter);

    }

    static void setStatusBarColor(AppCompatActivity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }
}
