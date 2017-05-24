package jc.cici.android.atom.bean;

/**
 * 登录信息
 * Created by atom on 2017/4/17.
 */

public class UserInfo {
    private String Code;
    private String Message;
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