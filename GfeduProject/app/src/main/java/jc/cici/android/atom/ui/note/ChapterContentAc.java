package jc.cici.android.atom.ui.note;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import jc.cici.android.atom.adapter.KnowledgeAdapter;
import jc.cici.android.atom.adapter.RelAnswerRecyclerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.SubjectBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
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
 * 科目章节activity
 * Created by atom on 2017/6/7.
 */

public class ChapterContentAc extends BaseActivity {

    private BaseActivity baseActivity;
    //    // 跳转来源id
//    private int jumpFlag;
    // 阶段id
    private int stageId;
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
    // 搜索更多布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 确定提交
    @BindView(R.id.register_txt)
    TextView register_txt;
    // recycleView
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private SweetAlertDialog dialog;
    // 相关科目列表
    private ArrayList<SubjectBean.Subject> relList;
    private LinearLayoutManager linearLayoutManager;
    private KnowledgeAdapter adapter;
    private int subjectId;
    private String subjectName;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Bundle bundle = msg.getData();
                subjectId = bundle.getInt("subjectId");
                subjectName = bundle.getString("subjectName");
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_knowledge_select;
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
        stageId = getIntent().getIntExtra("stageId", 0);
//        // 跳转来源标示
//        jumpFlag = getIntent().getIntExtra("jumpFlag", 0);
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
     * 网络请求获取数据并添加到视图上
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
            obj.put("client", commParam.getClient());
            obj.put("userId", commParam.getUserId());
            obj.put("stageId", stageId);
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<SubjectBean>> observable = httpPostService.getSubjectListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<SubjectBean>>() {
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
                            public void onNext(CommonBean<SubjectBean> commonBean) {
                                if (100 == commonBean.getCode()) {
                                    relList = commonBean.getBody().getSubjectList();
                                    adapter = new KnowledgeAdapter(baseActivity, relList, 2, handler);
                                    SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                    animationAdapter.setDuration(1000);
                                    recyclerView.setAdapter(adapter);
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

//        adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        });
    }

    /**
     * 初始化，获取固定页面设置
     */
    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        share_layout.setVisibility(View.VISIBLE);
        back_layout.setVisibility(View.VISIBLE);
        title_txt.setText("选择课表");
        register_txt.setText("确定");
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @OnClick({R.id.back_layout, R.id.register_txt})
    void OnClick(View view) {

        switch (view.getId()) { // 返回按钮
            case R.id.back_layout:
                this.finish();
                break;
            case R.id.register_txt: // 确定文字监听
                Intent quesIt = new Intent();
                Bundle quesBundle = new Bundle();
                quesBundle.putInt("subjectId", subjectId);
                quesBundle.putString("subjectName", subjectName);
                quesIt.putExtras(quesBundle);
                setResult(2, quesIt);
                this.finish();
//                switch (jumpFlag) {
//                    case 0: // 新增笔记提交
////                        Intent intent = new Intent(this, AddNoteActivity.class);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        Bundle bundle = new Bundle();
////                        bundle.putInt("subjectId",subjectId);
////                        bundle.putString("subjectName",subjectName);
////                        intent.putExtras(bundle);
////                        startActivity(intent);
////                        this.finish();
//                        Intent it = new Intent();
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("subjectId",subjectId);
//                        bundle.putString("subjectName",subjectName);
//                        it.putExtras(bundle);
//                        setResult(2,it);
//                        this.finish();
//                        break;
//                    case 1: // 新增问题提交
//                        Intent quesIt = new Intent();
//                        Bundle quesBundle = new Bundle();
//                        quesBundle.putInt("subjectId",subjectId);
//                        quesBundle.putString("subjectName",subjectName);
//                        quesIt.putExtras(quesBundle);
//                        setResult(2,quesIt);
//                        this.finish();
//                        break;
//                    case 2: // 新增回答提交
//                        break;
//                    case 3: // 追问提交
//                        break;
//                    default:
//                        break;
//                }
                break;
            default:
                break;
        }
    }
}
