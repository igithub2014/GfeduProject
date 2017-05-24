package jc.cici.android.atom.bean;

/**
 * 绑定金程账号信息
 * Created by atom on 2017/4/21.
 */

public class BindJCInfo {

    // 结果状态
    private String ResultState;
    // 结果描述
    private String ResultStr;
    // 绑定设备号
    private String DeviceId;
    // 用户信息
    private User ResultData;

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

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public User getResultData() {
        return ResultData;
    }

    public void setResultData(User resultData) {
        ResultData = resultData;
    }
}
