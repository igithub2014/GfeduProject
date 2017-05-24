package jc.cici.android.atom.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

/**
 * 工具类
 * Created by atom on 2017/4/11.
 */

public class ToolUtils {

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    public static String strReplaceAll(String str) {
        if (null != str && !"".equals(str)) {
            return str.replaceAll("&nbsp;", " ").replaceAll("&amp", "&").replaceAll("<br>", "\n");
        }
        return null;
    }

    /**
     * 手机号验证
     *
     * @param mobiles 手机号
     * @return (true:正确, false:错误)
     */
    public static boolean isMobileNo(String mobiles) {
        Pattern pattern = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
        Matcher m = pattern.matcher(mobiles);
        return m.matches();
    }

    /**
     * 密码设置验证
     *
     * @param pwd 密码
     * @return (true:正确, false:错误)
     */
    public static boolean isRightPwd(String pwd) {
        Pattern pattern = Pattern.compile("^(?![^a-zA-Z]+$)(?!\\D+$)[0-9a-zA-Z]{8,16}$");
        Matcher m = pattern.matcher(pwd);
        return m.matches();
    }

    /**
     * 邮箱格式验证
     *
     * @param email 邮箱号
     * @return (true:正确, false:错误)
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = pattern.matcher(email);
        return m.matches();
    }

    /**
     * 判断网址是否有效
     */
    public static boolean isLinkAvailable(String link) {
        Pattern pattern = Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断密码为8到15位数字与字符组成
     */
    public static boolean isPWDAvailable(String pwd) {
        Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,15}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pwd);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 获取UUID
     *
     * @param ctx
     * @return
     */
    public static String getUUID(Context ctx) {
        final TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        System.out.println("tmDevice - " + tmDevice);
        System.out.println("tmSerial - " + tmSerial);

        // UUID
        androidId = ""
                + android.provider.Settings.Secure.getString(
                ctx.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        System.out.println("--- " + androidId.hashCode());

        // 加密UUID
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

        System.out.println("deviceUuid - " + deviceUuid);

        String uniqueId = deviceUuid.toString();

        return uniqueId;
    }

    /**
     * 获取当前版本号
     *
     * @param ctx
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static String getVesion(Context ctx) throws PackageManager.NameNotFoundException {

        PackageManager manager = ctx.getPackageManager();
        PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
        String version = info.versionName;
        return version;
    }

    /*
     * MD5加密
	 */
    @SuppressWarnings("unused")
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        // 16位加密，从第9位到25位
        return md5StrBuff.substring(8, 24).toString().toUpperCase();
    }

    /**
     * 获取当前系统时间戳
     *
     * @return
     */
    public static String getCurrentTime() {
        Date dt = new Date();
        Long time = dt.getTime();
        long min = time / 1000;
        String minStr = String.valueOf(min);
        return minStr;
    }

    /**
     * 获取用户id
     *
     * @param ctx
     * @return
     */
    public static int getUserID(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(Global.LOGIN_FLAG,
                Activity.MODE_PRIVATE);
        int userId = sp.getInt("S_ID", 0);

        return userId;
    }

    /**
     * 字符串替换
     *
     * @param str
     * @return
     */
    public static String replaceAllChar(String str) {

        return str.replaceAll("&nbsp;", " ").replaceAll("&amp;", "&")
                .replaceAll("<br/>", "\n");
    }

    /**
     * TextView 文字大小设置
     *
     * @param ctx
     * @param str
     * @param style1
     * @param style2
     * @return
     */
    public static SpannableString setTextSize(Context ctx, String str,int start,int end,int style1,int style2) {
        SpannableString styledText = new SpannableString(str);
        styledText.setSpan(new TextAppearanceSpan(ctx,style1), 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(ctx,style2), start+1, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return styledText;
    }

    /**
     *  判断当前时间是否为今天
     * @param day
     * @return
     */
    public static boolean isCurrentDay(String day) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    /**
     * 判断是否是当前时间段
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isCurrentTimeDuring(String startTime,String endTime){

        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
        String[] startStr =startTime.split(":");
        String [] endStr = endTime.split(":");
        final int start = Integer.parseInt(startStr[0]) * 60 + Integer.parseInt(startStr[1]);// 起始时间
        final int end =  Integer.parseInt(endStr[0]) * 60+Integer.parseInt(endStr[1]);// 结束时间
        if (minuteOfDay >= start && minuteOfDay <= end) {
            return true;
        } else {
           return false;
        }
    }
}
