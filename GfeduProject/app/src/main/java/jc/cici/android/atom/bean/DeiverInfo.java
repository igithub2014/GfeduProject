package jc.cici.android.atom.bean;

/**
 * 设备信息
 * Created by atom on 2017/4/18.
 */

public class DeiverInfo {
    // 返回码
    private String Code;
    // 返回提示消息
    private String Message;
    // 消息体
    private User Body;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public User getBody() {
        return Body;
    }

    public void setBody(User body) {
        Body = body;
    }
}
