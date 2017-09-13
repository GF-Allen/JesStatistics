package com.richardliu.jesstatisticslib.bean;

import java.io.Serializable;

/**
 * Created by allen on 2017/9/12.
 */
public class IdResource implements Serializable {

    private int id;
    private String name;

    public IdResource() {
    }

    public IdResource(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
