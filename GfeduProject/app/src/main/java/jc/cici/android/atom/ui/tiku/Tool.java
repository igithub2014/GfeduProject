package jc.cici.android.atom.ui.tiku;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Tool {

	public static final String JsonArray_Log = "app_userInfo";

	/**
	 * 获取当前版本号
	 *
	 * @param ctx
	 * @return
	 * @throws NameNotFoundException
	 */
	public static String getVesion(Context ctx) throws NameNotFoundException {

		PackageManager manager = ctx.getPackageManager();
		PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
		String version = info.versionName;
		return version;
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

	public static int getUserId(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(JsonArray_Log,
				Activity.MODE_PRIVATE);
		int userId = sp.getInt("S_ID", 0);

		return userId;

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
	 * 字符串转整形
	 *
	 * @param str
	 * @return
	 */
	public static int integerPrase(String str) {

		int intValue = Integer.parseInt(str);
		return intValue;
	}

	/**
	 * 整形转字符串
	 *
	 * @param count
	 * @return
	 */
	public static String toStrPrase(int count) {

		return String.valueOf(count);

	}

	/**
	 *
	 * @param str
	 *            整个字符串
	 * @return
	 */
	public static SpannableStringBuilder setStringColorForPos(String str) {
		SpannableStringBuilder builder = new SpannableStringBuilder(str);

		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan nameSpan = new ForegroundColorSpan(
				Color.parseColor("#39B747"));

		builder.setSpan(nameSpan, str.lastIndexOf(",") - 4,
				str.lastIndexOf(","), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
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
	 * 字符串截取
	 *
	 * @param str
	 * @param index
	 * @return
	 */
	public static String cutString(String str, int index) {

		if (null != str && str.length() >= index) {
			return str.substring(0, index);
		}
		return null;
	}

	/**
	 * 判断手机内存是否满足需要
	 *
	 * @return
	 */
	public static boolean isEnoughForMemorySpace(Context mContext, int videoSize) {
		File sdcard_filedir = Environment.getExternalStorageDirectory();// 得到sdcard的目录作为一个文件对象
		long usableSpace = sdcard_filedir.getUsableSpace();// 获取文件目录对象剩余空间
		// 将一个long类型的文件大小格式化成用户可以看懂的M，G字符串
		String usableSpace_str = Formatter
				.formatFileSize(mContext, usableSpace);
		if (videoSize != 0) {
			if (usableSpace < videoSize) {// 判断剩余空间小于视频大小
				Toast.makeText(mContext,
						"sdcard剩余空间不足,无法满足下载；剩余空间为：" + usableSpace_str,
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}
}
