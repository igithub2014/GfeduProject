package jc.cici.android.atom.bean;

/**
 * 历史课程
 * Created by atom on 2017/5/13.
 */

public class HistoryLesson {

    // 课时id
    private int LessonId;
    // 课时名
    private String LessonName;
    // 课程类型
    private int LessonType;
    // 课时日期
    private String LessonDate;
    // 课时时段
    private String LessonDateType;
    // 开始时间
    private String LessonStartTime;
    // 结束时间
    private String LessonEndTime;
    // 出勤状态
    private int AttendanceStatus;

    public int getLessonId() {
        return LessonId;
    }

    public void setLessonId(int lessonId) {
        LessonId = lessonId;
    }

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

    public String getLessonDateType() {
        return LessonDateType;
    }

    public void setLessonDateType(String lessonDateType) {
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

    public int getAttendanceStatus() {
        return AttendanceStatus;
    }

    public void setAttendanceStatus(int attendanceStatus) {
        AttendanceStatus = attendanceStatus;
    }

}
