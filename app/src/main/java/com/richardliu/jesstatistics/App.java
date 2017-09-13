package com.richardliu.jesstatistics;

import android.app.Application;

import com.richardliu.jesstatisticslib.JesStatisticsManager;

/**
 * Created by AlenBeyond on 2017/9/14.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JesStatisticsManager.init(this,true);
    }
}
