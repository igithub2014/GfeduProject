package jc.cici.android.atom.bean;

import java.util.List;

/**
 * 课程信息
 * Created by atom on 2017/5/19.
 */

public class LessonInfo {

    // 章节课程名称
    private String LessonName;
    // 课程类型
    private int LessonType;
    // 课程日期
    private String LessonDate;
    // 课程时段(上午,中午,下午)
    private int LessonDateType;
    // 课程开始时间
    private String LessonStartTime;
    // 课程结束时间
    private String LessonEndTime;
    // 授课老师姓名
    private String TeacherName;
    // 授课老师图片
    private String TeacherImg;
    // 授课地点
    private String LessonPlace;
    // 出勤率
    private int AttendanceStatus;
    // 回看列表
    private List<ReplayInfo> ReplayVideoList;
    // 测试列表
    private List<TestPagerInfo> TestPaperList;
    // 下载列表
    private List<DownloadInfo> DownloadList;

    public String getLessonName() {
        return LessonName;
    }

    public void setLessonName(String lessonName) {
        LessonName = lessonName;
    }

    public int getLessonType() {
        return LessonType;
    }

    public void setLessonType(int lessonType) {
        LessonType = lessonType;
    }

    public String getLessonDate() {
        return LessonDate;
    }

    public void setLessonDate(String lessonDate) {
        LessonDate = lessonDate;
    }

    public int getLessonDateType() {
        return LessonDateType;
    }

    public void setLessonDateType(int lessonDateType) {
        LessonDateType = lessonDateType;
    }

    public String getLessonStartTime() {
        return LessonStartTime;
    }

    public void setLessonStartTime(String lessonStartTime) {
        LessonStartTime = lessonStartTime;
    }

    public String getLessonEndTime() {
        return LessonEndTime;
    }

    public void setLessonEndTime(String lessonEndTime) {
        LessonEndTime = lessonEndTime;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getTeacherImg() {
        return TeacherImg;
    }

    public void setTeacherImg(String teacherImg) {
        TeacherImg = teacherImg;
    }

    public String getLessonPlace() {
        return LessonPlace;
    }

    public void setLessonPlace(String lessonPlace) {
        LessonPlace = lessonPlace;
    }

    public int getAttendanceStatus() {
        return AttendanceStatus;
    }

    public void setAttendanceStatus(int attendanceStatus) {
        AttendanceStatus = attendanceStatus;
    }

    public List<ReplayInfo> getReplayVideoList() {
        return ReplayVideoList;
    }

    public void setReplayVideoList(List<ReplayInfo> replayVideoList) {
        ReplayVideoList = replayVideoList;
    }

    public List<TestPagerInfo> getTestPaperList() {
        return TestPaperList;
    }

    public void setTestPaperList(List<TestPagerInfo> testPaperList) {
        TestPaperList = testPaperList;
    }

    public List<DownloadInfo> getDownloadList() {
        return DownloadList;
    }

    public void setDownloadList(List<DownloadInfo> downloadList) {
        DownloadList = downloadList;
    }
}
