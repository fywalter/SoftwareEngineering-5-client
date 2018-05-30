package com.example.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.donkingliang.banner.CustomBanner;
import com.example.demo.adapter.MyAdapter;
import com.example.demo.adapter.NewsTitleAdapter;
import com.example.demo.model.Connection;
import com.example.demo.model.MyTask;
import com.example.demo.model.NewsTitle;
import com.example.demo.utils.NonSlideLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class NewsCategorizedFragment extends Fragment {
    private List<NewsTitle> ntList = null;
    private ListView newsListView;
    private MyAdapter<NewsTitle> myAdapter = null;
    private CustomBanner<String> mBanner;
    private MyTask<List<NewsTitle>> newsTitleTask = null;
    private int frag_type;

    public void setFrag_type(int frag_type) {
        this.frag_type = frag_type;
    }

    public static NewsCategorizedFragment newInstance(int t){
        NewsCategorizedFragment f = new NewsCategorizedFragment();
        f.setFrag_type(t);
        return f;
    }

    private void initNews(){
        ntList = Connection.getNewsList();
            NewsTitle nt = new NewsTitle("Trump Is Dust", "CNN", "2018-05-23","","");
            ntList.add(nt);
            NewsTitle nt2 = new NewsTitle("Tobu Railway Suffered From Human Accidents", "Japan Times", "2018-05-22","","");
            ntList.add(nt2);
            NewsTitle nt3 = new NewsTitle("Will Xi Jinping Change China?", "The Economist", "2018-05-21","","");
            ntList.add(nt3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_categorized, container, false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        NonSlideLinearLayoutManager layoutManager = new NonSlideLinearLayoutManager (getActivity());
        layoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        mBanner = (CustomBanner) rootView.findViewById(R.id.banner);

        if(frag_type == 1) {
            // 为了解决重复点击第一个界面报错需要每次new一个任务
            newsTitleTask = new MyTask("getNewsList");
            //自定义回调函数，在回调函数中更新界面
            newsTitleTask.setCallBack(newsTitleTask.new CallBack() {
                @Override
                public void setSomeThing(List<NewsTitle> newsList) {
                    ntList = newsList;
                    Log.i("len of ntList", Integer.toString(ntList.size()));
                    NewsTitleAdapter nta = new NewsTitleAdapter(ntList, getContext());
                    recyclerView.setAdapter(nta);
                    ArrayList<String> images = new ArrayList<>();
                    for(int i = 0; i < ntList.size(); i++){
                        images.add(ntList.get(i).getImgUrl());
                    }
                    setBean(images);
                }
            });
            newsTitleTask.execute();
        }
        else if (frag_type == 3){
            newsTitleTask = new MyTask("getRecommendNewsList");
            newsTitleTask.setCallBack(newsTitleTask.new CallBack() {
                @Override
                public void setSomeThing(List<NewsTitle> newsList) {
                    ntList = newsList;
                    Log.i("len of ntList", Integer.toString(ntList.size()));
                    NewsTitleAdapter nta = new NewsTitleAdapter(ntList, getContext());
                    recyclerView.setAdapter(nta);
                    ArrayList<String> images = new ArrayList<>();
                    for(int i = 0; i < ntList.size(); i++){
                        images.add(ntList.get(i).getImgUrl()); }setBean(images); }
            });
            newsTitleTask.execute();
        }
        else{
            initNews();
            NewsTitleAdapter nta = new NewsTitleAdapter(ntList, getContext());
            recyclerView.setAdapter(nta);
            ArrayList<String> images = new ArrayList<>();
            images.add("https://cdn.cnn.com/cnnnext/dam/assets/180526074218-03-north-korea-south-korea-meeting-0526-exlarge-169.jpg");
            images.add("https://cdn.cnn.com/cnnnext/dam/assets/180514162221-xinjiang-xi-jinping-poster-exlarge-169.jpg");
            images.add("https://cdn.cnn.com/cnnnext/dam/assets/180527213749-sao-paulo-area-truck-strike-exlarge-169.jpg");
            setBean(images);
        }

        return rootView;
    }
    private void setBean(final ArrayList beans) {
        mBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, beans);}
}
