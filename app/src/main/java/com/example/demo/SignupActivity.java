package com.example.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.model.MyTask;
import com.example.demo.model.News;

import java.util.ArrayList;

import static com.example.demo.LoginActivity.md5;

public class SignupActivity extends AppCompatActivity {
    private MyTask<String> registerTask=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(this, Color.parseColor("#303F9F"));
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
            actionBar.setTitle("Sign up");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView logint = (TextView) findViewById(R.id.lgin);
        logint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        final EditText userNameText = (EditText)findViewById(R.id.editText3);
        final EditText passText = (EditText)findViewById(R.id.editText4);
        final EditText pass2Text = (EditText)findViewById(R.id.editText5);
        Button btn = findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = passText.getText().toString();
                String pass2 = pass2Text.getText().toString();
                String usr = userNameText.getText().toString();
               if(!pass.equals(pass2)){
                    Toast.makeText(SignupActivity.this, "Please confirm your password.", Toast.LENGTH_SHORT).show();
                }
                else if(usr.isEmpty()){
                   Toast.makeText(SignupActivity.this, "Please enter your user name.", Toast.LENGTH_SHORT).show();
               }
                else{
                    ArrayList<Object> params = new ArrayList<>();
                    params.add(usr);
                    params.add(md5(pass));
                    registerTask = new MyTask<String>("register",params);
                    registerTask.setCallBack(registerTask.new CallBack() {
                        @Override
                        public void setSomeThing(String result) {
                            if(result.isEmpty())
                                Toast.makeText(SignupActivity.this, "A user with that username already exists.", Toast.LENGTH_SHORT).show();
                            else{
                                Toast.makeText(SignupActivity.this, "Registered successufully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                    registerTask.execute();
                }
            }
        });

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
