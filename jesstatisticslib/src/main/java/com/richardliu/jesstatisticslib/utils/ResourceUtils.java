package com.richardliu.jesstatisticslib.utils;


import com.richardliu.jesstatisticslib.bean.IdResource;
import com.richardliu.jesstatisticslib.helper.IDResourceHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by allen on 2017/9/12.
 */
public class ResourceUtils {
    private static final String TAG = "ResourceUtils";
    private static Object idResource = null;

    private static Object getIdResource(Class<?> resource) {
        if (idResource == null) {
            try {
                Class<?>[] classes = resource.getClasses();
                for (Class<?> c : classes) {
                    int i = c.getModifiers();
                    String className = c.getName();
                    String s = Modifier.toString(i);
                    if (s.contains("static") && className.contains("id")) {
                        return c.getConstructor().newInstance();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return idResource;
    }

    /**
     * 插入id表
     *
     * @param rClass
     */
    public static void insertIdResource(Class<?> rClass) {
        try {
            Class<?> aClass = getIdResource(rClass).getClass();
            Field[] fields = aClass.getFields();
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                int id = fields[i].getInt(aClass);
                IdResource idResource = new IdResource();
                idResource.setId(id);
                idResource.setName(name);
                IDResourceHelper.getInstance().insert(idResource);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据Id获取name
     *
     * @param rClass
     * @param id
     * @return
     */
    public static String getResourceNameById(Class<?> rClass, int id) {
        try {
            Class<?> aClass = getIdResource(rClass).getClass();
            Field[] fields = aClass.getFields();
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                if (id == fields[i].getInt(aClass)) {
                    IdResource idResource = new IdResource();
                    idResource.setId(id);
                    idResource.setName(name);
                    return name;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
