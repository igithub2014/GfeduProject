package jc.cici.android.atom.bean;

/**
 * 找回密码
 * Created by atom on 2017/4/19.
 */

public class PasswordInfo {

    // 类型
    private String Type;
    // 验证码
    private String Msg;
    // 用户名
    private String SName;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getSName() {
        return SName;
    }

    public void setSName(String SName) {
        this.SName = SName;
    }
}
