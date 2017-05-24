package jc.cici.android.atom.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理类
 * Created by atom on 2017/3/27.
 */

public class CrashException implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashException";
    // 创建单例模式
    private static CrashException crashException;

    public CrashException() {
    }

    public static CrashException getInstance() {
        if (null == crashException) {
            crashException = new CrashException();
        }
        return crashException;
    }

    // 系统默认处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // 上下文环境
    private Context mContext;
    // 存放异常信息
    private HashMap<String, String> infos = new HashMap<String, String>();
    // 格式化时间用于日志记录的一部分
    private DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统异常处理
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置CrashException 为默认异常处理类
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
            // 未调用自定义处理情况，调用系统自带异常处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }


    private boolean handleException(Throwable ex) {
        if (null == ex) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，程序出现异常!即将退出", Toast.LENGTH_LONG).show();
                Looper.loop();

            }
        }.start();
        // 收集错误信息
        collectDeviceInfos(mContext);
        // 保存日志文件
        saveCrashToFile(ex);
        // TODO 错误信息上传服务器
        return true;
    }

    /**
     * 错误信息保存到本地文件中
     *
     * @param ex
     * @return
     */
    private String saveCrashToFile(Throwable ex) {
        // 创建缓存区
        StringBuffer buffer = new StringBuffer();
        // 遍历hashMap 获取设备信息
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 把获取设备信息放入缓存区中
            buffer.append(key + "=" + value + "\n");
        }
        // 创建字符串写入对象
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        if (null != cause) {
            cause.printStackTrace();
            cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        buffer.append(result);
        long tempTime = System.currentTimeMillis();
        String time = format.format(new Date());
        String fileName = "crash" + time + "-" + tempTime + ".txt";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = "/sdcard/jc_school/crash/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(path + fileName);
                fos.write(buffer.toString().getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 搜集错误信息，保存在hashMap中
     *
     * @param context
     */
    private void collectDeviceInfos(Context context) {
        // 获取包管理器对象
        PackageManager packageManager = context.getPackageManager();
        // 获取当前活动包信息
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (null != packageInfo) {
                String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String versionCode = packageInfo.versionCode + "";
                infos.put("versionCode", versionCode);
                infos.put("versionName", versionName);
            }
            // 通过反射，获取属性信息
            Field[] fields = Build.class.getFields();
            // 遍历所有属性并设置权限
            for (Field field : fields) {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
