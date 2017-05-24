package jc.cici.android.atom.bean;

import java.util.List;

/**
 * 课程信息
 * Created by atom on 2017/5/17.
 */

public class Course {

    // 日期
    private String Date;
    private List<CourseInfo> LessonData;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public List<CourseInfo> getLessonData() {
        return LessonData;
    }

    public void setLessonData(List<CourseInfo> lessonData) {
        LessonData = lessonData;
    }
}
