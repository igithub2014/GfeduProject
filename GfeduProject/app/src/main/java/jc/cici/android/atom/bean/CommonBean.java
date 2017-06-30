package jc.cici.android.atom.bean;

/**
 * 公共返回参数
 * Created by atom on 2017/6/20.
 */

public class CommonBean<T> {

    private int Code;
    private String Message;
    private T Body;

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

    public T getBody() {
        return Body;
    }

    public void setBody(T body) {
        Body = body;
    }
}
