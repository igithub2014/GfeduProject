package jc.cici.android.atom.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

import jc.cici.android.R;


/**
 * 公共上啦刷新视图
 * Created by atom on 2017/6/7.
 */

public class CommonHeader extends LinearLayout implements SwipeRefreshTrigger, SwipeTrigger {

    private TextView tv;

    public CommonHeader(Context context) {
        this(context, null, 0);
    }

    public CommonHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(), R.layout.common_header_view, null);
        addView(view, params);
        tv = (TextView) view.findViewById(R.id.refresh_txt);
    }

    @Override
    public void onRefresh() {
        tv.setText("正在拼命加载数据...");
    }

    @Override
    public void onPrepare() {
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {

    }

    @Override
    public void onRelease() {
        tv.setText("释放刷新");
    }

    @Override
    public void onComplete() {
        tv.setText("刷新完成");
    }

    @Override
    public void onReset() {
        tv.setText("");
    }

}
