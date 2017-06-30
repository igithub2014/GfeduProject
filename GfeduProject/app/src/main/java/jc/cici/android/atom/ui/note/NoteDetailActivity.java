package jc.cici.android.atom.ui.note;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
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
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommentBean;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.NoteDetailsBean;
import jc.cici.android.atom.bean.TestPagerInfo;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.WiperSwitch;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 笔记详情页
 * Created by atom on 2017/6/12.
 */

public class NoteDetailActivity extends BaseActivity implements WiperSwitch.OnChangedListener {

    private BaseActivity baseActivity;
    // 来源标示
    private int key;
    // 笔记id
    private int noteId;
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
    // 课程名
    @BindView(R.id.course_txt)
    TextView course_txt;
    // 阶段面
    @BindView(R.id.project_txt)
    TextView project_txt;
    // 点赞数量
    @BindView(R.id.zanCount_txt)
    TextView zanCount_txt;
    // 点赞按钮
    @BindView(R.id.zanBtn)
    Button zanBtn;
    // 笔记内容
    @BindView(R.id.noteDetail_txt)
    TextView noteDetail_txt;
    // 截屏图片
    @BindView(R.id.shotScreenDetail_img)
    ImageView shotScreenDetail_img;
    // 上传时间
    @BindView(R.id.dateDetailTxt)
    TextView dateDetailTxt;
    // 记录人名称
    @BindView(R.id.record_txt)
    TextView record_txt;
    // 关联视频布局
    @BindView(R.id.relation_layout)
    RelativeLayout relation_layout;
    // 关联视频连接文字
    @BindView(R.id.relativeVideo_txt)
    TextView relativeVideo_txt;
    // 是否公开文字
    @BindView(R.id.switch_txt)
    TextView switch_txt;
    // 开关按钮
    @BindView(R.id.wiperSwitch)
    WiperSwitch wiperSwitch;
    // 判断共有私有标示
    private String typeID;
    private SweetAlertDialog dialog;
    // 笔记实体
    private NoteDetailsBean.NoteInfo info;
    // 视频实体
    private NoteDetailsBean.VideoInfo video;
    // 题库实体
    private NoteDetailsBean.TestPagerInfo testPagerInfo;
    // 存放图片url
    ArrayList<String> imgList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notedetial;
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
        // 笔记id
        noteId = getIntent().getIntExtra("noteId", 0);
        // 笔记标示
        key = getIntent().getIntExtra("key", 0);
        System.out.println("noteId >>>:" + noteId + ",key >>>:" + key);
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化view
        initView();
        // 初始化内容
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
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setTitle("");
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("appname", commParam.getAppname());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("client", commParam.getClient());
            obj.put("userId", commParam.getUserId());
            obj.put("nTBPkid", noteId);
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<NoteDetailsBean>> observable = httpPostService.getNotesDetailsInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<NoteDetailsBean>>() {
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
                            public void onNext(CommonBean<NoteDetailsBean> noteDetailsBeanCommonBean) {
                                if (100 == noteDetailsBeanCommonBean.getCode()) {
                                    // 获取笔记实体
                                    info = noteDetailsBeanCommonBean.getBody().getNotesInfo();
                                    // 获取视频实体
                                    video = noteDetailsBeanCommonBean.getBody().getVideo();
                                    // 获取试卷试题
                                    testPagerInfo = noteDetailsBeanCommonBean.getBody().getTestPaper();
                                    // 知识地图
                                    project_txt.setText(ToolUtils.strReplaceAll(info.getSubJectSName()));
                                    //  笔记内容
                                    noteDetail_txt.setText(ToolUtils.strReplaceAll(info.getNTBContent()));
                                    // 判断是否有图片
                                    String imgUrl = info.getNTBScreenShots();
                                    if (!"".equals(imgUrl) && imgUrl.length() > 0) {
                                        Glide.with(baseActivity).load(imgUrl)
                                                .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                                                .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                                                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                                                .override(280, 186) // 设置最终显示图片大小
                                                .centerCrop() // 中心剪裁
                                                .skipMemoryCache(true) // 跳过缓存
                                                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                                                .into(shotScreenDetail_img);
                                        shotScreenDetail_img.setVisibility(View.VISIBLE);
                                        imgList.add(imgUrl);
                                        // 获取上传时间
                                        dateDetailTxt.setText(info.getNTBAddTime());
                                        // 记录人名称
                                        record_txt.setText(ToolUtils.strReplaceAll(info.getS_NickName()));
                                        if (null != video) {
                                            //获取视频名称
                                            String videoName = video.getVNAME();
                                            if (!"".equals(videoName) && videoName.length() > 0) {
                                                // 添加关联文字
                                                relativeVideo_txt.setText(ToolUtils.strReplaceAll(videoName));
                                                relation_layout.setVisibility(View.VISIBLE);
                                            }
                                        } else if (null != testPagerInfo) {
                                            // 获取试卷名称
                                            String testPager = testPagerInfo.getTestPaper_Name();
                                            if (!"".equals(testPager) && testPager.length() > 0) {
                                                relativeVideo_txt.setText(ToolUtils.strReplaceAll(testPager));
                                                relation_layout.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            relation_layout.setVisibility(View.INVISIBLE);
                                        }
                                        // 判断是我的笔记还是大家的笔记
                                        switch (key) {
                                            case 1: // 我的笔记
                                                break;
                                            case 2: // 大家的笔记
                                                // 获取点赞数量
                                                zanCount_txt.setText("(" + info.getZcount() + ")");
                                                zanCount_txt.setVisibility(View.VISIBLE);
                                                zanBtn.setVisibility(View.VISIBLE);
                                                if (1 == info.getIsZan()) { // 说明已经点赞
                                                    zanBtn.setBackgroundResource(R.drawable.icon_zan_clickable);
                                                    zanBtn.setEnabled(false);
                                                    zanBtn.setClickable(false);
                                                    zanCount_txt.setEnabled(false);
                                                    zanCount_txt.setClickable(false);
                                                } else {// 未点赞
                                                    zanBtn.setBackgroundResource(R.drawable.icon_zan);
                                                    zanBtn.setEnabled(true);
                                                    zanBtn.setClickable(true);
                                                    zanCount_txt.setEnabled(true);
                                                    zanCount_txt.setClickable(true);
                                                }
                                            default:
                                                break;
                                        }
                                    }
                                } else {
                                    Toast.makeText(baseActivity, noteDetailsBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
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
     * 初始化视图控件
     */
    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        back_img.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        title_txt.setText("笔记详情");
        if (1 == key) { // 我的笔记
            register_txt.setText("删除");
            register_txt.setVisibility(View.VISIBLE);
            wiperSwitch.setVisibility(View.VISIBLE);
        } else { // 大家的笔记
            register_txt.setVisibility(View.GONE);
            wiperSwitch.setVisibility(View.GONE);
        }
        // 设置默认初始状态
        wiperSwitch.setChecked(false);
        wiperSwitch.setOnChangedListener(this);
    }

    @OnClick({R.id.back_layout, R.id.register_txt, R.id.zanBtn, R.id.shotScreenDetail_img})
    void Onclick(View view) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.error_hint_txt_color));
        dialog.setTitleText("");
        dialog.setTitle("");
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("appname", commParam.getAppname());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("client", commParam.getClient());
            obj.put("userId", commParam.getUserId());
            obj.put("nTBPkid", noteId);
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.zanBtn: // 点赞按钮
                Observable<CommonBean> praiseObser = httpPostService.getNotesPraiseInfo(body);
                praiseObser.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Subscriber<CommonBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismissWithAnimation();
                                            Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onNext(CommonBean commonBean) {
                                        if (100 == commonBean.getCode()) {
                                            zanBtn.setBackgroundResource(R.drawable.icon_zan_clickable);
                                            // 点赞数量加1
                                            int count = info.getZcount()+1;
                                            zanCount_txt.setText("(" + count + ")");
                                            Intent it = new Intent();
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("noteId",noteId);
                                            it.putExtras(bundle);
                                            it.setAction("com.jc.zan");
                                            baseActivity.sendBroadcast(it);
                                        } else {
                                            Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
                break;
            case R.id.register_txt: // 删除按钮
                Observable<CommonBean> observable = httpPostService.getDelNotesInfo(body);
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Subscriber<CommonBean>() {
                                    @Override
                                    public void onCompleted() {
//
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismissWithAnimation();
                                            Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onNext(CommonBean commonBean) {
                                        if (100 == commonBean.getCode()) {
                                            dialog.setTitleText("删除成功")
                                                    .setConfirmText("确定")
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                    Intent it = new Intent();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("noteId",noteId);
                                                    it.putExtras(bundle);
                                                    it.setAction("com.jc.delnote");
                                                    baseActivity.sendBroadcast(it);
                                                    baseActivity.finish();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.shotScreenDetail_img: // 图片点击放大
                Bundle bundle = new Bundle();
                bundle.putInt("position", 0);
                imgList.clear();
                imgList.add(info.getNTBScreenShots());
                bundle.putStringArrayList("imgList", imgList);
                baseActivity.openActivity(GalleryActivity.class, bundle);
                break;
            default:
                break;
        }
    }

    @Override
    public void onChanged(WiperSwitch wiperSwitch, boolean checkState) {
        if (checkState) {
            typeID = "1";

        } else {
            typeID = "0";
        }
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.error_hint_txt_color));
        dialog.setTitleText("");
        dialog.setTitle("");
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("appname", commParam.getAppname());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("client", commParam.getClient());
            obj.put("userId", commParam.getUserId());
            obj.put("nTBPkid", noteId);
            obj.put("nTBTempVal ", typeID);
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean> observable = httpPostService.getTempValInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean>() {
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
                            public void onNext(CommonBean commonBean) {
                                if (100 == commonBean.getCode()) {
                                    Toast.makeText(baseActivity, "设置成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
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
}
