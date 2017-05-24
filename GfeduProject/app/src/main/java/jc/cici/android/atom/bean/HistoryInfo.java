package jc.cici.android.atom.bean;

import java.util.List;

/**
 * 历史课程信息
 * Created by atom on 2017/5/13.
 */

public class HistoryInfo {
    // 完成课时
    private String FinishedPeriod;
    // 出勤率
    private String AttendanceRate;
    // 历史课程列表
    private List<HistoryLesson> HistoryLessonList;

    public String getFinishedPeriod() {
        return FinishedPeriod;
    }

    public void setFinishedPeriod(String finishedPeriod) {
        FinishedPeriod = finishedPeriod;
    }

    public String getAttendanceRate() {
        return AttendanceRate;
    }

    public void setAttendanceRate(String attendanceRate) {
        AttendanceRate = attendanceRate;
    }

    public List<HistoryLesson> getHistoryLessonList() {
        return HistoryLessonList;
    }

    public void setHistoryLessonList(List<HistoryLesson> historyLessonList) {
        HistoryLessonList = historyLessonList;
    }

}
