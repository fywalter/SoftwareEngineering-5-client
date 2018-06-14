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
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.model.MyTask;
import com.example.demo.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private MyTask<ArrayList<String>> loginTask=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(this, Color.parseColor("#303F9F"));
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
            actionBar.setTitle("Log in");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView signupt = (TextView) findViewById(R.id.signup);
        signupt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usr = (EditText) findViewById(R.id.editText);
                EditText pswd = (EditText) findViewById(R.id.editText2);
                final String usrName = usr.getText().toString();
                String password = pswd.getText().toString();
                if(usrName.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your user name.", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                }
                else{
                    ArrayList<Object> params = new ArrayList<>();
                    params.add(usrName);
                    params.add(md5(password));
                    loginTask = new MyTask<ArrayList<String>>("login",params);
                    loginTask.setCallBack(loginTask.new CallBack() {
                        @Override
                        public void setSomeThing(ArrayList<String> result) {
                            if(result.isEmpty()){
                                Toast.makeText(LoginActivity.this, "Unable to log in with provided credentials.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(LoginActivity.this,"Logged in successfully", Toast.LENGTH_SHORT).show();
                                User.getInstance().setToken(result.get(0));
                                User.getInstance().setUserID(new Integer(result.get(1)).intValue());
                                User.getInstance().setUserName(usrName);
                                User.getInstance().setLoggedIn(true);
                                User.getInstance().fetchWordList();
                                finish();
                            }
                        }
                    });
                    loginTask.execute();
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
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}
