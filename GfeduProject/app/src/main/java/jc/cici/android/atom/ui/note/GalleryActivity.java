package jc.cici.android.atom.ui.note;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
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
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.ViewPagerFixed;
import uk.co.senab.photoview.PhotoView;

import android.view.ViewGroup.LayoutParams;

/**
 * 用于图片大图浏览Activity
 * Created by atom on 2017/6/13.
 */

public class GalleryActivity extends BaseActivity {

    private BaseActivity baseActivity;
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
    // viewPager
    @BindView(R.id.gallery)
    ViewPagerFixed gallery;
    // acitivity传递的位置
    private int position;
    // 当前的位置
    private int location = 0;
    private ArrayList<String> imgList = new ArrayList<>();
    private ArrayList<View> listViews = null;
    private Context mContext;
    // 触摸图片关闭标示
    private boolean isClose;
    // 触屏的第一点坐标
    private PointF startPoint = new PointF();
    private MyPageAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gallery;
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
        mContext = this;
        // 去标题
        baseActivity.requestNoTitle();
        // 添加视图
        setContentView(getLayoutId());
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        imgList = bundle.getStringArrayList("imgList");
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 显示数目
        isShowOkBt();
        // 填充内容
        setContent();
    }

    /**
     * 获取网络数据填充视图
     */
    private void setContent() {
        for (int i = 0; i < imgList.size(); i++) {
            if (listViews == null)
                listViews = new ArrayList<View>();
            PhotoView img = new PhotoView(this);
            img.setBackgroundColor(0xff000000);
            Glide.with(this)
                    .load(imgList.get(i))
                    .error(R.drawable.loadingfail_icon) //加载失败时显示的图片
                    .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                    .skipMemoryCache(true) // 跳过缓存
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                    .into(img);
            img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            listViews.add(img);

            img.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            isClose = true;
                            startPoint.set(event.getX(), event.getY());
                            break;
                        case MotionEvent.ACTION_MOVE:

                            if (Math.abs(event.getX() - startPoint.x) > ToolUtils.dip2px(mContext, 5) ||
                                    Math.abs(event.getY() - startPoint.y) > ToolUtils.dip2px(mContext, 5)) {
                                isClose = false;
                            }
                            break;
                        case MotionEvent.ACTION_UP:

                            if (isClose) {
                                finish();
//						overridePendingTransition(R.anim.default_anim_out, R.anim.anim_stay);
                            }
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
        }
        adapter = new MyPageAdapter(listViews);
        gallery.setAdapter(adapter);
        gallery.setPageMargin((int) getResources().getDimensionPixelOffset(
                R.dimen.ui_10_dip));
        gallery.setCurrentItem(position);
    }

    /**
     * 初始化控件内容
     */
    private void initView() {

        title_layout.setBackgroundResource(R.color.black);
        back_layout.setVisibility(View.VISIBLE);
        back_img.setVisibility(View.VISIBLE);
        back_img.setBackgroundResource(R.drawable.icom_back_white);
        title_txt.setText("0/0");
    }

    public void isShowOkBt() {
        if (imgList.size() > 0) {
            title_txt.setText((position + 1) + "/"
                    + imgList.size());
            title_txt.setTextColor(Color.parseColor("#ffffff"));
        } else {
            title_txt.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    /**
     * viewpaer 滑动监听
     *
     * @param arg0
     */
    @OnPageChange({R.id.gallery})
    void onPageSelected(int arg0) {
        location = arg0;
        title_txt.setText((location + 1) + "/"
                + imgList.size());
    }

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;

        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    @OnClick({R.id.back_layout})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                this.finish();
                break;
            default:
                break;
        }
    }
}
