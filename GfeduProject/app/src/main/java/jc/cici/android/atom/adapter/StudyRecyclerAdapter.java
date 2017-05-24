package jc.cici.android.atom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.StudyBean;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * 面授(在线，直播)列表适配器
 * Created by atom on 2017/5/9.
 */

public class StudyRecyclerAdapter extends BaseRecycleerAdapter<StudyBean, StudyRecyclerAdapter.MyHolder> {

    private static final int TYPE_HEADER = 0; // 带有header
    //    private static final int TYPE_FOOTER = 1; // 带有footer
    private static final int TYPE_NORMAL = 2; // 不带有header和footer
    private Context mCtx;
    private List<StudyBean> mDatas;
    private View mHeaderView;
    private View mFooterView;

    public StudyRecyclerAdapter(Context context, List<StudyBean> items) {
        super(context, items);
        this.mCtx = context;
        this.mDatas = items;
    }

    public View getmHeaderView() {
        return mHeaderView;
    }

    public void setmHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
        notifyItemInserted(0);
    }

    public View getmFooterView() {
        return mFooterView;
    }

    public void setmFooterView(View mFooterView) {
        this.mFooterView = mFooterView;
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0) {
            return TYPE_HEADER;
        }
//        if (position == getItemCount() - 1) {
//            return TYPE_FOOTER;
//        }
        return TYPE_NORMAL;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new MyHolder(mHeaderView);
        }
//        if (mFooterView != null && viewType == TYPE_FOOTER) {
//            return new MyHolder(mFooterView);
//        }
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, StudyBean item, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) { // 正常情况数据
            // 列表面授面授(在线，直播)课程名称
            holder.title_Item_Txt.setText(ToolUtils.replaceAllChar(item.getClassName()));
            // 列表面授(在线，直播)课程时间
            holder.time_Item_Txt.setText(item.getClassStartTime()+"-"+item.getClassEndTime());
            // 设置课程类型(面授,在线,直播)
            holder.type_Item_Txt.setText(ToolUtils.replaceAllChar(item.getClassType()));
            // 设置item中Image布局
            Glide.with(mCtx).load(item.getClassImg())
                    .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                    .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                    .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                    .override(280, 186) // 设置最终显示图片大小
                    .centerCrop() // 中心剪裁
                    .skipMemoryCache(true) // 跳过缓存
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                    .into(holder.courseImgView);
            int status = item.getClassStatus();
            switch (status) {
                case 1: // 正常情况
                    holder.noSelect_Img.setVisibility(View.GONE);
                    break;
                case -1: // 欠费锁定
                    holder.hint_Item_txt.setText("欠费中，补齐费用后可解锁");
                    holder.lockImg.setVisibility(View.VISIBLE);
                    holder.noSelect_Img.setVisibility(View.VISIBLE);
                    break;
                case 0: // 未开始情况
                    holder.title_Item_Txt.setTextColor(Color.parseColor("#c8c8c8"));
                    holder.time_Item_Txt.setTextColor(Color.parseColor("#c8c8c8"));
                    holder.type_Item_Txt.setTextColor(Color.parseColor("#c8c8c8"));
                    holder.noSelect_Img.setText(R.string.nostart);
                    holder.noSelect_Img.setVisibility(View.VISIBLE);
                    break;
                case 2: // 过期情况
                    break;
                default:
                    break;
            }
        } else if (getItemViewType(position) == TYPE_HEADER) { // 添加头部情况
            return;
        }
//        else { // 添加脚部情况
//            return;
//        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recycleview_studyhome;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 标记图片
        @BindView(R.id.giveFlag_Img)
        ImageView giveFlag_Img;
        // 标记文字
        @BindView(R.id.noSelect_Img)
        Button noSelect_Img;
        // 锁定图片
        @BindView(R.id.lockImg)
        Button lockImg;
        // item 课程图片
        @BindView(R.id.courseImgView)
        ImageView courseImgView;
        // 课程名
        @BindView(R.id.title_Item_Txt)
        TextView title_Item_Txt;
        // 课程时间
        @BindView(R.id.time_Item_Txt)
        TextView time_Item_Txt;
        //课程类型(面授 or 在线)
        @BindView(R.id.type_Item_Txt)
        TextView type_Item_Txt;
        // 是否过期
        @BindView(R.id.hint_Item_txt)
        TextView hint_Item_txt;

        public MyHolder(View itemView) {
            super(itemView);
            //如果是headerView或者是footerView,直接返回
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            // 添加
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
