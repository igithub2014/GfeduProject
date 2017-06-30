package jc.cici.android.atom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.FaceBean;

/**
 * 面授适配
 * Created by atom on 2017/6/8.
 */

public class FaceRecyclerAdapter extends BaseRecycleerAdapter<FaceBean, FaceRecyclerAdapter.MyHolder> {


    private Context mCtx;
    private List<FaceBean> mItems;

    public FaceRecyclerAdapter(Context context, List<FaceBean> items) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, FaceBean item, int position) {
        if (position == 0 || !mItems.get(position - 1).getDate().equals(mItems.get(position).getDate())) {
            holder.item_date_txt.setText(mItems.get(position).getDate());
            holder.item_date_txt.setVisibility(View.VISIBLE);
            holder.item_inner_txt.setVisibility(View.GONE);
        } else {
            holder.item_inner_txt.setVisibility(View.VISIBLE);
            holder.item_date_txt.setVisibility(View.GONE);
        }
        holder.item_face_txt.setText(item.getCourse());
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_face_view;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        //item 布局
        @BindView(R.id.item_face_layout)
        RelativeLayout item_face_layout;
        // 日期
        @BindView(R.id.item_date_txt)
        TextView item_date_txt;
        // 隐藏布局
        @BindView(R.id.item_inner_txt)
        TextView item_inner_txt;
        // 课程内容
        @BindView(R.id.item_face_txt)
        TextView item_face_txt;
        // 选中图片
        @BindView(R.id.item_faceSelect_img)
        ImageView item_faceSelect_img;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.item_face_layout})
        void OnClick(View view) {
            switch (view.getId()) {
                case R.id.item_face_layout: // 点击选中
                    if (item_faceSelect_img.getVisibility() == View.VISIBLE) {
                        item_faceSelect_img.setVisibility(View.GONE);
                        item_face_txt.setTextColor(Color.parseColor("#333333"));
                    } else {
                        item_faceSelect_img.setVisibility(View.VISIBLE);
                        item_face_txt.setTextColor(Color.parseColor("#dd5555"));
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
