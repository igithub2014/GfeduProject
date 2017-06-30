package jc.cici.android.atom.ui.tiku;



public class Const {
    public static int  USERID=26146;
    public static String USERID_S="123898";
    public static String USERTEL="";
    public static String CLIENT = "ANDROID";
    public static String VERSION = "1.0.1";
    public static String APPNAME = "JC_SCHOOL";
    // 测试服务器地址
    //public static String URL = "http://mapitest.gfedu.org/";
    //上线服务器地址
    public static final String URL = "http://mapi.gfedu.cn/";
    /** 个人信息接口 **/
    public static final String GetRenXinIxAPI = "api/user/getuserdetail/v500";
    /** 更换手机获取验证码接口 **/
    public static final String GetMobleCodeAPI = "api/user/getupdatemobilecode/v500";
    /** 更换手机接口 **/
    public static final String GetChangeMobleAPI = "api/user/updateuserphone/v500";
    /** 获取用户绑定信息定接口 **/
    public static final String GetThridBingAPI = "api/user/getbindinfo/v500";
    /** 绑定第三方接口 **/
    public static final String BindUserAPI = "http://m.gfedu.cn/StudentWebService.asmx/BindUserThirdAccount?Student=";
    /** 解除第三方绑定接口 **/
    public static final String UnBingAPI = "api/user/delbind/v500";
    /** 修改密码接口 **/
    public static final String ChangePassAPI = "http://m.gfedu.cn/StudentWebService.asmx/ChangeUserPass?Student=";
    /** 修改个人信息接口 **/
    public static final String ChangeInfoAPI = "api/user/updateuserdetail/v500";
    /** 发送绑定邮箱验证码 **/
    public static final String SendBindeMailAPI = "api/user/sendbindemailurl/v500";
    /** 检查密码 **/
    public static final String CheckPwdAPI = "api/user/checkpwd/v500";
    /** 课程目录（在线） **/
    public static final String TonLineListAPI = "api/class/getonlinelist/v500";
    /** 在线课程详情 **/
    public static final String OnLineInfoAPI = "api/class/getonlineinfo/v500";
    /** 在线课程详情笔记/问题列表（在线） **/
    public static final String NotesListAPI = "api/notes/getnoteslist/v500";
    public static final String QuesListAPI = "api/classques/getqueslist/v500";
    /** 已结束课程 **/
    public static final String OverClassListAPI = "api/class/getendlist/v500";
    /**观看视频学会了**/
    public static final  String StudyEndAPI = "api/class/studyend/v500";

    //获取试卷信息
    public  static final  String ExamPaperInfo="api/exam/getpaperinfo/v500";
    //提交答案（单题）
    public  static final  String SubmitPaperAPI="api/exam/submituserquesanswer/v500";
    //提交答案(答题卡全部)
    public  static final  String SubmitQuesanswerAPI="api/exam/submituserpaper/v500";
    //提获取用户试卷报告
    public  static final  String GetUserPaperReportAPI="api/exam/getuserpaperreport/v500";
    //错题解析
    public static final String GetUserPaperCardErrorAPI = "api/exam/getuserpapercarderrorlist/v500";
    // 全部解析
    public static final String GetUserPaperCardAllAPI = "api/exam/getuserpapercardalllist/v500";

}
