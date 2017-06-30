package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 我的回答实体
 * Created by atom on 2017/6/24.
 */

public class MyAnswerBean {

    // 问题列表数量
    private int AnswerListCount;
    // 总页数
    private int TotalPageIndex;
    // 回答列表
    private ArrayList<MyAnswer> AnswerList;

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

    public ArrayList<MyAnswer> getAnswerList() {
        return AnswerList;
    }

    public void setAnswerList(ArrayList<MyAnswer> answerList) {
        AnswerList = answerList;
    }
}
