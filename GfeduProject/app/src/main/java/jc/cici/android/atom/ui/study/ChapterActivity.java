package jc.cici.android.atom.ui.study;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiang.android.indicatordialog.IndicatorBuilder;
import com.jiang.android.indicatordialog.IndicatorDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.MoreAdapter;
import jc.cici.android.atom.adapter.base.BaseAdapter;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.LessonInfo;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.note.NoteAllActivity;
import jc.cici.android.atom.ui.note.QuestionAllActivity;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.GlideCircleTransform;
import jc.cici.android.google.zxing.activity.CaptureActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 课程对应上课阶段
 * Created by atom on 2017/5/13.
 */

public class ChapterActivity extends BaseActivity {

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
    @BindView(R.id.moreBtn)
    Button moreBtn;
    // 二维码扫描布局
    @BindView(R.id.qr_layout)
    RelativeLayout qr_layout;
    // 出勤图片
    @BindView(R.id.attendanceImg)
    ImageView attendanceImg;
    // 课程名
    @BindView(R.id.chapterCourseName_Txt)
    TextView chapterCourseName_Txt;
    // 授课时间
    @BindView(R.id.schoolTime_Txt)
    TextView schoolTime_Txt;
    // 授课地点
    @BindView(R.id.chapterAddress_Txt)
    TextView chapterAddress_Txt;
    // 授课老师图片
    @BindView(R.id.teachImg)
    ImageView teachImg;
    // 授课老师姓名
    @BindView(R.id.teachName_Txt)
    TextView teachName_Txt;
    // popWindow背景
    @BindView(R.id.popBgImg)
    ImageView popBgImg;
    // 标题名称
    private String titleName;
    // 学员id
    private int userId;
    // 班型id
    private int classId;
    // 阶段id
    private int stageId;
    // 课程id
    private int lessonId;
    private SweetAlertDialog dialog;
    private MoreAdapter adapter;
    private List<String> mLists = new ArrayList<>();
    private List<Integer> mICons = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chapter;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        // 去标题
        baseActivity.requestNoTitle();
        titleName = getIntent().getStringExtra("titleName");
        classId = getIntent().getIntExtra("classId", 0);
        lessonId = getIntent().getIntExtra("lessonId", 0);
        stageId = getIntent().getIntExtra("stageId",0);
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

    private void initData() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setTitle("");
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        userId = commParam.getUserId();
        if (0 != userId && 0 != classId && 0 != lessonId) {
            try {
                obj.put("client", commParam.getClient());
                obj.put("userId", userId);
                obj.put("classId", classId);
                obj.put("lessonId", lessonId);
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
            Observable<CommonBean<LessonInfo>> observable = httpPostService.getLessonDetailInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<CommonBean<LessonInfo>>() {
                                @Override
                                public void onCompleted() {
                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.dismissWithAnimation();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (dialog != null && dialog.isShowing()) {
                                        Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNext(CommonBean<LessonInfo> lessonInfoCommonBean) {
                                    // 课程名
                                    chapterCourseName_Txt.setText(lessonInfoCommonBean.getBody().getLessonName());
                                    // 授课时段
                                    int type = lessonInfoCommonBean.getBody().getLessonDateType();
                                    String[] arr = lessonInfoCommonBean.getBody().getLessonDate().split(" ");
                                    String str = arr[0].replaceAll("/", "-");
                                    if (type == 1) { // 上午
                                        schoolTime_Txt.setText(str
                                                + " 上午 " + lessonInfoCommonBean.getBody().getLessonStartTime()
                                                + " - " + lessonInfoCommonBean.getBody().getLessonEndTime());

                                    } else if (type == 2) { // 中午
                                        schoolTime_Txt.setText(str
                                                + " 中午 " + lessonInfoCommonBean.getBody().getLessonStartTime()
                                                + " - " + lessonInfoCommonBean.getBody().getLessonEndTime());
                                    } else { // 下午
                                        schoolTime_Txt.setText(str
                                                + " 下午 " + lessonInfoCommonBean.getBody().getLessonStartTime()
                                                + " - " + lessonInfoCommonBean.getBody().getLessonEndTime());
                                    }
                                    // 判断是否出勤
                                    int attStatus = lessonInfoCommonBean.getBody().getAttendanceStatus();
                                    if (attStatus == 0) { // 表示缺勤
                                        attendanceImg.setBackgroundResource(R.drawable.icon_queqin);
                                    } else { // 出勤
                                        attendanceImg.setBackgroundResource(R.drawable.icon_chuqin);
                                    }
                                    // 授课地址
                                    chapterAddress_Txt.setText(lessonInfoCommonBean.getBody().getLessonPlace());
                                    // 授课老师姓名
                                    teachName_Txt.setText(lessonInfoCommonBean.getBody().getTeacherName());
                                    // 授课老师图片
                                    // 设置item中Image布局
                                    Glide.with(baseActivity).load(lessonInfoCommonBean.getBody().getTeacherImg())
                                            .placeholder(R.drawable.icon_avatar) //加载中显示的图片
                                            .error(R.drawable.icon_avatar) //加载失败时显示的图片
                                            .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                                            .override(280, 186) // 设置最终显示图片大小
                                            .centerCrop() // 中心剪裁
                                            .skipMemoryCache(true) // 跳过缓存
                                            .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                                            .transform(new GlideCircleTransform(baseActivity)) // 设置圆角
                                            .into(teachImg);
                                }
                            }
                    );

        } else {
            Toast.makeText(baseActivity, "当前用户不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始视图内容
     */
    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        moreBtn.setBackgroundResource(R.drawable.icon_more);
        moreBtn.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.GONE);
        title_txt.setText(titleName);
    }

    @OnClick({R.id.back_layout, R.id.qr_layout, R.id.share_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.qr_layout: // 二维码扫描
                Intent it = new Intent(this, CaptureActivity.class);
                startActivityForResult(it, 1);
                break;
            case R.id.share_layout: // 更多布局监听
                mLists.clear();
                mICons.clear();
                mLists.add("本课笔记");
                mICons.add(R.drawable.icon_note);
                mLists.add("本科提问");
                mICons.add(R.drawable.icon_que);
                adapter = new MoreAdapter(this, mLists, mICons, 0);
                /** 获取高度 **/
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int height = dm.heightPixels;
                final IndicatorDialog dialog = new IndicatorBuilder(baseActivity)
                        .width(350)
                        .height((int) (height * 0.5))
                        .animator(R.style.popAnimation)
                        .ArrowDirection(IndicatorBuilder.TOP) // 箭头方向
                        .bgColor(Color.WHITE) // 背景色
                        .gravity(IndicatorBuilder.GRAVITY_RIGHT)
                        .radius(10)
                        .ArrowRectage(0.9f)
                        .layoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
                        .adapter(adapter).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show(share_layout);

                adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (position) {
                            case 0: // 笔记监听
                                Bundle bundle = new Bundle();
                                bundle.putInt("cspkid",lessonId);
                                bundle.putInt("classId",classId);
                                bundle.putInt("stageId",stageId);
                                baseActivity.openActivity(NoteAllActivity.class,bundle);
                                dialog.dismiss();
                                break;
                            case 1: // 问题监听
                                Bundle quesBundle = new Bundle();
                                quesBundle.putInt("ClassScheduleId",lessonId);
                                quesBundle.putInt("classId",classId);
                                quesBundle.putInt("lessonId",lessonId);
                                quesBundle.putInt("stageId",stageId);
                                baseActivity.openActivity(QuestionAllActivity.class,quesBundle);
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
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
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
            Observable<Integer> observable = httpPostService.getSignInfo(body);
            observable.observeOn(Schedulers.io())
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
