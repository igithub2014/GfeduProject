package jc.cici.android.atom.bean;

import java.util.List;

/**
 * 班级信息
 * Created by atom on 2017/5/15.
 */

public class ClassInfoBean {

    private int Code;
    private String Message;
    private ClassInfo Body;

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

    public ClassInfo getBody() {
        return Body;
    }

    public void setBody(ClassInfo body) {
        Body = body;
    }

    public class ClassInfo {
        // 学习天数
        private int StudyDayCount;
        // 小贴士
        private String StudySlogan;
        // 学习信息
        private List<StudyBean> ClassList;

        public int getStudyDayCount() {
            return StudyDayCount;
        }

        public void setStudyDayCount(int studyDayCount) {
            StudyDayCount = studyDayCount;
        }

        public String getStudySlogan() {
            return StudySlogan;
        }

        public void setStudySlogan(String studySlogan) {
            StudySlogan = studySlogan;
        }

        public List<StudyBean> getClassList() {
            return ClassList;
        }

        public void setClassList(List<StudyBean> classList) {
            ClassList = classList;
        }
    }

}
