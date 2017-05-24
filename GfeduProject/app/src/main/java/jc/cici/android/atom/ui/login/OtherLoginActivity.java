package jc.cici.android.atom.ui.login;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.view.GlideCircleTransform;

/**
 * 第三方登录
 * Created by atom on 2017/4/14.
 */

public class OtherLoginActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    private Context mCtx;
    // 标题
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 返回按钮
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 用户图像
    @BindView(R.id.nike_Img)
    ImageView nike_Img;
    // 第三方昵称
    @BindView(R.id.nike_Txt)
    TextView nike_Txt;
    // 无金程账号tab
    @BindView(R.id.tab_noCard)
    TextView tab_noCard;
    // 有金程账号
    @BindView(R.id.tab_haveCard)
    TextView tab_haveCard;
    // ViewPager
    @BindView(R.id.vp)
    ViewPager vp;
    private ArrayList<Fragment> list;
    // 用户唯一标示
    public static String uid;
    // 用户昵称
    public static String nikeName;
    // 用户图像
    public static String iconurl;
    // 用户类型
    public static String type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_otherlogin;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 入栈
        AppManager.getInstance().addActivity(this);
        baseActivity = this;
        // 去标题
        baseActivity.requestNoTitle();
        // 获取uid
        uid = getIntent().getStringExtra("uid");
        // 获取昵称
        nikeName = getIntent().getStringExtra("name");
        // 获取第三方图像
        iconurl = getIntent().getStringExtra("iconurl");
        // 火气第三方类型
        type = getIntent().getStringExtra("type");
        // 添加视图
        setContentView(getLayoutId());
        // 添加绑定
        ButterKnife.bind(this);
        // 填充内容
        initView();
        // 填充内容
        setContent();
    }

    /**
     * 视图初始显示
     */
    private void initView() {
        title_txt.setText("合作账号登录");
        back_layout.setVisibility(View.VISIBLE);
        if(null != nikeName && !"null".equals(nikeName)){ // 获取昵称不空
            nike_Txt.setText(nikeName);
        }
        if(null != iconurl && !"null".equals(iconurl)){
            Glide.with(this).load(iconurl)
                    .placeholder(R.drawable.avatar_new) //加载中显示的图片
                    .error(R.drawable.loadingfail_icon) //加载失败时显示的图片
                    .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                    .override(120,120) // 设置最终显示图片大小
                    .centerCrop() // 中心剪裁
                    .skipMemoryCache(true) // 跳过缓存
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                    .transform(new GlideCircleTransform(this)) // 设置圆角
                    .into(nike_Img);
        }

    }
    /**
     * 填充视图内容
     */
    private void setContent() {
        list = new ArrayList<Fragment>();
        Fragment noCardFragment = new NoCardLoginFragment();
        Fragment haveCardFragment = new HaveCardFragment();
        list.add(noCardFragment);
        list.add(haveCardFragment);
        vp.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), list));
        vp.setCurrentItem(0);
        addTextLine(tab_noCard,tab_haveCard);

    }

    @OnPageChange(R.id.vp)
    void onPageSelected(int position) {
        vp.setCurrentItem(position);
        if (position == 0) {
            addTextLine(tab_noCard,tab_haveCard);
        } else if (position == 1) {
            addTextLine(tab_haveCard,tab_noCard);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        AppManager.getInstance().finishActivity(this);
    }

    /**
     * fragment 适配器
     */
    class MyFragmentAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> mList;

        public MyFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }

    @OnClick({R.id.back_layout,R.id.tab_noCard, R.id.tab_haveCard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮监听
                baseActivity.finish();
                break;
            case R.id.tab_noCard: // 普通登录
                vp.setCurrentItem(0);
                addTextLine(tab_noCard,tab_haveCard);
                break;
            case R.id.tab_haveCard: // 手机快速登录
                vp.setCurrentItem(1);
                addTextLine(tab_haveCard,tab_noCard);
                break;
            default:
                break;
        }
    }

    /**
     * 添加下划线
     *
     * @param tv1
     * @param tv2
     */
    private void addTextLine(TextView tv1, TextView tv2) {
        tv1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tv1.setTextColor(Color.parseColor("#dd5555"));
        tv1.getPaint().setAntiAlias(true);
        tv2.getPaint().setFlags(0);
        tv2.setTextColor(Color.parseColor("#bbbbbb"));
        tv2.getPaint().setAntiAlias(true);
    }
}
