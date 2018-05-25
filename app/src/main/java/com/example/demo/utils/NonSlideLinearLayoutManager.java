package com.example.demo.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class NonSlideLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public NonSlideLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
