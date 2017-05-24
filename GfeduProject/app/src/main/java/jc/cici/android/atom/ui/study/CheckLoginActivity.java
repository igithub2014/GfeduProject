package jc.cici.android.atom.ui.study;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.ui.login.NormalActivity;
import jc.cici.android.atom.ui.login.RegisterActivity;

/**
 * 检测用户是否登录
 * Created by atom on 2017/5/5.
 */

public class CheckLoginActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // Toolbar
    @BindView(R.id.title_layout)
    Toolbar title_layout;
    // 内容
    @BindView(R.id.title_content_layout)
    RelativeLayout title_content_layout;
    // 顶部图片
    @BindView(R.id.nike_Img)
    ImageView nike_Img;
    // 登录按钮
    @BindView(R.id.login_Btn)
    Button login_Btn;
    // 注册按钮
    @BindView(R.id.register_Btn)
    Button register_Btn;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_checklogin;
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
        // 去标题
        baseActivity.requestNoTitle();
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
    }

    private void initView() {
        title_layout.setBackgroundColor(Color.parseColor("#dd5555"));
       title_content_layout.setBackgroundColor(Color.parseColor("#dd5555"));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
        if (null != unbinder) {
            unbinder.unbind();

        }
    }

    /**
     * 按钮监听事件
     *
     * @param view
     */
    @OnClick({R.id.login_Btn, R.id.register_Btn})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.login_Btn: // 登录按钮
                baseActivity.openActivityAndCloseThis(NormalActivity.class);
                break;
            case R.id.register_Btn:

                baseActivity.openActivityAndCloseThis(RegisterActivity.class);
                break; // 注册按钮
            default:
                break;
        }
    }
}
