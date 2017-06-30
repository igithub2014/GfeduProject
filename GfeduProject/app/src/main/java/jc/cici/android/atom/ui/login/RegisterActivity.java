package jc.cici.android.atom.ui.login;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import android.widget.ImageView;
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
import jc.cici.android.atom.bean.Register;
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
 * 金程网校注册功能
 * Created by atom on 2017/4/12.
 */

public class RegisterActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    private Context mCtx;
    // 标题
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 返回按钮
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 注册昵称布局
    @BindView(R.id.register_layout)
    RelativeLayout register_layout;
    // 注册昵称输入框
    @BindView(R.id.register_userName_txt)
    EditText register_userName_txt;
    // 注册昵称错误提示
    @BindView(R.id.errorUserName_register_tapTxt)
    TextView errorUserName_register_tapTxt;
    // 注册手机布局
    @BindView(R.id.phone_layout)
    RelativeLayout phone_layout;
    // 注册手机输入框
    @BindView(R.id.phone_txt)
    EditText phone_txt;
    // 注册手机号输入错误提示图标
    @BindView(R.id.hint_phoneTag_img)
    ImageView hint_phoneTag_img;
    // 注册手机号格式错误提示
    @BindView(R.id.errorPhone_tap_txt)
    TextView errorPhone_tap_txt;
    // 本地验证码输入布局
    @BindView(R.id.identify_input_layout)
    RelativeLayout identify_input_layout;
    // 本地验证码输入框
    @BindView(R.id.identify_txt)
    EditText identify_txt;
    // 本地验证图片
    @BindView(R.id.identify_Tag_img)
    ImageView identify_Tag_img;
    //  本地验证码错误
    @BindView(R.id.errorIdentify_tap_txt)
    TextView errorIdentify_tap_txt;
    // 动态码布局
    @BindView(R.id.verify_input_layout)
    RelativeLayout verify_input_layout;
    // 动态码输入框
    @BindView(R.id.verify_txt)
    EditText verify_txt;
    // 动态码按钮
    @BindView(R.id.verify_Btn)
    Button verify_Btn;
    // 动态码错误提示
    @BindView(R.id.errorVerity_tap_txt)
    TextView errorVerity_tap_txt;
    // 注册密码布局
    @BindView(R.id.registerPWD_layout)
    RelativeLayout registerPWD_layout;
    //注册框密码输入框
    @BindView(R.id.register_PWD_txt)
    EditText register_PWD_txt;
    // 注册框密码显示图标
    @BindView(R.id.register_checkBox)
    CheckBox register_checkBox;
    // 注册密码框错误提示
    @BindView(R.id.errorPWD_register_tapTxt)
    TextView errorPWD_register_tapTxt;
    // 注册登录按钮
    @BindView(R.id.loginRegister_Btn)
    Button loginRegister_Btn;
    // 注意事项提示
    @BindView(R.id.userTip_txt)
    TextView userTip_txt;
    // 立即登录
    @BindView(R.id.immediatelyLogin_txt)
    TextView immediatelyLogin_txt;
    // 产生的验证码
    private String realCode;
    // 倒计时对象
    private TimeCount timeCount;
    // 点击标示
    private boolean clickFlag = true;
    private String nikeName, // 昵称字符串
            phoneName, // 手机号字符串
            identifyCode, // 验证码字符串
            verityCode,  // 动态码字符串
            pwd; // // 密码字符串
    // 进度对话框
    private SweetAlertDialog dialog;
    // 真正的动态验证码
    private String realVerityCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
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
        identify_Tag_img.setImageBitmap(VerityCodeView.getInstance().createBitmap());
        // 获取生成的验证码的值
        realCode = VerityCodeView.getInstance().getCode().toLowerCase();
        // 初始倒计时设置
        timeCount = new TimeCount(60000, 1000);
    }

    /**
     * 视图初始显示
     */
    private void initView() {
        title_txt.setText("注册");
        back_layout.setVisibility(View.VISIBLE);

    }

    /**
     * 视图交互按钮监听
     *
     * @param v
     */
    @OnClick({R.id.back_layout, R.id.identify_Tag_img, R.id.verify_Btn, R.id.loginRegister_Btn, R.id.userTip_txt, R.id.immediatelyLogin_txt})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮监听
                this.finish();
                break;
            case R.id.identify_Tag_img: // 本地图片验证监听
                identify_Tag_img.setImageBitmap(VerityCodeView.getInstance().createBitmap());
                realCode = VerityCodeView.getInstance().getCode().toLowerCase();
                break;
            case R.id.verify_Btn: // 动态码按钮监听
                String phoneStr = phone_txt.getText().toString();
                if (ToolUtils.isMobileNo(phoneStr)) { // 判断输入的号码为手机号码
                    // 当动态码按钮被点击后，无论是否成功，都设置为灰色不可点击状态
                    verify_Btn.setClickable(false);
                    verify_Btn.setEnabled(false);
                    verify_Btn.setBackgroundResource(R.drawable.login_button_normal_bg);
                    // 发送短信请求
                    sendPhoneMessage(phoneStr);
                } else {
                    setErrorInfo(errorPhone_tap_txt, "手机号为空或非法手机号，请输入正确号码");
                }
                break;
            case R.id.loginRegister_Btn: // 注册登录按钮监听
                if (baseActivity.verifyClickTime()) { // 防止快速点击
                    // 昵称
                    nikeName = register_userName_txt.getText().toString().trim();
                    // 手机号
                    phoneName = phone_txt.getText().toString().trim();
                    // 验证码
                    identifyCode = identify_txt.getText().toString().toLowerCase().trim();
                    // 动态码
                    verityCode = verify_txt.getText().toString().trim();
                    // 密码
                    pwd = register_PWD_txt.getText().toString().trim();
                    if (null != nikeName && nikeName.length() > 0) { // 昵称不空
                        if (null != phoneName && phoneName.length() > 0) { // 手机号不空
                            if (ToolUtils.isMobileNo(phoneName)) { // 手机号匹配
                                if (null != identifyCode && identifyCode.length() > 0) {
                                    if (identifyCode.equals(realCode)) { // 验证码输入正确
                                        if (null != verityCode && verityCode.length() > 0) { // 动态码不空
                                            if (null != pwd && pwd.length() > 0) {
                                                if (ToolUtils.isPWDAvailable(pwd)) { // 符合密码设置规则
                                                    if (NetUtil.isNetWorkConnected(mCtx)) {
                                                        getRegeistgerInfoFromHttp(nikeName, phoneName, pwd);
                                                    } else {
                                                        setErrorInfo(errorPWD_register_tapTxt, "网络连接失败，请检查网络连接");
                                                    }
                                                } else {
                                                    setErrorInfo(errorPWD_register_tapTxt, "密码格式必须为8到15位数字和字母组合");
                                                }
                                            } else {
                                                setErrorInfo(errorPWD_register_tapTxt, "密码不能为空");
                                            }
                                        } else {  // 动态码为空
                                            setErrorInfo(errorVerity_tap_txt, "动态验证码不能为空");
                                        }
                                    } else { // 验证码输入错误
                                        setErrorInfo(errorIdentify_tap_txt, "图形验证码输入错误,请输入正确验证码");
                                        identify_Tag_img.setImageBitmap(VerityCodeView.getInstance().createBitmap());
                                        realCode = VerityCodeView.getInstance().getCode().toLowerCase();
                                    }
                                } else {
                                    setErrorInfo(errorIdentify_tap_txt, "图形验证码不能为空");
                                }
                            } else {
                                setErrorInfo(errorPhone_tap_txt, "手机号格式错误,请输入正确手机号");
                            }
                        } else {
                            setErrorInfo(errorPhone_tap_txt, "手机号不能为空");
                        }
                    } else {
                        setErrorInfo(errorUserName_register_tapTxt, "昵称不能为空");
                    }
                }
                break;
            case R.id.userTip_txt: // 注意事项文字监听
                baseActivity.openActivity(NoticeActivity.class);
                break;
            case R.id.immediatelyLogin_txt: // 立即登录文字监听
                if (baseActivity.verifyClickTime()) {
                    baseActivity.finish();
                    AppManager.getInstance().removeActivity(this);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 网络请求获取短信验证码
     * @param phoneStr
     */
    private void sendPhoneMessage(String phoneStr) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        // 添加MD5加密字符串
        String mD5Result = ToolUtils.getMD5Str(phoneStr + Global.MD5_KEY);
        Observable<String> observable = httpPostService.getSendMsgInfo(phoneStr,mD5Result);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                Toast.makeText(RegisterActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(RegisterActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(String s) {
                                if("0".equals(s)){ // 服务出错
                                    setErrorInfo(errorVerity_tap_txt, "短信内部服务出错");
                                }else if("-1".equals(s)){ // 手机号已被注册
                                    setErrorInfo(errorVerity_tap_txt, "该手机号已被注册");
                                }else if("-2".equals(s)){ // 手机号格式错误
                                    setErrorInfo(errorVerity_tap_txt, "手机号格式错误");
                                }else{
                                    // 发送消息，并启动倒计时
                                    timeCount.start();
                                    clickFlag = false;
                                }
                            }
                        }
                );
    }

    /**
     * 网络请求获取注册信息
     *
     * @param nikeName  昵称
     * @param phoneName 手机号
     * @param pwd       密码
     */
    private void getRegeistgerInfoFromHttp(String nikeName, String phoneName, String pwd) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitle("");
        dialog.setTitleText("");
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        String strUrl = "{'SRealName':'" + nikeName
                + "',STelephone':'" + phoneName
                + "','SPassword':'" + pwd + "'}";
        // 添加MD5加密字符串
        String mD5Result = ToolUtils.getMD5Str(strUrl + Global.MD5_KEY);
        // 发送请求并返回请求数据
        Observable<Register> observable = httpPostService.getRegiesterInfo(strUrl, mD5Result);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Register>() {
                            @Override
                            public void onCompleted() {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismissWithAnimation();
                                    Toast.makeText(RegisterActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismissWithAnimation();
                                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(Register register) {

                                String s = register.getResultState();
                                if(null != s && !"".equals(s) && !"null".equals(s)){
                                    int status = Integer.parseInt(s);
                                    switch (status) {
                                        case 1: // 注册成功
                                            Toast.makeText(RegisterActivity.this, "注册成功,2秒后自动跳转登录页", Toast.LENGTH_SHORT).show();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    baseActivity.finish();
                                                }
                                            },2000);
                                            break;
                                        case -1: // 手机号格式错误
                                            setErrorInfo(register_PWD_txt, "手机号格式错误");
                                            break;
                                        case -2: // 姓名不能为空
                                            setErrorInfo(register_PWD_txt, "姓名不能为空,请输入真实姓名");
                                            break;
                                        case -3: // 密码格式必须为8到15位数字字符组合
                                            setErrorInfo(register_PWD_txt, "密码格式错误,密码格式必须为8到15位数字字符组合");
                                            break;
                                        case -4: // 用户已被注册
                                            setErrorInfo(register_PWD_txt, "该用户已被注册");
                                            break;
                                        case -5: // 账号在LMS已被注
                                            setErrorInfo(register_PWD_txt, "该用户已在LMS上注册");
                                            break;
                                        case -6: // 账号在BBS已被注册
                                            setErrorInfo(register_PWD_txt, "该用户已在金程bbs上注册");
                                            break;
                                        case -7: // 账号在eShop已被注册
                                            setErrorInfo(register_PWD_txt, "该用户已在金程eShop上注册");
                                            break;
                                        default:
                                            break;
                                    }
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
     * EditText 变化监听
     *
     * @param text
     */
    @OnTextChanged({R.id.register_userName_txt, R.id.phone_txt, R.id.identify_txt, R.id.verify_txt, R.id.register_PWD_txt})
    void afterTextChanged(CharSequence text) {

        if (errorUserName_register_tapTxt.getVisibility() == View.VISIBLE) {
            errorUserName_register_tapTxt.setVisibility(View.GONE);
        } else if (errorPhone_tap_txt.getVisibility() == View.VISIBLE) {
            errorPhone_tap_txt.setVisibility(View.GONE);
        } else if (errorIdentify_tap_txt.getVisibility() == View.VISIBLE) {
            errorIdentify_tap_txt.setVisibility(View.GONE);
        } else if (errorVerity_tap_txt.getVisibility() == View.VISIBLE) {
            errorVerity_tap_txt.setVisibility(View.GONE);
        } else if (errorPWD_register_tapTxt.getVisibility() == View.VISIBLE) {
            errorPWD_register_tapTxt.setVisibility(View.GONE);
        }
        phoneName = getEditTextInput();
        if ("1".equals(phoneName)) {
            if (!ToolUtils.isMobileNo(phone_txt.getText().toString().trim())) { // 手机号非法
                setErrorInfo(errorPhone_tap_txt, "非法手机号");
                phone_layout.setBackgroundResource(R.drawable.ic_error_box_input);
                hint_phoneTag_img.setBackgroundResource(R.drawable.icon_input_error);
                hint_phoneTag_img.setVisibility(View.VISIBLE);
                // 隐藏本地图形验证码
                identify_input_layout.setVisibility(View.GONE);
                identify_Tag_img.setVisibility(View.GONE);
            } else {
                phone_layout.setBackgroundResource(R.drawable.ic_login_box_input);
                hint_phoneTag_img.setBackgroundResource(R.drawable.icon_input_correct);
                hint_phoneTag_img.setVisibility(View.VISIBLE);
                // 显示本地图形验证码
                identify_input_layout.setVisibility(View.VISIBLE);
                verify_Btn.setTextColor(Color.parseColor("#ffffff"));
                identify_Tag_img.setVisibility(View.VISIBLE);
            }
        } else {
            phone_layout.setBackgroundResource(R.drawable.ic_login_box_input);
            hint_phoneTag_img.setVisibility(View.GONE);
        }

        // 图形验证码操作
        if (clickFlag) {
            identifyCode = identify_txt.getText().toString().toLowerCase().trim();
            if (identifyCode.equals(realCode)) {
                verify_Btn.setClickable(true);
                verify_Btn.setEnabled(true);
                verify_Btn.setBackgroundResource(R.drawable.login_button_bg);
            } else {
                verify_Btn.setClickable(false);
                verify_Btn.setEnabled(false);
                verify_Btn.setBackgroundResource(R.drawable.login_button_normal_bg);
            }
        }

    }

    /**
     * 密码显示隐藏
     *
     * @param buttonView
     * @param isChecked
     */
    @OnCheckedChanged({R.id.register_checkBox})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) { // 文本可见
            setShowOrHidePwd(register_PWD_txt, true);
            register_checkBox.setBackgroundResource(R.drawable.icon_show);
        } else {
            setShowOrHidePwd(register_PWD_txt, false);
            register_checkBox.setBackgroundResource(R.drawable.icon_hide);
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
     * 用于倒计时操作
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long totalTime, long jumpTime) {
            super(totalTime, jumpTime);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (null != verify_Btn) {
                verify_Btn.setClickable(false);
                verify_Btn.setEnabled(false);
                verify_Btn.setBackgroundResource(R.drawable.login_button_normal_bg);
                verify_Btn.setText("已发送" + (millisUntilFinished / 1000) + "");
            }
        }

        @Override
        public void onFinish() {
            if (null != verify_Btn) {
                clickFlag = true;
                verify_Btn.setText("获取动态码");
                verify_Btn.setTextColor(Color.parseColor("#ffffff"));
                verify_Btn.setClickable(true);
                verify_Btn.setEnabled(true);
                verify_Btn.setBackgroundResource(R.drawable.login_button_bg);
            }
        }
    }

    /**
     * 判断手机号输入
     *
     * @return
     */
    private String getEditTextInput() {
        String firstChar = phone_txt.getText().toString().trim();
        if (null != firstChar && firstChar.length() > 0) {
            char a = firstChar.charAt(0);
            StringBuffer buffer = new StringBuffer();
            return buffer.append(a).toString();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        if(null != timeCount){
            timeCount.cancel();
        }
    }
}
