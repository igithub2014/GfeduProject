package jc.cici.android.atom.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 密码重置
 * Created by User on 2017/4/13.
 */

public class ResetActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    private Context mCtx;
    // 标题
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 返回按钮
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 密码输入布局
    @BindView(R.id.reset_layout)
    RelativeLayout reset_layout;
    // 密码输入框
    @BindView(R.id.reset_txt)
    EditText reset_txt;
    // 密码格式错误提示图标
    @BindView(R.id.hint_resetTag_img)
    CheckBox hint_resetTag_img;
    // 密码格式错误提示
    @BindView(R.id.error_resetTap_txt)
    TextView error_resetTap_txt;
    // 确认密码布局
    @BindView(R.id.resetPWD_layout)
    RelativeLayout resetPWD_layout;
    // 确认密码输入框
    @BindView(R.id.restPassWord_txt)
    EditText restPassWord_txt;
    // 确认密码格式错误图标
    @BindView(R.id.hint_resetPWDTag_img)
    CheckBox hint_resetPWDTag_img;
    // 确认密码错误提示
    @BindView(R.id.errorPsd_resetTap_txt)
    TextView errorPsd_resetTap_txt;
    // 确认按钮
    @BindView(R.id.reset_Btn)
    Button reset_Btn;
    private String newPassWord, // 新密码,再次输入密码
            setAgainWord; // 再次输入密码
    private String phoneName; // 手机号
    private SweetAlertDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset;
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        mCtx = this;
        // 添加文字
        initView();
    }

    /**
     * 视图初始显示
     */
    private void initView() {
        title_txt.setText("密码重置");
        back_layout.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        // 获取动态码
        phoneName = bundle.getString("phoneName");

    }

    @OnClick({R.id.back_layout, R.id.reset_Btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮监听
                this.finish();
                break;
            case R.id.reset_Btn: // 重置确认按钮监听
                if (baseActivity.verifyClickTime()) { // 防止重复点击
                    newPassWord = reset_txt.getText().toString().trim();
                    setAgainWord = restPassWord_txt.getText().toString().trim();
                    if (null != newPassWord && newPassWord.length() > 0) { // 新密码不为空
                        if (ToolUtils.isPWDAvailable(newPassWord)) { // 符合密码设置规则
                            if (null != setAgainWord && setAgainWord.length() > 0) {
                                if (newPassWord.equals(setAgainWord)) { // 两次密码输入一致
                                    if (NetUtil.isNetWorkConnected(mCtx)) { // 网络连接正常
                                        getResetInfoFromHttp(phoneName, setAgainWord);
                                    } else {
                                        setErrorInfo(errorPsd_resetTap_txt, "网络连接失败");
                                    }
                                } else {
                                    setErrorInfo(errorPsd_resetTap_txt, "两次输入密码不一致");
                                }
                            } else {
                                setErrorInfo(errorPsd_resetTap_txt, "重复输入密码不能为空");
                            }
                        } else {
                            setErrorInfo(error_resetTap_txt, "新密码格式错误，密码必须为8到15位数字与字母组合");
                        }
                    } else { // 新密码为空
                        setErrorInfo(error_resetTap_txt, "新密码不能为空");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 网络获取重置信息
     *
     * @param phoneName   用户手机号
     * @param setAgainWord 新用户密码
     */
    private void getResetInfoFromHttp(String phoneName, String setAgainWord) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        dialog = new SweetAlertDialog(mCtx, SweetAlertDialog.PROGRESS_TYPE);

        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        String param = "{'Type':'S_Telephone','Code':'" + phoneName
                + "','SPassword':'" + setAgainWord + "'}";
        // 添加MD5加密字符串
        String mD5Result = ToolUtils.getMD5Str(param + Global.MD5_KEY);
        Observable<Integer> observable = httpPostService.getResetPwdInfo(param, mD5Result);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Integer>() {
                            @Override
                            public void onCompleted() {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismissWithAnimation();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismissWithAnimation();
                                    Toast.makeText(ResetActivity.this, "密码重置失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(Integer code) {
                                switch (code) {
                                    case 1: // 修改成功
                                        new SweetAlertDialog(mCtx, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                                .setTitleText("修改成功")
                                                .setCustomImage(R.drawable.icon_dialog_login)
                                                .setContentText("密码设置成功，请妥善保存您的密码")
                                                .setConfirmText("立即登录")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismissWithAnimation();
                                                        baseActivity.finish();
                                                    }
                                                }).show();
                                        break;
                                    case -1: // 密码格式错误
                                        Toast.makeText(ResetActivity.this, "密码必须为8到15位数字字符组合", Toast.LENGTH_SHORT).show();
                                        break;
                                    case -2: // 服务器内部错误
                                        Toast.makeText(ResetActivity.this, "服务器内部错误", Toast.LENGTH_SHORT).show();
                                        break;
                                    case -4:
                                        Toast.makeText(ResetActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                if (dialog != null && !dialog.isShowing()) {
                                    dialog.show();
                                }
                            }
                        }
                );
    }

    /**
     * 输入框内容变化监听
     *
     * @param text
     */
    @OnTextChanged({R.id.reset_txt, R.id.restPassWord_txt})
    void afterTextChanged(CharSequence text) {
        if (error_resetTap_txt.getVisibility() == View.VISIBLE) {
            error_resetTap_txt.setVisibility(View.GONE);
        } else if (errorPsd_resetTap_txt.getVisibility() == View.VISIBLE) {
            errorPsd_resetTap_txt.setVisibility(View.GONE);
        }
    }

    @OnCheckedChanged({R.id.hint_resetTag_img, R.id.hint_resetPWDTag_img})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.hint_resetTag_img: // 重新输入密码显示
                if (isChecked) { // 文本可见
                    setShowOrHidePwd(reset_txt, true);
                    hint_resetTag_img.setBackgroundResource(R.drawable.icon_show);
                } else {
                    setShowOrHidePwd(reset_txt, false);
                    hint_resetTag_img.setBackgroundResource(R.drawable.icon_hide);
                }
                break;
            case R.id.hint_resetPWDTag_img: // 确认重新输入密码显示
                if (isChecked) {
                    setShowOrHidePwd(restPassWord_txt, true);
                    hint_resetPWDTag_img.setBackgroundResource(R.drawable.icon_show);
                } else {
                    setShowOrHidePwd(restPassWord_txt, false);
                    hint_resetPWDTag_img.setBackgroundResource(R.drawable.icon_hide);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 密码显示与隐藏
     *
     * @param tv
     * @param flag
     */
    private void setShowOrHidePwd(TextView tv, boolean flag) {
        if (null != tv) { // 文本可见
            if (flag) { // 密码可见
                tv.setTransformationMethod(HideReturnsTransformationMethod
                        .getInstance());
            } else { // 密码不可见
                tv.setTransformationMethod(PasswordTransformationMethod
                        .getInstance());
            }
            // 切换后将EditText光标置于末尾
            CharSequence charSequence = tv.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText,
                        charSequence.length());
            }
        }
    }

    /**
     * 输出错误信息
     *
     * @param tv
     * @param str
     */
    private void setErrorInfo(TextView tv, String str) {
        if (null != tv) {
            tv.setText(str);
            tv.setVisibility(View.VISIBLE);
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
