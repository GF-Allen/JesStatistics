package com.richardliu.jesstatisticslib.utils;


import com.richardliu.jesstatisticslib.Constant;
import com.richardliu.jesstatisticslib.bean.StatisticsInfo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Created by allen on 2017/9/12.
 */
public class JesHttpUtils {

    private static JesHttpUtils jesHttpUtils;

    private OkHttpClient mClient;

    private JesHttpUtils() {
        mClient = new OkHttpClient.Builder()
                .connectTimeout(5 * 1000L, TimeUnit.MILLISECONDS)
                .readTimeout(5 * 1000L, TimeUnit.MILLISECONDS)
                .build();
    }

    private static JesHttpUtils getInstance() {
        if (jesHttpUtils == null) {
            jesHttpUtils = new JesHttpUtils();
        }
        return jesHttpUtils;
    }

    /**
     * 上传统计信息
     *
     * @param info
     */
    public static void sendCollentInfo(StatisticsInfo info) {
        String url = Constant.COLLECT_URL + "?" + getParamsToUrl(info);
        url = url.replace("\n", "");
        final Request request = new Request.Builder().url(url).build();
        getInstance().mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }

    /**
     * 自定义参数
     *
     * @param params
     */
    public static void sendCustomInfo(String params) {
        String url = Constant.COLLECT_URL + "?" + params;
        url = url.replace("\n", "");
        final Request request = new Request.Builder().url(url).build();
        getInstance().mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }

    private static String getParamsToUrl(StatisticsInfo info) {
        StringBuffer sb = new StringBuffer();
        sb.append("p=").append(info.getP()).append("&")
                .append("a=").append(info.getA()).append("&")
                .append("t=").append(info.getT()).append("&")
                .append("u=").append(info.getU()).append("&")
                .append("ua=").append(info.getUa());
        return sb.toString();
    }
}