package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 问题列表内容
 * Created by atom on 2017/6/24.
 */

public class Question{

    // 问题id
    private int QuesId;
    // 问题内容
    private String QuesContent;
    //  问题添加时间
    private String QuesAddTime;
    //  问题状态(1.未回答  2.已回答  3.已解决)
    private int QuesStatus;
    // 科目
    private String QuesSubjectName;
    // 图片数量
    private int ImageCount;
    // 问题图片
    private ArrayList<String> QuesImageUrl;
    // 用户昵称
    private String NickName;
    // 用户图像
    private String HeadImg;

    public String getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(String headImg) {
        HeadImg = headImg;
    }

    public int getQuesId() {
        return QuesId;
    }

    public void setQuesId(int quesId) {
        QuesId = quesId;
    }

    public String getQuesContent() {
        return QuesContent;
    }

    public void setQuesContent(String quesContent) {
        QuesContent = quesContent;
    }

    public String getQuesAddTime() {
        return QuesAddTime;
    }

    public void setQuesAddTime(String quesAddTime) {
        QuesAddTime = quesAddTime;
    }

    public int getQuesStatus() {
        return QuesStatus;
    }

    public void setQuesStatus(int quesStatus) {
        QuesStatus = quesStatus;
    }

    public String getQuesSubjectName() {
        return QuesSubjectName;
    }

    public void setQuesSubjectName(String quesSubjectName) {
        QuesSubjectName = quesSubjectName;
    }

    public int getImageCount() {
        return ImageCount;
    }

    public void setImageCount(int imageCount) {
        ImageCount = imageCount;
    }

    public ArrayList<String> getQuesImageUrl() {
        return QuesImageUrl;
    }

    public void setQuesImageUrl(ArrayList<String> quesImageUrl) {
        QuesImageUrl = quesImageUrl;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }
}
