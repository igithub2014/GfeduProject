package jc.cici.android.atom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;

/**
 * 相关问题适配器
 * Created by atom on 2017/6/15.
 */

public class RelAnswerRecyclerAdapter extends BaseRecycleerAdapter<String,RelAnswerRecyclerAdapter.MyHolder> {


    private Context mCtx;
    private ArrayList<String> mItems;
    public RelAnswerRecyclerAdapter(Context context, ArrayList<String> items) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, String item, int position) {
        if(!"".equals(item) && item.length() > 0){
            holder.relation_Txt.setText(item);
        }
        holder.relation_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.relation_Txt.setTextColor(Color.parseColor("#dd5555"));
                // TODO 跳转问题详情页
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_relation_answer;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        // 相关问题
        @BindView(R.id.relation_Txt)
        TextView relation_Txt;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
