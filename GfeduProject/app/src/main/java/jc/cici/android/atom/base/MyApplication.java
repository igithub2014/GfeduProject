package jc.cici.android.atom.base;

import android.app.Application;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;

import jc.cici.android.atom.exception.CrashException;

/**
 * 统一app管理类
 * Created by atom on 2017/3/27.
 */

public class MyApplication extends Application {

    // 存放登录信息
    private HashMap<String, Object> sharePreferenceInfos;
    //创建MyApplication对象
    private static MyApplication application;
    // 获取实例
    public static MyApplication getInstance() {
        return application;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // 异常处理
        CrashException.getInstance().init(getApplicationContext());
        UMShareAPI.get(this);
    }
    {
        PlatformConfig.setWeixin("wx47b381963fec27aa","d4624c36b6795d1d99dcf0547af5443d");
        PlatformConfig.setQQZone("1104520651","xCNiMO2Fw1iyU1VU");
        PlatformConfig.setSinaWeibo("1673809322","99879f5875e9169770aa5f4c46eb7f01","http://www.gfedu.com/");
    }

    public HashMap<String, Object> getSharePreferenceInfos() {
        return sharePreferenceInfos;
    }

    public void setSharePreferenceInfos(HashMap<String, Object> sharePreferenceInfos) {
        this.sharePreferenceInfos = sharePreferenceInfos;
    }
}
