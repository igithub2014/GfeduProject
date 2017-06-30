//package jc.cici.android.atom.ui.tiku;
//
//import android.content.Context;
//import android.content.pm.PackageManager.NameNotFoundException;
//
//public class CommonParams {
//
//	private String Client; // 客户来源
//	private String Version; // 版本号
//	private int UserId; // 用户id
//	private String TimeStamp; // 当前时间戳
//	private String oauth; // md5加密
//
//	public CommonParams(Context ctx) {
//
//		Client = "android";
//		// Version ="1.0";
//		// TODO 替换 用户id
//		try {
//			Version = Tool.getVesion(ctx);
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		// TODO 替换 用户id
//		int userid = Tool.getUserId(ctx);
//		UserId = userid;
//		// UserId = 115227;
//		TimeStamp = Tool.getCurrentTime();
//		// 获取加密字符串
//		String md5Str = UserId + TimeStamp + "android!%@%$@#$";
//		oauth = Tool.getMD5Str(md5Str);
//	}
//
//	public String getClient() {
//		return Client;
//	}
//
//	public String getVersion() {
//		return Version;
//	}
//
//	public int getUserId() {
//		return UserId;
//	}
//
//	public String getTimeStamp() {
//		return TimeStamp;
//	}
//
//	public String getOauth() {
//		return oauth;
//	}
//
//}
