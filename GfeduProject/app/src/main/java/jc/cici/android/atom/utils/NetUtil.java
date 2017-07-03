package jc.cici.android.atom.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络处理类
 * Created by atom on 2017/3/28.
 */

public class NetUtil {

    // 列举网络变化枚举类型
    public enum netType {
        WIFI, CMNET, CMWAP, NONENET // Wifi,联通，移动，无网络
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkAvalable(Context context) {
        if (null != context) {
            // 获取网络连接管理器对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfos = connMgr.getAllNetworkInfo();
            if (null != networkInfos) {
                for (int i = 0; i < networkInfos.length; i++) {
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) { // 有网络情况
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否为移动网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnectedByMobile(Context context) {
        if (null != context) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (null != networkInfo) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断wifi网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWIFIConnected(Context context) {
        if (null != context) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (null != networkInfo) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断网络是连接
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {

        if (null != context) {
            // 获取网络连接管理器对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (null != networkInfo) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接类型
     *
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (null != context) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (null != networkInfo && networkInfo.isAvailable()) {
                return networkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取APP运行网络类型(可用于后台Wifi情况下更新APP版本)
     *
     * @param context
     * @return
     */
    public static netType getAppNetType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (null == networkInfo) {
            return netType.NONENET; // 无网络状态
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if ("cmet".equals(networkInfo.getExtraInfo().toLowerCase())) {
                return netType.CMNET; // 中国联通
            } else {
                return netType.CMWAP; // 中国移动
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return netType.WIFI; // wifi情况
        }
        return netType.NONENET;
    }
}
