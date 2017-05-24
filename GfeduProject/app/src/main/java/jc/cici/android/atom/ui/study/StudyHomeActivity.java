package jc.cici.android.atom.ui.study;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.StudyRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.adapter.base.LoadingMore;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.ClassInfo;
import jc.cici.android.atom.bean.StudyBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.login.NormalActivity;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.GlideCircleTransform;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.attr.password;


/**
 * 学习首页
 * Created by atom on 2017/5/5.
 */

public class StudyHomeActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // Toolbar
    @BindView(R.id.title_studyHome_layout)
    Toolbar title_studyHome_layout;
    // 标题内容栏
    @BindView(R.id.title_content_layout)
    RelativeLayout title_content_layout;
    // 标题栏左上角布局
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 日历按钮
    @BindView(R.id.calender_Btn)
    ImageView calender_Btn;
    // 返回按钮
    @BindView(R.id.back_img)
    ImageView back_img;
    // listView
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    // 面授列表数据
    private ArrayList<StudyBean> studyList;
    private SweetAlertDialog dialog;
    private StudyRecyclerAdapter adapter;
    // list 位置
    private int mPosition;
    private LinearLayoutManager linearLayoutManager;
    // 用户id
    private int userId;
    // 学习天数
    private int studyDay;
    // 小贴士
    private String tips;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_studyhome;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        AppManager.getInstance().addActivity(this);
        // 去标题
        baseActivity.requestNoTitle();
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 加载数据
        getDataFromHttpRequest();
    }

    /**
     * 初始化视图布局
     */
    private void initView() {
        title_studyHome_layout.setBackgroundColor(Color.parseColor("#dd5555"));
        title_content_layout.setBackgroundColor(Color.parseColor("#dd5555"));
        back_layout.setVisibility(View.VISIBLE);
        calender_Btn.setVisibility(View.VISIBLE);
        back_img.setVisibility(View.GONE);
    }

    /**
     * 视图按钮监听
     */
    @OnClick({R.id.calender_Btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.calender_Btn: // 日历按钮
                break;
            default:
                break;
        }
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
     * 网络请求获取数据
     */
    public void getDataFromHttpRequest() {
        if (NetUtil.isMobileConnected(this)) {
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            dialog.setTitle("");
            dialog.setTitleText("");
            HttpPostService httpPostServie = retrofit.create(HttpPostService.class);
            JSONObject obj = new JSONObject();
            CommParam param = new CommParam(this);
            userId = param.getUserId();
            if (0 != userId) {
                try {
                    obj.put("client", param.getClient());
                    obj.put("version", param.getVersion());
                    obj.put("deviceid", param.getDeviceid());
                    obj.put("appname", param.getAppname());
                    obj.put("userId", userId);
                    obj.put("timeStamp", param.getTimeStamp());
                    obj.put("oauth", ToolUtils.getMD5Str(userId + param.getTimeStamp() + "android!%@%$@#$"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
//                Observable<ClassInfo> observable = httpPostServie.getClassInfo(body);
//                observable.subscribeOn(Schedulers.io())
//                        .unsubscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                new Subscriber<ClassInfo>() {
//                                    @Override
//                                    public void onCompleted() {
//                                        if (dialog != null && dialog.isShowing()) {
//                                            dialog.dismissWithAnimation();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        if (dialog != null && dialog.isShowing()) {
//                                            dialog.dismissWithAnimation();
//                                            Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onNext(ClassInfo classInfo) {
//                                        if(null != classInfo){
//                                            studyList = (ArrayList<StudyBean>) classInfo.getClassList();
//                                            linearLayoutManager = new LinearLayoutManager(baseActivity);
//                                            recycleView.setLayoutManager(linearLayoutManager);
//                                            adapter = new StudyRecyclerAdapter(baseActivity, studyList);
//                                            SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
//                                            animationAdapter.setDuration(1000);
//                                            recycleView.setAdapter(animationAdapter);
//
//                                            //为RecyclerView添加HeaderView和FooterView
//                                            setHeaderView(recycleView);
//                                        }
//                                    }
//                                }
//                        );
            } else {
                Toast.makeText(this, "用户未登陆", Toast.LENGTH_SHORT).show();
            }


            linearLayoutManager = new LinearLayoutManager(this);
            recycleView.setLayoutManager(linearLayoutManager);
            // 添加测试数据
            ClassInfo classInfo = new ClassInfo();
            classInfo.setStudyDayCount(20);
            classInfo.setStudySlogan("一寸光阴一寸金，寸金难买寸光阴");

            studyDay = classInfo.getStudyDayCount();
            tips = classInfo.getStudySlogan();

            studyList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                StudyBean bean = new StudyBean();
                bean.setClassId(17);
                bean.setClassName("CFA1级长线乐无忧");
                bean.setClassImg("http://image.gfedu.cn/class/14714_B.jpg");
                bean.setClassStartTime("2017.7.31");
                bean.setClassEndTime("2017.12.1");
                if (i % 2 == 0) {
                    bean.setClassStatus(1);
                    bean.setClassType("面授");
                } else if (i % 5 == 0) {
                    bean.setClassStatus(-1);
                    bean.setClassType("在线");
                } else {
                    bean.setClassStatus(0);
                    bean.setClassType("直播");
                }

                bean.setClassIsGift(0);
                studyList.add(bean);
            }
            adapter = new StudyRecyclerAdapter(this, studyList);
            SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
            animationAdapter.setDuration(1000);
            recycleView.setAdapter(animationAdapter);

            //为RecyclerView添加HeaderView和FooterView
            setHeaderView(recycleView);
//            setFooterView(recycleView);
            // 设置item监听
            adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int status = studyList.get(position).getClassStatus();
                    switch (status) {
                        case 1: // 正常情况
                            Bundle bundle = new Bundle();
                            bundle.putString("contentName", studyList.get(position).getClassName());
                            bundle.putInt("classId", studyList.get(position).getClassId());
                            baseActivity.openActivity(StudyDetailActivity.class, bundle);
                            break;
                        case -1: // 欠费锁定
                            Toast.makeText(baseActivity, "您已欠费，请联系客服", Toast.LENGTH_SHORT).show();
                            break;
                        case 0: // 未开始情况
                            Toast.makeText(baseActivity, "该课程暂未开始", Toast.LENGTH_SHORT).show();
                            break;
                        case 2: // 过期情况
                            Toast.makeText(baseActivity, "改课程已过期", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            });
            // 加载更多
            recycleView.addOnScrollListener(new LoadingMore(linearLayoutManager) {
                @Override
                public void onLoadMore(int currentPage) {
                    // 加载过程
                    loading();
                }
            });
        } else {
            Toast.makeText(this, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
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
        ArrayList<StudyBean> moreList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            StudyBean bean = new StudyBean();
            bean.setClassId(17);
            bean.setClassName("CFA1级长线乐无忧加载更多");
            bean.setClassImg("http://image.gfedu.cn/class/14714_B.jpg");
            bean.setClassStartTime("2017.7.31");
            bean.setClassEndTime("2017.12.1");
            bean.setClassStatus(1);
            bean.setClassIsGift(0);
            bean.setClassType("面授");
            moreList.add(bean);
        }
        studyList.addAll(moreList);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Load Finished!", Toast.LENGTH_SHORT).show();
    }

    private void setHeaderView(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.header_study_view, view, false);
        adapter.setmHeaderView(header);
        // 用户图像
        ImageView nike_Img = (ImageView) header.findViewById(R.id.nike_Img);
        //  已完成学时文字
        TextView finishStudy_Txt = (TextView) header.findViewById(R.id.finishStudy_Txt);
        // 状态提示文字
        TextView tips_Txt = (TextView) header.findViewById(R.id.tips_Txt);

        finishStudy_Txt.setText(studyDay + "天");
        tips_Txt.setText(tips);
        String imgUrl = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE).getString("S_Head", "");
        Glide.with(baseActivity).load(imgUrl)
                .placeholder(R.drawable.avatar_new) //加载中显示的图片
                .error(R.drawable.loadingfail_icon) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(120, 120) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .transform(new GlideCircleTransform(baseActivity)) // 设置圆角
                .into(nike_Img);
    }

//    private void setFooterView(RecyclerView view){
//        View footer = LayoutInflater.from(this).inflate(R.layout.footer, view, false);
//        adapter.setmFooterView(footer);
//    }
}
