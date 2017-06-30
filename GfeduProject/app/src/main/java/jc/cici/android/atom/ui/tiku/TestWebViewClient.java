package jc.cici.android.atom.ui.tiku;


import android.content.Context;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class TestWebViewClient extends WebViewClient {

	private HttpUtils httpUtils = new HttpUtils();
	private Context mCtx;
	private Handler mHandler;

	public TestWebViewClient(Context ctx,Handler hander) {
		this.mCtx = ctx;
		this.mHandler = hander;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {

		return super.shouldOverrideUrlLoading(view, url);
	}

	@Override
	public void onPageFinished(WebView view, String url) {

		super.onPageFinished(view, url);
		mHandler.sendEmptyMessage(10);
		if (!httpUtils.isNetworkConnected(mCtx)) { // 断网情况
			view.stopLoading();
		}
	}
}
