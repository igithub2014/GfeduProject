package jc.cici.android.atom.ui.tiku;

import java.io.Serializable;
import java.util.ArrayList;

public class CardStatus implements Serializable {
    private String Code;
    private String Message;
    private Body Body;

    public class Body {
        private String QuesTypeName;
        private String UserDoneTime;
        private String UserSubTime;
        private ArrayList<PaperQuesGroupList> PaperQuesGroupList;

        public class PaperQuesGroupList {
            private String QuesTypeName;
            private ArrayList<PaperQuesList> PaperQuesList;


            public class PaperQuesList {
                private String QuesNo;
                private String QuesId;
                private int QuesStatus;
                private String QuesType;

                public String getQuesNo() {
                    return QuesNo;
                }

                public void setQuesNo(String quesNo) {
                    QuesNo = quesNo;
                }

                public String getQuesId() {
                    return QuesId;
                }

                public void setQuesId(String quesId) {
                    QuesId = quesId;
                }

                public int getQuesStatus() {
                    return QuesStatus;
                }

                public void setQuesStatus(int quesStatus) {
                    QuesStatus = quesStatus;
                }

                public String getQuesType() {
                    return QuesType;
                }

                public void setQuesType(String quesType) {
                    QuesType = quesType;
                }
            }

            public String getQuesTypeName() {
                return QuesTypeName;
            }

            public void setQuesTypeName(String quesTypeName) {
                QuesTypeName = quesTypeName;
            }

            public ArrayList<CardStatus.Body.PaperQuesGroupList.PaperQuesList> getPaperQuesList() {
                return PaperQuesList;
            }

            public void setPaperQuesList(ArrayList<CardStatus.Body.PaperQuesGroupList.PaperQuesList> paperQuesList) {
                PaperQuesList = paperQuesList;
            }
        }

        public String getQuesTypeName() {
            return QuesTypeName;
        }

        public void setQuesTypeName(String quesTypeName) {
            QuesTypeName = quesTypeName;
        }

        public String getUserDoneTime() {
            return UserDoneTime;
        }

        public void setUserDoneTime(String userDoneTime) {
            UserDoneTime = userDoneTime;
        }

        public String getUserSubTime() {
            return UserSubTime;
        }

        public void setUserSubTime(String userSubTime) {
            UserSubTime = userSubTime;
        }

        public ArrayList<CardStatus.Body.PaperQuesGroupList> getPaperQuesGroupList() {
            return PaperQuesGroupList;
        }

        public void setPaperQuesGroupList(ArrayList<CardStatus.Body.PaperQuesGroupList> paperQuesGroupList) {
            PaperQuesGroupList = paperQuesGroupList;
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

    public CardStatus.Body getBody() {
        return Body;
    }

    public void setBody(CardStatus.Body body) {
        Body = body;
    }
}
