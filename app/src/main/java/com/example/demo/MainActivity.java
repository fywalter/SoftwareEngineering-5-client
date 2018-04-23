package com.example.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.text.method.LinkMovementMethod;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t1 = (TextView) findViewById(R.id.main);

        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.news_example));

        t1.setMovementMethod(LinkMovementMethod.getInstance());
        t1.setText(addClickPart(sb.toString()), TextView.BufferType.SPANNABLE);
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
                final String name = wordList[i];
                final int start = str.indexOf(name,currentIdx);
                final int end = start + name.length();
                currentIdx = end;

                ssb.setSpan(new ClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        new ShanbayAPI(MainActivity.this).execute(name);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //删除下划线，设置字体颜色为蓝色
                        ds.setColor(Color.GREEN);
                        //ds.setTextSize(100);
                        ds.setUnderlineText(false);
                    }
                },start,end,0);
            }
        }
        return ssb;
    }
}
