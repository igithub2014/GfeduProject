package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * Created by atom on 2017/6/15.
 */

public class QuestionContentBean {

    private String QuesID; // 问题id
    private String UserID; // 学员id
    private String UserName; // 学员名
    private String UserImg; // 学员头像
    private String QuesContent; // 问题内容
    private String QuesAddTime; // 上传时间
    private String ClassName; // 班级名
    private String CourseName; // 课程名
    private String LessionName; // 章节名
    private String QuesReplyCount; // 回复数量
    private String QuesPraiseCount; // 点赞数量
    private String QuesIsPraise; // 是否已被点赞
    private String QTBClassID; // 班级id
    private String QTBLessionID; // 班级对应课程id
    private String QuesIsReprint; // 是否点赞
    private String ReprintMyID; // 收藏id
    private String QuesIsCollect; // 是否被收藏
    private String FarCourseName; // 阶段名称
    private String QuesStatus; // 问题状态(0:表示未回答 ,1:表示未解决,2:表示已解决)
    private ArrayList<String> QuesImgs; // 回答图片列表
    private String DetailLink; // web链接地址

    public String getQuesID() {
        return QuesID;
    }

    public void setQuesID(String quesID) {
        QuesID = quesID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserImg() {
        return UserImg;
    }

    public void setUserImg(String userImg) {
        UserImg = userImg;
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

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getLessionName() {
        return LessionName;
    }

    public void setLessionName(String lessionName) {
        LessionName = lessionName;
    }

    public String getQuesReplyCount() {
        return QuesReplyCount;
    }

    public void setQuesReplyCount(String quesReplyCount) {
        QuesReplyCount = quesReplyCount;
    }

    public String getQuesPraiseCount() {
        return QuesPraiseCount;
    }

    public void setQuesPraiseCount(String quesPraiseCount) {
        QuesPraiseCount = quesPraiseCount;
    }

    public String getQuesIsPraise() {
        return QuesIsPraise;
    }

    public void setQuesIsPraise(String quesIsPraise) {
        QuesIsPraise = quesIsPraise;
    }

    public String getQTBClassID() {
        return QTBClassID;
    }

    public void setQTBClassID(String QTBClassID) {
        this.QTBClassID = QTBClassID;
    }

    public String getQTBLessionID() {
        return QTBLessionID;
    }

    public void setQTBLessionID(String QTBLessionID) {
        this.QTBLessionID = QTBLessionID;
    }

    public String getQuesIsReprint() {
        return QuesIsReprint;
    }

    public void setQuesIsReprint(String quesIsReprint) {
        QuesIsReprint = quesIsReprint;
    }

    public String getReprintMyID() {
        return ReprintMyID;
    }

    public void setReprintMyID(String reprintMyID) {
        ReprintMyID = reprintMyID;
    }

    public String getQuesIsCollect() {
        return QuesIsCollect;
    }

    public void setQuesIsCollect(String quesIsCollect) {
        QuesIsCollect = quesIsCollect;
    }

    public String getFarCourseName() {
        return FarCourseName;
    }

    public void setFarCourseName(String farCourseName) {
        FarCourseName = farCourseName;
    }

    public String getQuesStatus() {
        return QuesStatus;
    }

    public void setQuesStatus(String quesStatus) {
        QuesStatus = quesStatus;
    }

    public ArrayList<String> getQuesImgs() {
        return QuesImgs;
    }

    public void setQuesImgs(ArrayList<String> quesImgs) {
        QuesImgs = quesImgs;
    }

    public String getDetailLink() {
        return DetailLink;
    }

    public void setDetailLink(String detailLink) {
        DetailLink = detailLink;
    }
}
