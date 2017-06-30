package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 问题实体
 * Created by atom on 2017/6/25.
 */

public class QuesDetailBean {

    // 问题id
    private int QuesId;
    // 问题内容
    private String QuesContent;
    // 问题添加内容
    private String QuesAddTime;
    //  问题状态
    private String QuesStatus;
    // 问题所属科目名称
    private String QuesSubjectName;
    // 上传图片个数
    private int ImageCount;
    // 上传图片
    private ArrayList<String> QuesImageUrl;
    // 用户昵称
    private String NickName;
    // 视频id
    private int VideoId;
    // 视频名称
    private String VideoName;
    // 回答数量
    private int AnswerListCount;
    // 回答总页数
    private int TotalPageIndex;
    // 回答列表
    private ArrayList<Answer> AnswerList;

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

    public String getQuesStatus() {
        return QuesStatus;
    }

    public void setQuesStatus(String quesStatus) {
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

    public int getVideoId() {
        return VideoId;
    }

    public void setVideoId(int videoId) {
        VideoId = videoId;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public int getAnswerListCount() {
        return AnswerListCount;
    }

    public void setAnswerListCount(int answerListCount) {
        AnswerListCount = answerListCount;
    }

    public int getTotalPageIndex() {
        return TotalPageIndex;
    }

    public void setTotalPageIndex(int totalPageIndex) {
        TotalPageIndex = totalPageIndex;
    }

    public ArrayList<Answer> getAnswerList() {
        return AnswerList;
    }

    public void setAnswerList(ArrayList<Answer> answerList) {
        AnswerList = answerList;
    }
}
