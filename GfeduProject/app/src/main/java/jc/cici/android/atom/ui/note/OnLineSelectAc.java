package jc.cici.android.atom.ui.note;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.OnLineRecycleAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.OnLineBean;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * 在线课程选择
 * Created by atom on 2017/6/8.
 */

public class OnLineSelectAc extends BaseActivity {

    private BaseActivity baseActivity;
    private int classId;
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
    // 下划线布局
    @BindView(R.id.divImg)
    ImageView divImg;
    // 确定提交
    @BindView(R.id.register_txt)
    TextView register_txt;
    // recycleView
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<OnLineBean> itemList;
    private ArrayList<String>childList;
    private LinearLayoutManager linearLayoutManager;
    private OnLineRecycleAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_online;
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
        classId = getIntent().getIntExtra("classId", 0);
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
     *  网络请求获取数据并填充在视图上
     */
    private void setContent() {
        itemList = new ArrayList<>();
        for(int i = 0; i < 15; i++){
            OnLineBean childBean = new OnLineBean();
            if(i ==0 ){
                childBean.setTitleName("职业伦理");
                childList = new ArrayList<String>();
                childList.add("职业规划");
                childList.add("职业发展");
                childList.add("就业前景");
                childBean.setChildName(childList);
            }else if(i == 1){
                childBean.setTitleName("职业伦理测试");
                childList = new ArrayList<String>();
                childList.add("职业伦理规划");
                childList.add("职业伦理发展");
                childList.add("就业伦理前景");
                childBean.setChildName(childList);
            }else {
                childBean.setTitleName("职业发展"+i);
                childList = new ArrayList<String>();
                childList.add("职业发展");
                childList.add("职业伦理发展");
                childBean.setChildName(childList);
            }
            itemList.add(childBean);
        }
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OnLineRecycleAdapter(this, itemList);
        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(adapter);

    }

    /**
     * 初始化，获取固定页面设置
     */
    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        share_layout.setVisibility(View.GONE);
        back_layout.setVisibility(View.VISIBLE);
        divImg.setVisibility(View.VISIBLE);
        title_txt.setText("选择课表");
        register_txt.setText("确定");
    }

    @OnClick({R.id.back_layout})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_layout:
                this.finish();
                break;
            default:
                break;
        }
    }
}
