package jc.cici.android.atom.ui.study;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
//import jc.cici.android.atom.adapter.CardFragmentPagerAdapter;
import jc.cici.android.atom.adapter.CardPagerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CardItem;
import jc.cici.android.atom.bean.StageInfo;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.ScoreProgressBar;
import jc.cici.android.atom.view.ShadowTransformer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 阶段详情列表页
 * Created by atom on 2017/5/10.
 */

public class StudyDetailActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // title布局
    @BindView(R.id.title_layout)
    Toolbar title_layout;
    // 返回按钮布局
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 标题
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 距离时间
    @BindView(R.id.timeDru_Txt)
    TextView timeDru_Txt;
    // 距离天数
    @BindView(R.id.day_Txt)
    TextView day_Txt;
    // 进度
    @BindView(R.id.scoreProgressBar)
    ScoreProgressBar scoreProgressBar;
    // viewPager
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    // 圆点布局
    @BindView(R.id.point_layout)
    LinearLayout point_layout;
    // 标题内容
    private String strTitle;
    // 班级id
    private int classId;
    // 进度条
    private SweetAlertDialog dialog;
    private CardPagerAdapter cardPagerAdapter;
    private ShadowTransformer mCardShadowTransformer;
    // 用户id
    private int userId;
    // 班型名称
    private String className;
    // 项目名称
    private String proName;
    // 考试日期
    private String examDateStr;
    // 考试截止日期
    private String cutDownDayStr;
    // 学习进度
    private String studyPercent;
    // 广告点数组
    private ImageView[] imgViews;
    // 广告点
    private ImageView dots_img;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_studydetail;
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
        // 班级id
        classId = getIntent().getIntExtra("classId", 0);
        // 获取标题内容
        strTitle = getIntent().getStringExtra("contentName");
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        //初始化视图
        initView();
        // 初始化内容
        initData();
    }

    /**
     * 网络获取数据
     */
    private void initData() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setTitle("");
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        userId = commParam.getUserId();
        if (0 != userId && 0 != classId) {
            try {
                obj.put("client", commParam.getClient());
                obj.put("version", commParam.getVersion());
                obj.put("deviceid", commParam.getDeviceid());
                obj.put("appname", commParam.getAppname());
                obj.put("userId", userId);
                obj.put("classId", classId);
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
//            Observable<StageInfo> observable = httpPostService.getStageInfo(body);
//            observable.subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<StageInfo>() {
//                        @Override
//                        public void onCompleted() {
//                            if (dialog != null && dialog.isShowing()) {
//                                dialog.dismissWithAnimation();
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            if (dialog != null && dialog.isShowing()) {
//                                dialog.dismissWithAnimation();
//                                Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onNext(StageInfo stageInfo) {
//                            className = stageInfo.getClassName();
//                            proName = stageInfo.getProName();
//                            examDateStr = stageInfo.getExamDate();
//                            cutDownDayStr = stageInfo.getCountdown();
//                            studyPercent = stageInfo.getStudyPercent();
//                            mData = (ArrayList<CardItem>) stageInfo.getStageList();
//                        }
//                    });
        } else {
            Toast.makeText(this, "用户未登陆或班级不存在", Toast.LENGTH_SHORT).show();
        }


        // 测试数据
        StageInfo stageInfo = new StageInfo();
        stageInfo.setClassName("1706CFA一级无忧长线班");
        stageInfo.setProName("CFA一级");
        stageInfo.setExamDate("2017-12-30");
        stageInfo.setCountdown("45");
        stageInfo.setStudyPercent("50");

        title_txt.setText(stageInfo.getClassName());
        timeDru_Txt.setText("距离" + stageInfo.getExamDate() + "\n" + stageInfo.getProName() + "考试，还有");
        day_Txt.setText(ToolUtils.setTextSize(this, stageInfo.getCountdown() + "天",1,3,R.style.style0,R.style.style1), TextView.BufferType.SPANNABLE);
        // 设置进度
        scoreProgressBar.setProgress(50);
        cardPagerAdapter = new CardPagerAdapter(this,classId);

        imgViews = new ImageView[5];
        for (int i = 0; i < 5; i++) {
            CardItem item = new CardItem();
            if (i % 2 == 0) {
                item.setStageId(17);
                item.setStageName("基础段学习");
                item.setStageType("在线");
                item.setStageStatus(0);
                item.setStageStartTime("2017-05-01");
                item.setStageEndTime("2017-06-30");
                item.setStagePeriod(40);
                item.setStageProblem(0);
                item.setStageNote(0);
                item.setStageInformation(0);
            } else {
                item.setStageId(17);
                item.setStageName("强化段学习");
                item.setStageType("面授");
                item.setStageStatus(0);
                item.setStageStartTime("2017-05-01");
                item.setStageEndTime("2017-06-30");
                item.setStagePeriod(40);
                item.setStageProblem(1);
                item.setStageNote(1);
                item.setStageInformation(1);
            }
            cardPagerAdapter.addCardItem(item);
            // 添加白点
            dots_img = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);
            lp.setMargins(10,0,10,0);
            dots_img.setLayoutParams(lp);
            imgViews[i] = dots_img;
            if (i == 0) { // 第一张默认加载
                imgViews[i]
                        .setBackgroundResource(R.drawable.icon_dian_clickable);
            } else {
                imgViews[i]
                        .setBackgroundResource(R.drawable.icon_dian_normal);
            }
            point_layout.addView(imgViews[i]);
        }

        mCardShadowTransformer = new ShadowTransformer(viewPager, cardPagerAdapter,imgViews);
        viewPager.setAdapter(cardPagerAdapter);
        viewPager.setPageTransformer(false, mCardShadowTransformer);
        mCardShadowTransformer.enableScaling(true);
        viewPager.setOffscreenPageLimit(3);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        title_txt.setText(strTitle);
    }

    @OnClick({R.id.back_layout})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
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
