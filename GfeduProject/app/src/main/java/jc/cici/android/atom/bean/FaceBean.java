package jc.cici.android.atom.bean;

/**
 * 面授实体
 * Created by atom on 2017/6/8.
 */

public class FaceBean {

    private String date;
    private String course;

    public FaceBean(String date,String course){
        this.date = date;
        this.course = course;
    }
    public String getDate() {
        return date;
    }


    public String getCourse() {
        return course;
    }

}
