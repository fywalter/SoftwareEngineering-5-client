package com.example.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo.model.User;
import com.example.demo.model.Comment;

/**
 * Created by xiaowei on 2018/6/13.
 */

public class CommentActivity extends AppCompatActivity {

    private int newsID = -1;
    private EditText editText = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(this, Color.parseColor("#303F9F"));
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        newsID = intent.getIntExtra("newsID",-1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
            actionBar.setTitle(R.string.title_activity_comment);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editText = (EditText)findViewById(R.id.comment_content);


        Button btn = (Button)findViewById(R.id.comment_commit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CommentActivity.this,"send",Toast.LENGTH_SHORT);
                Comment cmt = new Comment();
                cmt.setArticle(newsID);
                cmt.setContent(editText.getText().toString());
                User.getInstance().addComment(cmt);
                CommentActivity.this.finish();
            }
        }
        );
    }

    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
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