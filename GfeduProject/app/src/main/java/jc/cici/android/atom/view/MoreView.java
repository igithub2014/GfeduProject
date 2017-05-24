package jc.cici.android.atom.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import jc.cici.android.R;

/**
 * 笔记，问题视图
 * Created by atom on 2017/5/20.
 */

public class MoreView extends PopupWindow {

    private Activity mCtx;
    private int mLessonId;
    private View convertView;

    public MoreView(Activity context, int lessonId) {

        this.mCtx = context;
        this.mLessonId = lessonId;
        convertView = LayoutInflater.from(mCtx).inflate(R.layout.popwindow_view, null);
        /** 获取默认高度和宽度 **/
        int h = mCtx.getWindowManager().getDefaultDisplay().getHeight();
        int w = mCtx.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(convertView);
        // 视图初始化操作
        initSetting();
        // 设置popWindows弹出宽度
        this.setWidth(w);
        // 设置popWindows弹出高度
        this.setHeight(h);
        // 设置可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化ColorDrable 颜色设置为透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);

    }

    private void initSetting() {
        LinearLayout note_Layout = (LinearLayout) convertView.findViewById(R.id.note_Layout);
        LinearLayout ques_Layout = (LinearLayout) convertView.findViewById(R.id.ques_Layout);

        setOnClick(note_Layout);
        setOnClick(ques_Layout);
    }

    public void setOnClick(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.note_Layout: // 笔记监听
                        Toast.makeText(mCtx, "笔记监听", Toast.LENGTH_SHORT).show();
                        MoreView.this.setAnimationStyle(R.style.popAnimation);
                        MoreView.this.dismiss();
                        break;
                    case R.id.ques_Layout: // 问题监听
                        Toast.makeText(mCtx, "问题监听", Toast.LENGTH_SHORT).show();
                        MoreView.this.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 弹出popWindow
     *
     * @param parent
     */
    public void showPopWindow(View parent) {
        if (!this.isShowing()) { // 如果没有被显示
            this.showAsDropDown(parent,
                    (parent.getLayoutParams().width ) - 150, 18);
        }else {
            this.dismiss();
        }
    }
}
