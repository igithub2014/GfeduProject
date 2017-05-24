package jc.cici.android.atom.ui.study;

import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.HistoryRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.adapter.base.LoadingMore;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.HistoryInfo;
import jc.cici.android.atom.bean.HistoryLesson;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.ToolUtils;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 课程历史列表activity
 * Created by atom on 2017/5/12.
 */

public class HistoryActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // title布局
    @BindView(R.id.title_layout)
    Toolbar title_layout;
    // 返回按钮
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 标题文字
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 右侧搜索布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 注册按钮屏蔽
    @BindView(R.id.register_txt)
    TextView register_txt;
    // 右侧搜索按钮
    @BindView(R.id.search_Btn)
    Button search_Btn;
    // 完成学时
    @BindView(R.id.finishTime_Txt)
    TextView finishTime_Txt;
    // 出勤率
    @BindView(R.id.attendenceTime_Txt)
    TextView attendenceTime_Txt;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    // 标题内容
    private String titleName;
    // 进度
    private SweetAlertDialog dialog;
    private LinearLayoutManager linearLayoutManager;
    private List<HistoryLesson> mData;
    private HistoryRecyclerAdapter adapter;
    // 学员id
    private int userId;
    // 班型id
    private int classId;
    // 阶段id
    private int stageId;
    // 完成学时
    private String finishedPeriod;
    // 出勤率
    private String attendanceRate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
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
        // 获取标题内容
        titleName = getIntent().getStringExtra("titleName");
        classId = getIntent().getIntExtra("classId", 0);
        stageId = getIntent().getIntExtra("stageId", 0);
        // 添加内容
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始话视图
        initView();
        // 初始数据
        initData();
    }

    /**
     * 网络获取数据
     */
    private void initData() {

        Retrofit retorfit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitle("");
        dialog.setTitleText("");
        HttpPostService httpPostService = retorfit.create(HttpPostService.class);

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        userId = commParam.getUserId();
        if (0 != userId && 0 != classId && 0 != stageId) {
            try {
                obj.put("client", commParam.getClient());
                obj.put("version", commParam.getVersion());
                obj.put("deviceid", commParam.getDeviceid());
                obj.put("appname", commParam.getAppname());
                obj.put("userId", userId);
                obj.put("classId", classId);
                obj.put("stageId", stageId);
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
            } catch (Exception e) {
                e.printStackTrace();
            }
//            RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
//            Observable<HistoryInfo> observable = httpPostService.getHistoryLessonInfo(body);
//            observable.observeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            new Subscriber<HistoryInfo>() {
//                                @Override
//                                public void onCompleted() {
//                                    if (dialog != null && dialog.isShowing()) {
//                                        dialog.dismissWithAnimation();
//                                    }
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    if (dialog != null && dialog.isShowing()) {
//                                        dialog.dismissWithAnimation();
//                                        Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                                @Override
//                                public void onNext(HistoryInfo historyInfo) {
//                                    finishedPeriod = historyInfo.getFinishedPeriod();
//                                    attendanceRate = historyInfo.getAttendanceRate();
//                                    mData = historyInfo.getHistoryLessonList();
//                                }
//                            }
//                    );
        } else {
            Toast.makeText(baseActivity, "用户不存在", Toast.LENGTH_SHORT).show();
        }

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mData = new ArrayList<>();
        for (int j = 0; j < 15; j++) {
            HistoryLesson lesson = new HistoryLesson();
            if (j % 2 == 0) {
                lesson.setLessonId(17);
                lesson.setLessonName("1706CFA一级无忧长线班");
                lesson.setLessonDateType("2");
                lesson.setLessonDate("2017-08-15");
                lesson.setLessonStartTime("09:00");
                lesson.setLessonEndTime("11:00");
                lesson.setAttendanceStatus(1);
            } else {
                lesson.setLessonId(19);
                lesson.setLessonName("1706CFA三级无忧长线班");
                lesson.setLessonDateType("1");
                lesson.setLessonDate("2017-08-15");
                lesson.setLessonStartTime("09:00");
                lesson.setLessonEndTime("11:00");
                lesson.setAttendanceStatus(0);
            }
            mData.add(lesson);
        }
        // 添加测试数据
        HistoryInfo info = new HistoryInfo();
        info.setAttendanceRate("59%");
        info.setFinishedPeriod("25H");
        info.setHistoryLessonList(mData);


        finishTime_Txt.setText(info.getFinishedPeriod());
        attendenceTime_Txt.setText(info.getAttendanceRate());

        adapter = new HistoryRecyclerAdapter(this, mData);
        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);

        recyclerView.addOnScrollListener(new LoadingMore(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loading();
            }
        });

        /**
         * item 点击
         */
        adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("titleName", mData.get(position).getLessonName());
                baseActivity.openActivity(ChapterActivity.class, bundle);
            }
        });
    }

    /**
     * 加载更多
     */
    private void loading() {

    }

    /**
     * c初始化基础视图
     */
    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        search_Btn.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.GONE);
        title_txt.setText(titleName);
    }

    /**
     * 视图监听
     *
     * @param view
     */
    @OnClick({R.id.back_layout, R.id.search_Btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.search_Btn: // 搜索按钮
                Bundle bundle = new Bundle();
                bundle.putInt("classId",classId);
                bundle.putInt("stageId",stageId);
                baseActivity.openActivity(SearchActivity.class,bundle);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
