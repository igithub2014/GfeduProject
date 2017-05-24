package jc.cici.android.atom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.Course;
import jc.cici.android.atom.bean.CourseInfo;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.GlideCircleTransform;

/**
 * 课程详情列表页内容适配器
 * Created by atom on 2017/5/12.
 */

public class CourseRecyclerAdapter extends BaseRecycleerAdapter<Course, CourseRecyclerAdapter.MyHolder> {

    private Context mCtx;
    private List<Course> mItems;

    public CourseRecyclerAdapter(Context context, List items) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        View convertView = LayoutInflater.from(mCtx).inflate(getLayoutId(), null);
        return new MyHolder(convertView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, Course item, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = holder;
            // 设置item中Image布局
            Glide.with(mCtx).load(item.getLessonData().get(position).getTeacherImg())
                    .placeholder(R.drawable.avatar_new) //加载中显示的图片
                    .error(R.drawable.avatar_new) //加载失败时显示的图片
                    .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                    .override(280, 186) // 设置最终显示图片大小
                    .centerCrop() // 中心剪裁
                    .skipMemoryCache(true) // 跳过缓存
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                    .transform(new GlideCircleTransform(mCtx)) // 设置圆角
                    .into(myHolder.courseItem_Img);
            // 老师名称
            myHolder.teacherName_Txt.setText(item.getLessonData().get(position).getTeacherName());
            // 课程名称
            myHolder.courseName_Txt.setText(item.getLessonData().get(position).getLessonName());
            int lessDateType = item.getLessonData().get(position).getLessonDateType();
            if (1 == lessDateType) {
                // 课程时间
                myHolder.courseTime_Txt.setText("上午 " + item.getLessonData().get(position).getLessonStartTime()
                        + "-"
                        + item.getLessonData().get(position).getLessonEndTime());
            } else if (2 == lessDateType) {
                // 课程时间
                myHolder.courseTime_Txt.setText("中午 " + item.getLessonData().get(position).getLessonStartTime()
                        + "-"
                        + item.getLessonData().get(position).getLessonEndTime());
            } else if (3 == lessDateType) {
                // 课程时间
                myHolder.courseTime_Txt.setText("下午 " + item.getLessonData().get(position).getLessonStartTime()
                        + "-"
                        + item.getLessonData().get(position).getLessonEndTime());
            }
            // 课程地点
            myHolder.address_Txt.setText(item.getLessonData().get(position).getLessonPlace());

            if (position == 0) { // 第一次悬浮显示item
                myHolder.header_title_layout.setVisibility(View.VISIBLE);
                // 判断是否是当天
                boolean isCurrentDay = ToolUtils.isCurrentDay(item.getDate());
                if (isCurrentDay) { // 表示今日
                    // 填充内容
                    myHolder.today_Txt.setText("今日");
                } else { // 非今日情况
                    String dateStr = item.getDate();
                    String[] str = dateStr.split("-");
                    String newStr = str[2] + str[1] + "月";
                    // 填充内容
                    myHolder.today_Txt.setText(ToolUtils.setTextSize(mCtx, newStr, 1, 5, R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                }
            } else {
                if (!mItems.get(position).getDate().equals(mItems.get(position - 1).getDate())) {
                    myHolder.header_title_layout.setVisibility(View.VISIBLE);
                    // 填充内容
                    String dateStr = item.getDate();
                    String[] str = dateStr.split("-");
                    String newStr = str[2] + str[1] + "月";
                    myHolder.today_Txt.setText(ToolUtils.setTextSize(mCtx, newStr, 1, 5, R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                    myHolder.qr_layout.setVisibility(View.GONE);
                } else {
                    myHolder.header_title_layout.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recyclerview_coursedetial;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        // 头部布局
        @BindView(R.id.header_title_layout)
        RelativeLayout header_title_layout;
        // 日期
        @BindView(R.id.today_Txt)
        TextView today_Txt;
        // 二维码扫码布局
        @BindView(R.id.qr_layout)
        RelativeLayout qr_layout;
        // 正常布局
        @BindView(R.id.item_normal_layout)
        RelativeLayout item_normal_layout;
        // 老师图像
        @BindView(R.id.courseItem_Img)
        ImageView courseItem_Img;
        // 老师名称
        @BindView(R.id.teacherName_Txt)
        TextView teacherName_Txt;
        // 课程名称
        @BindView(R.id.courseName_Txt)
        TextView courseName_Txt;
        // 上课时间
        @BindView(R.id.courseTime_Txt)
        TextView courseTime_Txt;
        // 上课地点
        @BindView(R.id.address_Txt)
        TextView address_Txt;
        // 是否测试进入图标
        @BindView(R.id.coming_Img)
        ImageView coming_Img;

        public MyHolder(View itemView) {
            super(itemView);
            // 添加
            ButterKnife.bind(this, itemView);
        }
    }
}
