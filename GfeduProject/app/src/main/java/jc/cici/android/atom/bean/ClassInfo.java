package jc.cici.android.atom.bean;

import java.util.List;

/**
 * 班级信息
 * Created by atom on 2017/5/15.
 */

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
