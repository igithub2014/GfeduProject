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
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 完善信息登录
 * Created by atom on 2017/4/20.
 */

public class ImproveActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    private Context mCtx;
    // 标题
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 返回按钮
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 标题文字
    @BindView(R.id.register_txt)
    TextView register_txt;
    // 用户名输入布局
    @BindView(R.id.improveLogin_layout)
    RelativeLayout improveLogin_layout;
    // 用户名输入框
    @BindView(R.id.improveUserName_txt)
    EditText improveUserName_txt;
    // 用户名错误图标
    @BindView(R.id.hint_tag_img)
    ImageView hint_tag_img;
    // 用户名错误提示
    @BindView(R.id.improve_errorUser_txt)
    TextView improve_errorUser_txt;
    // 密码输入布局
    @BindView(R.id.improveReset_layout)
    RelativeLayout improveReset_layout;
    // 密码输入框
    @BindView(R.id.improve_reset_txt)
    EditText improve_reset_txt;
    // 密码格式错误提示图标
    @BindView(R.id.improveResetTag_img)
    CheckBox improveResetTag_img;
    // 密码格式错误提示
    @BindView(R.id.improveErrorReset_txt)
    TextView improveErrorReset_txt;
    // 确认密码布局
    @BindView(R.id.improveResetPWD_layout)
    RelativeLayout improveResetPWD_layout;
    // 确认密码输入框
    @BindView(R.id.improveRestPassWord_txt)
    EditText improveRestPassWord_txt;
    // 确认密码格式错误图标
    @BindView(R.id.improve_resetPWDTag_img)
    CheckBox improve_resetPWDTag_img;
    // 确认密码错误提示
    @BindView(R.id.improve_errorPsd_txt)
    TextView improve_errorPsd_txt;
    // 完成按钮
    @BindView(R.id.improve_Btn)
    Button improve_Btn;
    private String nikeName, // 昵称字符串
            pwd, // 密码字符串
            reSetPwd; // 重复密码
    // 手机号
    private String phoneName;
    // 进度对话框
    private SweetAlertDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_improve;
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
        mCtx = this;
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 添加文字
        initView();
    }

    /**
     * 视图初始显示
     */
    private void initView() {
        phoneName = getIntent().getStringExtra("phoneName");
        title_txt.setText("完善信息");
        back_layout.setVisibility(View.VISIBLE);

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
     * 控件监听事件
     *
     * @param v
     */
    @OnClick({R.id.back_layout, R.id.improve_Btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮监听
                baseActivity.finish();
                break;
            case R.id.improve_Btn: // 完成按钮监听
                // 获取昵称
                nikeName = improveUserName_txt.getText().toString().trim();
                // 获取密码
                pwd = improve_reset_txt.getText().toString().trim();
                // 获取重置密码
                reSetPwd = improveRestPassWord_txt.getText().toString().trim();
                if (null != nikeName && !"null".equals(nikeName) && nikeName.length() > 0) { // 昵称不空
                    if (null != pwd && !"null".equals(pwd) && pwd.length() > 0) {// 密码不空
                        if (ToolUtils.isPWDAvailable(pwd)) { // 符合密码输入规则
                            if (null != reSetPwd && !"null".equals(reSetPwd) && reSetPwd.length() > 0) {
                                if (pwd.equals(reSetPwd)) { // 两次密码输入一致
                                    if (NetUtil.isNetWorkConnected(mCtx)) { // 网络连接正常
                                        // 网络获取登录信息
                                        getImproveInfoFromHttp(nikeName, phoneName, reSetPwd);
                                    } else {
                                        setErrorInfo(improve_errorPsd_txt, "网络连接失败");
                                    }
                                } else {
                                    setErrorInfo(improve_errorPsd_txt, "两次输入密码不一致");
                                }
                            } else {
                                setErrorInfo(improveErrorReset_txt, "确认密码不能为空");
                            }
                        } else {
                            setErrorInfo(improveErrorReset_txt, "密码格式错误，请输入正确密码格式");
                        }
                    } else {
                        setErrorInfo(improve_reset_txt, "密码不能为空");
                    }
                } else {
                    setErrorInfo(improve_errorUser_txt, "昵称不能为空");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 网络获取注册并返回用户信息
     *
     * @param nikeName
     * @param phoneNem
     * @param reSetPwd
     */
    private void getImproveInfoFromHttp(String nikeName, String phoneNem, String reSetPwd) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitle("");
        dialog.setTitleText("");
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        String strUrl = "{'SRealName':'" + nikeName
                + "','STelephone':'" + phoneName
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
                                    dialog.dismiss();
                                    Toast.makeText(mCtx, "请求成功", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                    Toast.makeText(mCtx, "注册失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(Register register) {

                                String s = register.getResultState();
                                if (null != s && !"".equals(s) && !"null".equals(s)) {
                                    int status = Integer.parseInt(s);
                                    switch (status) {
                                        case 1: // 注册成功
                                            // TODO 用户信息处理
                                            break;
                                        case -1: // 手机号格式错误
                                            setErrorInfo(improve_errorPsd_txt, "手机号格式错误");
                                            break;
                                        case -2: // 姓名不能为空
                                            setErrorInfo(improve_errorPsd_txt, "姓名不能为空,请输入真实姓名");
                                            break;
                                        case -3: // 密码格式必须为8到15位数字字符组合
                                            setErrorInfo(improve_errorPsd_txt, "密码格式错误,密码格式必须为8到15位数字字符组合");
                                            break;
                                        case -4: // 用户已被注册
                                            setErrorInfo(improve_errorPsd_txt, "该用户已被注册");
                                            break;
                                        case -5: // 账号在LMS已被注
                                            setErrorInfo(improve_errorPsd_txt, "该用户已在LMS上注册");
                                            break;
                                        case -6: // 账号在BBS已被注册
                                            setErrorInfo(improve_errorPsd_txt, "该用户已在金程bbs上注册");
                                            break;
                                        case -7: // 账号在eShop已被注册
                                            setErrorInfo(improve_errorPsd_txt, "该用户已在金程eShop上注册");
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
     * 输入框变化监听
     *
     * @param text
     */
    @OnTextChanged({R.id.improveUserName_txt, R.id.improve_reset_txt, R.id.improveRestPassWord_txt})
    void afterTextChanged(CharSequence text) {

        if (improve_errorUser_txt.getVisibility() == View.VISIBLE) {
            improve_errorUser_txt.setVisibility(View.GONE);
        } else if (improveErrorReset_txt.getVisibility() == View.VISIBLE) {
            improveErrorReset_txt.setVisibility(View.GONE);
        } else if (improve_errorPsd_txt.getVisibility() == View.VISIBLE) {
            improve_errorPsd_txt.setVisibility(View.GONE);
        }
    }

    /**
     * 密码显示隐藏
     *
     * @param buttonView
     * @param isChecked
     */
    @OnCheckedChanged({R.id.improveResetTag_img, R.id.improve_resetPWDTag_img})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.improveResetTag_img: // 密码
                if (isChecked) { // 文本可见
                    setShowOrHidePwd(improve_reset_txt, true);
                    improveResetTag_img.setBackgroundResource(R.drawable.icon_show);
                } else {
                    setShowOrHidePwd(improve_reset_txt, false);
                    improveResetTag_img.setBackgroundResource(R.drawable.icon_hide);
                }
                break;
            case R.id.improve_resetPWDTag_img:
                if (isChecked) { // 文本可见
                    setShowOrHidePwd(improveRestPassWord_txt, true);
                    improve_resetPWDTag_img.setBackgroundResource(R.drawable.icon_show);
                } else {
                    setShowOrHidePwd(improveRestPassWord_txt, false);
                    improve_resetPWDTag_img.setBackgroundResource(R.drawable.icon_hide);
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
}
