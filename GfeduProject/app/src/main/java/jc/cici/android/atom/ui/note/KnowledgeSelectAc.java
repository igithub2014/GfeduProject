package jc.cici.android.atom.ui.note;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import jc.cici.android.atom.adapter.KnowledgeAdapter;
import jc.cici.android.atom.adapter.MyNoteRecycleAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.SubjectBean;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * 知识点检索
 * Created by atom on 2017/6/7.
 */

public class KnowledgeSelectAc extends BaseActivity {

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
    // 下划线布局
    @BindView(R.id.divImg)
    ImageView divImg;
    // recycleView
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<SubjectBean.Subject> itemList;
    private LinearLayoutManager linearLayoutManager;
    private KnowledgeAdapter adapter;

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
        classId = getIntent().getIntExtra("classId", 0);
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
//        // 填充内容
//        setContent();
    }

//    /**
//     * 网络请求获取数据并添加到视图上
//     */
//    private void setContent() {
//
//        itemList = new ArrayList<String>();
//        for (int i = 0; i < 15; i++) {
//            if (i % 2 == 0 && i < 4) {
//                itemList.add("CFA" + i + "级");
//            }else {
//                itemList.add("RFP" + i + "级");
//            }
//        }
//
//        linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapter = new KnowledgeAdapter(this, itemList,0);
//        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
//        animationAdapter.setDuration(1000);
//        recyclerView.setAdapter(adapter);
//
//        adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                baseActivity.openActivity(ProgramActivity.class);
//            }
//        });
//    }

    /**
     * 初始化，获取固定页面设置
     */
    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        title_txt.setText("选择课表");
        divImg.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.back_layout})
    void OnClick(View view){

        switch (view.getId()){ // 返回按钮
            case R.id.back_layout:
                this.finish();
                break;
            default:
                break;
        }
    }

}
