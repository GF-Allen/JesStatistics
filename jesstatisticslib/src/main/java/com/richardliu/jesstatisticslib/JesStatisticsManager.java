package com.richardliu.jesstatisticslib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Process;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.richardliu.jesstatisticslib.bean.StatisticsInfo;
import com.richardliu.jesstatisticslib.utils.JesHttpUtils;
import com.richardliu.jesstatisticslib.utils.StatisticsUtils;

import java.io.File;

/**
 * 全局入口
 * Created by allen on 2017/9/12.
 */
public class JesStatisticsManager {

    private static final String TAG = "StatisticsManager";

    public static boolean isShowDialog = false;

    private static JesStatistics jesStatistics;
    private static Gson gson;
    public static String userId = "";
    public static String UA = "";

    public static void init(Application app,boolean isShowDialog) {

        StatisticsUtils.app = app;
        JesStatisticsManager.isShowDialog = isShowDialog;
        String processName = StatisticsUtils.getProcessName(app, Process.myPid());
        //处理app拥有多个进程
        if (!processName.equals(app.getPackageName())) {
            return;
        }
        WebView webView = new WebView(app);
        UA = webView.getSettings().getUserAgentString();
        gson = new Gson();
        initDB(app);

        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                StatisticsInfo statisticsInfo = new StatisticsInfo();
                statisticsInfo.setP(activity.getClass().getName());
                statisticsInfo.setT(activity.getClass().getSimpleName());
                statisticsInfo.setA(JesStatisticsType.ACTION_PAGE);
                statisticsInfo.setU(userId);
                statisticsInfo.setUa(UA);
                JesHttpUtils.sendCollentInfo(statisticsInfo);
                //切换活动中的Activity
                if (jesStatistics != null) {
                    if (!activity.getClass().getName().equals(jesStatistics.activity.getClass().getName())) {
                        jesStatistics = new JesStatistics(activity);
                    }
                } else {
                    jesStatistics = new JesStatistics(activity);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                jesStatistics.removeActivity(activity);
            }
        });
    }

    private static void initDB(Application app) {
        try {
            SharedPreferences spf = app.getSharedPreferences(Constant.SPF_NAME, Context.MODE_PRIVATE);
            int old_versionCode = spf.getInt("old_versionCode", -1);
            PackageInfo packageInfo = app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
            if (packageInfo.versionCode > old_versionCode || BuildConfig.DEBUG) {
                File path = app.getDatabasePath(Constant.DB_NAME);
                if (path.exists()) {
                    path.delete();
                    app.getDatabasePath(Constant.DB_NAME + "-journal").delete();
                }
                spf.edit().putInt("old_versionCode", packageInfo.versionCode).apply();
            }
            // TODO: 2017/9/13 创建数据库
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置UserId
     *
     * @param userId
     */
    public static void setUserId(String userId) {
        JesStatisticsManager.userId = StatisticsUtils.des(userId);
    }


    /**
     * 自定义收集
     *
     * @param path   路径
     * @param action 动作
     * @param target 目标
     */
    public static void customCollect(String path, String action, String target) {
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        statisticsInfo.setP(path);
        statisticsInfo.setA(action);
        statisticsInfo.setT(target);
        statisticsInfo.setU(userId);
        statisticsInfo.setUa(UA);
        JesHttpUtils.sendCollentInfo(statisticsInfo);
    }

    /**
     * 自定义收集,自定义参数类型,默认User和UserAgent写好
     */
    public static void customCollect(String params) {
        StringBuffer sb = new StringBuffer();
        sb.append(params).append("&")
                .append("u=").append(userId).append("&")
                .append("ua=").append(UA);
        JesHttpUtils.sendCustomInfo(sb.toString());
    }

    /**
     * 统计订单相关
     *
     * @param path
     * @param action
     * @param target
     * @param order_no
     */
    public static void customCollectForOrder(String path, String action, String target, String order_no) {
        StringBuffer sb = new StringBuffer();
        sb.append("p=").append(path).append("&")
                .append("a=").append(action).append("&")
                .append("t=").append(target).append("&")
                .append("order_no=").append(order_no).append("&")
                .append("u=").append(userId).append("&")
                .append("ua=").append(UA);
        JesHttpUtils.sendCustomInfo(sb.toString());
    }
}
