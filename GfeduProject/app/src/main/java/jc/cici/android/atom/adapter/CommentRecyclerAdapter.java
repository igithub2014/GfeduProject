package jc.cici.android.atom.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.CommentBean;
import jc.cici.android.atom.bean.ZhuiWen;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.GlideCircleTransform;

/**
 * 评论列表适配器
 * Created by atom on 2017/6/17.
 */

public class CommentRecyclerAdapter extends BaseRecycleerAdapter<ZhuiWen, CommentRecyclerAdapter.MyHolder> {

    private Activity mCtx;
    private ArrayList<ZhuiWen> mItems;
    private Handler mHandler;

    public CommentRecyclerAdapter(Activity context, ArrayList<ZhuiWen> items, Handler handler) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
        this.mHandler = handler;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, ZhuiWen item, final int position) {
        // 用户图像
        Glide.with(mCtx).load(mItems.get(position).getHeadImg())
                .placeholder(R.drawable.icon_avatar) //加载中显示的图片
                .error(R.drawable.icon_avatar) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(80, 80) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .transform(new GlideCircleTransform(mCtx)) // 设置圆角
                .into(holder.userImg_comment);
        int replayID = item.getStudentID();
        // 回复人名称
        if (replayID == ToolUtils.getUserID(mCtx)) { // 评论人
            holder.commentName_txt.setText(item.getNickName());
            holder.delComment_Btn.setVisibility(View.VISIBLE);
            // 删除按钮设置监听
            holder.delComment_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 删除操作
                }
            });
        } else { // 回复某人
            holder.commentName_txt.setText(item.getNickName() + "：回复" + item.getToNickName());
            holder.delComment_Btn.setVisibility(View.GONE);
        }
        // 评论上传时间
        holder.ourQuesDate_txt.setText(item.getAddTime());
        // 评论内容
        holder.commentContent_txt.setText(item.getContent());
        // 评论按钮设置监听
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = position;
                mHandler.sendMessage(msg);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_comment_view;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 评论人图像
        ImageView userImg_comment;
        // 评论人名称
        TextView commentName_txt;
        // 删除按钮
        Button delComment_Btn;
        // 评论按钮
        Button commentBtn;
        // 评论上传时间
        TextView ourQuesDate_txt;
        // 评论内容
        TextView commentContent_txt;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this.itemView);
            userImg_comment = (ImageView) itemView.findViewById(R.id.userImg_comment);
            commentName_txt = (TextView) itemView.findViewById(R.id.commentName_txt);
            delComment_Btn = (Button) itemView.findViewById(R.id.delComment_Btn);
            commentBtn = (Button) itemView.findViewById(R.id.commentBtn);
            ourQuesDate_txt = (TextView) itemView.findViewById(R.id.ourQuesDate_txt);
            commentContent_txt = (TextView) itemView.findViewById(R.id.commentContent_txt);
        }
    }
}
