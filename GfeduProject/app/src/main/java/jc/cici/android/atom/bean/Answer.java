package jc.cici.android.atom.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 答案实体
 * Created by atom on 2017/6/15.
 */

public class Answer implements Serializable{

    private static final long serialVersionUID = -8817851803223281213L;

    // 问题id
    private int AnswerId;
    // 问题内容
    private String AnswerContent;
    // 问题添加时间
    private String AnswerAddTime;
    //问题状态
    private int AnswerStatus;
    // 图片数量
    private int ImageCount;
    // 图片
    private ArrayList<String> AnswerImageUrl;
    // 追问数量
    private int AfterQuesCount;
    // 评论数量
    private int CommentsCount;
    // 点赞数量
    private int PraiseCount;
    // 点赞状态：1.已赞   2.未赞
    private int UserPraiseStatus;
    // 用户昵称
    private String NickName;
    // 用户图像
    private String HeadImg;

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(String headImg) {
        HeadImg = headImg;
    }

    public int getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(int answerId) {
        AnswerId = answerId;
    }

    public String getAnswerContent() {
        return AnswerContent;
    }

    public void setAnswerContent(String answerContent) {
        AnswerContent = answerContent;
    }

    public String getAnswerAddTime() {
        return AnswerAddTime;
    }

    public void setAnswerAddTime(String answerAddTime) {
        AnswerAddTime = answerAddTime;
    }

    public int getAnswerStatus() {
        return AnswerStatus;
    }

    public void setAnswerStatus(int answerStatus) {
        AnswerStatus = answerStatus;
    }

    public int getImageCount() {
        return ImageCount;
    }

    public void setImageCount(int imageCount) {
        ImageCount = imageCount;
    }

    public ArrayList<String> getAnswerImageUrl() {
        return AnswerImageUrl;
    }

    public void setAnswerImageUrl(ArrayList<String> answerImageUrl) {
        AnswerImageUrl = answerImageUrl;
    }

    public int getAfterQuesCount() {
        return AfterQuesCount;
    }

    public void setAfterQuesCount(int afterQuesCount) {
        AfterQuesCount = afterQuesCount;
    }

    public int getCommentsCount() {
        return CommentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        CommentsCount = commentsCount;
    }

    public int getPraiseCount() {
        return PraiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        PraiseCount = praiseCount;
    }

    public int getUserPraiseStatus() {
        return UserPraiseStatus;
    }

    public void setUserPraiseStatus(int userPraiseStatus) {
        UserPraiseStatus = userPraiseStatus;
    }
}
