package jc.cici.android.atom.ui.tiku;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;


abstract public class BaseActivity extends FragmentActivity implements NetBroadcastReceiver.NetEvevt {

	public static NetBroadcastReceiver.NetEvevt evevt;
	/**
	 * 网络类型
	 */
	private int netMobile;

	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
		evevt = this;
		inspectNet();
	}

	/**
	 * 初始化时判断有没有网络
	 */

	public boolean inspectNet() {
		this.netMobile = NetUtil.getNetWorkState(BaseActivity.this);

		if (netMobile == 1) {
			System.out.println("inspectNet：连接wifi");
		} else if (netMobile == 0) {
			System.out.println("inspectNet:连接移动数据");
		} else if (netMobile == -1) {
			System.out.println("inspectNet:当前没有网络");

		}

		return isNetConnect();

	}

	/**
	 * 网络变化之后的类型
	 */
	@Override
	public void onNetChange(int netMobile) {
		this.netMobile = netMobile;
		isNetConnect();

	}

	/**
	 * 判断有无网络 。
	 *
	 * @return true 有网, false 没有网络.
	 */
	public boolean isNetConnect() {
		if (netMobile == 1) {
			return true;
		} else if (netMobile == 0) {
			return true;
		} else if (netMobile == -1) {
			return false;

		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

}
