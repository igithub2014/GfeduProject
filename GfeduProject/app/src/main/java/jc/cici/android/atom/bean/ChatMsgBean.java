package jc.cici.android.atom.bean;

/**
 * 消息实体
 * Created by atom on 2017/6/16.
 */

public class ChatMsgBean {
    // 判断位置(0:居左，1:居右)
    private int loacationFlag;
    // 用户图像
    private String headImg;
    // 用户名
    private String nikeName;
    // 发送图片
    private String sendImg;
    // 发送文字
    private String msgContent;

    public int getLoacationFlag() {
        return loacationFlag;
    }

    public void setLoacationFlag(int loacationFlag) {
        this.loacationFlag = loacationFlag;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getSendImg() {
        return sendImg;
    }

    public void setSendImg(String sendImg) {
        this.sendImg = sendImg;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    private String nikeImg; // 用户图像
    private String name;//昵称

    private String date;//时间

    private String text;//msg content

    private String chatImg; // 消息图片

    private int talkerId;//msg接收人id

    private int status;//网络连接状态(0:连接成功,非0:连接失败)

    private int fromId;//msg发起人id

    public String getNikeImg() {
        return nikeImg;
    }

    public void setNikeImg(String nikeImg) {
        this.nikeImg = nikeImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChatImg() {
        return chatImg;
    }

    public void setChatImg(String chatImg) {
        this.chatImg = chatImg;
    }

    public int getTalkerId() {
        return talkerId;
    }

    public void setTalkerId(int talkerId) {
        this.talkerId = talkerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }
}
