package jc.cici.android.atom.ui.note;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiang.android.indicatordialog.IndicatorBuilder;
import com.jiang.android.indicatordialog.IndicatorDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import jc.cici.android.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jc.cici.android.atom.adapter.MoreAdapter;
import jc.cici.android.atom.adapter.OnLineRecycleAdapter;
import jc.cici.android.atom.adapter.base.BaseAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;

/**
 * 笔记总入口Activity
 * Created by atom on 2017/6/5.
 */

public class NoteAllActivity extends BaseActivity {

    private BaseActivity baseActivity;
    // 班级id
    private int classId;
    // 班型对应下课程id
    private int cspkid;
    // 阶段id
    private int stageId;
    // 课程id
    private int lessonId;
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
    // 搜索按钮
    @BindView(R.id.noteMore_Btn)
    Button noteMore_Btn;
    // 更多按钮
    @BindView(R.id.search_Btn)
    Button search_Btn;
    // 隐藏布局
    @BindView(R.id.register_txt)
    TextView register_txt;
    //悬浮按钮
    @BindView(R.id.floatAction_Btn)
    FloatingActionButton floatAction_Btn;
    // tab布局
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    // viewPager
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    // 标题切换
    private ArrayList<String> titleList;
    // fragment列表
    private ArrayList<Fragment> list;
    // 筛选适配
    private MoreAdapter adapter;
    private List<String> mLists = new ArrayList<>();
    private List<Integer> mICons = new ArrayList<>();
    // 定义广播接收器对象
    private BroadcastReceiver receiver;
    // 定义广播过滤器
    private IntentFilter filter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_noteall;
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
        // 课程id
        cspkid = getIntent().getIntExtra("cspkid", 0);
        classId = getIntent().getIntExtra("classId", 0);
        lessonId = getIntent().getIntExtra("lessonId", 0);
        stageId = getIntent().getIntExtra("stageId", 0);
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 填充内容
        setContent();
    }

    /**
     * 初始化数据
     */
    private void setContent() {

        titleList = new ArrayList<String>();
        titleList.add("我的笔记");
        titleList.add("大家的笔记");
        list = new ArrayList<Fragment>();
        Fragment myNoteFragment = new MyNoteFragment();
        Fragment ourNoteFragment = new OurNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("cspkid", cspkid);
        bundle.putInt("stageId", stageId);
        myNoteFragment.setArguments(bundle);
        ourNoteFragment.setArguments(bundle);
        list.add(myNoteFragment);
        list.add(ourNoteFragment);
        // 添加tab类型 设置为有进度条类型
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // tab添加名称
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout, 50, 50);
            }
        });
        // viewPager 设置适配器
        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), list, titleList));
        viewPager.setCurrentItem(0);
        // tab关联viewpager
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 初始化，获取固定页面设置
     */
    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.GONE);
        title_txt.setText("笔记");
        noteMore_Btn.setBackgroundResource(R.drawable.icon_note_search);
        noteMore_Btn.setVisibility(View.VISIBLE);
        search_Btn.setBackgroundResource(R.drawable.icon_note_more);
        search_Btn.setVisibility(View.VISIBLE);

        // 初始化广播接受器
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("com.jc.addnote".equals(intent.getAction())) { // 添加笔记广播
                    // 跳转我的笔记
                    viewPager.setCurrentItem(0);
                }
            }
        };
        // 注册广播
        filter = new IntentFilter();
        // 添加数据广播
        filter.addAction("com.jc.addnote");
        // 注册广播
        baseActivity.registerReceiver(receiver, filter);
    }

    /**
     * fragment 适配器
     */
    class MyFragmentAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> mList;
        private ArrayList<String> mTitleList;

        public MyFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list, ArrayList<String> titleList) {
            super(fm);
            this.mList = list;
            this.mTitleList = titleList;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position % mTitleList.size());
        }
    }

    /**
     * 设置tabLayout 下划线宽度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @OnClick({R.id.back_layout, R.id.noteMore_Btn, R.id.search_Btn, R.id.floatAction_Btn})
    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
                break;
            case R.id.noteMore_Btn: // 搜索按钮
                break;
            case R.id.search_Btn: // 更多按钮
                mLists.clear();
                mICons.clear();
                mLists.add("知识框架检索");
                mICons.add(R.drawable.icon_knowledage_search);
                mLists.add("学习课时检索");
                mICons.add(R.drawable.icon_studytime_search);
                adapter = new MoreAdapter(this, mLists, mICons, 1);
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
                            case 0: // 知识框架检索监听
                                baseActivity.openActivity(KnowledgeSelectAc.class);
                                dialog.dismiss();
                                break;
                            case 1: // 学习课时检索监听
                                stageId = 1;
                                if (stageId == 0) { // 在线
                                    baseActivity.openActivity(OnLineSelectAc.class);
                                } else if (stageId == 1) { // 面授
                                    baseActivity.openActivity(FaceToFaceActivity.class);
                                }
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
                break;
            case R.id.floatAction_Btn: // 悬浮按钮
                Bundle bundle = new Bundle();
                // 笔记添加标记
                bundle.putInt("jumpFlag", 0);
                bundle.putInt("classId", classId);
                bundle.putInt("lessonId", lessonId);
                bundle.putInt("stageId", stageId);
                baseActivity.openActivity(AddNoteActivity.class, bundle);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != unbinder){
            unbinder.unbind();
        }
        if (null != receiver) {
            baseActivity.unregisterReceiver(receiver);
        }
    }
}
