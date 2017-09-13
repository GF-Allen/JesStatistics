package com.richardliu.jesstatisticslib.bean;

/**
 * 统计信息实体类
 * Created by allen on 2017/9/12.
 */
public class StatisticsInfo {
    private String p; //path 路径
    private String a; //action 动作 pv/kv
    private String t; //target 目标 id
    private String u; //des3aes(user_id) 用户
    private String ua; // user_agent 设备

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    @Override
    public String toString() {
        return "StatisticsInfo{" +
                "p='" + p + '\'' +
                ", a='" + a + '\'' +
                ", t='" + t + '\'' +
                ", u='" + u + '\'' +
                ", ua='" + ua + '\'' +
                '}';
    }
}
