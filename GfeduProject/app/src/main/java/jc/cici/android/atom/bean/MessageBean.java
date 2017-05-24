package jc.cici.android.atom.bean;

/**
 * 短信返回信息
 * Created by atom on 2017/4/25.
 */

public class MessageBean {

    // 返回状态值
    private int Code;
    // 返回消息
    private String Message;
    // 返回内容
    private String Body;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }
}
