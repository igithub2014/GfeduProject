package jc.cici.android.atom.ui.tiku;


import java.util.ArrayList;

public class ContentZhenTi {
    private String Code;
    private String Message;
    private Body Body;

    public class Body {
        private String LastDoNo;
        private String PaperId;
        private String PaperQuesCount;
        private ArrayList<PaperQuesCardList> PaperQuesCardList;


        public class PaperQuesCardList {
            private String QuesNo;
            private int QuesId;
            private String QuesStatus;
            private String QuesType;
            private int QuesOptionCount;
            private String QuesUserAnswer;
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

            public int getQuesOptionCount() {
                return QuesOptionCount;
            }

            public void setQuesOptionCount(int quesOptionCount) {
                QuesOptionCount = quesOptionCount;
            }

            public String getQuesUserAnswer() {
                return QuesUserAnswer;
            }

            public void setQuesUserAnswer(String quesUserAnswer) {
                QuesUserAnswer = quesUserAnswer;
            }

            public String getQuesUrl() {
                return QuesUrl;
            }

            public void setQuesUrl(String quesUrl) {
                QuesUrl = quesUrl;
            }
        }

        public String getPaperId() {
            return PaperId;
        }

        public void setPaperId(String paperId) {
            PaperId = paperId;
        }

        public String getPaperQuesCount() {
            return PaperQuesCount;
        }

        public void setPaperQuesCount(String paperQuesCount) {
            PaperQuesCount = paperQuesCount;
        }

        public ArrayList<ContentZhenTi.Body.PaperQuesCardList> getPaperQuesCardList() {
            return PaperQuesCardList;
        }

        public void setPaperQuesCardList(ArrayList<ContentZhenTi.Body.PaperQuesCardList> paperQuesCardList) {
            PaperQuesCardList = paperQuesCardList;
        }

        public String getLastDoNo() {
            return LastDoNo;
        }

        public void setLastDoNo(String lastDoNo) {
            LastDoNo = lastDoNo;
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

    public ContentZhenTi.Body getBody() {
        return Body;
    }

    public void setBody(ContentZhenTi.Body body) {
        Body = body;
    }
}
