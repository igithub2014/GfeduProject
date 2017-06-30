package jc.cici.android.atom.common;

import android.content.Context;
import android.content.pm.PackageManager;

import jc.cici.android.atom.utils.ToolUtils;

/**
 * 请求公共参数
 * Created by atom on 2017/4/18.
 */

public class CommParam {
    private String client;
    private String deviceid;
    private String appname;
    private String version;
    private String TimeStamp;
    private String oauth;
    private int UserId;

    public CommParam(Context ctx){
        setClient(Global.CLIENT);
        try {
            setVersion( ToolUtils.getVesion(ctx));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setAppname(Global.APPNAME);
        setDeviceid(ToolUtils.getUUID(ctx));
        setTimeStamp(ToolUtils.getCurrentTime());
        // 测试数据
        setUserId(ToolUtils.getUserID(ctx));
        // 设置加密
        setOauth(ToolUtils.getMD5Str(UserId + TimeStamp + "android!%@%$@#$"));
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getOauth() {
        return oauth;
    }

    public void setOauth(String oauth) {
        this.oauth = oauth;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }
}
