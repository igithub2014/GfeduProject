package jc.cici.android.atom.bean;

/**
 * 面授列表内容
 * Created by atom on 2017/5/5.
 */

public class StudyBean {

    // 用户id
    private int ClassId;
    // 课程列表图片
    private String ClassImg;
    // 班级名称
    private String ClassName;
    // 节数据
    private int ClassIsGift;
    // 课程开始时间
    private String ClassStartTime;
    // 课程结束时间
    private String ClassEndTime;
    // 班级状态( -1:欠费锁定0:未开始 1:正常 2:已过期)
    private int ClassStatus;
    // 班级类型(在线,面授,基础)
    private String ClassType;

    public int getClassId() {
        return ClassId;
    }

    public void setClassId(int classId) {
        ClassId = classId;
    }

    public String getClassImg() {
        return ClassImg;
    }

    public void setClassImg(String classImg) {
        ClassImg = classImg;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public int getClassIsGift() {
        return ClassIsGift;
    }

    public void setClassIsGift(int classIsGift) {
        ClassIsGift = classIsGift;
    }

    public String getClassStartTime() {
        return ClassStartTime;
    }

    public void setClassStartTime(String classStartTime) {
        ClassStartTime = classStartTime;
    }

    public String getClassEndTime() {
        return ClassEndTime;
    }

    public void setClassEndTime(String classEndTime) {
        ClassEndTime = classEndTime;
    }

    public int getClassStatus() {
        return ClassStatus;
    }

    public void setClassStatus(int classStatus) {
        ClassStatus = classStatus;
    }

    public String getClassType() {
        return ClassType;
    }

    public void setClassType(String classType) {
        ClassType = classType;
    }
}
