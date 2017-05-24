package jc.cici.android.atom.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 仿qq弹窗基类
 * Created by atom on 2017/5/24.
 */

public abstract class BaseAdapter<M extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        if (clickable()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, holder.getAdapterPosition());
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                    }
                }
            });
        }

        onBindView(holder, holder.getLayoutPosition());
    }

    public abstract void onBindView(BaseViewHolder holder, int position);

    @Override
    public int getItemViewType(int position) {
        return getLayoutID(position);
    }


    public abstract int getLayoutID(int position);

    public abstract boolean clickable();

    /**
     * 会先调用此方法，再调用 setOnItemLongClickListener 中的 onItemLongClick。
     */
    protected void onItemClick(View view, int position) {
    }

    protected BaseAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(BaseAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
