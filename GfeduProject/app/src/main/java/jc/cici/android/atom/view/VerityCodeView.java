package jc.cici.android.atom.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * 创建图形验证码
 * Created by atom on 2017/4/5.
 */

public class VerityCodeView {

    // 随机数组
    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z'
    };
    // 定义VirityCodeView 对象
    private static VerityCodeView verityCodeView;

    // 获取单例
    public static VerityCodeView getInstance() {
        if (verityCodeView == null) {
            verityCodeView = new VerityCodeView();
        }
        return verityCodeView;
    }

    // 验证码默认随机个数
    private static final int DEFAULT_INPUT_COUNT = 4;
    // 验证码默认字体大小
    private static final int DEFAULT_INPUT_SIZE = 25;
    // 验证码默认线条个数
    private static final int DEFAULT_LINE_COUNT = 5;
    // padding
    private static final int BASE_PADDING_LEFT = 10, // 左边距
            RANGE_PADDING_LEFT = 10, // 左边距范围
            BASE_PADDING_TOP = 10, // 上边距
            RANGE_PADDING_TOP = 10; // 上边距范围
    // 验证码默认宽高
    private static final int DEFAULT_WIDTH = 100,
            DEFAULT_HEIGHT = 40;
    // 画布默认宽度高度
    private int width = DEFAULT_WIDTH,
            height = DEFAULT_HEIGHT;
    // 画布范围设置
    private int base_padding_left = BASE_PADDING_LEFT,
            range_padding_left = RANGE_PADDING_LEFT,
            base_padding_top = BASE_PADDING_TOP,
            range_padding_top = RANGE_PADDING_TOP;
    // 验证码范围设置
    private int verityCodeView_length = DEFAULT_INPUT_COUNT,
            line_count = DEFAULT_LINE_COUNT,
            font_size = DEFAULT_INPUT_SIZE;
    // 验证码字符
    private String code;
    private int padding_left,
            padding_top;
    // 随机数对象
    private Random random = new Random();

    public String getCode() {
        return code;
    }

    /**
     * 生成验证码bitMap
     *
     * @return
     */
    public Bitmap createBitmap() {

        padding_left = 0;
        padding_top = 18;
        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bp);
        // 生成验证码，并赋值给code
        code = createCode();
        // 画布颜色
        canvas.drawColor(Color.WHITE);
        // 定义画笔
        Paint paint = new Paint();
        // 画笔防止锯齿设置
        paint.setAntiAlias(true);
        // 设置字体大小
        paint.setTextSize(font_size);
        // 画验证码
        for (int i = 0; i < code.length(); i++) {
            // 设置验证码字体样式风格
            randomTextStyle(paint);
            // 设置间距
            randomPadding();
            canvas.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
        }
//        // 画线条
//        for (int i = 0; i < line_count; i++) {
//            drawLine(canvas, paint);
//        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bp;
    }

    /**
     * 画干扰线
     *
     * @param canvas
     * @param paint
     */
    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(width);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    /**
     * 设置间距
     */
    private void randomPadding() {
        padding_left += base_padding_left + random.nextInt(range_padding_left);
        padding_top ++;
    }

    /**
     * 随机生成文字样式，颜色，粗细，倾斜度
     *
     * @param paint
     */
    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean()); // true 为粗体，false 为非粗体
//        // 设置斜线标示
//        float skewX = random.nextInt(11) / 10;
//        skewX = random.nextBoolean() ? skewX : -skewX;
//        paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
//        //paint.setUnderlineText(true); //true为下划线，false为非下划线
//        paint.setStrikeThruText(true); //true为删除线，false为非删除线

    }

    /**
     * 随机生成颜色
     *
     * @return
     */
    private int randomColor() {
        return randomColor(1);
    }

    private int randomColor(int rate) {
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    /**
     * 生成验证码
     *
     * @return
     */
    private String createCode() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < verityCodeView_length; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }
}
