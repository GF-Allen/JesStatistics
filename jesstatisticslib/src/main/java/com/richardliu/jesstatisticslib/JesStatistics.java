package com.richardliu.jesstatisticslib;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.richardliu.jesstatisticslib.bean.StatisticsInfo;
import com.richardliu.jesstatisticslib.service.JesStatisticsService;
import com.richardliu.jesstatisticslib.utils.JesHttpUtils;
import com.richardliu.jesstatisticslib.utils.ReflectUtils;
import com.richardliu.jesstatisticslib.utils.ResourceUtils;
import com.richardliu.jesstatisticslib.utils.StatisticsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计收集
 * Created by allen on 2017/9/12.
 */
public class JesStatistics {
    private static final String TAG = "JesStatistics";

    public Activity activity;

    public JesStatistics(Activity activity) {
        this.activity = activity;
        init();
    }

    private static ArrayList<String> isHookActivity = new ArrayList<>();


    private void init() {

        boolean have = isHookActivity.contains(activity.getClass().getName());
        if (have) {
            return;
        }
        //获取当前Activity所有的子View
        List<View> allChildViews = StatisticsUtils.getAllChildViews(activity);
        for (final View view : allChildViews) {
            handleOnClickListener(view);
        }
        isHookActivity.add(activity.getClass().getName());
    }

    public void removeActivity(Activity activity) {
        isHookActivity.remove(activity.getClass().getName());
    }

    private boolean handleOnClickListener(View view) {
        //利用反射获取view中设置的listener
        View.OnClickListener onClickListener = (View.OnClickListener) getListener(view, "mOnClickListener");
        if (onClickListener != null) {
            try {
                hookOnClickListener(view, onClickListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {

        }

        return false;
    }


    /**
     * 添加统计代码
     * @param view
     * @param onClickListener
     */
    private void hookOnClickListener(final View view, final View.OnClickListener onClickListener) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (view.getId() == -1) {
                    return;
                }
                View.OnClickListener onClickListenerHooked = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            String name = ResourceUtils.getResourceNameById(Class.forName(StatisticsUtils.getRClassName()), view.getId());
                            StatisticsInfo statisticsInfo = new StatisticsInfo();
                            statisticsInfo.setP(activity.getClass().getName());
                            statisticsInfo.setA(JesStatisticsType.ACTION_CLICK);
                            statisticsInfo.setT(name);
                            statisticsInfo.setU(JesStatisticsManager.userId);
                            statisticsInfo.setUa(JesStatisticsManager.UA);
                            JesHttpUtils.sendCollentInfo(statisticsInfo);
                            if (JesStatisticsManager.isShowDialog) {
                                Intent intent = new Intent(activity, JesStatisticsService.class);
                                intent.putExtra("activity", activity.getClass().getSimpleName());
                                intent.putExtra("id", name);
                                activity.startService(intent);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        onClickListener.onClick(view);
                    }
                };
                setListener(view, "mOnClickListener", onClickListenerHooked);
            }
        });
    }

    private void setListener(View view, String listenerName, Object value) {
        setListener(view, getClassByListenerName(listenerName), listenerName, value);
    }

    private void setListener(View view, Class<?> targetClass, String fieldName, Object value) {
        int level = countLevelFromViewToFather(view, targetClass);
        if (-1 == level) {
            return;
        }

        try {
            if (!(view instanceof AdapterView) && Build.VERSION.SDK_INT > 14) {
                Object mListenerInfo = ReflectUtils.getField(view, targetClass.getName(),
                        "mListenerInfo");
                if (null == mListenerInfo) {
                    return;
                }
                ReflectUtils.setField(mListenerInfo, null, fieldName, value);
            } else {
                ReflectUtils.setField(view, targetClass.getName(), fieldName, value);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Class<?> getClassByListenerName(String listenerName) {
        Class<?> viewClass = null;
        if ("mOnItemClickListener".equals(listenerName)
                || "mOnItemLongClickListener".equals(listenerName)) {
            viewClass = AdapterView.class;
        } else if ("mOnScrollListener".equals(listenerName)) {
            viewClass = AbsListView.class;
        } else if ("mOnChildClickListener".equals(listenerName)
                || "mOnGroupClickListener".equals(listenerName)) {
            viewClass = ExpandableListView.class;
        } else {
            viewClass = View.class;
        }
        return viewClass;
    }

    private Object getListener(View view, String fieldName) {
        return getListener(view, getClassByListenerName(fieldName), fieldName);
    }

    /**
     * 获取Listener
     *
     * @param view
     * @param targetClass
     * @param fieldName
     * @return
     */
    private Object getListener(View view, Class<?> targetClass, String fieldName) {
        //获取层级
        int level = countLevelFromViewToFather(view, targetClass);
        if (-1 == level) {
            return null;
        }

        try {
            //处理API14以上的情况，View中的listener封装下ListenerInfo类中
            if (!(view instanceof AdapterView) && Build.VERSION.SDK_INT > 14) {
                Object mListenerInfo = ReflectUtils.getField(view, targetClass.getName(),
                        "mListenerInfo");
                return null == mListenerInfo ? null : ReflectUtils.getField(mListenerInfo, null,
                        fieldName);
            } else {
                return ReflectUtils.getField(view, targetClass.getName(), fieldName);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // eat it
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取子类在父类的层级
     *
     * @param view
     * @param father
     * @return
     */
    private int countLevelFromViewToFather(View view, Class<?> father) {
        if (null == view) {
            return -1;
        }
        int level = 0;
        Class<?> originalClass = view.getClass();
        while (true) {
            if (originalClass.equals(Object.class)) {
                return -1;
            } else if (originalClass.equals(father)) {
                return level;
            } else {
                level++;
                originalClass = originalClass.getSuperclass();
            }
        }
    }
}
