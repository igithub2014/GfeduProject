package jc.cici.android.atom.bean;

/**
 * 测试信息
 * Created by atom on 2017/5/19.
 */

public class TestPagerInfo {

    // 试卷id
    private int PaperID;
    // 试卷名称
    private String PaperName;
    // 试卷总时长
    private String PaperTime;

    public int getPaperID() {
        return PaperID;
    }

    public void setPaperID(int paperID) {
        PaperID = paperID;
    }

    public String getPaperName() {
        return PaperName;
    }

    public void setPaperName(String paperName) {
        PaperName = paperName;
    }

    public String getPaperTime() {
        return PaperTime;
    }

    public void setPaperTime(String paperTime) {
        PaperTime = paperTime;
    }
}
