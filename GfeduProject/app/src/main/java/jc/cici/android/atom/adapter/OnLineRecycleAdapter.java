package jc.cici.android.atom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jc.cici.android.R;
import jc.cici.android.atom.bean.OnLineBean;

/**
 * 在线筛选适配
 * Created by atom on 2017/6/8.
 */

public class OnLineRecycleAdapter extends ExpandableRecyclerAdapter<OnLineBean, String, OnLineRecycleAdapter.RecipeViewHolder, OnLineRecycleAdapter.ChildHolder> {

    private LayoutInflater mInflater;
    private List<OnLineBean> mList;

    public OnLineRecycleAdapter(Context context, @NonNull List<OnLineBean> recipeList) {
        super(recipeList);
        mInflater = LayoutInflater.from(context);
        this.mList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView = mInflater.inflate(R.layout.item_recipe_view, parentViewGroup, false);
        return new RecipeViewHolder(recipeView);
    }

    @NonNull
    @Override
    public ChildHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View childView = mInflater.inflate(R.layout.item_expand_child_view, childViewGroup, false);
        return new ChildHolder(childView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull RecipeViewHolder parentViewHolder, int parentPosition, @NonNull OnLineBean parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull ChildHolder childViewHolder, int parentPosition, int childPosition, @NonNull String child) {
        childViewHolder.bind(child,childPosition);
    }


    /**
     * 第一层ViewHolder
     */
    class RecipeViewHolder extends ParentViewHolder {

        RelativeLayout item_parent_layout;
        // 设置item文字
        @BindView(R.id.item_parent_txt)
        TextView item_parent_txt;
        // 设置打开关闭图片
        @BindView(R.id.item_parentFlag_img)
        ImageView item_parentFlag_img;

        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            item_parent_layout = (RelativeLayout) itemView.findViewById(R.id.item_parent_layout);
            ButterKnife.bind(this, itemView);

            item_parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.item_parent_layout) { // 表示选中
                        if (isExpanded()) { // 当前扩展
                            collapseView();// 收缩
                            item_parentFlag_img.setBackgroundResource(R.drawable.icon_expandable_close);
                        } else { // 当前收缩
                            collapseAllParents(); // 如果有其他打开的先关闭其他
                            expandView();
                            item_parentFlag_img.setBackgroundResource(R.drawable.icon_expandable_open);
                        }

                    }
                }
            });
        }

        public void bind(OnLineBean onLineBean) {
            item_parent_txt.setText(onLineBean.getTitleName());
        }


        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return false;
        }


    }

    /**
     * 子分类viewHolder
     * Created by atom on 2017/6/8.
     */

    class ChildHolder extends ChildViewHolder {

        // item 布局
        @BindView(R.id.item_child_layout)
        RelativeLayout item_child_layout;
        // 设置item文字
        @BindView(R.id.item_child_txt)
        TextView item_child_txt;
        // 设置打开关闭图片
        @BindView(R.id.item_child_Img)
        ImageView item_child_Img;
        private int mPos;

        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */
        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String childItem,int position) {
            item_child_txt.setTag(position);
            item_child_txt.setText(childItem);
            mPos = position;
        }

        @OnClick({R.id.item_child_layout})
        void onClick(View v) {
            if (v.getId() == R.id.item_child_layout) { // 表示选中
                if( item_child_Img.getVisibility() == View.GONE && mPos == (int)item_child_txt.getTag()){
                    item_child_txt.setTextColor(Color.parseColor("#dd5555"));
                    item_child_Img.setVisibility(View.VISIBLE);
                }else{
                    item_child_txt.setTextColor(Color.parseColor("#333333"));
                    item_child_Img.setVisibility(View.GONE);
                }

            }
        }
    }


}
