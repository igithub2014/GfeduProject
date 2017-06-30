package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 笔记实体类
 * Created by atom on 2017/6/6.
 */

public class NoteBean {
    // 笔记列表
    private ArrayList<Note> NotesList;

    public ArrayList<Note> getNotesList() {
        return NotesList;
    }

    public void setNotesList(ArrayList<Note> notesList) {
        NotesList = notesList;
    }

    public static class Note {
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
        // 点赞状态(0:未点赞,1：点赞)
        private int IsZan;
        // 用户昵称
        private String S_NickName;
        // 科目名称
        private String SubJectSName;
        // 用户图像
        private String SN_Head;
        // 公开状态(0:公开,1:私人)
        private int NTBTempVal;
        // 审核状态
        private int CheckStatus;

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
