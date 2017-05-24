package jc.cici.android.atom.ui.login;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.PasswordInfo;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.VerityCodeView;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 忘记密码手机验证
 * Created by atom on 2017/4/13.
 */

public class ResetPwdActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    private Context mCtx;
    // 标题
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 返回按钮
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 快速登录手机输入框布局
    @BindView(R.id.resetPwd_phone_layout)
    RelativeLayout resetPwd_phone_layout;
    // 快速登录手机号输入框
    @BindView(R.id.resetPwd_phone_txt)
    EditText resetPwd_phone_txt;
    //快速登录手机号错误提示图标
    @BindView(R.id.errorHint_resetPwdPhone_img)
    ImageView errorHint_resetPwdPhone_img;
    //快速登录手机号错误提示
    @BindView(R.id.errorPhone_resetPwdHint_txt)
    TextView errorPhone_resetPwdHint_txt;
    // 本地验证码布局
    @BindView(R.id.resetPwd_identifyInput_layout)
    RelativeLayout resetPwd_identifyInput_layout;
    // 本地验证码输入框
    @BindView(R.id.resetPwd_identify_txt)
    EditText resetPwd_identify_txt;
    // 本地验证码图片
    @BindView(R.id.resetPwd_identify_img)
    ImageView resetPwd_identify_img;
    // 本地验证码错误提示
    @BindView(R.id.resetPwd_errorIdentify_txt)
    TextView resetPwd_errorIdentify_txt;
    //  获取动态码布局
    @BindView(R.id.resetPwd_verifyInput_layout)
    RelativeLayout resetPwd_verifyInput_layout;
    // 获取动态码输入框
    @BindView(R.id.resetPwd_verify_txt)
    EditText resetPwd_verify_txt;
    // 获取动态码按钮
    @BindView(R.id.resetPwd_verify_Btn)
    Button resetPwd_verify_Btn;
    // 获取动态码错误提示
    @BindView(R.id.resetPwd_errorVerity_txt)
    TextView resetPwd_errorVerity_txt;
    // 快速登录按钮
    @BindView(R.id.resetPwd_Btn)
    Button resetPwd_Btn;
    private String phoneName, // 手机号字符串
            identifyCode, // 验证码字符串
            verityCode; // 动态码字符串

    // 点击标示
    private boolean clickFlag = true;
    // 产生的验证码
    private String realCode;
    // 倒计时对象
    private TimeCount timeCount;
    // 接口返回的动态验证码
    private String realMessage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_resetpwd;
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
        mCtx = this;
        // 添加文字
        initView();
        // 将验证码以图形的形式显示出来
        resetPwd_identify_img.setImageBitmap(VerityCodeView.getInstance().createBitmap());
        // 获取生成的验证码的值
        realCode = VerityCodeView.getInstance().getCode().toLowerCase();
        // 初始倒计时设置
        timeCount = new TimeCount(60000, 1000);
    }

    /**
     * 视图所有点击事件监听
     *
     * @param v
     */
    @OnClick({R.id.back_layout, R.id.resetPwd_identify_img, R.id.resetPwd_verify_Btn, R.id.resetPwd_Btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.resetPwd_identify_img: // 本地验证码图片监听
                resetPwd_identify_img.setImageBitmap(VerityCodeView.getInstance().createBitmap());
                realCode = VerityCodeView.getInstance().getCode().toLowerCase();
                break;
            case R.id.resetPwd_verify_Btn: // 获取动态验证码监听
                resetPwd_verify_Btn.setClickable(false);
                resetPwd_verify_Btn.setEnabled(false);
                resetPwd_verify_Btn.setBackgroundResource(R.drawable.login_button_normal_bg);
                // 发送消息，并启动倒计时
                String phoneStr = resetPwd_phone_txt.getText().toString();
                if (ToolUtils.isMobileNo(phoneStr)) { // 判断输入的号码为手机号码
                    // 获取验证码
                    sendPwdMessage(phoneStr);
                } else {
                    setErrorInfo(errorPhone_resetPwdHint_txt, "手机号为空或非法手机号，请输入正确号码");
                }
                break;
            case R.id.resetPwd_Btn: // 快速登录按钮监听
                if (baseActivity.verifyClickTime()) { // 防止快速点击
                    // 手机号
                    phoneName = resetPwd_phone_txt.getText().toString().trim();
                    // 验证码
                    identifyCode = resetPwd_identify_txt.getText().toString().toLowerCase().trim();
                    // 动态码
                    verityCode = resetPwd_verify_txt.getText().toString().trim();
                    if (null != phoneName && phoneName.length() > 0) {
                        if (ToolUtils.isMobileNo(phoneName)) { // 通过手机号验证
                            if (null != identifyCode && identifyCode.length() > 0) { // 图形输入框不为空
                                if (identifyCode.equals(realCode)) { // 图形输入框内容与所给图形内容相符
                                    if (null != verityCode && verityCode.length() > 0) { // 动态码不为空
                                        if (verityCode.equals(realMessage)) {
                                            if (NetUtil.isNetWorkConnected(mCtx)) { // 网络连接通过
                                                Bundle bundle = new Bundle();
                                                bundle.putString("phoneName", phoneName);
                                                baseActivity.openActivityAndCloseThis(ResetActivity.class, bundle);
                                            } else {
                                                setErrorInfo(resetPwd_errorVerity_txt, "网络连接失败，请检查网络连接");
                                            }
                                        } else {
                                            setErrorInfo(resetPwd_errorVerity_txt, "短信验证码与输入验证码不一致，请确认是否输入正确");
                                        }
                                    } else {
                                        setErrorInfo(resetPwd_errorVerity_txt, "手机验证码不能为空");
                                    }
                                } else {
                                    setErrorInfo(resetPwd_errorIdentify_txt, "输入验证码与图形验证码不相符");
                                }
                            } else {
                                setErrorInfo(resetPwd_errorIdentify_txt, "图形验证码不能为空");
                            }
                        } else {
                            setErrorInfo(errorPhone_resetPwdHint_txt, "手机号格式错误,请输入正确的手机号");
                        }
                    } else {
                        setErrorInfo(errorPhone_resetPwdHint_txt, "手机号不能为空");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 网络请求获取动态验证码
     *
     * @param phoneStr
     */
    private void sendPwdMessage(String phoneStr) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        String param = "{'Type':'S_Telephone','Code':'" + phoneStr + "'}";
        // 添加MD5加密字符串
        String mD5Result = ToolUtils.getMD5Str(param + Global.MD5_KEY);
        Observable<PasswordInfo> observable = httpPostService.sendFindPwdMessage(param, mD5Result);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<PasswordInfo>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(ResetPwdActivity.this, "短信发送失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(PasswordInfo passwordInfo) {
                                if ("1".equals(passwordInfo.getType())) {// 成功发送验证码
                                    timeCount.start();
                                    clickFlag = false;
                                    // 获取短信验证码
                                    realMessage = passwordInfo.getMsg();
                                } else if ("-1".equals(passwordInfo.getType())) { // 用户不存在
                                    Toast.makeText(ResetPwdActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                                } else if ("0".equals(passwordInfo.getType())) {
                                    Toast.makeText(ResetPwdActivity.this, "该手机号短信发送已经超过了今日上限，请明天再试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 视图输入框变化监听
     *
     * @param text
     */
    @OnTextChanged({R.id.resetPwd_phone_txt, R.id.resetPwd_identify_txt, R.id.resetPwd_verify_txt})
    void afterTextChanged(CharSequence text) {
        if (errorPhone_resetPwdHint_txt.getVisibility() == View.VISIBLE) { // 手机号错误提示
            errorPhone_resetPwdHint_txt.setVisibility(View.GONE);
        } else if (resetPwd_errorIdentify_txt.getVisibility() == View.VISIBLE) { // 图片验证错误提示
            resetPwd_errorIdentify_txt.setVisibility(View.GONE);
        } else if (resetPwd_errorVerity_txt.getVisibility() == View.VISIBLE) { // 动态码错误提示
            resetPwd_errorVerity_txt.setVisibility(View.GONE);
        }
        phoneName = getEditTextInput();
        if ("1".equals(phoneName)) {
            if (!ToolUtils.isMobileNo(resetPwd_phone_txt.getText().toString().trim())) { // 手机号非法
                setErrorInfo(errorPhone_resetPwdHint_txt, "非法手机号");
                resetPwd_phone_layout.setBackgroundResource(R.drawable.ic_error_box_input);
                errorHint_resetPwdPhone_img.setBackgroundResource(R.drawable.icon_input_error);
                errorHint_resetPwdPhone_img.setVisibility(View.VISIBLE);
                // 隐藏本地图形验证码
                resetPwd_identifyInput_layout.setVisibility(View.GONE);
                resetPwd_identify_img.setVisibility(View.GONE);
            } else {
                resetPwd_phone_layout.setBackgroundResource(R.drawable.ic_login_box_input);
                errorHint_resetPwdPhone_img.setBackgroundResource(R.drawable.icon_input_correct);
                errorHint_resetPwdPhone_img.setVisibility(View.VISIBLE);
                // 显示本地图形验证码
                resetPwd_identifyInput_layout.setVisibility(View.VISIBLE);
                resetPwd_identify_img.setVisibility(View.VISIBLE);
            }
        } else {
            resetPwd_phone_layout.setBackgroundResource(R.drawable.ic_login_box_input);
            errorHint_resetPwdPhone_img.setVisibility(View.GONE);
        }

        // 图形验证码操作
        if (clickFlag) {
            identifyCode = resetPwd_identify_txt.getText().toString().toLowerCase().trim();
            if (identifyCode.equals(realCode)) { // 输入验证码与图形验证码相同情况
                resetPwd_verify_Btn.setClickable(true);
                resetPwd_verify_Btn.setEnabled(true);
                resetPwd_verify_Btn.setTextColor(Color.parseColor("#ffffff"));
                resetPwd_verify_Btn.setBackgroundResource(R.drawable.login_button_bg);
            } else {
                resetPwd_verify_Btn.setClickable(false);
                resetPwd_verify_Btn.setEnabled(false);
                resetPwd_verify_Btn.setBackgroundResource(R.drawable.login_button_normal_bg);
            }
        }
    }

    /**
     * 视图初始显示
     */
    private void initView() {
        title_txt.setText("找回密码");
        back_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 用于倒计时操作
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long totalTime, long jumpTime) {
            super(totalTime, jumpTime);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (null != resetPwd_verify_Btn) {
                resetPwd_verify_Btn.setClickable(false);
                resetPwd_verify_Btn.setEnabled(false);
                resetPwd_verify_Btn.setBackgroundResource(R.drawable.login_button_normal_bg);
                resetPwd_verify_Btn.setText("已发送" + (millisUntilFinished / 1000) + "");
            }
        }

        @Override
        public void onFinish() {
            if (null != resetPwd_verify_Btn) {
                clickFlag = true;
                resetPwd_verify_Btn.setText("获取动态码");
                resetPwd_verify_Btn.setTextColor(Color.parseColor("#ffffff"));
                resetPwd_verify_Btn.setClickable(true);
                resetPwd_verify_Btn.setEnabled(true);
                resetPwd_verify_Btn.setBackgroundResource(R.drawable.login_button_bg);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
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

    /**
     * 判断手机号输入
     *
     * @return
     */
    private String getEditTextInput() {
        String firstChar = resetPwd_phone_txt.getText().toString().trim();
        if (null != firstChar && firstChar.length() > 0) {
            char a = firstChar.charAt(0);
            StringBuffer buffer = new StringBuffer();
            return buffer.append(a).toString();
        }
        return null;
    }
}
