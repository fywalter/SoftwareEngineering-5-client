package com.example.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist);
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
                holder.setText(R.id.txt_wname,obj.getwName());
                holder.setText(R.id.txt_wexplain,obj.getwExplain());
            }
        };
        //ListView设置下Adapter：
        wordListView.setAdapter(myAdapter);

    }


}
