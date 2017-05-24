package jc.cici.android.atom.http;

import jc.cici.android.atom.bean.BindJCInfo;
import jc.cici.android.atom.bean.BindedInfo;
import jc.cici.android.atom.bean.ClassInfo;
import jc.cici.android.atom.bean.DeiverInfo;
import jc.cici.android.atom.bean.HistoryInfo;
import jc.cici.android.atom.bean.LessInfo;
import jc.cici.android.atom.bean.LessonInfo;
import jc.cici.android.atom.bean.MessageBean;
import jc.cici.android.atom.bean.PasswordInfo;
import jc.cici.android.atom.bean.Register;
import jc.cici.android.atom.bean.StageInfo;
import jc.cici.android.atom.bean.UserInfo;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 网络请求方法接口
 * Created by atom on 2017/4/17.
 */

public interface HttpPostService {

    /**
     * 登录请求
     *
     * @param requestBody
     * @return
     */
    @POST("user/login/v500")
    Observable<UserInfo> getUserInfo(@Body RequestBody requestBody);

    /**
     * 绑定设备
     *
     * @param requestBody
     * @return
     */
    @POST("user/binddevice/v500")
    Observable<DeiverInfo> getDeiverInfo(@Body RequestBody requestBody);

    /**
     * 新用户注册
     *
     * @param student
     * @param md5
     * @return
     */
    @GET("CheckRegisterTwoV500?")
    Observable<Register> getRegiesterInfo(@Query("Student") String student, @Query("MD5_Code") String md5);

    /**
     * 发送短信接口
     *
     * @param mobile
     * @param md5
     * @return
     */
    @GET("SendRegCheckMsg")
    Observable<String> getSendMsgInfo(@Query("Mobile") String mobile, @Query("MD5_Code") String md5);

    /**
     * 找回密码短信码验证
     *
     * @param student
     * @param md5
     * @return
     */
    @GET("SNameOrSTelephoneExistence")
    Observable<PasswordInfo> sendFindPwdMessage(@Query("Student") String student, @Query("MD5_Code") String md5);

    /**
     * 重置密码
     *
     * @param student
     * @param md5
     * @return
     */
    @GET("ChangeUserPass")
    Observable<Integer> getResetPwdInfo(@Query("Student") String student, @Query("MD5_Code") String md5);

    /**
     * 快速登录短信发送
     *
     * @param requestBody
     * @return
     */
    @POST("user/getlogincode/v500")
    Observable<MessageBean> sendFastLoginMsg(@Body RequestBody requestBody);

    /**
     * 快速登录
     *
     * @param requestBody
     * @return
     */
    @POST("user/codelogin/v500")
    Observable<UserInfo> getFastLoginInfo(@Body RequestBody requestBody);

    /**
     * 检测第三方登录是否绑定设备号
     *
     * @param student
     * @param md5
     * @return
     */
    @GET("CheckThirdAccountLogin")
    Observable<BindJCInfo> checkBindJCInof(@Query("Student") String student, @Query("MD5code") String md5);

    /**
     * 第三方未绑定金程账号登录绑定
     *
     * @param student
     * @param md5
     * @return
     */
    @GET("BindNewUserThirdAccount")
    Observable<BindJCInfo> getBindJCInfo(@Query("Student") String student, @Query("MD5code") String md5);

    /**
     * 第三方已绑定金程账号登录情况
     *
     * @param student
     * @param md5
     * @return
     */
    @GET("BindUserThirdAccount")
    Observable<BindedInfo> getBindedInfo(@Query("Student") String student, @Query("MD5code") String md5);

    /**
     * 获取班级列表信息
     *
     * @param requestBody
     * @return
     */
    @POST("class/getmyclasslist/v500")
    Observable<ClassInfo> getClassInfo(@Body RequestBody requestBody);

    /**
     * 获取阶段信息
     *
     * @param requestBody
     * @return
     */
    @POST("/class/getclassstagelist/v500")
    Observable<StageInfo> getStageInfo(@Body RequestBody requestBody);

    /**
     * 获取阶段课程信息
     *
     * @param requestBody
     * @return
     */
    @POST("class/getclasslessonlist/v500")
    Observable<LessInfo> getLessInfo(@Body RequestBody requestBody);

    /**
     * 获取签到信息
     * @param requestBody
     * @return
     */
    @POST("class/setclasslessonsignin/v500")
    Observable<Integer> getSignInfo(@Body RequestBody requestBody);

    /**
     * 获取阶段历史信息
     * @param requestBody
     * @return
     */
    @POST("class/getclasshistorylessonlist/v500")
    Observable<HistoryInfo> getHistoryLessonInfo(@Body RequestBody requestBody);

    @POST("class/getclasslessondetail/v500")
    Observable<LessonInfo> getLessonDetailInfo(@Body RequestBody requestBody);

}
