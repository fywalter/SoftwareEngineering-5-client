package com.example.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demo.NewsActivity;
import com.example.demo.R;
import com.example.demo.model.MyTask;
import com.example.demo.model.News;
import com.example.demo.model.NewsTitle;

import java.util.ArrayList;
import java.util.List;


public class NewsTitleAdapter extends RecyclerView.Adapter<NewsTitleAdapter.ViewHolder> {
    private List<NewsTitle> ntList;
    private final Context context;
    private MyTask<News> newsTask = null;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View newsView;
        TextView nt_title;
        TextView nt_source;
        TextView nt_abstract;

        public ViewHolder(View view){
            super(view);
            newsView = view;
            nt_title = (TextView) view.findViewById(R.id.news_title);
            nt_source = (TextView) view.findViewById(R.id.news_source);
            nt_abstract = (TextView) view.findViewById(R.id.news_abstract);
        }
    }

    public NewsTitleAdapter(List<NewsTitle> ntList_, Context context_){
        this.ntList = ntList_;
        this.context = context_;
    }
    //添加一个元素
    public void add(NewsTitle data) {
        if (ntList == null) {
            ntList = new ArrayList<NewsTitle>();
        }
        ntList.add(data);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.newsView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                NewsTitle nt = ntList.get(position);
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("url",nt.getUrl());
                intent.putExtra("newsID",nt.getNewsID());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        NewsTitle nt = ntList.get(position);
        Typeface tf_medium = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Medium.ttf");
        Typeface tf_light = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Light.ttf");
        holder.nt_title.setTypeface(tf_medium);
        holder.nt_abstract.setTypeface(tf_light);
        holder.nt_title.setText(nt.getTitle());
        holder.nt_source.setText(nt.getSource().concat("  ").concat(nt.getNewsdate()));
        ArrayList<Object> params = new ArrayList<>();
        params.add(nt.getUrl());
        newsTask = new MyTask<News>("getNews",params);
        newsTask.setCallBack(newsTask.new CallBack() {
            @Override
            public void setSomeThing(News result) {
                String c = result.content;
                c = c.replaceAll("\n", " ");
                if(c.isEmpty())
                    holder.nt_abstract.setText("No abstract");
                else
                    holder.nt_abstract.setText(c);
            }
        });
        newsTask.execute();
    }

    @Override
    public int getItemCount(){
        return ntList.size();
    }
}
