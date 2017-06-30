package jc.cici.android.atom.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;

import static jc.cici.android.R.id.webView;

/**
 * 注意事项
 * Created by atom on 2017/4/13.
 */

public class NoticeActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // 标题
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 返回按钮
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // webView
    @BindView(R.id.webView)
    WebView webView;
    private SweetAlertDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 入栈
        AppManager.getInstance().addActivity(this);
        baseActivity = this;
        //去标题
        baseActivity.requestNoTitle();
        // 添加视图
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        //填充内容
        setContent();
    }

    private void setContent() {

        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setTitle("");
        dialog.show();

        WebSettings webSettings = webView.getSettings();
        //支持缩放，默认为true。
        webSettings.setSupportZoom(false);
        //调整图片至适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //设置默认编码
        webSettings.setDefaultTextEncodingName("utf-8");
        //设置自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
            }
        });
        webView.loadUrl("");

        /**
         * 禁止复制粘贴
         */
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    private void initView() {
        title_txt.setText("用户协议");
        back_layout.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.back_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }
}
