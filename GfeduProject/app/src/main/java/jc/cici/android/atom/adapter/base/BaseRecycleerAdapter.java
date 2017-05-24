package jc.cici.android.atom.adapter.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装RecycleAdapter
 * Created by atom on 2017/5/9.
 */

public abstract class BaseRecycleerAdapter<DATA, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Context context;
    protected List<DATA> items = new ArrayList<>();

    public BaseRecycleerAdapter(Context context) {
        this.context = context;
    }

    public BaseRecycleerAdapter(Context context, List<DATA> items) {
        this.context = context;
        this.items = items;
    }

    /**
     * 设置数据源
     */
    public void setItems(List<DATA> items) {
        if (items != null) {
            this.items = items;
        }
    }

    /**
     * 添加一个数据集
     */
    public void addItems(List<DATA> items) {
        if (items != null) {
            this.items.addAll(items);
        }
    }

    /**
     * 获取一条数据
     */
    public DATA getItem(int position) {
        return items.get(position);
    }

    /**
     * 清空数据
     */
    public void clear() {
        items.clear();
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v, holder.getAdapterPosition());
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClick(v, holder.getAdapterPosition());
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClick(v, holder.getAdapterPosition());
                }
                return true;
            }
        });
        onBindViewHolder(holder, items.get(position), position);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(getLayoutId(), parent, false);
        return onCreateViewHolder(view,viewType);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * 抽象创建viewHolder
     *
     * @param view
     * @return
     */
    public abstract VH onCreateViewHolder(View view,int viewType);

    /**
     * @param item 为当前 item 对应的数据
     */
    public abstract void onBindViewHolder(VH holder, DATA item, int position);

    @LayoutRes
    public abstract int getLayoutId();

    /**
     * 会先调用此方法，再调用 OnItemClickListener 中的 onItemClick。
     */
    protected void onItemClick(View view, int position) {
    }

    /**
     * 会先调用此方法，再调用 setOnItemLongClickListener 中的 onItemLongClick。
     */
    protected void onItemLongClick(View view, int position) {
    }

    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}
