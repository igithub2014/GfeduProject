package jc.cici.android.atom.bean;

import java.util.List;

/**
 * 阶段课程信息
 * Created by atom on 2017/5/17.
 */

public class LessInfo {
    private int Code;
    private List<Course> Body;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public List<Course> getBody() {
        return Body;
    }

    public void setBody(List<Course> body) {
        Body = body;
    }
}
