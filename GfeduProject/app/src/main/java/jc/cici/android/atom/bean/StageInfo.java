package jc.cici.android.atom.bean;

import java.util.List;

/**
 * 阶段信息
 * Created by atom on 2017/5/16.
 */

public class StageInfo {

    //  班级名称
    private String ClassName;
    // 项目名称
    private String ProName;
    // 考试日期
    private String ExamDate;
    // 倒计时
    private String Countdown;
    // 学习进度
    private String StudyPercent;
    // 阶段内容
    private List<CardItem> StageList;

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getProName() {
        return ProName;
    }

    public void setProName(String proName) {
        ProName = proName;
    }

    public String getExamDate() {
        return ExamDate;
    }

    public void setExamDate(String examDate) {
        ExamDate = examDate;
    }

    public String getCountdown() {
        return Countdown;
    }

    public void setCountdown(String countdown) {
        Countdown = countdown;
    }

    public String getStudyPercent() {
        return StudyPercent;
    }

    public void setStudyPercent(String studyPercent) {
        StudyPercent = studyPercent;
    }

    public List<CardItem> getStageList() {
        return StageList;
    }

    public void setStageList(List<CardItem> stageList) {
        StageList = stageList;
    }
}
