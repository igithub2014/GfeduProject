package jc.cici.android.atom.base;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * 所有Activity管理类
 * Created by atom on 2017/4/20.
 */

public class AppManager {
    // 创建栈对象
    private static Stack<Activity> activityStack;
    // 创建单利模式保证App运行时候，只有一个管理类对象
    private static AppManager activityManager;

    public static AppManager getInstance() {
        synchronized (AppManager.class) {
            if (null == activityManager) {
                activityManager = new AppManager();
            }
        }
        return activityManager;
    }

    /**
     * 添加activity
     *
     * @param ac
     */
    public void addActivity(Activity ac) {
        if (null == activityStack) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(ac);
    }

    /**
     * 获取当前栈顶Activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 关闭当前栈顶Activity
     */
    public void finishCurrentActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {

        if (null != activity) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 移除指定activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (null != activity) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束App中所有的activity
     */
    public void finishAllActivity() {

        for (Activity activity : activityStack) {
            if (null != activity) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出程序，结束所有Activity
     * @param context
     * @param isBackground
     */
    public void exitAPP(Context context, Boolean isBackground) {

        finishAllActivity();
        try {
            android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 如果有后台操作，请不要支持此语句
            if (!isBackground) {
                System.exit(0);
            }
        }
    }
}
