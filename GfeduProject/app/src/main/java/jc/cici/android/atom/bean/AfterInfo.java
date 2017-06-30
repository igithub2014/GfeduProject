package jc.cici.android.atom.bean;

/**
 * 追问追答实体
 * Created by atom on 2017/6/26.
 */

public class AfterInfo {

    // 追问/追答/评论id
    private int id;
    //追问/追答/评论内容
    private String Content;
    // 图片数量
    private int ImageCount;
    // 昵称 A用户 回复 B用户     昵称：A用户
    private String NickName;
    // A用户 回复 B用户     昵称：B用户
    private String ToNickName;
    // 图像
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

    public int getImageCount() {
        return ImageCount;
    }

    public void setImageCount(int imageCount) {
        ImageCount = imageCount;
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
