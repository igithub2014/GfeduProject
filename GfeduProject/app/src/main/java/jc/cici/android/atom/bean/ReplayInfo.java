package jc.cici.android.atom.bean;

/**
 * 视频回看信息
 * Created by atom on 2017/5/19.
 */

public class ReplayInfo {

    // 视频id
    private int VideoID;
    // 视频名称
    private String VideoName;
    // 视频时长
    private String VideoTime;
    // 视频图片
    private String VideoImg;

    public int getVideoID() {
        return VideoID;
    }

    public void setVideoID(int videoID) {
        VideoID = videoID;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getVideoTime() {
        return VideoTime;
    }

    public void setVideoTime(String videoTime) {
        VideoTime = videoTime;
    }

    public String getVideoImg() {
        return VideoImg;
    }

    public void setVideoImg(String videoImg) {
        VideoImg = videoImg;
    }
}
