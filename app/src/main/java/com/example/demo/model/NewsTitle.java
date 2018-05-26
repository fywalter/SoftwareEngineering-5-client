package com.example.demo.model;

public class NewsTitle {
    private String title;
    private String source;
    private String newsdate;
    public NewsTitle(String title_, String source_, String newsdate_){
        this.title = title_;
        this.source = source_;
        this.newsdate = newsdate_;
    }
    public String get_title(){
        return this.title;
    }
    public String get_source(){
        return this.source;
    }
    public String get_newsdate(){
        return this.newsdate;
    }
}
