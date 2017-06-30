package jc.cici.android.atom.ui.tiku;


import java.util.ArrayList;

public class WrongAnalysisContent {
    private String Code;
    private String Message;
    private ArrayList<Body> Body;

    public class Body {
        private String QuesNo;
        private int QuesId;
        private String QuesStatus;
        private String QuesType;
        private String QuesProblemCount;
        private String QuesNoteCount;
        private String QuesUrl;

        public String getQuesNo() {
            return QuesNo;
        }

        public void setQuesNo(String quesNo) {
            QuesNo = quesNo;
        }

        public int getQuesId() {
            return QuesId;
        }

        public void setQuesId(int quesId) {
            QuesId = quesId;
        }

        public String getQuesStatus() {
            return QuesStatus;
        }

        public void setQuesStatus(String quesStatus) {
            QuesStatus = quesStatus;
        }

        public String getQuesType() {
            return QuesType;
        }

        public void setQuesType(String quesType) {
            QuesType = quesType;
        }

        public String getQuesProblemCount() {
            return QuesProblemCount;
        }

        public void setQuesProblemCount(String quesProblemCount) {
            QuesProblemCount = quesProblemCount;
        }

        public String getQuesNoteCount() {
            return QuesNoteCount;
        }

        public void setQuesNoteCount(String quesNoteCount) {
            QuesNoteCount = quesNoteCount;
        }

        public String getQuesUrl() {
            return QuesUrl;
        }

        public void setQuesUrl(String quesUrl) {
            QuesUrl = quesUrl;
        }
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<WrongAnalysisContent.Body> getBody() {
        return Body;
    }

    public void setBody(ArrayList<WrongAnalysisContent.Body> body) {
        Body = body;
    }
}
