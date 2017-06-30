package jc.cici.android.atom.ui.note;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.AnswerDetailRecyclerlAdapter;
import jc.cici.android.atom.adapter.CommentRecyclerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.AfterInfo;
import jc.cici.android.atom.bean.Answer;
import jc.cici.android.atom.bean.CommentBean;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.ZhuiWen;
import jc.cici.android.atom.bean.ZhuiWenBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 评论详情页Activity
 * Created by atom on 2017/6/17.
 */

public class CommentActivity extends BaseActivity {

    private BaseActivity baseActivity;
    // 答案id
    private int answerId;
    // 类型
    private int afterType;
    private Unbinder unbinder;
    // title布局
    @BindView(R.id.title_layout)
    Toolbar title_layout;
    // 返回按钮布局
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    @BindView(R.id.back_img)
    ImageView back_img;
    // 标题
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 评论列表
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    // 评论输入框
    @BindView(R.id.inputTxt)
    EditText inputTxt;
    // 评论按钮
    @BindView(R.id.submitBtn)
    Button submitBtn;
    private int mPos;
    private SweetAlertDialog dialog;
    private String content;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mPos = (int) msg.obj;
                inputTxt.setHint("回复:" + commentList.get(mPos).getNickName());
                inputTxt.setHintTextColor(Color.parseColor("#575757"));
                View decorView = getWindow().getDecorView();
                inputTxt.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, inputTxt));
                inputTxt.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(inputTxt, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    };
    // 线性布局管理器
    private LinearLayoutManager linearLayoutManager;
    // 定义适配器对象
    private CommentRecyclerAdapter adapter;
    // 评论列表
//    private ArrayList<CommentBean> commentList;
    private ArrayList<ZhuiWen> commentList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().addActivity(this);
        baseActivity = this;
        // 去标题
        baseActivity.requestNoTitle();
        // 答案id
        answerId = getIntent().getIntExtra("answerId", 0);
        // 类型id
        afterType = getIntent().getIntExtra("afterType", 0);
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 初始数据
        if (NetUtil.isMobileConnected(this)) {
            setContent();
        } else {
            Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置内容
     */
    private void setContent() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest();
        Observable<CommonBean<ZhuiWenBean>> observable = httpPostService.getAfterQuesAnswerListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<ZhuiWenBean>>() {
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
                                    Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(CommonBean<ZhuiWenBean> zhuiWenBeanCommonBean) {
                                if (100 == zhuiWenBeanCommonBean.getCode()) {
                                    // 获取评论列表
                                    commentList = zhuiWenBeanCommonBean.getBody().getAfterList();
                                    if (null != commentList && commentList.size() > 0) {
                                        adapter = new CommentRecyclerAdapter(baseActivity, commentList, mHandler);
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        Toast.makeText(baseActivity, "暂无评论数据", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(baseActivity, zhuiWenBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                if (null != dialog && !dialog.isShowing()) {
                                    dialog.show();
                                }
                            }
                        }
                );
    }

    /**
     * 初始化视图
     */
    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        back_img.setVisibility(View.VISIBLE);
        title_txt.setText("回答评论");
        linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();
                //软键盘高度  softInputHeight
                int softInputHeight = screenHeight - (r.bottom);
                //就是屏幕高度(screenHeight)减去键盘高度(softInputHeight)、状态栏高度(statusBarH)、输入框高度(titleBarH)再加上间隔的高度就是屏幕剩下的高度(也就是偏移量)
                int height = screenHeight - softInputHeight - ToolUtils.dip2px(baseActivity, title_layout.getHeight()) - ToolUtils.dip2px(baseActivity, inputTxt.getHeight()) + ToolUtils.dip2px(baseActivity, 20);
                recyclerView.scrollBy(0, height);
            }
        };
    }

    /**
     * 公共请求参数
     *
     * @return
     */
    private RequestBody commRequest() {

        dialog = new SweetAlertDialog(baseActivity, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setTitle("");

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("appname", commParam.getAppname());
            obj.put("userId", commParam.getUserId());
            // 答案id
            obj.put("answerId", answerId);
            // 类型id
            obj.put("afterType", afterType);
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }

    @OnTextChanged({R.id.inputTxt})
    void afterTextChanged(CharSequence text) {
        content = text.toString();
    }

    @OnClick({R.id.back_layout, R.id.submitBtn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮监听
                this.finish();
                break;
            case R.id.submitBtn: // 提交按钮监听
                if (NetUtil.isMobileConnected(this)) {
                    doSubmit(content);
                } else {
                    Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 提交回复内容
     */
    private void doSubmit(String str) {
        if (!"".equals(str) && str.length() > 0) {
            // 添加网络请求
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            HttpPostService httpPostService = retrofit.create(HttpPostService.class);
            RequestBody body = commRequestBody(str);
            // 具体请求
            Observable<CommonBean<Answer>> observable = httpPostService.getAddAnswerInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<CommonBean<Answer>>() {
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
                                        Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNext(CommonBean<Answer> answerCommonBean) {
                                    if (100 == answerCommonBean.getCode()) {
                                        inputTxt.setHint("");
                                        inputTxt.setText("");
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        if (null != imm) {
                                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                                                    0);
                                        }
                                        requestAgain();
                                        // TODO 发送广播，改变列表评论数量
//                                        Intent broadIt = new Intent();
//                                        broadIt.putExtra("quesId", quesId);
//                                        broadIt.setAction("com.jc.changeQues");
//                                        baseActivity.sendBroadcast(broadIt);
//                                        Intent it = new Intent();
//                                        setResult(2, it);
//                                        baseActivity.finish();
                                    } else {
                                        Toast.makeText(baseActivity, answerCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                    );
        } else {
            Toast.makeText(baseActivity, "提交内容不能为空", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 公共请求参数
     *
     * @param content
     * @return
     */
    private RequestBody commRequestBody(String content) {

        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setTitle("");
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            obj.put("client", commParam.getClient());
            obj.put("userId", commParam.getUserId());
            // 请求类型，1.评论  2.追问追答  3.回答  4.问题
            //问题id
            obj.put("answerId", answerId);
            //回复id
            obj.put("replyId", commentList.get(mPos).getStudentID());
            obj.put("afterType", afterType);
            obj.put("Content", content);
            //时间戳
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }

    /**
     * 重新请求获取数据
     */
    private void requestAgain() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest();
        Observable<CommonBean<ZhuiWenBean>> observable = httpPostService.getAfterQuesAnswerListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<ZhuiWenBean>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean<ZhuiWenBean> zhuiWenBeanCommonBean) {
                                if (100 == zhuiWenBeanCommonBean.getCode()) {
                                    // 获取评论列表
                                    ArrayList moreList = zhuiWenBeanCommonBean.getBody().getAfterList();
                                    if (null != moreList && moreList.size() > 0) {
                                        commentList.clear();
                                        commentList.addAll(moreList);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(baseActivity, "暂无评论数据", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(baseActivity, zhuiWenBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

}
