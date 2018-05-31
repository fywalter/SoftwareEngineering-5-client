package com.example.demo.model;

import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;

import java.util.ArrayList;

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
    //重载equals方法以用于myAdaptec删除元素
    public boolean equals(Object word){
        if(this == word) return true;
        if(word == null) return false; //能调用这个方法，this肯定不为null，所以不判断this
        if(this.getClass() != word.getClass()) return false; //如果不死同一个类，则必然false
        Word that = (Word)word; //将Object类型的x转换为Data型。因为上一行已经判断了x是否为Data型，所以可以直接转换

        if(this.wName != that.wName) return false;
        if(this.wExplain != that.wExplain) return false;

        return true;
    }
    public void setwName(String wName) {
        this.wName = wName;
    }
    public void setwExplain(String wExplain) {
        this.wExplain = wExplain;
    }
    public String toJson(){
        String jformat = null;
        try {
            jformat = new JSONObject()
                    .put("raw", getwName())
                    .put("meaning", getwExplain()).toString();
        }catch(Exception e){
            Log.e("wordToJson","can't transform word to String");
        }
        return jformat;
    }
    static public ArrayList<Word> parseWordList(String content) throws Exception {
        ArrayList<Word> wordList = new ArrayList<>();
        JSONArray array = new JSONObject(content).getJSONArray("glossary");
        for (int i = 0; i < array.length(); i++) {
            JSONObject results = (JSONObject) array.get(i);
            Word w = new Word();
            w.setwName(results.getString("raw"));
            w.setwExplain(results.getString("meaning"));
            wordList.add(w);
        }
        return wordList;
    }
    static public String beautify(String explain){
        String shorter= explain;
        int firstLineEnd = shorter.indexOf('\n');
        if (firstLineEnd > 0){
            shorter = shorter.substring(0,firstLineEnd);
        }
        if (shorter.length() > 18){
            shorter = shorter.substring(0,15)+"...";
        }
        return shorter;
    }
}
