package jc.cici.android.atom.ui.login;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.base.BaseFragment;
import jc.cici.android.atom.bean.BindJCInfo;
import jc.cici.android.atom.bean.DeiverInfo;
import jc.cici.android.atom.bean.MessageBean;
import jc.cici.android.atom.bean.User;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.VerityCodeView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 无金程账号的第三方登录
 * Created by atom on 2017/4/14.
 */

public class NoCardLoginFragment extends BaseFragment {

    private Unbinder unbinder;
    private Activity mCtx;
    private BaseActivity baseActivity;
    // 无金程账号手机输入框布局
    @BindView(R.id.noCard_phone_layout)
    RelativeLayout noCard_phone_layout;
    // 无金程账号无金程账号手机号输入框
    @BindView(R.id.noCard_phone_txt)
    EditText noCard_phone_txt;
    //无金程账号手机号错误提示图标
    @BindView(R.id.errorHint_noCardPhone_img)
    ImageView errorHint_noCardPhone_img;
    //无金程账号手机号错误提示
    @BindView(R.id.errorPhone_noCardHint_txt)
    TextView errorPhone_noCardHint_txt;
    // 本地验证码布局
    @BindView(R.id.noCard_identifyInput_layout)
    RelativeLayout noCard_identifyInput_layout;
    // 本地验证码输入框
    @BindView(R.id.noCard_identify_txt)
    EditText noCard_identify_txt;
    // 本地验证码图片
    @BindView(R.id.noCard_identify_img)
    ImageView noCard_identify_img;
    // 本地验证码错误提示
    @BindView(R.id.noCard_errorIdentify_txt)
    TextView noCard_errorIdentify_txt;
    //  获取动态码布局
    @BindView(R.id.noCard_verifyInput_layout)
    RelativeLayout noCard_verifyInput_layout;
    // 获取动态码输入框
    @BindView(R.id.noCard_verify_txt)
    EditText noCard_verify_txt;
    // 获取动态码按钮
    @BindView(R.id.noCard_verify_Btn)
    Button noCard_verify_Btn;
    // 获取动态码错误提示
    @BindView(R.id.noCard_errorVerity_txt)
    TextView noCard_errorVerity_txt;
    // 无金程账号密码布局
    @BindView(R.id.psd_noCard_layout)
    RelativeLayout psd_noCard_layout;
    // 无金程账号密码输入框
    @BindView(R.id.passWord_noCard_txt)
    EditText passWord_noCard_txt;
    // 无金程账号检查框
    @BindView(R.id.hint_noCardPWDTag_img)
    CheckBox hint_noCardPWDTag_img;
    // 无金程账号密码错误提示
    @BindView(R.id.noCard_errorPsd_txt)
    TextView noCard_errorPsd_txt;
    // 无金程账号按钮
    @BindView(R.id.noCard_Btn)
    Button noCard_Btn;
    private String phoneName, // 手机号字符串
            identifyCode, // 验证码字符串
            verityCode, // 动态码字符串
            passWord; // 密码字符串
    // 点击标示
    private boolean clickFlag = true;
    // 产生的验证码
    private String realCode;
    // 倒计时对象
    private TimeCount timeCount;
    // 进度对话框
    private SweetAlertDialog dialog;
    // 保存用户信息对象
    private SharedPreferences userInfoPrueferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = this.getActivity();
        baseActivity = (BaseActivity) mCtx;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        // 将验证码以图形的形式显示出来
        noCard_identify_img.setImageBitmap(VerityCodeView.getInstance().createBitmap());
        // 获取生成的验证码的值
        realCode = VerityCodeView.getInstance().getCode().toLowerCase();
        // 初始倒计时设置
        timeCount = new TimeCount(60000, 1000);
        return view;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nocard;
    }

    /**
     * 视图所有点击事件监听
     *
     * @param v
     */
    @OnClick({R.id.noCard_identify_img, R.id.noCard_verify_Btn, R.id.noCard_Btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.noCard_identify_img: // 本地验证码图片监听
                noCard_identify_img.setImageBitmap(VerityCodeView.getInstance().createBitmap());
                realCode = VerityCodeView.getInstance().getCode().toLowerCase();
                break;
            case R.id.noCard_verify_Btn: // 获取动态验证码监听
                // 发送消息，并启动倒计时
                timeCount.start();
                clickFlag = false;
                String phoneStr = noCard_phone_txt.getText().toString();
                if (ToolUtils.isMobileNo(phoneStr)) { // 判断输入的号码为手机号码
                    // 快速登录短信发送
                    sendFastLoginMessage(phoneStr);
                } else {
                    setErrorInfo(errorPhone_noCardHint_txt, "手机号为空或非法手机号，请输入正确号码");
                }
                break;
            case R.id.noCard_Btn: // 无金程账号按钮监听
                if (baseActivity.verifyClickTime()) { // 防止快速点击
                    // 手机号
                    phoneName = noCard_phone_txt.getText().toString().trim();
                    // 验证码
                    identifyCode = noCard_identify_txt.getText().toString().toLowerCase().trim();
                    // 动态码
                    verityCode = noCard_verify_txt.getText().toString().trim();
                    // 密码
                    passWord = passWord_noCard_txt.getText().toString().trim();
                    if (null != phoneName && phoneName.length() > 0) {
                        if (ToolUtils.isMobileNo(phoneName)) { // 通过手机号验证
                            if (null != identifyCode && identifyCode.length() > 0) { // 图形输入框不为空
                                if (identifyCode.equals(realCode)) {// 图形输入框内容与所给图形内容相符
                                    if (null != verityCode && verityCode.length() > 0) { // 动态码不为空
                                        if (null != passWord && passWord.length() > 0) {
                                            if (NetUtil.isNetWorkConnected(mCtx)) { // 网络连接通过
                                                getRegiestInfoFromHttp(phoneName, passWord);
                                            } else {
                                                setErrorInfo(noCard_errorPsd_txt, "网络连接失败，请检查网络连接");
                                            }
                                        } else {
                                            setErrorInfo(noCard_errorPsd_txt, "手机验证码不能为空");
                                        }
                                    } else {
                                        setErrorInfo(noCard_errorVerity_txt, "手机验证码不能为空");
                                    }
                                } else {
                                    setErrorInfo(noCard_errorIdentify_txt, "输入验证码与图形验证码不相符");
                                }
                            } else {
                                setErrorInfo(noCard_errorIdentify_txt, "图形验证码不能为空");
                            }
                        } else {
                            setErrorInfo(errorPhone_noCardHint_txt, "手机号格式错误,请输入正确的手机号");
                        }
                    } else {
                        setErrorInfo(errorPhone_noCardHint_txt, "手机号不能为空");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 网络获取第三方注册登录信息
     *
     * @param phoneName
     * @param passWord
     */
    private void getRegiestInfoFromHttp(String phoneName, String passWord) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        dialog = new SweetAlertDialog(mCtx, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitle("");
        dialog.setTitleText("");
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        String str=String.valueOf(OtherLoginActivity.type);
        if("WEIXIN".equals(str)){
            str = "WECHAT";
        }
        String strUrl = "{'UserRealName':'" + OtherLoginActivity.nikeName
                + "','UserName':'" + phoneName
                + "','UserPwd':'" + passWord
                + "','OpenID':'" + OtherLoginActivity.uid
                + "','ThirdType':'" + str + "'}";
        // 添加MD5加密字符串
        String mD5Result = ToolUtils.getMD5Str(strUrl + Global.MD5_KEY);
        // 发送请求并返回请求数据
        Observable<BindJCInfo> observable = httpPostService.getBindJCInfo(strUrl, mD5Result);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<BindJCInfo>() {
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
                                    Toast.makeText(mCtx, "登录失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(final BindJCInfo bindJCInfo) {
                                String s = bindJCInfo.getResultState();
                                if (null != s && !"".equals(s) && !"null".equals(s)) {
                                    int status = Integer.parseInt(s);
                                    switch (status) {
                                        case 1: // 绑定成功，直接登录
                                            Toast.makeText(mCtx, "登录成功", Toast.LENGTH_SHORT).show();
                                            // 绑定成功后判断是否绑定设备
                                            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
                                            // 获取上次登录保存的用户id
                                            int lastLoginSID = sharedPreferences.getInt("S_ID", 0);
                                            if (0 == lastLoginSID) { // 无绑定设备信息
                                                new SweetAlertDialog(mCtx, SweetAlertDialog.WARNING_TYPE)
                                                        .setTitleText("提示")
                                                        .setContentText("该设备还未绑定,请绑定该设备")
                                                        .setConfirmText("绑定")
                                                        .setCancelText("取消")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                // 提示绑定设备
                                                                bindDeiver(bindJCInfo.getResultData().getS_PassWord(), bindJCInfo.getResultData().getS_Name(), sweetAlertDialog);
                                                            }
                                                        })
                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismissWithAnimation();
                                                            }
                                                        }).show();

                                            } else if (lastLoginSID == bindJCInfo.getResultData().getS_ID()) { // 如果有登录信息，则对比两次登录id
                                                if (0 == bindJCInfo.getResultData().getBindStatus()) { // 未被任何设备绑定
                                                    new SweetAlertDialog(mCtx, SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText("提示")
                                                            .setContentText("该设备还未绑定,请绑定该设备")
                                                            .setConfirmText("绑定")
                                                            .setCancelText("取消")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    // 提示绑定设备
                                                                    bindDeiver(bindJCInfo.getResultData().getS_PassWord(), bindJCInfo.getResultData().getS_Name(), sweetAlertDialog);
                                                                }
                                                            })
                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismissWithAnimation();
                                                                }
                                                            }).show();
                                                } else if (1 == bindJCInfo.getResultData().getBindStatus()) { // 已被当前设备绑定
                                                    // 保存用户信息
                                                    saveUserSharePreferences(bindJCInfo.getResultData());
                                                    // TODO 登录成功跳转

                                                } else if (2 == bindJCInfo.getResultData().getBindStatus()) {
                                                    new SweetAlertDialog(mCtx, SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText("提示")
                                                            .setContentText("当前账号已被其他设备绑定,请先解绑账号!或者联系客服进行人工解绑!")
                                                            .setConfirmText("确定")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismissWithAnimation();
                                                                }
                                                            }).show();
                                                }
                                            } else {
                                                new SweetAlertDialog(mCtx, SweetAlertDialog.WARNING_TYPE)
                                                        .setTitleText("提示")
                                                        .setContentText("登陆账号和上次不一致,确定登陆将清空之前缓存!")
                                                        .setConfirmText("确定")
                                                        .setCancelText("取消")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismissWithAnimation();
                                                                // 清除上次登录信息
                                                                clearLastLoginInfo();
                                                                // 绑定设备信息
                                                                setBindDiever(bindJCInfo.getResultData().getBindStatus(), bindJCInfo.getResultData());
                                                            }
                                                        })
                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismissWithAnimation();
                                                            }
                                                        }).show();

                                            }
                                            break;
                                        case 0: // 需要绑定账号
                                            Toast.makeText(mCtx, "绑定失败", Toast.LENGTH_SHORT).show();
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

    private void setBindDiever(int bindStatus, final User user) {

        switch (bindStatus) {
            case 0: // 未被任何设备绑定
                new SweetAlertDialog(mCtx, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("该设备还未绑定,请绑定该设备")
                        .setConfirmText("绑定")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                // 提示绑定设备
                                bindDeiver(user.getS_PassWord(), user.getS_Name(), sweetAlertDialog);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        }).show();
                break;
            case 1: // 已被当前设备绑定
                Toast.makeText(mCtx, "登录成功", Toast.LENGTH_SHORT).show();
                // 保存用户信息
                saveUserSharePreferences(user);
                // TODO 登录成功跳转处理
                break;
            case 2: // 已被其他设备绑定
                new SweetAlertDialog(mCtx, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("当前账号已被其他设备绑定,请先解绑账号!或者联系客服进行人工解绑!")
                        .setConfirmText("确定")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        }).show();
                break;
            default:
                break;
        }
    }

    /**
     * 清除上次登录保存信息
     */
    private void clearLastLoginInfo() {

        // 清除保存本地用户信息
        userInfoPrueferences = mCtx.getSharedPreferences(Global.LOGIN_FLAG,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor userInfo_editor = userInfoPrueferences.edit();
        userInfo_editor.clear();
        userInfo_editor.commit();

    }


    /**
     * 保存当前登录信息并保存登录成功后用户信息
     *
     * @param user
     */
    private void saveUserSharePreferences(User user) {

        // 获取最近登录preferences
        SharedPreferences lastSharePre = mCtx.getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = lastSharePre.edit();
        editor.putInt("S_ID", user.getS_ID());
        editor.commit();
        // 保存用户信息
        saveUserInfo(user);

    }

    /**
     * 绑定设备信息
     *
     * @param s_passWord
     * @param s_name
     * @param sweetAlertDialog
     */
    private void bindDeiver(String s_passWord, String s_name, final SweetAlertDialog sweetAlertDialog) {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("oauth", ToolUtils.getMD5Str(s_name + s_passWord + "android!%@%$@#$"));
            obj.put("appname", commParam.getAppname());
            obj.put("loginname", s_name);
            obj.put("password", s_passWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<DeiverInfo> observable = httpPostService.getDeiverInfo(body);
        observable.observeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeiverInfo>() {
                    @Override
                    public void onCompleted() {
                        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                            sweetAlertDialog.dismissWithAnimation();
                            Toast.makeText(mCtx, "绑定失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNext(DeiverInfo deiverInfo) {
                        // 保存本地信息
                        SaveSharePreferences(deiverInfo);
                        // TODO 跳转处理
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        if (sweetAlertDialog != null && !sweetAlertDialog.isShowing()) {
                            sweetAlertDialog.show();
                        }
                    }
                });
    }

    /**
     * 保存绑定后用户信息
     *
     * @param deiverInfo
     */
    private void SaveSharePreferences(DeiverInfo deiverInfo) {
        // 获取最近登录preferences
        SharedPreferences lastSharePre = mCtx.getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = lastSharePre.edit();
        editor.putInt("S_ID", deiverInfo.getBody().getS_ID());
        editor.commit();

        // 存储登录信息
        saveUserInfo(deiverInfo.getBody());

        // 绑定UUID
        SharedPreferences uuidPre = mCtx.getSharedPreferences(Global.UUID_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor uuidEditor = uuidPre.edit();
        uuidEditor.putString("DeviceId", ToolUtils.getUUID(mCtx));
        uuidEditor.commit();
    }

    /**
     * 保存用户信息
     *
     * @param user
     */
    private void saveUserInfo(User user) {
        // 存储登录信息
        SharedPreferences loginPre = mCtx.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor loginEditor = loginPre.edit();
        loginEditor.putInt("S_ID", user.getS_ID());
        loginEditor.putString("S_Name", user.getS_Name());
        loginEditor.putString("S_PassWord", user.getS_PassWord());
        loginEditor.putString("S_NickName", user.getS_NickName());
        loginEditor.putString("S_RealName", user.getS_RealName());
        loginEditor.putString("S_Sex", user.getS_Sex() + "");
        loginEditor.putString("S_Email", user.getS_Email());
        loginEditor.putString("S_Telephone", user.getS_Telephone());
        loginEditor.putString("S_Address", user.getS_Address());
        loginEditor.putString("S_MailCode", user.getS_MailCode()
                + "");
        loginEditor.putString("S_RegDate", user.getS_RegDate());
        loginEditor.putString("S_LastLogin", user.getS_LastLogin());
        loginEditor.putString("S_Type", user.getS_Type() + "");
        loginEditor.putString("S_State", user.getS_State() + "");
        loginEditor.putString("S_Money", user.getS_Money() + "");
        if (!"".equals(user.getS_Head())) {
            loginEditor.putString("S_Head", user.getS_Head());
        } else if ("null".equals(user.getS_Head())) {
            loginEditor.putString("S_Head", "");
        } else {
            loginEditor.putString("S_Head", "");
        }
        loginEditor.commit();

    }

    /**
     * 快速登录短信发送请求
     *
     * @param phoneStr
     */
    private void sendFastLoginMessage(String phoneStr) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("oauth", ToolUtils.getMD5Str(phoneStr + "android!%@%$@#$"));
            obj.put("appname", commParam.getAppname());
            obj.put("mobile", phoneStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<MessageBean> observable = httpPostService.sendFastLoginMsg(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<MessageBean>() {
                            @Override
                            public void onCompleted() {
                                Toast.makeText(mCtx, "短信发送成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                Toast.makeText(mCtx, "短信发送失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(MessageBean messageBean) {
                                switch (messageBean.getCode()) {
                                    case 100: // 短信发送成功
                                        timeCount.start();
                                        clickFlag = false;
                                        break;
                                    case 102:
                                        Toast.makeText(mCtx, "此号码已达到当天3次发送限制", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
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
    @OnTextChanged({R.id.noCard_phone_txt, R.id.noCard_identify_txt, R.id.noCard_verify_txt, R.id.passWord_noCard_txt})
    void afterTextChanged(CharSequence text) {
        if (errorPhone_noCardHint_txt.getVisibility() == View.VISIBLE) { // 手机号错误提示
            errorPhone_noCardHint_txt.setVisibility(View.GONE);
        } else if (noCard_errorIdentify_txt.getVisibility() == View.VISIBLE) { // 图片验证错误提示
            noCard_errorIdentify_txt.setVisibility(View.GONE);
        } else if (noCard_errorVerity_txt.getVisibility() == View.VISIBLE) { // 动态码错误提示
            noCard_errorVerity_txt.setVisibility(View.GONE);
        } else if (noCard_errorPsd_txt.getVisibility() == View.VISIBLE) { // 密码错误提示
            noCard_errorPsd_txt.setVisibility(View.GONE);
        }
        phoneName = getEditTextInput();
        if ("1".equals(phoneName)) {
            if (!ToolUtils.isMobileNo(noCard_phone_txt.getText().toString().trim())) { // 手机号非法
                setErrorInfo(errorPhone_noCardHint_txt, "非法手机号");
                noCard_phone_layout.setBackgroundResource(R.drawable.ic_error_box_input);
                errorHint_noCardPhone_img.setBackgroundResource(R.drawable.icon_input_error);
                errorHint_noCardPhone_img.setVisibility(View.VISIBLE);
                // 隐藏本地图形验证码
                noCard_identifyInput_layout.setVisibility(View.GONE);
                noCard_identify_img.setVisibility(View.GONE);
            } else {
                noCard_phone_layout.setBackgroundResource(R.drawable.ic_login_box_input);
                errorHint_noCardPhone_img.setBackgroundResource(R.drawable.icon_input_correct);
                errorHint_noCardPhone_img.setVisibility(View.VISIBLE);
                // 显示本地图形验证码
                noCard_identifyInput_layout.setVisibility(View.VISIBLE);
                noCard_identify_img.setVisibility(View.VISIBLE);
            }
        } else {
            noCard_phone_layout.setBackgroundResource(R.drawable.ic_login_box_input);
            errorHint_noCardPhone_img.setVisibility(View.GONE);
        }

        // 图形验证码操作
        if (clickFlag) {
            identifyCode = noCard_identify_txt.getText().toString().toLowerCase().trim();
            if (identifyCode.equals(realCode)) { // 输入验证码与图形验证码相同情况
                noCard_verify_Btn.setClickable(true);
                noCard_verify_Btn.setEnabled(true);
                noCard_verify_Btn.setTextColor(Color.parseColor("#ffffff"));
                noCard_verify_Btn.setBackgroundResource(R.drawable.login_button_bg);
            } else {
                noCard_verify_Btn.setClickable(false);
                noCard_verify_Btn.setEnabled(false);
                noCard_verify_Btn.setBackgroundResource(R.drawable.login_button_normal_bg);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
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
            if (null != noCard_verify_Btn) {
                noCard_verify_Btn.setClickable(false);
                noCard_verify_Btn.setEnabled(false);
                noCard_verify_Btn.setBackgroundResource(R.drawable.login_button_normal_bg);
                noCard_verify_Btn.setText("已发送" + (millisUntilFinished / 1000) + "");
            }
        }

        @Override
        public void onFinish() {
            if (null != noCard_verify_Btn) {
                clickFlag = true;
                noCard_verify_Btn.setText("获取动态码");
                noCard_verify_Btn.setTextColor(Color.parseColor("#ffffff"));
                noCard_verify_Btn.setClickable(true);
                noCard_verify_Btn.setEnabled(true);
                noCard_verify_Btn.setBackgroundResource(R.drawable.login_button_bg);
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

    /**
     * 判断手机号输入
     *
     * @return
     */
    private String getEditTextInput() {
        String firstChar = noCard_phone_txt.getText().toString().trim();
        if (null != firstChar && firstChar.length() > 0) {
            char a = firstChar.charAt(0);
            StringBuffer buffer = new StringBuffer();
            return buffer.append(a).toString();
        }
        return null;
    }

    @OnCheckedChanged({R.id.hint_noCardPWDTag_img})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) { // 文本可见
            setShowOrHidePwd(passWord_noCard_txt, true);
            hint_noCardPWDTag_img.setBackgroundResource(R.drawable.icon_show);
        } else {
            setShowOrHidePwd(passWord_noCard_txt, false);
            hint_noCardPWDTag_img.setBackgroundResource(R.drawable.icon_hide);
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
}
