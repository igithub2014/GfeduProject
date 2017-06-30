package jc.cici.android.atom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.library.bubbleview.BubbleImageView;
import com.github.library.bubbleview.BubbleTextView;
import com.google.gson.Gson;

import java.util.ArrayList;

import jc.cici.android.R;
import jc.cici.android.atom.bean.ZhuiWen;
import jc.cici.android.atom.ui.note.GalleryActivity;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.GlideCircleTransform;

/**
 * 答案详情适配器
 * Created by atom on 2017/6/16.
 */

public class AnswerDetailRecyclerlAdapter extends
        RecyclerView.Adapter<AnswerDetailRecyclerlAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<ZhuiWen> mItems;
    private static final int SEND_TO_TYPE = 1;
    private static final int FROM_TO_TYPE = 2;
    private LayoutInflater mInflater;
    private ArrayList<String> imgList;

    public AnswerDetailRecyclerlAdapter(Context context, ArrayList<ZhuiWen> items) {

        this.mCtx = context;
        this.mItems = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        MyHolder holder = null;
        switch (viewType) {
            case SEND_TO_TYPE:
                view = mInflater.inflate(R.layout.item_send_view, parent, false);
                holder = new MyHolder(view);
                break;
            case FROM_TO_TYPE:
                view = mInflater.inflate(R.layout.item_from_view, parent, false);
                holder = new MyHolder(view);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        int itemType = getItemViewType(position);
        switch (itemType) {
            case FROM_TO_TYPE: // 其他用户
                // 设置时间
                timeSetting(holder.dateTxtFrom, position);
                Glide.with(mCtx).load(mItems.get(position).getHeadImg())
                        .placeholder(R.drawable.icon_avatar) //加载中显示的图片
                        .error(R.drawable.icon_avatar) //加载失败时显示的图片
                        .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                        .override(80, 80) // 设置最终显示图片大小
                        .centerCrop() // 中心剪裁
                        .skipMemoryCache(true) // 跳过缓存
                        .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                        .transform(new GlideCircleTransform(mCtx)) // 设置圆角
                        .into(holder.userImg_From);
                // 用户名
                holder.userName_from.setText(mItems.get(position).getNickName());
                // 设置内容
                contentSetting(holder.bubbleFrom_txt, position);
                if (null == mItems.get(position).getImageUrl() || mItems.get(position).getImageUrl().length() == 0) {
                    holder.bubbleImage_from_layout.setVisibility(View.GONE);
                } else {
                    // 设置图片
                    Glide.with(mCtx).load(mItems.get(position).getImageUrl())
                            .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                            .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                            .override(350, 200) // 设置最终显示图片大小
                            .skipMemoryCache(true) // 跳过缓存
                            .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                            .into(holder.bubbleFrom_img);
                    holder.bubbleImage_from_layout.setVisibility(View.VISIBLE);
                    imgList = new ArrayList<>();
                    imgList.add(mItems.get(position).getImageUrl());
                    // 图片设置点击监听
                    setOnclick(holder.bubbleFrom_img, imgList, 0);
                }
                break;
            case SEND_TO_TYPE:// 当前用户
                // 设置时间
                timeSetting(holder.dateTxt, position);
                // 设置内容
                contentSetting(holder.bubble_txt, position);
                String str = mItems.get(position).getImageUrl();
                if (null == str || str.length() == 0) {
                    holder.bubbleImage_layout.setVisibility(View.GONE);
                } else {
                    Glide.with(mCtx).load(mItems.get(position).getImageUrl())
                            .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                            .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                            .override(350, 200) // 设置最终显示图片大小
                            .skipMemoryCache(true) // 跳过缓存
                            .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                            .into(holder.bubble_img);
                    holder.bubbleImage_layout.setVisibility(View.VISIBLE);
                    imgList = new ArrayList<>();
                    imgList.add(mItems.get(position).getImageUrl());
                    setOnclick(holder.bubble_img, imgList, 0);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position).getStudentID() == ToolUtils.getUserID(mCtx)) { // 当前用户情况
            return SEND_TO_TYPE;
        } else {
            return FROM_TO_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 追问(or 追问)时间
        TextView dateTxtFrom;
        // 追问(or 追问)人图像
        ImageView userImg_From;
        // 追问(or 追答)人姓名
        TextView userName_from;
        //  其他发送者追问(or追答)内容
        BubbleTextView bubbleFrom_txt;
        // 其他发送者追问(or追答)图片布局
        RelativeLayout bubbleImage_from_layout;
        // 其他发送者追问(or追答)图片
        BubbleImageView bubbleFrom_img;
        //  用户追问(or追答)内容
        // 追问(or 追问)时间
        TextView dateTxt;
        BubbleTextView bubble_txt;
        // 用户追问(or追答)图片布局
        RelativeLayout bubbleImage_layout;
        // 用户追问(or追答)图片
        BubbleImageView bubble_img;


        public MyHolder(View itemView) {
            super(itemView);
            dateTxtFrom = (TextView) itemView.findViewById(R.id.dateTxtFrom);
            userImg_From = (ImageView) itemView.findViewById(R.id.userImg_From);
            userName_from = (TextView) itemView.findViewById(R.id.userName_from);
            bubbleFrom_txt = (BubbleTextView) itemView.findViewById(R.id.bubbleFrom_txt);
            bubbleImage_from_layout = (RelativeLayout) itemView.findViewById(R.id.bubbleImage_from_layout);
            bubbleFrom_img = (BubbleImageView) itemView.findViewById(R.id.bubbleFrom_img);
            dateTxt = (TextView) itemView.findViewById(R.id.dateTxt);
            bubble_txt = (BubbleTextView) itemView.findViewById(R.id.bubble_txt);
            bubbleImage_layout = (RelativeLayout) itemView.findViewById(R.id.bubbleImage_layout);
            bubble_img = (BubbleImageView) itemView.findViewById(R.id.bubble_img);
        }
    }

    /**
     * 设置监听
     *
     * @param img
     * @param list
     * @param currentPos
     */
    private void setOnclick(ImageView img, final ArrayList<String> list, final int currentPos) {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mCtx, GalleryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", currentPos);
                bundle.putStringArrayList("imgList", list);
                it.putExtras(bundle);
                mCtx.startActivity(it);
            }
        });
    }

    /**
     * 设置时间
     *
     * @param tv
     * @param position
     */
    private void timeSetting(TextView tv, int position) {
        boolean b = true;
        if (position > 0) {
            b = mItems.get(position).getAddTime().equals(mItems.get(
                    position - 1).getAddTime());
        }
        if (position == 0 || !b) {
            tv.setText(mItems.get(position).getAddTime());
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    private void contentSetting(TextView tv, int position) {
        {
            boolean b = true;
            if (position > 0) {
                b = mItems.get(position).getContent().equals(mItems.get(
                        position - 1).getContent());
            }
            if (position == 0 || !b) {
                tv.setText(mItems.get(position).getContent());
                tv.setVisibility(View.VISIBLE);
            } else {
                tv.setVisibility(View.GONE);
            }
        }
    }
}
