package com.example.demo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.demo.model.MyTask;
import com.example.demo.model.News;
import com.example.demo.model.User;
import com.example.demo.model.Word;
import com.example.demo.utils.ShanbayAPI;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 费  渝 on 2018/5/24.
 */

public class NewsActivity extends AppCompatActivity {
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private ScrollView sc;
    private String url=null;
    private MyTask<News> newsTask=null;
    final private Context mcontext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setStatusBarColor(this,R.color.colorPrimaryDark);
        sc=(ScrollView) findViewById(R.id.sc);

        final ImageView iv_image = (ImageView)findViewById(R.id.news_image);
        final StringBuilder sb_title = new StringBuilder();
        final TextView tv_title = (TextView) findViewById(R.id.news_title);
        final TextView tv_source = (TextView) findViewById(R.id.news_source);
        final TextView tv_time = (TextView) findViewById(R.id.news_time);
        final TextView tv_content = (TextView) findViewById(R.id.news_content);
        final StringBuilder sb_content = new StringBuilder();

        Intent intent = getIntent();
        url=intent.getStringExtra("url");
        ArrayList<String> params = new ArrayList<>();
        params.add(url);
        newsTask=new MyTask<>("getNews",params);
        newsTask.setCallBack(newsTask.new CallBack() {
            @Override
            public void setSomeThing(News result) {
                //title
                sb_title.append(result.title);
                tv_title.setMovementMethod(LinkMovementMethod.getInstance());
                tv_title.setText(addClickPart(sb_title.toString()), TextView.BufferType.SPANNABLE);
                //img
                Glide.with(mcontext).load(result.imgUrl).into(iv_image);
                //source
                String source=null;
                if(result.fromMedia.length()!=0 && result.fromMedia!=null){
                    if(result.author.length()!=0 && result.author!="Nobody"){
                        source=result.fromMedia.concat(" by ").concat(result.author);
                    }else{
                        source=result.fromMedia;
                    }
                }else{
                    if(result.author.length()!=0 && result.author!="Nobody"){
                        source=source.concat("By ").concat(result.author);
                    }else{
                        source="This ia an anonymous news...";
                    }
                }
                tv_source.setText(source);

                //time
                tv_time.setText(result.date);

                //content
                sb_content.append(result.content);
                tv_content.setMovementMethod(LinkMovementMethod.getInstance());
                tv_content.setText(addClickPart(sb_content.toString()), TextView.BufferType.SPANNABLE);
            }
        });
        newsTask.execute();


        //再来一篇按钮
        Button btn_another = (Button) findViewById(R.id.anotherone);
        btn_another.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( NewsActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        //返回顶部按钮
        Button btn_backtotop = (Button) findViewById(R.id.backtotop);
        btn_backtotop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sc.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
        }
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

    //定义一个点击每个部分文字的处理方法
    private SpannableStringBuilder addClickPart(String str) {

        //创建一个SpannableStringBuilder对象，连接多个字符串
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        String[] wordList = str.split(" ");
        int currentIdx = 0;
        if (wordList.length > 0) {
            for (int i = 0; i < wordList.length; i++) {
                final String word = wordList[i];
                final int start = str.indexOf(word,currentIdx);
                final int end = start + word.length();
                currentIdx = end;
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        final ShanbayAPI sbAPI =new ShanbayAPI(NewsActivity.this);
                        sbAPI.setCallBack(new ShanbayAPI.callBack() {
                            @Override
                            public void setTranslate(String trans) {
                                final String translation = trans;
                                alert = null;
                                builder = new AlertDialog.Builder(NewsActivity.this);
                                Log.i("sbAPI.translate",trans);
                                alert = builder
                                        .setTitle(word.replaceAll("\\p{Punct}",""))
                                        .setMessage(trans)
                                        .setPositiveButton("添加单词", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                User.getInstance().addWord(new Word(word.replaceAll("\\p{Punct}",""),translation));
                                                Toast.makeText(NewsActivity.this, "成功添加~", Toast.LENGTH_SHORT).show();
                                            }
                                        }).create();             //创建AlertDialog对象
                                Window alertWindow = alert.getWindow();
                                alertWindow.setGravity(Gravity.BOTTOM);
                                WindowManager.LayoutParams lp = alertWindow.getAttributes(); // 获取对话框当前的参数值
                                lp.x = 0; // 新位置X坐标
                                lp.y = -40; // 新位置Y坐标
                                lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
                                alertWindow.setAttributes(lp);
                                alert.show();                    //显示对话框
                            }
                        });
                        sbAPI.execute(word);

                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //删除下划线，设置字体颜色
                        ds.setColor(Color.BLACK);
                        //ds.setTextSize(100);
                        ds.setUnderlineText(false);
                    }
                },start,end,0);
            }
        }
        return ssb;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
