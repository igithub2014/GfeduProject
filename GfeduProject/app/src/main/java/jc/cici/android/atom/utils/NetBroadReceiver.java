package jc.cici.android.atom.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import jc.cici.android.atom.base.BaseActivity;

/**
 * 网络监听变化通知类
 * Created by atom on 2017/3/28.
 */

public class NetBroadReceiver extends BroadcastReceiver {

    NetEvent event = BaseActivity.event;

    public NetBroadReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            int netType = NetUtil.getConnectedType(context);
            event.onNetChange(netType);
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 网络变化接口
     */
    public interface NetEvent{
        // 网络变化
        void onNetChange(int netMobile);
    }
}
