package com.example.demo.model;

/**
 * Created by XiaoWei on 2018/5/25 0022.
 */
public class Word {
    private String wName;
    private String wExplain;

    public Word() {
    }

    public Word(String wName, String wExplain) {
        this.wName = wName;
        this.wExplain = wExplain;
    }

    public String getwName() {
        return wName;
    }

    public String getwExplain() {
        return wExplain;
    }

    public void setwName(String wName) {
        this.wName = wName;
    }

    public void setwExplain(String wExplain) {
        this.wExplain = wExplain;
    }
}
