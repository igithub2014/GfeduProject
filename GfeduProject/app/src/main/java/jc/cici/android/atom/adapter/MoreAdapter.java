package jc.cici.android.atom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseAdapter;
import jc.cici.android.atom.adapter.base.BaseViewHolder;

/**
 * 点击更多按钮弹出pop框内容
 * Created by atom on 2017/5/24.
 */

public class MoreAdapter extends BaseAdapter {

    private Context mCtx;
    private List<String> mList;
    private List<Integer> mICons;
    private int mStatus;

    public MoreAdapter(Context context, List<String> list, List<Integer> icons, int status) {
        this.mCtx = context;
        this.mList = list;
        mICons = icons;
        this.mStatus = status;
    }


    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        TextView moreTv = holder.getView(R.id.item_more);
        TextView tv = holder.getView(R.id.item_add);
        if (mStatus == 1) {
            moreTv.setText(mList.get(position));
            moreTv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        } else {
            tv.setText(mList.get(position));
            tv.setVisibility(View.VISIBLE);
            moreTv.setVisibility(View.GONE);
        }
        ImageView icon = holder.getView(R.id.icon_item);
        icon.setBackgroundResource(mICons.get(position));

        if (position == mList.size() - 1) {
            holder.setVisibility(R.id.item_line, BaseViewHolder.GONE);
        } else {
            holder.setVisibility(R.id.item_line, BaseViewHolder.VISIBLE);

        }
    }

    @Override
    public int getLayoutID(int position) {
        return R.layout.item;
    }

    @Override
    public boolean clickable() {
        return true;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
