package jc.cici.android.atom.bean;

/**
 * 笔记详情实体
 * Created by atom on 2017/6/22.
 */

public class NoteDetailsBean {

    // 我的笔记OR大家的笔记(1:我的笔记，0:大家的笔记)
    private int IsUser;
    private NoteInfo NotesInfo;
    private VideoInfo Video;
    private TestPagerInfo TestPaper;

    public int getIsUser() {
        return IsUser;
    }

    public void setIsUser(int isUser) {
        IsUser = isUser;
    }

    public NoteInfo getNotesInfo() {
        return NotesInfo;
    }

    public void setNotesInfo(NoteInfo notesInfo) {
        NotesInfo = notesInfo;
    }

    public VideoInfo getVideo() {
        return Video;
    }

    public void setVideo(VideoInfo video) {
        Video = video;
    }

    public TestPagerInfo getTestPaper() {
        return TestPaper;
    }

    public void setTestPaper(TestPagerInfo testPaper) {
        TestPaper = testPaper;
    }

    public class TestPagerInfo {
        // 试卷id
        private int TestPaper_PKID;
        // 试卷名称
        private String TestPaper_Name;
        // 题目数量
        private int TestPaper_QuesNum;

        public int getTestPaper_PKID() {
            return TestPaper_PKID;
        }

        public void setTestPaper_PKID(int testPaper_PKID) {
            TestPaper_PKID = testPaper_PKID;
        }

        public String getTestPaper_Name() {
            return TestPaper_Name;
        }

        public void setTestPaper_Name(String testPaper_Name) {
            TestPaper_Name = testPaper_Name;
        }

        public int getTestPaper_QuesNum() {
            return TestPaper_QuesNum;
        }

        public void setTestPaper_QuesNum(int testPaper_QuesNum) {
            TestPaper_QuesNum = testPaper_QuesNum;
        }
    }

    public class VideoInfo {
        // 视频id
        private int PKID;
        // 视频名称
        private String VNAME;
        // 视频时长
        private String VTIME;
        // 视频vid
        private String VID;

        public int getPKID() {
            return PKID;
        }

        public void setPKID(int PKID) {
            this.PKID = PKID;
        }

        public String getVNAME() {
            return VNAME;
        }

        public void setVNAME(String VNAME) {
            this.VNAME = VNAME;
        }

        public String getVTIME() {
            return VTIME;
        }

        public void setVTIME(String VTIME) {
            this.VTIME = VTIME;
        }

        public String getVID() {
            return VID;
        }

        public void setVID(String VID) {
            this.VID = VID;
        }
    }

    public class NoteInfo {
        // 科目id
        private int CoursewareData_PKID;
        // 视频id
        private int NTBVideoId;
        // 试卷id
        private int NTBTestPaperId;
        // 用户id
        private int NTBStudentID;
        // 笔记id
        private int NTBPkid;
        // 笔记内容
        private String NTBContent;
        // 笔记添加时间
        private String NTBAddTime;
        // 笔记图片
        private String NTBScreenShots;
        // 点赞数量
        private int Zcount;
        // 是否点赞(0:未点赞,1点赞)
        private int IsZan;
        // 用户昵称
        private String S_NickName;
        // 科目名称
        private String SubJectSName;
        // 用户图像
        private String SN_Head;
        // 是否公开(0:公开,1私人)
        private int NTBTempVal;
        // 审核状态(0:审核失败，1:审核通过)
        private int CheckStatus;

        public int getCoursewareData_PKID() {
            return CoursewareData_PKID;
        }

        public void setCoursewareData_PKID(int coursewareData_PKID) {
            CoursewareData_PKID = coursewareData_PKID;
        }

        public int getNTBVideoId() {
            return NTBVideoId;
        }

        public void setNTBVideoId(int NTBVideoId) {
            this.NTBVideoId = NTBVideoId;
        }

        public int getNTBTestPaperId() {
            return NTBTestPaperId;
        }

        public void setNTBTestPaperId(int NTBTestPaperId) {
            this.NTBTestPaperId = NTBTestPaperId;
        }

        public int getNTBStudentID() {
            return NTBStudentID;
        }

        public void setNTBStudentID(int NTBStudentID) {
            this.NTBStudentID = NTBStudentID;
        }

        public int getNTBPkid() {
            return NTBPkid;
        }

        public void setNTBPkid(int NTBPkid) {
            this.NTBPkid = NTBPkid;
        }

        public String getNTBContent() {
            return NTBContent;
        }

        public void setNTBContent(String NTBContent) {
            this.NTBContent = NTBContent;
        }

        public String getNTBAddTime() {
            return NTBAddTime;
        }

        public void setNTBAddTime(String NTBAddTime) {
            this.NTBAddTime = NTBAddTime;
        }

        public String getNTBScreenShots() {
            return NTBScreenShots;
        }

        public void setNTBScreenShots(String NTBScreenShots) {
            this.NTBScreenShots = NTBScreenShots;
        }

        public int getZcount() {
            return Zcount;
        }

        public void setZcount(int zcount) {
            Zcount = zcount;
        }

        public int getIsZan() {
            return IsZan;
        }

        public void setIsZan(int isZan) {
            IsZan = isZan;
        }

        public String getS_NickName() {
            return S_NickName;
        }

        public void setS_NickName(String s_NickName) {
            S_NickName = s_NickName;
        }

        public String getSubJectSName() {
            return SubJectSName;
        }

        public void setSubJectSName(String subJectSName) {
            SubJectSName = subJectSName;
        }

        public String getSN_Head() {
            return SN_Head;
        }

        public void setSN_Head(String SN_Head) {
            this.SN_Head = SN_Head;
        }

        public int getNTBTempVal() {
            return NTBTempVal;
        }

        public void setNTBTempVal(int NTBTempVal) {
            this.NTBTempVal = NTBTempVal;
        }

        public int getCheckStatus() {
            return CheckStatus;
        }

        public void setCheckStatus(int checkStatus) {
            CheckStatus = checkStatus;
        }
    }
}
