package com.richardliu.jesstatisticslib.helper;


import com.richardliu.jesstatisticslib.bean.IdResource;

/**
 * TODO 数据存储
 * Created by allen on 2017/9/12.
 */
public class IDResourceHelper {
    private static IDResourceHelper helper = null;

    public static synchronized IDResourceHelper getInstance() {
        if (helper == null) {
            helper = new IDResourceHelper();
        }
        return helper;
    }

    private IDResourceHelper() {

    }

    /**
     * TODO 插入
     *
     * @param info
     */
    public void insert(IdResource info) {

    }

    /**
     * TODO 根据ID查找控件
     *
     * @param id
     * @return
     */
    public IdResource getIdResourceById(int id) {

        return null;
    }
}
