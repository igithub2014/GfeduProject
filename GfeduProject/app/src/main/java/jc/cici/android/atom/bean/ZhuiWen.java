package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 具体追问(追答)内容
 * Created by atom on 2017/6/26.
 */

public class ZhuiWen {

    // 追问(追答)id
    private int id;
    // 追问(追答)内容
    private String Content;
    // 追问(追答)图片
    private String ImageUrl;
    // 用户id
    private int StudentID;
    // 用户昵称 A用户 回复 B用户     昵称：A用户
    private String NickName;
    // 用户昵称 A用户 回复 B用户     昵称：B用户
    private String ToNickName;
    // 用户图像
    private String HeadImg;
    // 添加时间
    private String AddTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public int getStudentID() {
        return StudentID;
    }

    public void setStudentID(int studentID) {
        StudentID = studentID;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getToNickName() {
        return ToNickName;
    }

    public void setToNickName(String toNickName) {
        ToNickName = toNickName;
    }

    public String getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(String headImg) {
        HeadImg = headImg;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }
}
