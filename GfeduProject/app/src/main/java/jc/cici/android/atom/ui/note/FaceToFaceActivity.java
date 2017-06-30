package jc.cici.android.atom.ui.note;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjiazhe.wavesidebar.WaveSideBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.FaceRecyclerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.FaceBean;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * 面授activity
 * Created by atom on 2017/6/8.
 */

public class FaceToFaceActivity extends BaseActivity {

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
    @BindView(R.id.side_bar)
    WaveSideBar side_bar;
    // 面授数据
    private ArrayList<FaceBean> itemList;
    private LinearLayoutManager linearLayoutManager;
    private FaceRecyclerAdapter adapter;
    private StringBuffer buffer;
    private String[] str;

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
     * 填充内容
     */
    private void setContent() {

        itemList = new ArrayList<FaceBean>();
        itemList.add(new FaceBean("1月", "职业伦理"));
        itemList.add(new FaceBean("1月", "CFA从业"));
        itemList.add(new FaceBean("1月", "拓展与联系"));
        itemList.add(new FaceBean("1月", "金程教育"));

        itemList.add(new FaceBean("2月", "CFA从业"));
        itemList.add(new FaceBean("2月", "拓展与联系"));
        itemList.add(new FaceBean("2月", "金程教育"));

        itemList.add(new FaceBean("3月", "CFA从业"));
        itemList.add(new FaceBean("3月", "拓展与联系"));
        itemList.add(new FaceBean("3月", "金程教育"));

        itemList.add(new FaceBean("4月", "CFA从业"));
        itemList.add(new FaceBean("4月", "拓展与联系"));
        itemList.add(new FaceBean("4月", "金程教育"));

        itemList.add(new FaceBean("5月", "CFA从业"));
        itemList.add(new FaceBean("5月", "拓展与联系"));
        itemList.add(new FaceBean("6月", "金程教育"));
        itemList.add(new FaceBean("6月", "CFA从业"));
        itemList.add(new FaceBean("6月", "拓展与联系"));
        itemList.add(new FaceBean("7月", "金程教育"));
        itemList.add(new FaceBean("7月", "CFA从业"));
        itemList.add(new FaceBean("7月", "拓展与联系"));
        itemList.add(new FaceBean("7月", "金程教育"));
        itemList.add(new FaceBean("8月", "CFA从业"));
        itemList.add(new FaceBean("8月", "拓展与联系"));
        itemList.add(new FaceBean("8月", "金程教育"));
        itemList.add(new FaceBean("8月", "CFA从业"));
        itemList.add(new FaceBean("8月", "拓展与联系"));
        itemList.add(new FaceBean("9月", "金程教育"));
        itemList.add(new FaceBean("9月", "CFA从业"));
        itemList.add(new FaceBean("9月", "拓展与联系"));
        itemList.add(new FaceBean("9月", "金程教育"));

        buffer = new StringBuffer();
        for (int i = 0; i < itemList.size(); i++) {
            if (i == 0) {
                buffer.append(itemList.get(i).getDate());
            } else if (!(itemList.get(i - 1).getDate()).equals(itemList.get(i).getDate())) {
                buffer.append(","+itemList.get(i).getDate());
            }
        }
        String bf =  buffer.toString();
        str = bf.split(",");
        side_bar.setIndexItems(str);
        side_bar.setVisibility(View.VISIBLE);
        side_bar.setLazyRespond(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new FaceRecyclerAdapter(this, itemList);
        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(adapter);

        side_bar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i = 0; i < itemList.size(); i++) {
                    if (index.equals(itemList.get(i).getDate())) {
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }

    /**
     * 填充初始内容
     */
    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        share_layout.setVisibility(View.GONE);
        back_layout.setVisibility(View.VISIBLE);
        divImg.setVisibility(View.VISIBLE);
        title_txt.setText("选择课表");
        register_txt.setText("确定");

    }
}
