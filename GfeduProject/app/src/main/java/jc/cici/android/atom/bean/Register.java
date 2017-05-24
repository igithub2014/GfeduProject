package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 注册信息
 * Created by atom on 2017/4/19.
 */

public class Register {
    // 结果状态
    private String ResultState;
    // 结果描述
    private String ResultStr;
    // 返回用户信息
    private ArrayList<UserInfo> ResultData;

    public String getResultState() {
        return ResultState;
    }

    public void setResultState(String resultState) {
        ResultState = resultState;
    }

    public String getResultStr() {
        return ResultStr;
    }

    public void setResultStr(String resultStr) {
        ResultStr = resultStr;
    }

    public ArrayList<UserInfo> getResultData() {
        return ResultData;
    }

    public void setResultData(ArrayList<UserInfo> resultData) {
        ResultData = resultData;
    }
}
