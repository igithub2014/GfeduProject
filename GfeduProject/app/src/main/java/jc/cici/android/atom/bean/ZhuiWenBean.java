package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 追问追答实体
 * Created by atom on 2017/6/26.
 */

public class ZhuiWenBean {

    // 是否可以追答 1:是 0：否
    private int CanZhuiDa;
    // 是否可以追问 1:是 0：否
    private int CanZhuiWen;
    // 追问(追答)对应问题实体
    private Question QuesInfo;
    // 追问(追答)对应的回答实体
    private Answer AnswerInfo;
    // 追问(追答)列表数量
    private int AfterListCount;
    // 追问追答内容
    private ArrayList<ZhuiWen> AfterList;

    public int getCanZhuiDa() {
        return CanZhuiDa;
    }

    public void setCanZhuiDa(int canZhuiDa) {
        CanZhuiDa = canZhuiDa;
    }

    public int getCanZhuiWen() {
        return CanZhuiWen;
    }

    public void setCanZhuiWen(int canZhuiWen) {
        CanZhuiWen = canZhuiWen;
    }

    public Question getQuesInfo() {
        return QuesInfo;
    }

    public void setQuesInfo(Question quesInfo) {
        QuesInfo = quesInfo;
    }

    public Answer getAnswerInfo() {
        return AnswerInfo;
    }

    public void setAnswerInfo(Answer answerInfo) {
        AnswerInfo = answerInfo;
    }

    public int getAfterListCount() {
        return AfterListCount;
    }

    public void setAfterListCount(int afterListCount) {
        AfterListCount = afterListCount;
    }

    public ArrayList<ZhuiWen> getAfterList() {
        return AfterList;
    }

    public void setAfterList(ArrayList<ZhuiWen> afterList) {
        AfterList = afterList;
    }
}
