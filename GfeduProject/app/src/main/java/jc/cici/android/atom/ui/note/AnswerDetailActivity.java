package jc.cici.android.atom.ui.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.AnswerDetailRecyclerlAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.Answer;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.Question;
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
 * 回答详情页Activity
 * Created by atom on 2017/6/16.
 */

public class AnswerDetailActivity extends BaseActivity {

    private BaseActivity baseActivity;
    // 问题id
    private int quesId;
    // 答案id
    private int answerId;
    // 获取类型：1.获取评论   2.获取追问追答
    private int afterType;
    // 跳转标识(我的问题OR大家的问题OR我的回答)
    private int jumpFlag;
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
    // 搜索更多布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 搜索按钮
    @BindView(R.id.noteMore_Btn)
    Button noteMore_Btn;
    // 更多按钮
    @BindView(R.id.search_Btn)
    Button search_Btn;
    // 隐藏布局
    @BindView(R.id.register_txt)
    TextView register_txt;
    // 追问(追答)文字
    @BindView(R.id.zhuiwenCount_txt)
    TextView zhuiwenCount_txt;
    // 评论布局
    @BindView(R.id.zhuiWen_layout)
    RelativeLayout zhuiWen_layout;
    // 点赞布局
    @BindView(R.id.zan_layout)
    LinearLayout zan_layout;
    // 点赞图片
    @BindView(R.id.zanBtn)
    Button zanBtn;
    // 点赞数量
    @BindView(R.id.zanResponseCount)
    TextView zanResponseCount;
    // 评论布局
    @BindView(R.id.commit_layout)
    LinearLayout commit_layout;
    // 评论数量
    @BindView(R.id.commitCount_txt)
    TextView commitCount_txt;
    // 详情列表
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    // 线性布局管理器
    private LinearLayoutManager linearLayoutManager;
    // 答案适配器对象
    private AnswerDetailRecyclerlAdapter adapter;
    private SweetAlertDialog dialog;
    // 是否追问
    private int canZhuiWen;
    // 是否追答
    private int canZhuiDa;
    private Answer answer;

    // 追问列表
    private ArrayList<ZhuiWen> afterList;
    // 最终需要传递给adapter 列表
    private ArrayList<ZhuiWen> mList = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_answerdetail;
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
        // 问题id
        quesId = getIntent().getIntExtra("quesId", 0);
        // 答案id
        answerId = getIntent().getIntExtra("answerId", 0);
        afterType = getIntent().getIntExtra("afterType", 0);
        // 跳转来源标识(我的问题OR大家的问题)
        jumpFlag = getIntent().getIntExtra("jumpFlag", 0);
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 填充内容
        if (NetUtil.isMobileConnected(this)) {
            setContent();
        } else {
            Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 网络获取数据
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
                                    // 追问
                                    canZhuiWen = zhuiWenBeanCommonBean.getBody().getCanZhuiWen();
                                    // 追答
                                    canZhuiDa = zhuiWenBeanCommonBean.getBody().getCanZhuiDa();
                                    // 答案实体
                                    answer = zhuiWenBeanCommonBean.getBody().getAnswerInfo();
                                    // 设置点赞数量
                                    commitCount_txt.setText("(" + zhuiWenBeanCommonBean.getBody().getAnswerInfo().getCommentsCount() + ")");
                                    // 设置评论数量
                                    zanResponseCount.setText("(" + zhuiWenBeanCommonBean.getBody().getAnswerInfo().getPraiseCount() + ")");
                                    if (1 == zhuiWenBeanCommonBean.getBody().getAnswerInfo().getUserPraiseStatus()) {
                                        zanBtn.setBackgroundResource(R.drawable.icon_zan_clickable);
                                    } else {
                                        zanBtn.setBackgroundResource(R.drawable.icon_zan);
                                    }

                                    if (0 == canZhuiWen && 0 == canZhuiDa) {  // 表示是第三个人进入
                                        zhuiwenCount_txt.setVisibility(View.GONE);
                                    } else if (1 == canZhuiDa && 0 == canZhuiWen) {
                                        zhuiwenCount_txt.setText("我要追答");
                                        zhuiwenCount_txt.setVisibility(View.VISIBLE);
                                    } else if (1 == canZhuiWen && 0 == canZhuiDa) {
                                        zhuiwenCount_txt.setText("我要追问");
                                        zhuiwenCount_txt.setVisibility(View.VISIBLE);
                                    }
                                    // 获取追问(追答)列表
                                    afterList = zhuiWenBeanCommonBean.getBody().getAfterList();
                                    if (null == afterList) { // 没有追问情况(只显示问题和答案)
                                        // 添加公共内容
                                        setCommonContent(zhuiWenBeanCommonBean.getBody().getQuesInfo(), zhuiWenBeanCommonBean.getBody().getAnswerInfo());
                                    } else { // 有追问追答情况
                                        // 添加公共内容
                                        setCommonContent(zhuiWenBeanCommonBean.getBody().getQuesInfo(), zhuiWenBeanCommonBean.getBody().getAnswerInfo());

                                        for (int i = 0; i < afterList.size(); i++) {
                                            ZhuiWen zhuiWen2 = new ZhuiWen();
                                            zhuiWen2.setId(afterList.get(i).getId());
                                            zhuiWen2.setContent(afterList.get(i).getContent());
                                            ArrayList<String> arr = new ArrayList<String>();
                                            arr.add(afterList.get(i).getHeadImg());
                                            zhuiWen2.setImageUrl(afterList.get(i).getImageUrl());
                                            zhuiWen2.setStudentID(afterList.get(i).getStudentID());
                                            zhuiWen2.setNickName(afterList.get(i).getNickName());
                                            zhuiWen2.setToNickName(afterList.get(i).getToNickName());
                                            zhuiWen2.setAddTime(afterList.get(i).getAddTime());
                                            mList.add(zhuiWen2);
                                        }
                                    }
                                    if (null != mList && mList.size() > 0) {
                                        System.out.println("mList >>>:" + new Gson().toJson(mList).toString());
                                        adapter = new AnswerDetailRecyclerlAdapter(baseActivity, mList);
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        Toast.makeText(baseActivity, "暂无数据", Toast.LENGTH_SHORT).show();
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
     * 添加公共参数到bean中
     *
     * @param question
     * @param answer
     */
    private void setCommonContent(Question question, Answer answer) {

        // 问题
        if (null != question.getQuesImageUrl() && question.getQuesImageUrl().size() > 0) { // 有图片情况
            if (question.getQuesImageUrl().size() == 1) {
                ZhuiWen zhuiWen = new ZhuiWen();
                zhuiWen.setId(0);
                zhuiWen.setContent(question.getQuesContent());
                zhuiWen.setImageUrl(question.getQuesImageUrl().get(0));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答第一条居左
                    zhuiWen.setStudentID(0);
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 第一条居右
                    zhuiWen.setStudentID(ToolUtils.getUserID(baseActivity));
                }
                zhuiWen.setNickName(question.getNickName());
                zhuiWen.setToNickName("");
                zhuiWen.setHeadImg(question.getHeadImg());
                zhuiWen.setAddTime(question.getQuesAddTime());
                mList.add(zhuiWen);
            } else if (question.getQuesImageUrl().size() == 2) {
                ZhuiWen zhuiWen = new ZhuiWen();
                zhuiWen.setId(0);
                zhuiWen.setContent(question.getQuesContent());
                zhuiWen.setImageUrl(question.getQuesImageUrl().get(0));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答第一条居左
                    zhuiWen.setStudentID(0);
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 第一条居右
                    zhuiWen.setStudentID(ToolUtils.getUserID(baseActivity));
                }
                zhuiWen.setNickName(question.getNickName());
                zhuiWen.setToNickName("");
                zhuiWen.setHeadImg(question.getHeadImg());
                zhuiWen.setAddTime(question.getQuesAddTime());
                mList.add(zhuiWen);

                ZhuiWen zhuiWen1 = new ZhuiWen();
                zhuiWen1.setId(0);
                zhuiWen1.setContent(question.getQuesContent());
                zhuiWen1.setImageUrl(question.getQuesImageUrl().get(1));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答第一条居左
                    zhuiWen1.setStudentID(0);
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 第一条居右
                    zhuiWen1.setStudentID(ToolUtils.getUserID(baseActivity));
                }
                zhuiWen1.setNickName(question.getNickName());
                zhuiWen1.setToNickName("");
                zhuiWen1.setHeadImg(question.getHeadImg());
                zhuiWen1.setAddTime(question.getQuesAddTime());
                mList.add(zhuiWen1);
            } else if (question.getQuesImageUrl().size() == 3) {
                ZhuiWen zhuiWen = new ZhuiWen();
                zhuiWen.setId(0);
                zhuiWen.setContent(question.getQuesContent());
                zhuiWen.setImageUrl(question.getQuesImageUrl().get(0));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答第一条居左
                    zhuiWen.setStudentID(0);
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 第一条居右
                    zhuiWen.setStudentID(ToolUtils.getUserID(baseActivity));
                }
                zhuiWen.setNickName(question.getNickName());
                zhuiWen.setToNickName("");
                zhuiWen.setHeadImg(question.getHeadImg());
                zhuiWen.setAddTime(question.getQuesAddTime());
                mList.add(zhuiWen);

                ZhuiWen zhuiWen1 = new ZhuiWen();
                zhuiWen1.setId(0);
                zhuiWen1.setContent(question.getQuesContent());
                zhuiWen1.setImageUrl(question.getQuesImageUrl().get(1));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答第一条居左
                    zhuiWen1.setStudentID(0);
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 第一条居右
                    zhuiWen1.setStudentID(ToolUtils.getUserID(baseActivity));
                }
                zhuiWen1.setNickName(question.getNickName());
                zhuiWen1.setToNickName("");
                zhuiWen1.setHeadImg(question.getHeadImg());
                zhuiWen1.setAddTime(question.getQuesAddTime());
                mList.add(zhuiWen1);

                ZhuiWen zhuiWen2 = new ZhuiWen();
                zhuiWen2.setId(0);
                zhuiWen2.setContent(question.getQuesContent());
                zhuiWen2.setImageUrl(question.getQuesImageUrl().get(2));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答第一条居左
                    zhuiWen2.setStudentID(0);
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 第一条居右
                    zhuiWen2.setStudentID(ToolUtils.getUserID(baseActivity));
                }
                zhuiWen2.setNickName(question.getNickName());
                zhuiWen2.setToNickName("");
                zhuiWen2.setHeadImg(question.getHeadImg());
                zhuiWen2.setAddTime(question.getQuesAddTime());
                mList.add(zhuiWen2);
            }
        } else { // 问题没有图片情况
            ZhuiWen zhuiWen = new ZhuiWen();
            zhuiWen.setId(0);
            zhuiWen.setContent(question.getQuesContent());
            zhuiWen.setImageUrl("");
            if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答第一条居左
                zhuiWen.setStudentID(0);
            } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 第一条居右
                zhuiWen.setStudentID(ToolUtils.getUserID(baseActivity));
            }
            zhuiWen.setNickName(question.getNickName());
            zhuiWen.setToNickName("");
            zhuiWen.setHeadImg(question.getHeadImg());
            zhuiWen.setAddTime(question.getQuesAddTime());
            mList.add(zhuiWen);
        }

        // 答案情况
        if (null != answer.getAnswerImageUrl() && answer.getAnswerImageUrl().size() > 0) { // 有图片情况
            if (answer.getAnswerImageUrl().size() == 1) {
                ZhuiWen zhuiWen1 = new ZhuiWen();
                zhuiWen1.setId(0);
                zhuiWen1.setContent(answer.getAnswerContent());
                zhuiWen1.setImageUrl(answer.getAnswerImageUrl().get(0));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答 答案居右
                    zhuiWen1.setStudentID(ToolUtils.getUserID(baseActivity));
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 答案居左
                    zhuiWen1.setStudentID(0);
                }
                zhuiWen1.setNickName("");
                zhuiWen1.setToNickName("");
                zhuiWen1.setHeadImg(answer.getHeadImg());
                zhuiWen1.setAddTime(answer.getAnswerAddTime());
                mList.add(zhuiWen1);
            } else if (answer.getAnswerImageUrl().size() == 2) {
                ZhuiWen zhuiWen1 = new ZhuiWen();
                zhuiWen1.setId(0);
                zhuiWen1.setContent(answer.getAnswerContent());
                zhuiWen1.setImageUrl(answer.getAnswerImageUrl().get(0));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答 答案居右
                    zhuiWen1.setStudentID(ToolUtils.getUserID(baseActivity));
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 答案居左
                    zhuiWen1.setStudentID(0);
                }
                zhuiWen1.setNickName("");
                zhuiWen1.setToNickName("");
                zhuiWen1.setHeadImg(answer.getHeadImg());
                zhuiWen1.setAddTime(answer.getAnswerAddTime());
                mList.add(zhuiWen1);

                ZhuiWen zhuiWen2 = new ZhuiWen();
                zhuiWen2.setId(0);
                zhuiWen2.setContent(answer.getAnswerContent());
                zhuiWen2.setImageUrl(answer.getAnswerImageUrl().get(1));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答 答案居右
                    zhuiWen2.setStudentID(ToolUtils.getUserID(baseActivity));
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 答案居左
                    zhuiWen2.setStudentID(0);
                }
                zhuiWen2.setNickName("");
                zhuiWen2.setToNickName("");
                zhuiWen2.setHeadImg(answer.getHeadImg());
                zhuiWen2.setAddTime(answer.getAnswerAddTime());
                mList.add(zhuiWen2);
            } else if (answer.getAnswerImageUrl().size() == 3) {
                ZhuiWen zhuiWen1 = new ZhuiWen();
                zhuiWen1.setId(0);
                zhuiWen1.setContent(answer.getAnswerContent());
                zhuiWen1.setImageUrl(answer.getAnswerImageUrl().get(0));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答 答案居右
                    zhuiWen1.setStudentID(ToolUtils.getUserID(baseActivity));
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 答案居左
                    zhuiWen1.setStudentID(0);
                }
                zhuiWen1.setNickName("");
                zhuiWen1.setToNickName("");
                zhuiWen1.setHeadImg(answer.getHeadImg());
                zhuiWen1.setAddTime(answer.getAnswerAddTime());
                mList.add(zhuiWen1);

                ZhuiWen zhuiWen2 = new ZhuiWen();
                zhuiWen2.setId(0);
                zhuiWen2.setContent(answer.getAnswerContent());
                zhuiWen2.setImageUrl(answer.getAnswerImageUrl().get(1));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答 答案居右
                    zhuiWen2.setStudentID(ToolUtils.getUserID(baseActivity));
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 答案居左
                    zhuiWen2.setStudentID(0);
                }
                zhuiWen2.setNickName("");
                zhuiWen2.setToNickName("");
                zhuiWen2.setHeadImg(answer.getHeadImg());
                zhuiWen2.setAddTime(answer.getAnswerAddTime());
                mList.add(zhuiWen2);

                ZhuiWen zhuiWen3 = new ZhuiWen();
                zhuiWen3.setId(0);
                zhuiWen3.setContent(answer.getAnswerContent());
                zhuiWen3.setImageUrl(answer.getAnswerImageUrl().get(2));
                if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答 答案居右
                    zhuiWen3.setStudentID(ToolUtils.getUserID(baseActivity));
                } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 答案居左
                    zhuiWen3.setStudentID(0);
                }
                zhuiWen3.setNickName("");
                zhuiWen3.setToNickName("");
                zhuiWen3.setHeadImg(answer.getHeadImg());
                zhuiWen3.setAddTime(answer.getAnswerAddTime());
                mList.add(zhuiWen3);
            }
        } else { // 没有图片情况
            ZhuiWen zhuiWen1 = new ZhuiWen();
            zhuiWen1.setId(0);
            zhuiWen1.setContent(answer.getAnswerContent());
            zhuiWen1.setImageUrl("");
            if (1 == canZhuiDa && 0 == canZhuiWen) { // 追答 答案居右
                zhuiWen1.setStudentID(ToolUtils.getUserID(baseActivity));
            } else if (1 == canZhuiWen && 0 == canZhuiDa) { //追问 答案居左
                zhuiWen1.setStudentID(0);
            }
            zhuiWen1.setNickName("");
            zhuiWen1.setToNickName("");
            zhuiWen1.setHeadImg(answer.getHeadImg());
            zhuiWen1.setAddTime(answer.getAnswerAddTime());
            mList.add(zhuiWen1);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        back_img.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        title_txt.setText("回答详情");
        if (1 == jumpFlag) {
            register_txt.setText("采纳答案");
            register_txt.setVisibility(View.VISIBLE);
        } else {
            register_txt.setVisibility(View.GONE);
        }
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @OnClick({R.id.back_layout, R.id.register_txt, R.id.zhuiwenCount_txt, R.id.zan_layout, R.id.commit_layout})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.register_txt: // 采纳答案
                if (NetUtil.isMobileConnected(this)) {
                    setBest();
                } else {
                    Toast.makeText(baseActivity, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.zhuiwenCount_txt: // 追问(追答)监听
                Bundle bundle = new Bundle();
                bundle.putInt("jumpFlag", 3);
                bundle.putInt("afterType", 2);
                bundle.putInt("answerId", answerId);
                baseActivity.openActivity(AddNoteActivity.class, bundle);
                break;
            case R.id.zan_layout: // 点赞布局监听
                if (null != answer && answer.getUserPraiseStatus() == 2) { // 未点赞
                    if (NetUtil.isMobileConnected(baseActivity)) {
                        addZan();
                    } else {

                    }
                } else {
                    Toast.makeText(baseActivity, "你已点赞", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.commit_layout: // 评论布局
                Bundle commBundle = new Bundle();
                commBundle.putInt("answerId", answerId);
                commBundle.putInt("afterType", 1);
                baseActivity.openActivity(CommentActivity.class, commBundle);
                break;
            default:
                break;
        }
    }

    private void addZan() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("appname", commParam.getAppname());
            obj.put("userId", commParam.getUserId());
            // 答案id
            obj.put("answerId", answer.getAnswerId());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean> observable = httpPostService.addUserPraiseInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean BeanCommonBean) {
                                if (100 == BeanCommonBean.getCode()) {
                                    Toast.makeText(baseActivity, "点赞成功", Toast.LENGTH_SHORT).show();
                                    zanBtn.setBackgroundResource(R.drawable.icon_zan_clickable);
                                    int count = answer.getPraiseCount() + 1;
                                    zanResponseCount.setText("(" + count + ")");
                                } else {
                                    Toast.makeText(baseActivity, BeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                            }
                        }
                );
    }

    /**
     * 最佳答案设置
     */
    private void setBest() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("appname", commParam.getAppname());
            obj.put("userId", commParam.getUserId());
            // 答案ida
            obj.put("answerId", answerId);
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean> observable = httpPostService.setClassQuesStatusInfo(body);

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean BeanCommonBean) {
                                if (100 == BeanCommonBean.getCode()) {
                                    register_txt.setText("采纳成功");
                                    register_txt.setEnabled(false);
                                    register_txt.setClickable(false);
                                    // 发送广播
                                    Intent it = new Intent();
                                    it.setAction("com.jc.setBest");
                                    it.putExtra("quesId", quesId);
                                    baseActivity.sendBroadcast(it);
                                } else {
                                    Toast.makeText(baseActivity, BeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                            }
                        }
                );
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
}
