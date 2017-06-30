package jc.cici.android.atom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.SubjectBean;
import jc.cici.android.atom.ui.note.ChapterContentAc;
import jc.cici.android.atom.ui.note.ProgramActivity;

/**
 * 知识点框架适配
 * Created by atom on 2017/6/7.
 */

public class KnowledgeAdapter extends BaseRecycleerAdapter<SubjectBean.Subject, KnowledgeAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<SubjectBean.Subject> mData;
    // 根据传入flag判断跳转
    private int mFlag;
    private Handler mHanlder;

    public KnowledgeAdapter(Context context, ArrayList<SubjectBean.Subject> items, int flag, Handler handler) {
        super(context, items);
        this.mCtx = context;
        this.mData = items;
        this.mFlag = flag;
        this.mHanlder = handler;
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_common_view;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final SubjectBean.Subject item, final int position) {
        holder.item_knowledge_txt.setText(item.getSubjectName());
        holder.item_knowledge_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.goImg.setBackgroundResource(R.drawable.icon_selected_flag);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("subjectId", item.getSubjectId());
                bundle.putString("subjectName", item.getSubjectName());
                msg.what = 0;
                msg.setData(bundle);
                mHanlder.sendMessage(msg);
            }
        });

    }

    class MyHolder extends RecyclerView.ViewHolder {
        // item 布局
        @BindView(R.id.item_knowledge_layout)
        RelativeLayout item_knowledge_layout;
        // item内容
        @BindView(R.id.item_knowledge_txt)
        TextView item_knowledge_txt;
        // 选中图片
        @BindView(R.id.goImg)
        ImageView goImg;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

//        @OnClick({R.id.item_knowledge_layout})
//        void OnClick(View view) {
//            switch (view.getId()) {
//                case R.id.item_knowledge_layout:
//                    goImg.setBackgroundResource(R.drawable.icon_selected_flag);
//                    if (mFlag == 0) {
//                        Intent it = new Intent(mCtx, ProgramActivity.class);
//                        mCtx.startActivity(it);
//                    } else if (mFlag == 1) {
//                        Intent it = new Intent(mCtx, ChapterContentAc.class);
//                        mCtx.startActivity(it);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
    }
}
