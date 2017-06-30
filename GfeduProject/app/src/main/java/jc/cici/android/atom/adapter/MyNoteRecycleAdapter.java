package jc.cici.android.atom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.lib.PictureSelector;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.NoteBean;
import jc.cici.android.atom.ui.note.AddNoteActivity;
import jc.cici.android.atom.ui.note.GalleryActivity;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.GlideCircleTransform;

/**
 * 我的笔记列表适配
 * Created by atom on 2017/6/6.
 */

public class MyNoteRecycleAdapter extends BaseRecycleerAdapter<NoteBean.Note, MyNoteRecycleAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<NoteBean.Note> mList;
    private int status;
    // 存放图片url
    ArrayList<String> imgList = new ArrayList<>();


    public MyNoteRecycleAdapter(Context context, ArrayList<NoteBean.Note> items, int status) {
        super(context, items);
        this.mCtx = context;
        this.mList = items;
        this.status = status;
    }

    public void addItem(int position,NoteBean.Note note){
        mList.add(position,note);
        notifyItemRemoved(position);
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, NoteBean.Note item, final int position) {

        if (2 == status) { // 大家笔记
            // 获取用户图像
            String userImg = item.getSN_Head();
            // 设置item中Image布局
            Glide.with(mCtx).load(userImg)
                    .placeholder(R.drawable.icon_avatar) //加载中显示的图片
                    .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                    .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                    .override(280, 186) // 设置最终显示图片大小
                    .centerCrop() // 中心剪裁
                    .skipMemoryCache(true) // 跳过缓存
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                    .transform(new GlideCircleTransform(mCtx)) // 设置圆角
                    .into(holder.userImg);
            holder.userImg.setVisibility(View.VISIBLE);
            holder.our_layout.setVisibility(View.VISIBLE);
            // 用户名
            holder.userName.setText(item.getS_NickName());
            holder.userName.setVisibility(View.VISIBLE);
            // 上传时间
            holder.ourDate_txt.setText(item.getNTBAddTime());
            holder.ourDate_txt.setVisibility(View.VISIBLE);
            holder.dateTxt.setVisibility(View.GONE);
            // 点赞
            holder.zanCount.setText("(" + item.getZcount() + ")");
            holder.zanCount.setVisibility(View.VISIBLE);
            // 点赞按钮
            holder.zanBtn.setVisibility(View.VISIBLE);
            // 知识地图
            String relStr = item.getSubJectSName();
            if (null != relStr && !"".equals(relStr) && relStr.length() > 0) {
                holder.relative_txt.setText("知识地图：" + ToolUtils.strReplaceAll(relStr));
            }
        } else { // 我的笔记
            // 上传时间
            holder.dateTxt.setText(item.getNTBAddTime());
            holder.dateTxt.setVisibility(View.VISIBLE);
            holder.our_layout.setVisibility(View.GONE);
            holder.userImg.setVisibility(View.GONE);
            holder.userName.setVisibility(View.GONE);
            holder.zanCount.setVisibility(View.GONE);
            holder.zanBtn.setVisibility(View.GONE);
            holder.ourDate_txt.setVisibility(View.GONE);
            // 知识地图
            String relStr = item.getSubJectSName();
            if (null != relStr && !"".equals(relStr) && relStr.length() > 0) {
                holder.relative_txt.setText("知识地图：" + ToolUtils.strReplaceAll(relStr));
                holder.rel_layout.setVisibility(View.VISIBLE);
                holder.divLine.setVisibility(View.VISIBLE);
            }else{
                holder.rel_layout.setVisibility(View.GONE);
                holder.divLine.setVisibility(View.INVISIBLE);
            }
        }
        // 笔记内容
        holder.note_txt.setText(ToolUtils.strReplaceAll(item.getNTBContent()));
        // 截屏图片
        String screenImgStr = item.getNTBScreenShots();
        if (null != screenImgStr && !"".equals(screenImgStr) && screenImgStr.length() > 0) { // 有截图图片情况
            Glide.with(mCtx).load(screenImgStr)
                    .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                    .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                    .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                    .override(280, 186) // 设置最终显示图片大小
                    .centerCrop() // 中心剪裁
                    .skipMemoryCache(true) // 跳过缓存
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                    .into(holder.shotScreen_img);
            holder.shotScreen_img.setVisibility(View.VISIBLE);
            holder.shotScreen_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mCtx, GalleryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",0);
                    imgList.clear();
                    imgList.add(mList.get(position).getNTBScreenShots());
                    bundle.putStringArrayList("imgList",imgList);
                    it.putExtras(bundle);
                    mCtx.startActivity(it);
                }
            });
        } else { // 无截屏图片情况
            holder.shotScreen_img.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_notefragment_view;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 大家笔记布局
        @BindView(R.id.our_layout)
        RelativeLayout our_layout;
        // 用户图像
        @BindView(R.id.userImg)
        ImageView userImg;
        // 用户名
        @BindView(R.id.userName)
        TextView userName;
        // 大家笔记上传时间
        @BindView(R.id.ourDate_txt)
        TextView ourDate_txt;
        // 笔记内容
        @BindView(R.id.note_txt)
        TextView note_txt;
        // 截屏图片
        @BindView(R.id.shotScreen_img)
        ImageView shotScreen_img;
        // 上传日期时间
        @BindView(R.id.dateTxt)
        TextView dateTxt;
        // 知识地图布局
        @BindView(R.id.rel_layout)
        RelativeLayout rel_layout;
        // 下划线
        @BindView(R.id.divLine)
        ImageView divLine;
        // 知识地图
        @BindView(R.id.relative_txt)
        TextView relative_txt;
        // 点赞布局
        @BindView(R.id.zan_layout)
        RelativeLayout zan_layout;
        // 点赞数量
        @BindView(R.id.zanCount)
        TextView zanCount;
        // 点赞按钮
        @BindView(R.id.zanBtn)
        Button zanBtn;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
