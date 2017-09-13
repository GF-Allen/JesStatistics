package com.richardliu.jesstatisticslib.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.richardliu.jesstatisticslib.view.OkStatisticsDialog;

/**
 * Created by allen on 2017/9/12.
 */
public class JesStatisticsService extends Service {
    public JesStatisticsService() {
    }

    @Override
    public void onCreate() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String activity = intent.getStringExtra("activity");
        String id = intent.getStringExtra("id");
        OkStatisticsDialog.getInstance(this, activity, id).show();
        return super.onStartCommand(intent, flags, startId);
    }
}
