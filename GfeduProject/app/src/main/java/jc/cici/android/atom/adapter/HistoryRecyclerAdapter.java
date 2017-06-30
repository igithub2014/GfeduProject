package jc.cici.android.atom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.HistoryInfo;
import jc.cici.android.atom.bean.HistoryLesson;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * 课程历史适配
 * Created by atom on 2017/5/13.
 */

public class HistoryRecyclerAdapter extends BaseRecycleerAdapter<HistoryLesson, HistoryRecyclerAdapter.MyHolder> {

    private Context mCtx;
    private List<HistoryLesson> mItems;

    public HistoryRecyclerAdapter(Context context, List<HistoryLesson> items) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, HistoryLesson item, int position) {

        String dateStr = item.getLessonDate();
        String[] str = dateStr.split("/");
        String subStr = str[2].substring(0,2);
        String newStr = subStr + str[1] + "月";
        holder.date_Txt.setText(ToolUtils.setTextSize(mCtx,newStr,1,newStr.length(),R.style.style4,R.style.style5));
        holder.time_Txt.setText(item.getLessonStartTime()+item.getLessonEndTime());
        holder.courseContent_Txt.setText(item.getLessonName());
        // 出勤状态
        int status = item.getAttendanceStatus();
        if (0 == status) { // 缺勤状态
            holder.attendenceFlag_img.setBackgroundResource(R.drawable.icon_queqin);
        } else { // 出勤状态
            holder.attendenceFlag_img.setBackgroundResource(R.drawable.icon_chuqin);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recyclerview_history;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        // 日期
        @BindView(R.id.date_Txt)
        TextView date_Txt;
        // 课时时段
        @BindView(R.id.time_Txt)
        TextView time_Txt;
        // 课时内容
        @BindView(R.id.courseContent_Txt)
        TextView courseContent_Txt;
        // 出勤状态
        @BindView(R.id.attendenceFlag_img)
        ImageView attendenceFlag_img;

        public MyHolder(View itemView) {
            super(itemView);
            // 添加
            ButterKnife.bind(this, itemView);
        }
    }
}
