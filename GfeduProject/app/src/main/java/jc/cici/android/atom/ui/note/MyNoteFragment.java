package jc.cici.android.atom.ui.note;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.MyNoteRecycleAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.BaseFragment;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.NoteBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.CommonHeader;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的笔记
 * Created by atom on 2017/6/5.
 */

public class MyNoteFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_refresh_header)
    CommonHeader swipe_refresh_header;
    @BindView(R.id.swipe_target)
    RecyclerView swipe_target;
    private Activity mCtx;
    // 当前传递课程id
    private int cspkid;
    // 阶段id
    private int childClassTypeId;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<NoteBean.Note> list;
    private MyNoteRecycleAdapter adapter;
    private SweetAlertDialog dialog;
    // 用户id
    private int userId;
    // 当前页
    private int page = 1;
    // 笔记类型(1:我的笔记，2:大家的笔记)
    private int type = 1;
    // 定义广播接收器对象
    private BroadcastReceiver receiver;
    // 定义广播过滤器
    private IntentFilter filter;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        // 添加刷新视图
        swipeToLoadLayout.setRefreshHeaderView(view);
        //添加过渡滑动
        swipeToLoadLayout.setRefreshCompleteDelayDuration(1000);

        linearLayoutManager = new LinearLayoutManager(mCtx);
        swipe_target.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.notefragment_view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = this.getActivity();
        Bundle bundle = getArguments();
        cspkid = bundle.getInt("cspkid");
        // 阶段id
        childClassTypeId = bundle.getInt("stageId");
        System.out.println("caspkid >>>:" + cspkid);
        // 初始化广播接受器
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("com.jc.delnote".equals(intent.getAction())) { // 删除笔记广播
                    Bundle bundle = intent.getExtras();
                    int delNoteId = bundle.getInt("noteId");
                    for (int i = 0; i < list.size(); i++) {
                        if (delNoteId == (list.get(i).getNTBPkid())) {
                            list.remove(i);
                            adapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                } else if ("com.jc.addnote".equals(intent.getAction())) { // 添加笔记广播
                    // 刷新数据
                    refreshData();
                }
            }
        };
        // 注册广播
        filter = new IntentFilter();
        // 添加数据广播
        filter.addAction("com.jc.addnote");
        // 删除数据广播
        filter.addAction("com.jc.delnote");
        // 注册广播
        mCtx.registerReceiver(receiver, filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(swipe_refresh_header, null);
        // 添加数据
        if (NetUtil.isMobileConnected(mCtx)) {
            addData();
        } else {
            Toast.makeText(mCtx, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
        // 加载更多设置监听
        setLoadingMoreClick();
        // 刷新数据监听
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refreshData();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        if (NetUtil.isMobileConnected(mCtx)) {
            page = 1;
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            HttpPostService httpPostService = retrofit.create(HttpPostService.class);
            RequestBody body = commRequest(page);
            Observable<CommonBean<NoteBean>> observable = httpPostService.getNotesListInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<CommonBean<NoteBean>>() {
                                @Override
                                public void onCompleted() {
                                    swipeToLoadLayout.setRefreshing(false);//收头
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(mCtx, "网络请求错误", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNext(CommonBean<NoteBean> noteBeanCommonBean) {
                                    if (100 == noteBeanCommonBean.getCode()) {
                                        if (null != noteBeanCommonBean.getBody().getNotesList() && noteBeanCommonBean.getBody().getNotesList().size() > 0) {
                                            list.clear();
                                            list.addAll(noteBeanCommonBean.getBody().getNotesList());
                                            if (null != adapter) {
                                                adapter.notifyDataSetChanged();
                                            }
                                            // 设置加载更多监听
                                            setLoadingMoreClick();
                                        } else {
                                            // TODO 添加没有内容图片
                                            Toast.makeText(mCtx, "抱歉，还没有该课程问题", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(mCtx, noteBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );

        } else {
            Toast.makeText(mCtx, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加载更多
     */
    private void setLoadingMoreClick() {
        if (null != swipe_target && null != linearLayoutManager) {
            swipe_target.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    }

    /**
     * 添加填充内容
     */
    private void addData() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest(page);
        Observable<CommonBean<NoteBean>> observable = httpPostService.getNotesListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<NoteBean>>() {
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
                                    Toast.makeText(mCtx, "网络请求错误", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(CommonBean<NoteBean> noteBeanCommonBean) {
                                if (100 == noteBeanCommonBean.getCode()) {
                                    list = noteBeanCommonBean.getBody().getNotesList();
                                    if (null != list && list.size() > 0) {
                                        adapter = new MyNoteRecycleAdapter(mCtx, list, type);
                                        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                        animationAdapter.setDuration(1000);
                                        swipe_target.setAdapter(adapter);

                                        /**
                                         * item 点击
                                         */
                                        adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                Intent it = new Intent(mCtx, NoteDetailActivity.class);
                                                Bundle bundle = new Bundle();
                                                // 笔记标示(1:我的笔记)
                                                bundle.putInt("key", 1);
                                                bundle.putInt("noteId", list.get(position).getNTBPkid());
                                                it.putExtras(bundle);
                                                mCtx.startActivity(it);
                                            }
                                        });

                                    } else {
                                        // TODO 无数据加载图片
                                        Toast.makeText(mCtx, "暂无笔记，您可以点击添加按钮添加笔记", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mCtx, noteBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
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
     * 请求加载更多数据
     */
    private void Loading() {
        page++;
        if (NetUtil.isMobileConnected(mCtx)) {
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            HttpPostService httpPostService = retrofit.create(HttpPostService.class);
            RequestBody body = commRequest(page);
            Observable<CommonBean<NoteBean>> observable = httpPostService.getNotesListInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<CommonBean<NoteBean>>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(mCtx, "网络请求错误", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNext(CommonBean<NoteBean> noteBeanCommonBean) {
                                    if (100 == noteBeanCommonBean.getCode()) {
                                        ArrayList<NoteBean.Note> moreList = new ArrayList<>();
                                        moreList = noteBeanCommonBean.getBody().getNotesList();
                                        if (null != moreList && moreList.size() > 0) {
                                            list.addAll(moreList);
                                            if (null != adapter) {
                                                adapter.notifyDataSetChanged();
                                            }
                                        } else {
                                            Toast.makeText(mCtx, "亲爱的小主,已经没有更多数据了哦", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(mCtx, noteBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );

        } else {
            Toast.makeText(mCtx, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        if (null != receiver) {
            mCtx.unregisterReceiver(receiver);
        }
    }

    /**
     * 公共请求参数
     *
     * @return
     */
    private RequestBody commRequest(int page) {

        dialog = new SweetAlertDialog(mCtx, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setTitle("");

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        userId = commParam.getUserId();
        try {
            obj.put("client", commParam.getClient());
            obj.put("userId", userId);
            // 课表id
            obj.put("cspkid", cspkid);
            // 阶段id
            obj.put("childClassTypeId", childClassTypeId);
            // 第一页
            obj.put("pageIndex", page);
            // 每页个数
            obj.put("pageSize", 10);
            // 笔记类型(1:我的笔记,2:大家的笔记)
            obj.put("type", type);
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }
}
