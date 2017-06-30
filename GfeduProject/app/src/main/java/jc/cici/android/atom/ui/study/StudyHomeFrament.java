package jc.cici.android.atom.ui.study;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.StudyRecyclerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.base.BaseFragment;
import jc.cici.android.atom.bean.ClassInfoBean;
import jc.cici.android.atom.bean.StudyBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.GlideCircleTransform;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
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

public class StudyHomeFrament extends BaseFragment {

    private Activity baseActivity;
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

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_studyhome;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this.getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null, false);
        // 添加注解
        unbinder = ButterKnife.bind(this, view);
        // 初始化视图
        initView();
        // 加载数据
        getDataFromHttpRequest();
        return view;
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
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();

        }
    }

    /**
     * 网络请求获取数据
     */
    public void getDataFromHttpRequest() {
        if (NetUtil.isMobileConnected(baseActivity)) {
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            dialog = new SweetAlertDialog(baseActivity, SweetAlertDialog.PROGRESS_TYPE);
            dialog.setTitle("");
            dialog.setTitleText("");
            HttpPostService httpPostServie = retrofit.create(HttpPostService.class);
            JSONObject obj = new JSONObject();
            CommParam param = new CommParam(baseActivity);
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
                Observable<ClassInfoBean> observable = httpPostServie.getClassInfo(body);
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Subscriber<ClassInfoBean>() {
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
                                    public void onNext(ClassInfoBean classInfoBean) {
                                        if (100 == classInfoBean.getCode()) {
                                            ClassInfoBean.ClassInfo classInfo = classInfoBean.getBody();
                                            if (null != classInfo) {
                                                studyList = (ArrayList<StudyBean>) classInfo.getClassList();
                                                if (null != studyList && studyList.size() > 0) {
                                                    linearLayoutManager = new LinearLayoutManager(baseActivity);
                                                    recycleView.setLayoutManager(linearLayoutManager);
                                                    adapter = new StudyRecyclerAdapter(baseActivity, studyList, listener);
                                                    SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                                    animationAdapter.setDuration(1000);
                                                    recycleView.setAdapter(animationAdapter);

                                                    //为RecyclerView添加HeaderView和FooterView
                                                    setHeaderView(recycleView, classInfoBean.getBody().getStudyDayCount(), classInfoBean.getBody().getStudySlogan());

                                                    // 加载更多

                                                    recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                        @Override
                                                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                                            super.onScrollStateChanged(recyclerView, newState);
                                                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                                                int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                                                                if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
//                                                                loading();
                                                                }
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(baseActivity, "暂无课程信息", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        } else {
                                            Toast.makeText(baseActivity, classInfoBean.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

//                                    @Override
//                                    public void onStart() {
//                                        super.onStart();
//                                        if (dialog != null && !dialog.isShowing()) {
//                                            dialog.show();
//                                        }
//                                    }
                                }

                        );
            } else {
                Toast.makeText(baseActivity, "用户未登陆", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(baseActivity, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 网络请求获取加载更多数据
     */
    private void loading() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        dialog = new SweetAlertDialog(baseActivity, SweetAlertDialog.PROGRESS_TYPE);
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
        Toast.makeText(baseActivity, "Load Finished!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 添加headerView 并填充文字
     *
     * @param view
     * @param studyDay
     * @param slogan
     */
    private void setHeaderView(RecyclerView view, int studyDay, String slogan) {
        View header = LayoutInflater.from(baseActivity).inflate(R.layout.header_study_view, view, false);
        adapter.setHeaderView(header);
        // 用户图像
        ImageView nike_Img = (ImageView) header.findViewById(R.id.nike_Img);
        //  已完成学时文字
        TextView finishStudy_Txt = (TextView) header.findViewById(R.id.finishStudy_Txt);
        // 状态提示文字
        TextView tips_Txt = (TextView) header.findViewById(R.id.tips_Txt);

        finishStudy_Txt.setText(studyDay + "天");
        tips_Txt.setText(slogan);
        String imgUrl = baseActivity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE).getString("S_Head", "");
        Glide.with(baseActivity).load(imgUrl)
                .placeholder(R.drawable.icon_avatar) //加载中显示的图片
                .error(R.drawable.icon_avatar) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(120, 120) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .transform(new GlideCircleTransform(baseActivity)) // 设置圆角
                .into(nike_Img);
    }

    private StudyRecyclerAdapter.OnItemClickListener listener = new StudyRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, StudyBean data) {
            int status = data.getClassStatus();
            switch (status) {
                case 1: // 正常情况
                    Intent it = new Intent(baseActivity, StudyDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("contentName", data.getClassName());
                    bundle.putInt("classId", data.getClassId());
                    it.putExtras(bundle);
                    baseActivity.startActivity(it);
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
    };
}
