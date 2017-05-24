package jc.cici.android.atom.ui.study;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.HistoryRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.adapter.base.LoadingMore;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.HistoryLesson;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.ToolUtils;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import retrofit2.Retrofit;

/**
 * 课程搜索
 * Created by atom on 2017/5/13.
 */

public class SearchActivity extends BaseActivity {

    // 查询view
    @BindView(R.id.editSearch_Txt)
    EditText editSearch_Txt;
    // 取消文字
    @BindView(R.id.cancel_Txt)
    TextView cancel_Txt;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    private SweetAlertDialog dialog;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<HistoryLesson> dataList;
    private HistoryRecyclerAdapter adapter;
    // 班型id
    private int userId;
    // 班型id
    private int classId;
    // 阶段id
    private int stageId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
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
        classId = getIntent().getIntExtra("classId", 0);
        stageId = getIntent().getIntExtra("stageId", 0);
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);

    }
    

    /**
     * 网络请求获取数组
     */
    private void initData() {
//        search_View.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setTitle("");
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
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

        }else{
            Toast.makeText(baseActivity, "用户不存在", Toast.LENGTH_SHORT).show();
        }

        dataList = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            HistoryLesson lesson = new HistoryLesson();
            lesson.setLessonId(19);
            lesson.setLessonName("1706CFA三级无忧长线班");
            lesson.setLessonDateType("1");
            lesson.setLessonDate("2017-08-15");
            lesson.setLessonStartTime("09:00");
            lesson.setLessonEndTime("11:00");
            lesson.setAttendanceStatus(0);
            dataList.add(lesson);
        }

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HistoryRecyclerAdapter(this, dataList);
        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);
        /**
         * 加载更多
         */
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
                bundle.putString("titleName",dataList.get(position).getLessonName());
                bundle.putInt("classId",classId);
                bundle.putInt("lessonId",dataList.get(position).getLessonId());
                baseActivity.openActivity(ChapterActivity.class,bundle);

            }
        });
    }

    /**
     * 加载更多
     */
    private void loading() {

    }

    @OnClick({R.id.cancel_Txt})
    void onClick(View view){
        switch (view.getId()){

            case R.id.cancel_Txt: // 取消按钮
                baseActivity.finish();
                break;
            default:
                break;
        }
    }

    @OnTextChanged(R.id.editSearch_Txt)
    void afterTextChanged(CharSequence text) {
        // 初始数据
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }
}
