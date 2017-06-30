package jc.cici.android.atom.ui.tiku;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;




public class NetBroadcastReceiver extends BroadcastReceiver {
	public NetEvevt evevt = BaseActivity.evevt;

	@Override
	public void onReceive(Context context, Intent intent) {
		// 如果相等的话就说明网络状态发生了变化
		System.out.println("111--- " + intent.getAction());
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			int netWorkState = NetUtil.getNetWorkState(context);
			// 接口回调传过去状态的类型
			try {
				evevt.onNetChange(netWorkState);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {// 不相等，也需要回调
			System.out.println("1111");
			int netWorkState = NetUtil.getNetWorkState(context);
			// 接口回调传过去状态的类型
			evevt.onNetChange(netWorkState);
		}
	}

	// 自定义接口
	public interface NetEvevt {
		public void onNetChange(int netMobile);
	}
}
