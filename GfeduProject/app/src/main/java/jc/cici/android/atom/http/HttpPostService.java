package jc.cici.android.atom.http;

import java.util.List;
import java.util.Map;

import jc.cici.android.atom.bean.AfterInfo;
import jc.cici.android.atom.bean.Answer;
import jc.cici.android.atom.bean.BindJCInfo;
import jc.cici.android.atom.bean.BindedInfo;
import jc.cici.android.atom.bean.ClassInfoBean;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.DeiverInfo;
import jc.cici.android.atom.bean.HistoryInfo;
import jc.cici.android.atom.bean.LessInfo;
import jc.cici.android.atom.bean.LessonInfo;
import jc.cici.android.atom.bean.MessageBean;
import jc.cici.android.atom.bean.MyAnswerBean;
import jc.cici.android.atom.bean.NoteBean;
import jc.cici.android.atom.bean.NoteDetailsBean;
import jc.cici.android.atom.bean.PasswordInfo;
import jc.cici.android.atom.bean.QuesDetailBean;
import jc.cici.android.atom.bean.Question;
import jc.cici.android.atom.bean.QuestionBean;
import jc.cici.android.atom.bean.Register;
import jc.cici.android.atom.bean.StageInfo;
import jc.cici.android.atom.bean.SubjectBean;
import jc.cici.android.atom.bean.UserInfo;
import jc.cici.android.atom.bean.ZhuiWenBean;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
    Observable<ClassInfoBean> getClassInfo(@Body RequestBody requestBody);

    /**
     * 获取阶段信息
     *
     * @param requestBody
     * @return
     */
    @POST("class/getclassstagelist/v500")
    Observable<CommonBean<StageInfo>> getStageInfo(@Body RequestBody requestBody);

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
     *
     * @param requestBody
     * @return
     */
    @POST("class/setclasslessonsignin/v500")
    Observable<Integer> getSignInfo(@Body RequestBody requestBody);

    /**
     * 获取阶段历史信息
     *
     * @param requestBody
     * @return
     */
    @POST("class/getclasshistorylessonlist/v500")
    Observable<CommonBean<HistoryInfo>> getHistoryLessonInfo(@Body RequestBody requestBody);

    /**
     * 课程详情页
     *
     * @param requestBody
     * @return
     */
    @POST("class/getclasslessondetail/v500")
    Observable<CommonBean<LessonInfo>> getLessonDetailInfo(@Body RequestBody requestBody);

    /**
     * 笔记列表接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/getnoteslist/v500")
    Observable<CommonBean<NoteBean>> getNotesListInfo(@Body RequestBody requestBody);

    /**
     * 科目列表接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/getsubjectlist/v500")
    Observable<CommonBean<SubjectBean>> getSubjectListInfo(@Body RequestBody requestBody);

    /**
     * 添加笔记接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/addnotes/v500")
    Observable<CommonBean> getAddNotesInfo(@Body RequestBody requestBody);

    /**
     * 笔记详情接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/getnotesinfo/v500")
    Observable<CommonBean<NoteDetailsBean>> getNotesDetailsInfo(@Body RequestBody requestBody);

    /**
     * 删除笔记接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/delnotes/v500")
    Observable<CommonBean> getDelNotesInfo(@Body RequestBody requestBody);

    /**
     * 笔记点赞接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/addnotespraise/v500")
    Observable<CommonBean> getNotesPraiseInfo(@Body RequestBody requestBody);

    /**
     * 笔记公开或私人设置接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/settempval/v500")
    Observable<CommonBean> getTempValInfo(@Body RequestBody requestBody);

    /**
     * 获取我的问题(OR 大家的问题)
     *
     * @param requestBody
     * @return
     */
    @POST("classques/getqueslist/v500")
    Observable<CommonBean<QuestionBean>> getQuesListInfo(@Body RequestBody requestBody);

    /**
     * 获取我的回答列表接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/getanswerlist/v500")
    Observable<CommonBean<MyAnswerBean>> getAnswerListInfo(@Body RequestBody requestBody);

    /**
     * 添加问题接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/addclassquesinfo/v500")
    Observable<CommonBean<Question>> getAddClassQuesInfo(@Body RequestBody requestBody);

    /**
     * 添加我的回答接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/addclassquesinfo/v500")
    Observable<CommonBean<Answer>> getAddAnswerInfo(@Body RequestBody requestBody);

    /**
     * 问题详情接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/getquesdetail/v500")
    Observable<CommonBean<QuesDetailBean>> getQuesDetailInfo(@Body RequestBody requestBody);

    /**
     * 答案点赞功能
     *
     * @param requestBody
     * @return
     */
    @POST("classques/adduserpraise/v500")
    Observable<CommonBean> addUserPraiseInfo(@Body RequestBody requestBody);

    /**
     * 答案设置为最佳
     * @param requestBody
     * @return
     */
    @POST("classques/setclassquesstatus/v500")
    Observable<CommonBean>setClassQuesStatusInfo(@Body RequestBody requestBody);

    /**
     * 追问(追答,评论)列表接口
     * @param requestBody
     * @return
     */
    @POST("classques/getafterquesanswerlist/v500")
    Observable<CommonBean<ZhuiWenBean>> getAfterQuesAnswerListInfo(@Body RequestBody requestBody);


}
