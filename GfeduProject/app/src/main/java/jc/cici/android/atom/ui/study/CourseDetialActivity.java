package jc.cici.android.atom.ui.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.CourseRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.Course;
import jc.cici.android.atom.bean.CourseInfo;
import jc.cici.android.atom.bean.LessInfo;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.google.zxing.activity.CaptureActivity;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 体课程具详情列表页
 * Created by atom on 2017/5/11.
 */

public class CourseDetialActivity extends BaseActivity {

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
    // 右侧历史文字布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 右侧历史文字
    @BindView(R.id.register_txt)
    TextView register_txt;
    // RecycleView
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.header_title_layout)
    RelativeLayout header_item_course;
    @BindView(R.id.today_Txt)
    TextView today_Txt;
    @BindView(R.id.qr_layout)
    RelativeLayout qr_layout;

    // 标题内容
    private String titleName;
    // 班型id
    private int classId;
    // 阶段id
    private int stageId;
    // 学员id
    private int userId;
    private SweetAlertDialog dialog;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Course> dataList;
    private ArrayList<CourseInfo> info;
    private CourseRecyclerAdapter adapter;

    private int mCurrentPosition = 0;

    private int mSuspensionHeight;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coursedetial;
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
        // 获取标题
        titleName = getIntent().getStringExtra("titleName");
        // 获取班型id
        classId = getIntent().getIntExtra("classId", 0);
        // 获取阶段id
        stageId = getIntent().getIntExtra("stageId", 0);
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 初始数据
        if (NetUtil.isMobileConnected(this)) {
            initData();
        } else {
            Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }

    }

    // 网络请求获取详情数据
    private void initData() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
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
                obj.put("userId", userId);
                obj.put("classId", classId);
                obj.put("stageId", stageId);
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
            Observable<LessInfo> observable = httpPostService.getLessInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<LessInfo>() {
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
                                public void onNext(LessInfo lessInfo) {
                                    int code = lessInfo.getCode();
                                    if (code == 100) { // 请求成功
                                        dataList = (ArrayList<Course>) lessInfo.getBody();
                                        info = new ArrayList<CourseInfo>();
                                        int i = 0;
                                        for (Course course : dataList) {
                                            List<CourseInfo> lessons = course.getLessonData();
                                            for (CourseInfo couseInfo : lessons) {
                                                couseInfo.setIndex(i);
                                                couseInfo.setDate(course.getDate());
                                                info.add(couseInfo);
                                            }
                                            i++;
                                        }

                                        adapter = new CourseRecyclerAdapter(baseActivity, info);
                                        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                        animationAdapter.setDuration(1000);
                                        recyclerView.setAdapter(animationAdapter);

                                        /**
                                         * item 点击
                                         */
                                        adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                Bundle bundle = new Bundle();
                                                bundle.putString("titleName", titleName);
                                                bundle.putInt("classId", classId);
                                                bundle.putInt("stageId", stageId);
                                                bundle.putInt("lessonId", info.get(position).getLessonId());
                                                baseActivity.openActivity(ChapterActivity.class, bundle);
                                            }
                                        });

                                    } else { // 请求失败
                                        Toast.makeText(baseActivity, "网络请求失败", Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(this, "用户未登陆或班级不存在", Toast.LENGTH_SHORT).show();
        }

        /**
         * 加载更多
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
//                        loading();
                    }
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = header_item_course.getHeight();

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = linearLayoutManager.findViewByPosition(mCurrentPosition + 1);
                if (view != null) {
                    if (view.getTop() <= mSuspensionHeight) {
                        header_item_course.setY(-(mSuspensionHeight - view.getTop()));
                    } else {
                        header_item_course.setY(0);
                        if ("今日".equals(info.get(mCurrentPosition).getDate())) { // 表示未今日
                            String today = info.get(mCurrentPosition).getDate();
                            today_Txt.setText(ToolUtils.setTextSize(baseActivity, today, 0, today.length(), R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                            // 课程开始时间
                            String startTime = info.get(mCurrentPosition).getLessonStartTime();
                            // 课程结束时间
                            String endTime = info.get(mCurrentPosition).getLessonEndTime();
                            if (ToolUtils.isCurrentTimeDuring(startTime, endTime)) { // 当前时间段
                                qr_layout.setVisibility(View.VISIBLE);
                            } else { // 非当前时间段
                                qr_layout.setVisibility(View.GONE);
                            }
                        } else { // 非今日情况
                            String dateStr = info.get(mCurrentPosition).getDate();
                            String[] str = dateStr.split("日");
                            String newStr = str[0] + str[1];
                            today_Txt.setText(ToolUtils.setTextSize(baseActivity, newStr, 1, newStr.length(), R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                            qr_layout.setVisibility(View.GONE);
                        }
                    }
                }
                if (mCurrentPosition != linearLayoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    header_item_course.setY(0);
                    if ("今日".equals(info.get(mCurrentPosition).getDate())) { // 表示未今日
                        String today = info.get(mCurrentPosition).getDate();
                        today_Txt.setText(ToolUtils.setTextSize(baseActivity, today, 0, today.length(), R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                    } else { // 非今日情况
                        String dateStr = info.get(mCurrentPosition).getDate();
                        String[] str = dateStr.split("日");
                        String newStr = str[0] + str[1];
                        today_Txt.setText(ToolUtils.setTextSize(baseActivity, newStr, 1, newStr.length(), R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                    }
                }
            }
        });
    }


    /**
     * 网络请求获取加载更多数据
     */
    private void loading() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitle("");
        dialog.setTitleText("");
//            HttpPostService httpPostServie = retrofit.create(HttpPostService.class);
//            Observable<StudyBean> obserable = httpPostServie.get
        List<Course> moreList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Course course = new Course();
            info = new ArrayList<>();
            if (i == 0) {
                course.setDate("2017-05-17");
            } else {
                course.setDate("2017-05-1" + i);
            }
            for (int j = 0; j < 4; j++) {
                CourseInfo courseInfo = new CourseInfo();
                if (j % 2 == 0) {
                    courseInfo.setLessonId(17);
                    courseInfo.setLessonName("1706CFA一级无忧长线班");
                    courseInfo.setLessonType(1);
                    courseInfo.setLessonDateType(1);
                    courseInfo.setLessonStartTime("16:00");
                    courseInfo.setLessonEndTime("18:00");
                    courseInfo.setTeacherName("邓老师");
                    courseInfo.setTeacherImg("http://image.gfedu.cn/class/15012_B.jpg");
                    courseInfo.setLessonPlace("上海市杨浦区松花江路2539号1号楼1101");
                } else {
                    courseInfo.setLessonId(19);
                    courseInfo.setLessonName("1706CFA二级无忧长线班");
                    courseInfo.setLessonType(2);
                    courseInfo.setLessonDateType(2);
                    courseInfo.setLessonStartTime("14:00");
                    courseInfo.setLessonEndTime("16:00");
                    courseInfo.setTeacherName("李老师");
                    courseInfo.setTeacherImg("http://image.gfedu.cn/class/15012_B.jpg");
                    courseInfo.setLessonPlace("上海市杨浦区松花江路2539号1号楼1101");
                }
                info.add(courseInfo);
            }
            course.setLessonData(info);
            moreList.add(course);
        }
        dataList.addAll(moreList);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Load Finished!", Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setText("历史");
        title_txt.setText(titleName);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @OnClick({R.id.back_layout, R.id.register_txt, R.id.qr_layout})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
                break;
            case R.id.register_txt: // 历史按钮
                Bundle bundle = new Bundle();
                bundle.putString("titleName", titleName);
                bundle.putInt("classId", classId);
                bundle.putInt("stageId", stageId);
                baseActivity.openActivity(HistoryActivity.class, bundle);
                break;
            case R.id.qr_layout: // 二维码扫码监听
                Intent it = new Intent(this, CaptureActivity.class);
                startActivityForResult(it, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
            if (2 == resultCode) {
                String result = data.getStringExtra("result");
                System.out.println("back result >>>:" + result);
                if (null != result && !"null".equals(result)) {
                    if (NetUtil.isMobileConnected(this)) {
                        getSignInfo(result);
                    } else {
                        Toast.makeText(this, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // TODO 扫码无返回值
                }
            }
        }
    }

    /**
     * 网络请求获取签到结果
     *
     * @param result
     */
    private void getSignInfo(String result) {

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
                obj.put("userId", userId);
                obj.put("classId", classId);
                obj.put("lessonId", result);
                obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
            Observable<Integer> observable = httpPostService.getSignInfo(body);
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
                                        Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNext(Integer integer) {
                                    if (100 == integer) {
                                        new SweetAlertDialog(baseActivity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                                .setCustomImage(R.drawable.icon_have_qrcourse)
                                                .setContentText("签到成功")
                                                .setConfirmText("确定")
                                                .setTitleText("")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismissWithAnimation();
                                                    }
                                                }).show();
                                    } else {
                                        new SweetAlertDialog(baseActivity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                                .setCustomImage(R.drawable.icon_no_qrcourse)
                                                .setContentText("很抱歉你现在没有需要签到的课程")
                                                .setConfirmText("确定")
                                                .setTitleText("")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismissWithAnimation();
                                                    }
                                                }).show();
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
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitle("");
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
