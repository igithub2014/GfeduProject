package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 科目实体
 * Created by atom on 2017/6/22.
 */

public class SubjectBean {

    private int SubjectListCount;
    private ArrayList<Subject> SubjectList;

    public int getSubjectListCount() {
        return SubjectListCount;
    }

    public void setSubjectListCount(int subjectListCount) {
        SubjectListCount = subjectListCount;
    }

    public ArrayList<Subject> getSubjectList() {
        return SubjectList;
    }

    public void setSubjectList(ArrayList<Subject> subjectList) {
        SubjectList = subjectList;
    }

    public class Subject {
        // 科目id
        private int SubjectId;
        // 科目名称
        private String SubjectName;

        public int getSubjectId() {
            return SubjectId;
        }

        public void setSubjectId(int subjectId) {
            SubjectId = subjectId;
        }

        public String getSubjectName() {
            return SubjectName;
        }

        public void setSubjectName(String subjectName) {
            SubjectName = subjectName;
        }
    }
}
