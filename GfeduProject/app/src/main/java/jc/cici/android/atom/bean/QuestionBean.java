package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 问题实体
 * Created by atom on 2017/6/12.
 */

public class QuestionBean {

    // 问题列表数量
    private int QuesListCount;
    // 总页数
    private int TotalPageIndex;
    // 问题列表
    private ArrayList<Question> QuesList;

    public int getQuesListCount() {
        return QuesListCount;
    }

    public void setQuesListCount(int quesListCount) {
        QuesListCount = quesListCount;
    }

    public int getTotalPageIndex() {
        return TotalPageIndex;
    }

    public void setTotalPageIndex(int totalPageIndex) {
        TotalPageIndex = totalPageIndex;
    }

    public ArrayList<Question> getQuesList() {
        return QuesList;
    }

    public void setQuesList(ArrayList<Question> quesList) {
        QuesList = quesList;
    }
}
