package jc.cici.android.atom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.MyAnswer;
import jc.cici.android.atom.bean.Question;
import jc.cici.android.atom.ui.note.GalleryActivity;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * 我的回答适配器
 * Created by atom on 2017/6/24.
 */

public class MyAnswerRecyclerAdapter extends BaseRecycleerAdapter<MyAnswer, MyAnswerRecyclerAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<MyAnswer> mItems;
    private int mStatus;
    private ArrayList<String> imgList;

    public MyAnswerRecyclerAdapter(Context context, ArrayList items, int status) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
        this.mStatus = status;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_questionfragment_view;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, MyAnswer item, int position) {

        switch (mStatus) {
            case 3: // 我的回答
                holder.dateTxt.setText(item.getAnswerAddTime());
                holder.dateTxt.setVisibility(View.VISIBLE);
                // 隐藏关联布局
                holder.relationAndStatus_layout.setVisibility(View.GONE);
                // 显示追问，点赞布局
                holder.response_layout.setVisibility(View.VISIBLE);
                // 追问按钮设置内容
                holder.responseCount_txt.setText(item.getAfterQuesCount() + "追问");
                // 评论数
                holder.commitCount_txt.setText("(" + item.getCommentsCount() + ")");
                // 点赞数量
                holder.zanResponseCount.setText("(" + item.getPraiseCount() + ")");
                // 是否为最佳答案
                if ("1".equals(item.getAnswerStatus())) {
                    holder.bestAnswer_Img.setVisibility(View.VISIBLE);
                } else {
                    holder.bestAnswer_Img.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
        // 问题内容
        holder.ques_txt.setText(ToolUtils.strReplaceAll(item.getAnswerContent()));
        // 获取上传图片
        imgList = item.getAnswerImageUrl();
        if (null != imgList && imgList.size() > 0) {
            // 获取图片个数
            int size = imgList.size();
            System.out.println("size >>>:" + size);
            switch (size) {

                case 1: // 一张图片
                    // 加载显示第一张图片
                    loadImg(holder.shotScreen1_img, imgList.get(0));
                    setImageVisible(holder.shotScreen1_img, true);
                    setImageVisible(holder.shotScreen2_img, false);
                    setImageVisible(holder.shotScreen3_img, false);
                    // 第一张图片设置监听
                    setOnclick(holder.shotScreen1_img, imgList, 0);
                    break;
                case 2: // 两张图片
                    // 加载显示第一张图片
                    loadImg(holder.shotScreen1_img, imgList.get(0));
                    // 加载显示第二张图片
                    loadImg(holder.shotScreen2_img, imgList.get(1));
                    setImageVisible(holder.shotScreen1_img, true);
                    setImageVisible(holder.shotScreen2_img, true);
                    setImageVisible(holder.shotScreen3_img, false);

                    // 第一张图片设置监听
                    setOnclick(holder.shotScreen1_img, imgList, 0);
                    // 第二张图片设置监听
                    setOnclick(holder.shotScreen2_img, imgList, 1);
                    break;
                case 3: // 三张图片
                    // 加载显示第一张图片
                    loadImg(holder.shotScreen1_img, imgList.get(0));
                    // 加载显示第二张图片
                    loadImg(holder.shotScreen2_img, imgList.get(1));
                    // 加载显示第三张图片
                    loadImg(holder.shotScreen3_img, imgList.get(2));
                    setImageVisible(holder.shotScreen1_img, true);
                    setImageVisible(holder.shotScreen2_img, true);
                    setImageVisible(holder.shotScreen3_img, true);

                    // 第一张图片设置监听
                    setOnclick(holder.shotScreen1_img, imgList, 0);
                    // 第二张图片设置监听
                    setOnclick(holder.shotScreen2_img, imgList, 1);
                    // 第三张图片设置监听
                    setOnclick(holder.shotScreen3_img, imgList, 2);

                    break;
                default:
                    break;
            }
        } else {
            setImageVisible(holder.shotScreen1_img, false);
            setImageVisible(holder.shotScreen2_img, false);
            setImageVisible(holder.shotScreen3_img, false);
        }

        // 问题状态
        int quesStatus = item.getAnswerStatus();
        switch (quesStatus) {
            case 1: // 未回答状态
                holder.quesStatus_txt.setBackgroundResource(R.drawable.flag_no_answer_bg);
                break;
            case 2: // 未解决状态
                holder.quesStatus_txt.setBackgroundResource(R.drawable.flag_have_answer_bg);
                break;
            case 3: // 已解决状态
                holder.quesStatus_txt.setBackgroundResource(R.drawable.flag_have_abslove_bg);
                break;
            default:
                break;
        }
    }

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
     * 加载显示图片
     *
     * @param img
     * @param url
     */
    private void loadImg(ImageView img, String url) {
        Glide.with(mCtx).load(url)
                .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(280, 186) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .into(img);
    }

    /**
     * 设置图片显示隐藏
     *
     * @param img
     * @param flag
     */
    private void setImageVisible(ImageView img, boolean flag) {
        if (flag) {
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.GONE);
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 大家答疑用户布局
        @BindView(R.id.ourQues_layout)
        RelativeLayout ourQues_layout;
        // 用户图像
        @BindView(R.id.userImg_ques)
        ImageView userImg_ques;
        //  用户名
        @BindView(R.id.userName_ques)
        TextView userName_ques;
        // 大家答疑上传日期
        @BindView(R.id.ourQuesDate_txt)
        TextView ourQuesDate_txt;
        // 答疑内容
        @BindView(R.id.ques_txt)
        TextView ques_txt;
        // 截屏图片1
        @BindView(R.id.shotScreen1_img)
        ImageView shotScreen1_img;
        // 截屏图片2
        @BindView(R.id.shotScreen2_img)
        ImageView shotScreen2_img;
        // 截屏图片3
        @BindView(R.id.shotScreen3_img)
        ImageView shotScreen3_img;
        // 截屏当前播放时间
        @BindView(R.id.dateTxt)
        TextView dateTxt;
        // 关联布局
        @BindView(R.id.relationAndStatus_layout)
        RelativeLayout relationAndStatus_layout;
        // 关联视频
        @BindView(R.id.relativeQues_txt)
        TextView relativeQues_txt;
        // 问题状态
        @BindView(R.id.quesStatus_txt)
        ImageView quesStatus_txt;
        // 追问布局
        @BindView(R.id.response_layout)
        RelativeLayout response_layout;
        // 追问按钮
        @BindView(R.id.responseCount_txt)
        Button responseCount_txt;
        // 点赞布局
        @BindView(R.id.zan_layout)
        LinearLayout zan_layout;
        // 点赞数量
        @BindView(R.id.zanResponseCount)
        TextView zanResponseCount;
        // 评论布局
        @BindView(R.id.commit_layout)
        LinearLayout commit_layout;
        // 评论按钮
        @BindView(R.id.commit_Btn)
        Button commit_Btn;
        // 评论数量
        @BindView(R.id.commitCount_txt)
        TextView commitCount_txt;
        // 最佳答案标记
        @BindView(R.id.bestAnswer_Img)
        ImageView bestAnswer_Img;


        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
