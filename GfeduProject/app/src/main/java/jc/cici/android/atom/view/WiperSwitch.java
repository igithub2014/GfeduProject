package jc.cici.android.atom.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import jc.cici.android.R;
import jc.cici.android.atom.ui.note.NoteDetailActivity;

/**
 * 自定义开关按钮
 * Created by atom on 2017/6/12.
 */

public class WiperSwitch extends View implements View.OnTouchListener {

    private Bitmap bg_private, bg_public, slipper_btn;
    // 按下时的x和现在的x
    private float downX, nowX;
    // 记录用户是否在滑动
    private boolean onSlip = false;
    // 当前状态
    private boolean nowStatus = false;
    // 监听接口
    private OnChangedListener listener;

    /**
     * 设置监听器
     *
     * @param activity
     */
    public void setOnChangedListener(NoteDetailActivity activity) {
        this.listener = activity;
    }
//
//    /**
//     * 设置监听器
//     *
//     * @param editAc
//     */
//    public void setOnChangedListener(AddNoteAc addnoteAc) {
//        this.listener = addnoteAc;
//    }
//    /**
//     * 设置监听器
//     *
//     * @param editAc
//     */
//    public void setOnChangedListener(AddQuestion addQuestion) {
//        this.listener = addQuestion;
//    }

    public WiperSwitch(Context context) {
        super(context);
        init();
    }

    public WiperSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public WiperSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        float x = 0;
        // 根据当前状态画开关状态
        if (nowX < bg_public.getWidth() / 2) {
            canvas.drawBitmap(bg_private, matrix, paint); // 画出关闭时候的按钮
        } else {
            canvas.drawBitmap(bg_public, matrix, paint); // 画出打开时候的按钮
        }

        if (onSlip) { // 判断是否在滑动
            if (nowX >= bg_public.getWidth()) { // 是否划出指定范围,阻止滑块滑到按钮之外
                x = bg_public.getWidth() - slipper_btn.getWidth() / 2; // 获取滑块一半的位置大小
            } else {
                x = nowX - slipper_btn.getWidth() / 2;
            }
        } else {
            if (nowStatus) { // 根据当前状态设置滑块的x值
                x = bg_public.getWidth() - slipper_btn.getWidth();
            } else {
                x = 0;
            }
        }

        // 滑块异常处理
        if (x < 0) {
            x = 0;
        } else if (x > bg_public.getWidth() - slipper_btn.getWidth()) {
            x = bg_public.getWidth() - slipper_btn.getWidth();
        }

        // 画出滑块
        canvas.drawBitmap(slipper_btn, x, 0, paint);
    }

    /**
     * 初始化操作
     */
    private void init() {

        // 载入图片资源
        bg_private = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_switch_public);
        bg_public = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_switch_private);
        slipper_btn = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_switch_whitebtn);
//         bg_private.setWidth(50);
//         bg_private.setHeight(30);
//         bg_public.setWidth(50);
//         bg_private.setHeight(30);
//         slipper_btn.setWidth(50);
//         slipper_btn.setHeight(30);

        setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (event.getX() > bg_private.getWidth()
                        || event.getY() > bg_private.getHeight()) {
                    return false;
                } else {
                    onSlip = true;
                    downX = event.getX();
                    nowX = downX;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                nowX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                onSlip = false;
                if (event.getX() >= bg_public.getWidth() / 2) {
                    nowStatus = true;
                    nowX = bg_public.getWidth() - slipper_btn.getWidth();
                } else {
                    nowStatus = false;
                    nowX = 0;
                }
                if (listener != null) {
                    listener.onChanged(this, nowStatus);
                }
                break;
            default:
                break;
        }
        // 刷新界面
        invalidate();
        return true;
    }

    /**
     * 设置开关初始状态，供外部调用
     *
     * @param checked
     */
    public void setChecked(boolean checked) {

        if (checked) {
            nowX = bg_private.getWidth();
        } else {
            nowX = 0;
        }
        nowStatus = checked;
    }

    /**
     * 定义回调接口
     *
     * @author atom
     *
     */
    public interface OnChangedListener {

        public void onChanged(WiperSwitch wiperSwitch, boolean checkState);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 这里要计算一下控件的实际大小，然后调用setMeasuredDimension来设置
        int width = this.getMeasuredSize(widthMeasureSpec, true);
        int height = this.getMeasuredSize(heightMeasureSpec, false);
        setMeasuredDimension(width, height);
    }

    /**
     * 计算控件的实际大小
     *
     * @param length
     *            onMeasure方法的参数，widthMeasureSpec或者heightMeasureSpec
     * @param isWidth
     *            是宽度还是高度
     * @return int 计算后的实际大小
     */
    private int getMeasuredSize(int length, boolean isWidth) {
        // 模式
        int specMode = MeasureSpec.getMode(length);
        // 尺寸
        int specSize = MeasureSpec.getSize(length);
        // 计算所得的实际尺寸，要被返回
        int retSize = 0;
        // 得到两侧的padding（留边）
        int padding = (isWidth ? getPaddingLeft() + getPaddingRight()
                : getPaddingTop() + getPaddingBottom());

        // 对不同的指定模式进行判断
        if (specMode == MeasureSpec.EXACTLY) { // 显式指定大小，如40dp或fill_parent
            retSize = specSize;
        } else { // 如使用wrap_content
            retSize = (isWidth ? bg_private.getWidth() + padding : bg_private
                    .getHeight() + padding);
            if (specMode == MeasureSpec.UNSPECIFIED) {
                retSize = Math.min(retSize, specSize);
            }
        }

        return retSize;
    }

}
