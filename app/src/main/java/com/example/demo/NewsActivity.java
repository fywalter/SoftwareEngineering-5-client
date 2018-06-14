package com.example.demo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.demo.model.MyTask;
import com.example.demo.model.News;
import com.example.demo.model.User;
import com.example.demo.model.Word;
import com.example.demo.utils.ShanbayAPI;

import java.util.ArrayList;


/**
 * Created by 费  渝 on 2018/5/24.
 */

public class NewsActivity extends AppCompatActivity {
    private Dialog wordCard = null;
    private ScrollView sc;
    private  int newsID = -1;
    private  int favorId=-1;
    private String url=null;
    private MyTask<News> newsTask=null;
    private MyTask<String> checkingTask = null;
    private MyTask<Integer> favorTask=null;
    private MyTask<Integer> disfavorTask=null;
    private MyTask<Integer> checkFavotTask = null;
    final private Context mcontext = this;
    private FloatingActionButton fbtn_backToTop;
    private FloatingActionButton fbtn_like;
    private Boolean isLiked = false;
    Typeface tf_light = null;
    Typeface tf_medium = null;
    Typeface tf_regular = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setStatusBarColor(this,Color.parseColor("#303F9F"));
        tf_light = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");
        tf_medium = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Medium.ttf");
        tf_regular = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");


        Intent intent = getIntent();
        url=intent.getStringExtra("url");
        newsID = intent.getIntExtra("newsID",-1);
        if (!url.isEmpty() && newsID == -1){
            newsID = new Integer(url.substring(url.indexOf("articles")).split("/")[1]).intValue();
        }
        Log.i("CurrentNewsID",new Integer(newsID).toString());

        //判断是否点过赞
        ArrayList<Object> params = new ArrayList<>();
        params.add(newsID);
        checkFavotTask = new MyTask<Integer>("checkFavor",params);
        checkFavotTask.setCallBack(checkFavotTask.new CallBack() {
            @Override
            public void setSomeThing(Integer result) {
                favorId=result;
                if(favorId!=-1){
                    isLiked=true;
                    fbtn_like.setImageResource(R.mipmap.like);
                }
            }
        });
        checkFavotTask.execute();
        initNews();

        //再来一篇按钮
        Button btn_another = (Button) findViewById(R.id.anotherone);
        btn_another.setTypeface(tf_regular);
        btn_another.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, NewsActivity.class);
                int newsListLength=User.getInstance().getNewsTitleList().size();
                String newUrl=null;
                do{
                    newUrl=User.getInstance().getNewsTitleList().get((int)(Math.random()*newsListLength)).getUrl();
                }while(newUrl.equals(url));
                intent.putExtra("url",newUrl);
                startActivity(intent);
            }
        });

        //打卡按钮（暂时为）
        Button btn_clockIn = (Button) findViewById(R.id.clockIn);
        btn_clockIn.setTypeface(tf_regular);
        btn_clockIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkingTask=new MyTask<String>("checking");
                checkingTask.setCallBack(checkingTask.new CallBack() {
                    @Override
                    public void setSomeThing(String result) {
                        String message="Check successfully! Day ";
                        Toast.makeText(NewsActivity.this, message.concat(result), Toast.LENGTH_SHORT).show();
                    }
                });
                checkingTask.execute();
            }
        });

        //赞按钮
        fbtn_like = (FloatingActionButton) findViewById(R.id.floating_btn_like);
        fbtn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLiked==false){
                    fbtn_like.setImageResource(R.mipmap.like);
                    isLiked=true;
                    ArrayList<Object> params = new ArrayList<>();
                    params.add(newsID);
                    favorTask = new MyTask<Integer>("favorite",params);
                    favorTask.setCallBack(favorTask.new CallBack() {
                        @Override
                        public void setSomeThing(Integer result) {
                            if(result==null){
                                favorId=-1;
                            }else{
                                favorId=result;
                            }
                        }
                    });
                    favorTask.execute();
                }else {
                    fbtn_like.setImageResource(R.mipmap.dislike);
                    isLiked = false;
                    if(favorId==-1){
                        return;
                    }
                    ArrayList<Object> params = new ArrayList<>();
                    params.add(newsID);
                    params.add(favorId);
                    disfavorTask = new MyTask<Integer>("disfavorite",params);
                    disfavorTask.setCallBack(disfavorTask.new CallBack() {
                        @Override
                        public void setSomeThing(Integer result) {
                            favorId=-1;
                        }
                    });
                    disfavorTask.execute();
                }
            }
        });

        //设置工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
        }

        //返回顶部按钮
        fbtn_backToTop = (FloatingActionButton) findViewById(R.id.floating_btn_btt);
        fbtn_backToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sc.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        // 评论
        final TextView viewComment =  (TextView) findViewById(R.id.view_comment);
        viewComment.setTypeface(tf_light);
        viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(mcontext, CommentListActivity.class);
                commentIntent.putExtra("newsID", newsID);
                startActivity(commentIntent);
            }
        });

    }
    private void initNews(){
        sc=(ScrollView) findViewById(R.id.sc);

        final ImageView iv_image = (ImageView)findViewById(R.id.news_image);
        final StringBuilder sb_title = new StringBuilder();
        final TextView tv_title = (TextView) findViewById(R.id.news_title);
        tv_title.setTypeface(tf_light);
        final TextView tv_source = (TextView) findViewById(R.id.news_source);
        tv_source.setTypeface(tf_medium);
        final TextView tv_time = (TextView) findViewById(R.id.news_time);
        tv_time.setTypeface(tf_light);
        final TextView tv_content = (TextView) findViewById(R.id.news_content);
        tv_content.setTypeface(tf_light);
        final StringBuilder sb_content = new StringBuilder();
        ArrayList<Object> params = new ArrayList<>();
        params.add(url);
        newsTask=new MyTask<News>("getNews",params);
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
                sb_content.append(result.content.replaceAll("\n"," \n\n"));
                tv_content.setMovementMethod(LinkMovementMethod.getInstance());
                tv_content.setText(addClickPart(sb_content.toString()), TextView.BufferType.SPANNABLE);

            }
        });
        newsTask.execute();
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
                                wordCard=new Dialog(mcontext,R.style.WordCard);
                                LinearLayout root =(LinearLayout) LayoutInflater.from(mcontext).inflate(R.layout.layout_wordcard,null);

                                TextView tv_word=(TextView)root.findViewById(R.id.tv_word);
                                TextView tv_explanation = (TextView)root.findViewById(R.id.tv_explanation);
                                Button btn_addWord= (Button)root.findViewById(R.id.btn_addWord);

                                tv_word.setText(word.replaceAll("[\\p{Punct} \\n]",""));
                                tv_explanation.setText(trans);
                                btn_addWord.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        if(translation=="Cannot find translation..."){
                                            Toast.makeText(NewsActivity.this, "并没有找到翻译:(", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            User.getInstance().addWord(new Word(word.replaceAll("[\\p{Punct} \\n]",""),translation));
                                            Toast.makeText(NewsActivity.this, "成功添加~", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                wordCard.setContentView(root);
                                Window dialogWindow = wordCard.getWindow();
                                dialogWindow.setGravity(Gravity.BOTTOM);
                                WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                                lp.x = 0; // 新位置X坐标
                                lp.y = 0; // 新位置Y坐标
                                lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
                                root.measure(0, 0);
                                lp.height = root.getMeasuredHeight();
                                dialogWindow.setAttributes(lp);
                                wordCard.show();
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


