package jc.cici.android.atom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
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

public class StudyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private Context mCtx;
    private ArrayList<StudyBean> mDatas = new ArrayList<>();

    private View mHeaderView;

    private OnItemClickListener mListener;

    public StudyRecyclerAdapter(Context context, ArrayList<StudyBean> items, OnItemClickListener li) {
        this.mCtx = context;
        this.mDatas = items;
        this.mListener = li;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }


    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new Holder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview_studyhome, parent, false);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(viewHolder);
        final StudyBean item = mDatas.get(pos);
        if (viewHolder instanceof Holder) {
            // 列表面授面授(在线，直播)课程名称
            ((Holder) viewHolder).title_Item_Txt.setText(ToolUtils.replaceAllChar(item.getClassName()));
            // 列表面授(在线，直播)课程时间
            ((Holder) viewHolder).time_Item_Txt.setText(item.getClassStartTime().replaceAll("-",".") + "-" + item.getClassEndTime().replaceAll("-","."));
            // 设置课程类型(面授,在线,直播)
            ((Holder) viewHolder).type_Item_Txt.setText(ToolUtils.replaceAllChar(item.getClassType()));
            // 设置item中Image布局
            Glide.with(mCtx).load(item.getClassImg())
                    .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                    .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                    .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                    .override(280, 186) // 设置最终显示图片大小
                    .centerCrop() // 中心剪裁
                    .skipMemoryCache(true) // 跳过缓存
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                    .into(((Holder) viewHolder).courseImgView);
            int status = mDatas.get(pos).getClassStatus();
            switch (status) {
                case 1: // 正常情况
                    ((Holder) viewHolder).noSelect_Img.setVisibility(View.GONE);
                    break;
                case -1: // 欠费锁定
                    ((Holder) viewHolder).hint_Item_txt.setText("欠费中，补齐费用后可解锁");
                    ((Holder) viewHolder).lockImg.setVisibility(View.VISIBLE);
                    ((Holder) viewHolder).noSelect_Img.setVisibility(View.VISIBLE);
                    break;
                case 0: // 未开始情况
                    ((Holder) viewHolder).title_Item_Txt.setTextColor(Color.parseColor("#c8c8c8"));
                    ((Holder) viewHolder).time_Item_Txt.setTextColor(Color.parseColor("#c8c8c8"));
                    ((Holder) viewHolder).type_Item_Txt.setTextColor(Color.parseColor("#c8c8c8"));
                    ((Holder) viewHolder).noSelect_Img.setText(R.string.nostart);
                    ((Holder) viewHolder).noSelect_Img.setVisibility(View.VISIBLE);
                    break;
                case 2: // 过期情况
                    break;
                default:
                    break;
            }
            if (mListener == null) return;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos, item);
                }
            });
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }

    class Holder extends RecyclerView.ViewHolder {

        // 标记图片
        ImageView giveFlag_Img;
        // 标记文字
        Button noSelect_Img;
        // 锁定图片
        Button lockImg;
        // 课程图片
        ImageView courseImgView;
        // 课程名
        TextView title_Item_Txt;
        // 课程时间
        TextView time_Item_Txt;
        // 课程类型(面授 OR 在线)
        TextView type_Item_Txt;
        // 是否过期
        TextView hint_Item_txt;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            giveFlag_Img = (ImageView) itemView.findViewById(R.id.giveFlag_Img);
            noSelect_Img = (Button) itemView.findViewById(R.id.noSelect_Img);
            lockImg = (Button) itemView.findViewById(R.id.lockImg);
            courseImgView = (ImageView) itemView.findViewById(R.id.courseImgView);
            title_Item_Txt = (TextView) itemView.findViewById(R.id.title_Item_Txt);
            time_Item_Txt = (TextView) itemView.findViewById(R.id.time_Item_Txt);
            type_Item_Txt = (TextView) itemView.findViewById(R.id.type_Item_Txt);
            hint_Item_txt = (TextView) itemView.findViewById(R.id.hint_Item_txt);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, StudyBean data);
    }
}
