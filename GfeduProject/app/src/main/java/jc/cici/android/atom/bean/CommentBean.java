package jc.cici.android.atom.bean;

/**
 * 评论实体
 * Created by atom on 2017/6/17.
 */

public class CommentBean {

    private String ReplyID; // 回复id
    private String ReplyUserName; // 评论用户名
    private String ReplyUserImg; // 评论图片
    private String ReplyReplyName; // 评论回复用户名
    private String ReplyContent; // 评论回复内容
    private String ReplyAddTime; // 评论回复添加时间
    private String ReplyPraiseCount; // 评论回复点赞数量
    private String ReplyIsPraise; // 评论回复是否点赞
    private String ReplyIsSimple; // 是否直接回复(1:表示直接回复人,0:表示其他人对评论人回复)

    public String getReplyUserImg() {
        return ReplyUserImg;
    }

    public void setReplyUserImg(String replyUserImg) {
        ReplyUserImg = replyUserImg;
    }

    public String getReplyID() {
        return ReplyID;
    }

    public void setReplyID(String replyID) {
        ReplyID = replyID;
    }

    public String getReplyUserName() {
        return ReplyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        ReplyUserName = replyUserName;
    }

    public String getReplyReplyName() {
        return ReplyReplyName;
    }

    public void setReplyReplyName(String replyReplyName) {
        ReplyReplyName = replyReplyName;
    }

    public String getReplyContent() {
        return ReplyContent;
    }

    public void setReplyContent(String replyContent) {
        ReplyContent = replyContent;
    }

    public String getReplyAddTime() {
        return ReplyAddTime;
    }

    public void setReplyAddTime(String replyAddTime) {
        ReplyAddTime = replyAddTime;
    }

    public String getReplyPraiseCount() {
        return ReplyPraiseCount;
    }

    public void setReplyPraiseCount(String replyPraiseCount) {
        ReplyPraiseCount = replyPraiseCount;
    }

    public String getReplyIsPraise() {
        return ReplyIsPraise;
    }

    public void setReplyIsPraise(String replyIsPraise) {
        ReplyIsPraise = replyIsPraise;
    }

    public String getReplyIsSimple() {
        return ReplyIsSimple;
    }

    public void setReplyIsSimple(String replyIsSimple) {
        ReplyIsSimple = replyIsSimple;
    }
}
