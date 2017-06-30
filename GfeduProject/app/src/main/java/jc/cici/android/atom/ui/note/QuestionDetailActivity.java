package jc.cici.android.atom.ui.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import jc.cici.android.atom.adapter.AnswerRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;

import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.Answer;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.QuesDetailBean;
import jc.cici.android.atom.bean.Question;
import jc.cici.android.atom.bean.QuestionBean;
import jc.cici.android.atom.bean.QuestionContentBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.FullyLinearLayoutManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 问题详情页activity
 * Created by atom on 2017/6/15.
 */

public class QuestionDetailActivity extends BaseActivity {

    private BaseActivity baseActivity;
    // 问题id
    private int quesId;
    // 当前页数
    private int page;
    // 子班型类型
    private int childClassTypeId;
    // 跳转标识(我的问题OR大家的问题)
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
    // scrollView
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    // 课程文字
    @BindView(R.id.courseQues_txt)
    TextView courseQues_txt;
    // 阶段文字
    @BindView(R.id.projectQues_txt)
    TextView projectQues_txt;
    // 问题内容
    @BindView(R.id.quesDetail_txt)
    TextView quesDetail_txt;
    // 截屏图片1
    @BindView(R.id.shotScreen1_img)
    ImageView shotScreen1_img;
    // 截屏图片2
    @BindView(R.id.shotScreen2_img)
    ImageView shotScreen2_img;
    // 截屏图片3
    @BindView(R.id.shotScreen3_img)
    ImageView shotScreen3_img;
    // 上传时间
    @BindView(R.id.quesDateTxt)
    TextView quesDateTxt;
    // 记录人
    @BindView(R.id.recordName_txt)
    TextView recordName_txt;
    // 关联视频布局
    @BindView(R.id.relation_layout)
    RelativeLayout relation_layout;
    // 关联视频文字
    @BindView(R.id.relVideoQues_txt)
    TextView relVideoQues_txt;
    // 回答列表布局
    @BindView(R.id.answerList_layout)
    RelativeLayout answerList_layout;
    // 回答列表
    @BindView(R.id.answerRecycler)
    RecyclerView answerRecycler;
    // 没有回答内容显示文字
    @BindView(R.id.noAnswer_Txt)
    TextView noAnswer_Txt;
    // 详情内容
    private QuesDetailBean quesContentBean;
    private FullyLinearLayoutManager linearLayoutManager;
    // 图片列表
    private ArrayList<String> imgList;
    // 答案列表
    private ArrayList<Answer> answerList;
    // 答案列表适配器
    private AnswerRecyclerAdapter adapter;
    private SweetAlertDialog dialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_questiondetail;
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
        // 当前页
        page = getIntent().getIntExtra("page", 0);
        childClassTypeId = getIntent().getIntExtra("childClassTypeId", 0);
        // 跳转来源标识(我的问题OR大家的问题)
        jumpFlag = getIntent().getIntExtra("jumpFlag", 0);
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

    private void setContent() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest(page);
        Observable<CommonBean<QuesDetailBean>> observable = httpPostService.getQuesDetailInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<QuesDetailBean>>() {
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
                            public void onNext(CommonBean<QuesDetailBean> quesDetailBeanCommonBean) {
                                if (100 == quesDetailBeanCommonBean.getCode()) {
                                    quesContentBean = quesDetailBeanCommonBean.getBody();
                                    if (null != quesContentBean) {
                                        // 科目名
                                        projectQues_txt.setText(ToolUtils.strReplaceAll(quesContentBean.getQuesSubjectName()));
                                        // 问题内容
                                        quesDetail_txt.setText(quesContentBean.getQuesContent());
                                        // 获取图片列表
                                        imgList = quesContentBean.getQuesImageUrl();
                                        if (null != imgList && imgList.size() > 0) {
                                            int size = imgList.size();
                                            switch (size) {
                                                case 1: // 只有一张图片情况
                                                    // 加载显示第一张图片
                                                    loadImg(shotScreen1_img, imgList.get(0));
                                                    setImageVisible(shotScreen1_img, true);
                                                    setImageVisible(shotScreen2_img, false);
                                                    setImageVisible(shotScreen3_img, false);
                                                    break;
                                                case 2: // 两张图片情况
                                                    // 加载显示第一张图片
                                                    loadImg(shotScreen1_img, imgList.get(0));
                                                    // 加载显示第二张图片
                                                    loadImg(shotScreen2_img, imgList.get(1));
                                                    setImageVisible(shotScreen1_img, true);
                                                    setImageVisible(shotScreen2_img, true);
                                                    setImageVisible(shotScreen3_img, false);
                                                    break;
                                                case 3: // 三张图片情况
                                                    // 加载显示第一张图片
                                                    loadImg(shotScreen1_img, imgList.get(0));
                                                    // 加载显示第二张图片
                                                    loadImg(shotScreen2_img, imgList.get(1));
                                                    // 加载显示第三张图片
                                                    loadImg(shotScreen3_img, imgList.get(2));
                                                    setImageVisible(shotScreen1_img, true);
                                                    setImageVisible(shotScreen2_img, true);
                                                    setImageVisible(shotScreen3_img, true);
                                                    break;
                                                default:
                                                    break;
                                            }
                                        } else {
                                            setImageVisible(shotScreen1_img, false);
                                            setImageVisible(shotScreen2_img, false);
                                            setImageVisible(shotScreen3_img, false);
                                        }
                                        // 上传时间
                                        quesDateTxt.setText(quesContentBean.getQuesAddTime());
                                        // 记录人
                                        recordName_txt.setText("提问人：" + quesContentBean.getNickName());
                                        // 获取关联视频
                                        String videoUlr = quesContentBean.getVideoName();
                                        if (!"null".equals(videoUlr) && !"".equals(videoUlr)) {
                                            relation_layout.setVisibility(View.VISIBLE);
                                            relVideoQues_txt.setText(videoUlr);
                                        } else {
                                            relation_layout.setVisibility(View.GONE);
                                        }
                                        // 获取回答列表
                                        answerList = quesContentBean.getAnswerList();
                                        if (null != answerList && answerList.size() > 0) {
                                            noAnswer_Txt.setVisibility(View.GONE);
                                            adapter = new AnswerRecyclerAdapter(baseActivity, answerList, jumpFlag, quesId);
                                            answerRecycler.setAdapter(adapter);

                                            /**
                                             * item 点击监听
                                             */
                                            adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View view, int position) {
                                                    Bundle bundle = new Bundle();
                                                    // 问题id
                                                    bundle.putInt("quesId", quesId);
                                                    // 答案id
                                                    bundle.putInt("answerId", answerList.get(position).getAnswerId());
                                                    // 类型
                                                    bundle.putInt("afterType", 2);
                                                    // 跳转来源
                                                    bundle.putInt("jumpFlag", jumpFlag);
                                                    baseActivity.openActivity(AnswerDetailActivity.class, bundle);
                                                }
                                            });
                                        } else {
                                            noAnswer_Txt.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        Toast.makeText(baseActivity, "暂无详情数据", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(baseActivity, quesDetailBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
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

        /**
         *  加载更多
         */
        answerRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
                        Loading();
                    }
                }
            }
        });


    }

    /**
     * 加载更多
     */
    private void Loading() {
        page++;
        // 添加数据
        if (NetUtil.isMobileConnected(baseActivity)) {
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            HttpPostService httpPostService = retrofit.create(HttpPostService.class);
            RequestBody body = commRequest(page);
            Observable<CommonBean<QuesDetailBean>> observable = httpPostService.getQuesDetailInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CommonBean<QuesDetailBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(CommonBean<QuesDetailBean> quesDetailBeanCommonBean) {
                            if (100 == quesDetailBeanCommonBean.getCode()) {
                                ArrayList<Answer> moreList = new ArrayList<>();
                                moreList = quesDetailBeanCommonBean.getBody().getAnswerList();
                                if (null != moreList && moreList.size() > 0) {
                                    answerList.addAll(moreList);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(baseActivity, "亲爱的小主,已经没有更多数据了哦", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(baseActivity, quesDetailBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        back_img.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText("问题详情");
        if (2 == jumpFlag) { // 大家问题跳转
            register_txt.setText("我来回答");
        } else { // 我的问题跳转
            register_txt.setText("删除");
            register_txt.setVisibility(View.GONE);
        }
        linearLayoutManager = new FullyLinearLayoutManager(this);
        linearLayoutManager.setScrollEnabled(false);
        answerRecycler.setLayoutManager(linearLayoutManager);
    }

    @OnClick({R.id.back_layout, R.id.register_txt, R.id.relVideoQues_txt, R.id.shotScreen1_img, R.id.shotScreen2_img, R.id.shotScreen3_img})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.register_txt: // 删除or我要作答
                if (2 == jumpFlag) { // 我要作答
                    Intent it = new Intent(baseActivity, AddNoteActivity.class);
                    Bundle bundle = new Bundle();
                    // 跳转标示，标示从答案详情跳转
                    bundle.putInt("jumpFlag", 2);
                    bundle.putInt("quesId", quesId);
                    bundle.putInt("afterType", 3);
                    bundle.putInt("childClassTypeId", childClassTypeId);
                    it.putExtras(bundle);
                    baseActivity.startActivityForResult(it, 1);
                } else { // 我的答疑中删除
                    if (NetUtil.isMobileConnected(this)) {
                        delQues();
                    } else {
                        Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.relVideoQues_txt: // 相关联视频
                // TODO 关联视频跳转
                break;
            case R.id.shotScreen1_img: // 第一张图片点击监听
                Bundle bundle = new Bundle();
                bundle.putInt("position", 0);
                bundle.putStringArrayList("imgList", imgList);
                baseActivity.openActivity(GalleryActivity.class, bundle);
                break;
            case R.id.shotScreen2_img: // 第二张图片点击监听
                Bundle bundle2 = new Bundle();
                bundle2.putInt("position", 1);
                bundle2.putStringArrayList("imgList", imgList);
                baseActivity.openActivity(GalleryActivity.class, bundle2);
                break;
            case R.id.shotScreen3_img: // 第三张图片点击监听
                Bundle bundle3 = new Bundle();
                bundle3.putInt("position", 2);
                bundle3.putStringArrayList("imgList", imgList);
                baseActivity.openActivity(GalleryActivity.class, bundle3);
                break;
            default:
                break;
        }
    }

    /**
     * 删除问题
     */
    private void delQues() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest(page);
        // TODO 删除操作
//        Observable<> observable = httpPostService.
//        observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        new Subscriber<CommonBean<QuesDetailBean>>() {
//                            @Override
//                            public void onCompleted() {
//                                if (dialog != null && dialog.isShowing()) {
//                                    dialog.dismissWithAnimation();
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                if (dialog != null && dialog.isShowing()) {
//                                    dialog.dismissWithAnimation();
//                                    Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onNext(CommonBean<QuesDetailBean> quesDetailBeanCommonBean) {
//                                if (100 == quesDetailBeanCommonBean.getCode()) {
//
//                                    dialog.setTitleText("删除成功")
//                                            .setConfirmText("确定")
//                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                            sweetAlertDialog.dismissWithAnimation();
//                                            Intent it = new Intent();
//                                            Bundle bundle = new Bundle();
//                                            bundle.putInt("quesId",quesId);
//                                            it.putExtras(bundle);
//                                            it.setAction("com.jc.delQues");
//                                            baseActivity.sendBroadcast(it);
//                                            baseActivity.finish();
//                                        }
//                                    });
//                                } else {
//                                    Toast.makeText(baseActivity, quesDetailBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onStart() {
//                                super.onStart();
//                                if (null != dialog && !dialog.isShowing()) {
//                                    dialog.show();
//                                }
//                            }
//                        }
//                );

    }

    /**
     * 加载显示图片
     *
     * @param img
     * @param url
     */
    private void loadImg(ImageView img, String url) {
        Glide.with(baseActivity).load(url)
                .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(220, 148) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .into(img);
    }

    /**
     * 设置图片显示隐藏
     *
     * @param img
     * @param flag
     */
    private void setImageVisible(ImageView img, boolean flag) {
        if (flag) {
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.GONE);
        }
    }


    /**
     * 公共请求参数
     *
     * @return
     */
    private RequestBody commRequest(int page) {

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
            // 问题id
            obj.put("quesId", quesId);
            // 当前页
            obj.put("pageIndex", page);
            // 每页个数
            obj.put("pageSize", 10);
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
            if (2 == resultCode) {
                setContent();
            }
        }
    }
}
