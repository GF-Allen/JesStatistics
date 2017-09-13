package com.richardliu.jesstatisticslib.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;

import com.richardliu.jesstatisticslib.encrypt.DES3;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by allen on 2017/9/12.
 */
public class StatisticsUtils {

    public static Application app;

    public static List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }

    public static List<View> getAllChildViews(Activity activity) {
        View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        return getAllChildViews(view);
    }

    /**
     * 获取R文件类名
     *
     * @return
     */
    public static String getRClassName() {
        String name = app.getClass().getName();
        String simpleName = app.getClass().getSimpleName();
        String[] split = name.split(simpleName);
        String r = split[0] + "R";
        return r;
    }

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static String des(String str) {
        byte[] secretArr = null;
        try {
            secretArr = DES3.encryptMode(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(secretArr, Base64.URL_SAFE);
    }


    /**
     * 获取当前进程名
     *
     * @param cxt
     * @param pid
     * @return
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static PackageInfo getPackageInfo(Application app) {
        try {
            return app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
