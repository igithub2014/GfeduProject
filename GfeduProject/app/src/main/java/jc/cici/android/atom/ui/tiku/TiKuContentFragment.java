package jc.cici.android.atom.ui.tiku;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import jc.cici.android.R;
import jc.cici.android.atom.common.CommParam;


public class TiKuContentFragment extends Fragment {
    private ScrollView judgescrollView;
    private ScrollView danxuanscrollView;
    private ScrollView mulscrollView;
    private ScrollView jiandascrollView;
    // 简答webView
    private WebView jianda_webView;
    // 填空webView
    private WebView tKong_webView;
    // 多选webView
    private WebView double_webView;
    // 单选webView
    private WebView test_webView;
    // 判断题webView
    private WebView judge_webView;
    // 解析题webView
    private WebView analysis_WebView;
    // 单选答案选项布局
    private LinearLayout btnA_layout;
    private LinearLayout btnB_layout;
    private LinearLayout btnC_layout;
    private LinearLayout btnD_layout;
    private LinearLayout btnE_layout;
    // 多选答案选项布局
    private LinearLayout mul_btnA_layout;
    private LinearLayout mul_btnB_layout;
    private LinearLayout mul_btnC_layout;
    private LinearLayout mul_btnD_layout;
    private LinearLayout mul_btnE_layout;
    // 答案选项A
    private Button btn_A, mul_btn_A;
    // 答案选项B
    private Button btn_B, mul_btn_B;
    // 答案选项C
    private Button btn_C, mul_btn_C;
    // 答案选项D
    private Button btn_D, mul_btn_D;
    // 答案选项E
    private Button btn_E, mul_btn_E;
    // 简答editText
    private static EditText edit_answer;
    // 简答添加图片gridView
    private GridView addgridView;
    private ImageView icon_img;
    private Handler mHandler;
    private int questionPKID; // 试卷试题号
    private String title;// 题干
    private int optionCount; // 答案选项个数
    private String content; // 选项
    private String AnswerType; // 答案类型
    private int userId; // 用户id
    private int testPaKID;// 试卷id
    private String quesUserAnswer; // 用户答案
    private String time; // 答题时间
    private String name; // 标题
    private String type; // 题库类型
    // 当前页
    private int currPage;
    // 试题个数
    private int size;
    private ArrayList<Card> cardList;
    // 类型
    private String CardAnwserType;
    private MyQuestionActivity mCtx;
    private WrongAnalysisActivity wrMCtx;
    private AnalysisActivity anMCtx;
    public static Handler handler;
    // 判断题正确选项按钮
    private Button btnA_judge;
    // 判断题错误选项按钮
    private Button btnB_judge;
    private HttpUtils httpUtils = new HttpUtils();
    private FragmentActivity context;
    // 多选题按钮是否选中
    private boolean multiselect_btnA = false;
    private boolean multiselect_btnB = false;
    private boolean multiselect_btnC = false;
    private boolean multiselect_btnD = false;
    private boolean multiselect_btnE = false;
    private boolean multiselect_btnF = false;
    // 多选按钮设置传递给服务器
    private String multiselect_btnA_String = "";
    private String multiselect_btnB_String = "";
    private String multiselect_btnC_String = "";
    private String multiselect_btnD_String = "";
    private String multiselect_btnE_String = "";
    private String multiselect_btnF_String = "";
    // 答题
    private View view;
    // 解析view
    private View analysis_view;
    private SubmitQuesAnswer submitQuesAnswer;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.tiankong.braodcast".equals(intent.getAction())) {
                if (null != tKong_webView) {
                    tKong_webView.loadUrl("javascript:submit_tiankong()");
                }
            }
        }

    };
    private IntentFilter filter;
    // 传递上传到服务器上的图片数组
    private ArrayList<String> mImagList;
    // 简答题 返回结果
    private int resultStr;

    public TiKuContentFragment(MyQuestionActivity context, Handler handler) {
        this.mCtx = context;
        this.mHandler = handler;
    }

    public TiKuContentFragment(WrongAnalysisActivity context, Handler handler) {
        this.wrMCtx = context;
        this.mHandler = handler;
    }

    public TiKuContentFragment(AnalysisActivity context, Handler handler) {
        this.anMCtx = context;
        this.mHandler = handler;
    }

    public TiKuContentFragment() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = TiKuContentFragment.this.getActivity();
        // 注册广播
        filter = new IntentFilter();
        filter.addAction("com.notify.broadcast");
        filter.addAction("com.tiankong.braodcast");
        context.registerReceiver(receiver, filter);
        // 获取题库类型
        type = getArguments().getString("type");
        if (type != null && type.equals("zhenTiType")) {
            AnswerType = getArguments().getString("AnswerType");
            questionPKID = getArguments().getInt("num");
            // 获取当前所做题目标题
            name = getArguments().getString("name");
            title = getArguments().getString("title");
            /** add by atom 2016/12/21 **/
            optionCount = getArguments().getInt("optionCount");
            /** add by atom 2016/12/21 **/

            userId = getArguments().getInt("userId");
            testPaKID = getArguments().getInt("testPaKID");
            currPage = getArguments().getInt("currPage");
            size = getArguments().getInt("size");
            mImagList = getArguments().getStringArrayList("mImageList");
            cardList = (ArrayList<Card>) getArguments().getSerializable(
                    "cardList");

            // 用户答案
            quesUserAnswer = getArguments().getString("QuesUserAnswer");
            // 类型
            CardAnwserType = getArguments().getString("CardAnwserType");
        } else if (type != null && type.equals("zhenTiAnalysisType")) {
            AnswerType = getArguments().getString("AnswerType");
            questionPKID = getArguments().getInt("num");
            // 获取当前所做题目标题
//            name = getArguments().getString("name");
            // h5題干
            title = getArguments().getString("title");
            size = getArguments().getInt("size");
            // 类型
            CardAnwserType = getArguments().getString("CardAnwserType");
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 调用getTime()方法获取time
        if (null != mCtx) {
            time = mCtx.getTime();
        }
//		else if (null != wrongCtx) {
//			time = wrongCtx.getTime();
//		}
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    context.finish();
                }
            }

        };
        if (type != null && type.equals("zhenTiType")
                || "Special_Question".equals(type)) {// 题库类型为为真题解析情况
            if (AnswerType.equals("1")) { // 判断答题布局
                if (null == view) {
                    view = inflater.inflate(R.layout.righterror_content_view,
                            null);
                    judge_webView = (WebView) view
                            .findViewById(R.id.judge_webView);
                    judgescrollView = (ScrollView) view
                            .findViewById(R.id.judgescrollView);
                    btnA_judge = (Button) view.findViewById(R.id.btnA_judge);
                    btnB_judge = (Button) view.findViewById(R.id.btnB_judge);
                    // webView基本设置
                    webViewSetting(judge_webView, title);
                    judgescrollView.scrollTo(0, 0);
                    // 答题一半退出后,再次进入答案设置
                    if (!"".equals(quesUserAnswer)) {
                        if ("A".equals(quesUserAnswer)) {
                            btnA_judge
                                    .setBackgroundResource(R.drawable.right_selected_bg);
                        } else if ("B".equals(quesUserAnswer)) {
                            btnB_judge
                                    .setBackgroundResource(R.drawable.error_selected_bg);
                        }
                    }
                    // 正确判断按钮设置监听
                    setOnclick(btnA_judge);
                    // 错误判断按钮设置监听
                    setOnclick(btnB_judge);

                }
                // 清除父类缓存中已存在的状态
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) {
                    parent.removeView(view);
                }
                return view;
            } else if (AnswerType.equals("2")) { // 单选答题布局
                if (null == view) {
                    view = inflater
                            .inflate(R.layout.danxuan_content_view, null);
                    test_webView = (WebView) view
                            .findViewById(R.id.test_webView);
                    danxuanscrollView = (ScrollView) view
                            .findViewById(R.id.danxuanscrollView);
                    btnA_layout = (LinearLayout) view
                            .findViewById(R.id.btnA_layout);
                    btnB_layout = (LinearLayout) view
                            .findViewById(R.id.btnB_layout);
                    btnC_layout = (LinearLayout) view
                            .findViewById(R.id.btnC_layout);
                    btnD_layout = (LinearLayout) view
                            .findViewById(R.id.btnD_layout);
                    btnE_layout = (LinearLayout) view
                            .findViewById(R.id.btnE_layout);

                    btn_A = (Button) view.findViewById(R.id.btn_A);
                    btn_B = (Button) view.findViewById(R.id.btn_B);
                    btn_C = (Button) view.findViewById(R.id.btn_C);
                    btn_D = (Button) view.findViewById(R.id.btn_D);
                    btn_E = (Button) view.findViewById(R.id.btn_E);
                    webViewSetting(test_webView, title);
                    danxuanscrollView.scrollTo(0, 0);
                    // 答案选项设置是否可显示
                    switch (optionCount) {
                        case 1:
                            btnA_layout.setVisibility(View.VISIBLE);
                            btnB_layout.setVisibility(View.GONE);
                            btnC_layout.setVisibility(View.GONE);
                            btnD_layout.setVisibility(View.GONE);
                            btnE_layout.setVisibility(View.GONE);
                            break;
                        case 2:
                            btnA_layout.setVisibility(View.VISIBLE);
                            btnB_layout.setVisibility(View.VISIBLE);
                            btnC_layout.setVisibility(View.GONE);
                            btnD_layout.setVisibility(View.GONE);
                            btnE_layout.setVisibility(View.GONE);
                            break;
                        case 3:
                            btnA_layout.setVisibility(View.VISIBLE);
                            btnB_layout.setVisibility(View.VISIBLE);
                            btnC_layout.setVisibility(View.VISIBLE);
                            btnD_layout.setVisibility(View.GONE);
                            btnE_layout.setVisibility(View.GONE);
                            break;
                        case 4:
                            btnA_layout.setVisibility(View.VISIBLE);
                            btnB_layout.setVisibility(View.VISIBLE);
                            btnC_layout.setVisibility(View.VISIBLE);
                            btnD_layout.setVisibility(View.VISIBLE);
                            btnE_layout.setVisibility(View.GONE);
                            break;
                        case 5:
                            btnA_layout.setVisibility(View.VISIBLE);
                            btnB_layout.setVisibility(View.VISIBLE);
                            btnC_layout.setVisibility(View.VISIBLE);
                            btnD_layout.setVisibility(View.VISIBLE);
                            btnE_layout.setVisibility(View.VISIBLE);
                            break;

                        default:
                            break;
                    }
                    // 答题一半(其他单选)，再次进入答题已选中颜色设置
                    if (!"".equals(quesUserAnswer)) {
                        if ("A".equals(quesUserAnswer)) {
                            setSelectedButtonBackgroundResource(btn_A, "A");
                        } else if ("B".equals(quesUserAnswer)) {
                            setSelectedButtonBackgroundResource(btn_B, "B");
                        } else if ("C".equals(quesUserAnswer)) {
                            setSelectedButtonBackgroundResource(btn_C, "C");
                        } else if ("D".equals(quesUserAnswer)) {
                            setSelectedButtonBackgroundResource(btn_D, "D");
                        } else if ("E".equals(quesUserAnswer)) {
                            setSelectedButtonBackgroundResource(btn_E, "E");
                        }
                    }
                    // 按钮设置监听
                    setOnclick(btn_A);
                    setOnclick(btn_B);
                    setOnclick(btn_C);
                    setOnclick(btn_D);
                    setOnclick(btn_E);
                }
                // 清除父类缓存中已存在的状态
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) {
                    parent.removeView(view);
                }
                return view;
            } else if (AnswerType.equals("3")) { // 多选题布局
                if (null == view) {
                    view = inflater
                            .inflate(R.layout.doubule_content_view, null);
                    double_webView = (WebView) view
                            .findViewById(R.id.double_webView);
                    mulscrollView = (ScrollView) view
                            .findViewById(R.id.mulscrollView);
                    // 多选答案选项获取id
                    mul_btnA_layout = (LinearLayout) view
                            .findViewById(R.id.mul_btnA_layout);
                    mul_btnB_layout = (LinearLayout) view
                            .findViewById(R.id.mul_btnB_layout);
                    mul_btnC_layout = (LinearLayout) view
                            .findViewById(R.id.mul_btnC_layout);
                    mul_btnD_layout = (LinearLayout) view
                            .findViewById(R.id.mul_btnD_layout);
                    mul_btnE_layout = (LinearLayout) view
                            .findViewById(R.id.mul_btnE_layout);
                    // 选项按钮获取id
                    mul_btn_A = (Button) view.findViewById(R.id.mul_btn_A);
                    mul_btn_B = (Button) view.findViewById(R.id.mul_btn_B);
                    mul_btn_C = (Button) view.findViewById(R.id.mul_btn_C);
                    mul_btn_D = (Button) view.findViewById(R.id.mul_btn_D);
                    mul_btn_E = (Button) view.findViewById(R.id.mul_btn_E);
                    // webView基本设置
                    webViewSetting(double_webView, title);
                    mulscrollView.scrollTo(0, 0);
                    // 多选按钮显示设置
                    switch (optionCount) {
                        case 1:
                            mul_btnA_layout.setVisibility(View.VISIBLE);
                            mul_btnB_layout.setVisibility(View.GONE);
                            mul_btnC_layout.setVisibility(View.GONE);
                            mul_btnD_layout.setVisibility(View.GONE);
                            mul_btnE_layout.setVisibility(View.GONE);
                            break;
                        case 2:
                            mul_btnA_layout.setVisibility(View.VISIBLE);
                            mul_btnB_layout.setVisibility(View.VISIBLE);
                            mul_btnC_layout.setVisibility(View.GONE);
                            mul_btnD_layout.setVisibility(View.GONE);
                            mul_btnE_layout.setVisibility(View.GONE);
                            break;
                        case 3:
                            mul_btnA_layout.setVisibility(View.VISIBLE);
                            mul_btnB_layout.setVisibility(View.VISIBLE);
                            mul_btnC_layout.setVisibility(View.VISIBLE);
                            mul_btnD_layout.setVisibility(View.GONE);
                            mul_btnE_layout.setVisibility(View.GONE);
                            break;
                        case 4:
                            mul_btnA_layout.setVisibility(View.VISIBLE);
                            mul_btnB_layout.setVisibility(View.VISIBLE);
                            mul_btnC_layout.setVisibility(View.VISIBLE);
                            mul_btnD_layout.setVisibility(View.VISIBLE);
                            mul_btnE_layout.setVisibility(View.GONE);
                            break;
                        case 5:
                            mul_btnA_layout.setVisibility(View.VISIBLE);
                            mul_btnB_layout.setVisibility(View.VISIBLE);
                            mul_btnC_layout.setVisibility(View.VISIBLE);
                            mul_btnD_layout.setVisibility(View.VISIBLE);
                            mul_btnE_layout.setVisibility(View.VISIBLE);
                            break;

                        default:
                            break;
                    }
                    // 答题一半(其他单选)，再次进入答题已选中颜色设置
                    if (!"".equals(quesUserAnswer)) {
                        String arrAnswer[] = quesUserAnswer.split("、");
                        System.out.println("arrAnswer.length >>>" + arrAnswer.length);
                        for (int i = 0; i < arrAnswer.length; i++) {
                            if ("A".equals(arrAnswer[i])) {
                                setMulBtnBackgroundColor(mul_btn_A);
                            } else if ("B".equals(arrAnswer[i])) {
                                setMulBtnBackgroundColor(mul_btn_B);
                            } else if ("C".equals(arrAnswer[i])) {
                                setMulBtnBackgroundColor(mul_btn_C);
                            } else if ("D".equals(arrAnswer[i])) {
                                setMulBtnBackgroundColor(mul_btn_D);
                            } else if ("E".equals(arrAnswer[i])) {
                                setMulBtnBackgroundColor(mul_btn_E);
                            }
                        }
                    }
                    // 按钮设置监听
                    setOnclick(mul_btn_A);
                    setOnclick(mul_btn_B);
                    setOnclick(mul_btn_C);
                    setOnclick(mul_btn_D);
                    setOnclick(mul_btn_E);
                }
                // 清除父类缓存中已存在的状态
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) {
                    parent.removeView(view);
                }
                return view;
            }
        } else if (type != null && type.equals("zhenTiAnalysisType")) {// 解析情况
            if (null == analysis_view) {
                analysis_view = inflater.inflate(R.layout.analysis_view, null);
                analysis_WebView = (WebView) analysis_view
                        .findViewById(R.id.analysis_WebView);
                webViewSetting(analysis_WebView, title);
            }
            // 清除父类缓存中已存在的状态
            ViewGroup parent = (ViewGroup) analysis_view.getParent();
            if (parent != null) {
                parent.removeView(analysis_view);
            }
            return analysis_view;
        }
        return null;
    }

    /**
     * 多选按钮设置选中背景色
     *
     * @param btn
     */
    private void setMulBtnBackgroundColor(Button btn) {
        btn.setBackgroundResource(R.drawable.s_rectangle_fill);
        btn.setTextColor(Color.parseColor("#ffffff"));
    }

    /**
     * 按钮设置监听
     *
     * @param btn
     */
    private void setOnclick(final Button btn) {
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnA_judge: // 正确判断按钮A监听
                        if (httpUtils.isNetworkConnected(context)) {
                            AsChoiceTask asChoiceTask = new AsChoiceTask();
                            asChoiceTask.execute("A");
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        btnA_judge
                                .setBackgroundResource(R.drawable.right_selected_bg);
                        btnB_judge.setBackgroundResource(R.drawable.error_nomal_bg);
                        break;
                    case R.id.btnB_judge: // 错误判断按钮B监听
                        if (httpUtils.isNetworkConnected(context)) {
                            AsChoiceTask asChoiceTask = new AsChoiceTask();
                            asChoiceTask.execute("B");
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        btnB_judge
                                .setBackgroundResource(R.drawable.error_selected_bg);
                        btnA_judge.setBackgroundResource(R.drawable.right_nomal_bg);
                        break;
                    case R.id.btn_A: // 选项按钮A监听
                        if (httpUtils.isNetworkConnected(context)) {
                            AsChoiceTask asChoiceTask = new AsChoiceTask();
                            asChoiceTask.execute("A");
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // 设置选中颜色A选中，B、C、D、E 正常
                        setSelectedButtonBackgroundResource(btn_A, "A");
                        // 未选中按钮颜色设置
                        setCommButtonBackgroudResource(btn_B, "B");
                        setCommButtonBackgroudResource(btn_C, "C");
                        setCommButtonBackgroudResource(btn_D, "D");
                        setCommButtonBackgroudResource(btn_E, "E");
                        break;
                    case R.id.btn_B: // 选项按钮B监听
                        if (httpUtils.isNetworkConnected(context)) {
                            AsChoiceTask asChoiceTask = new AsChoiceTask();
                            asChoiceTask.execute("B");
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // 设置选中颜色B选中，A、C、D、E 正常
                        setSelectedButtonBackgroundResource(btn_B, "B");
                        // 未选中按钮颜色设置
                        setCommButtonBackgroudResource(btn_A, "A");
                        setCommButtonBackgroudResource(btn_C, "C");
                        setCommButtonBackgroudResource(btn_D, "D");
                        setCommButtonBackgroudResource(btn_E, "E");
                        break;
                    case R.id.btn_C: // 选项按钮C监听
                        if (httpUtils.isNetworkConnected(context)) {
                            AsChoiceTask asChoiceTask = new AsChoiceTask();
                            asChoiceTask.execute("C");
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // 设置选中颜色C选中，A、B、D、E 正常
                        setSelectedButtonBackgroundResource(btn_C, "C");
                        // 未选中按钮颜色设置
                        setCommButtonBackgroudResource(btn_A, "A");
                        setCommButtonBackgroudResource(btn_B, "B");
                        setCommButtonBackgroudResource(btn_D, "D");
                        setCommButtonBackgroudResource(btn_E, "E");
                        break;
                    case R.id.btn_D: // 选项按钮D监听
                        if (httpUtils.isNetworkConnected(context)) {
                            AsChoiceTask asChoiceTask = new AsChoiceTask();
                            asChoiceTask.execute("D");
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // 设置选中颜色D选中，A、B、C、E 正常
                        setSelectedButtonBackgroundResource(btn_D, "D");
                        // 未选中按钮颜色设置
                        setCommButtonBackgroudResource(btn_A, "A");
                        setCommButtonBackgroudResource(btn_B, "B");
                        setCommButtonBackgroudResource(btn_C, "C");
                        setCommButtonBackgroudResource(btn_E, "E");
                        break;
                    case R.id.btn_E: // 选项按钮E监听
                        if (httpUtils.isNetworkConnected(context)) {
                            AsChoiceTask asChoiceTask = new AsChoiceTask();
                            asChoiceTask.execute("E");
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // 设置选中颜色E选中，A、B、C、D 正常
                        setSelectedButtonBackgroundResource(btn_E, "E");
                        // 未选中按钮颜色设置
                        setCommButtonBackgroudResource(btn_A, "A");
                        setCommButtonBackgroudResource(btn_B, "B");
                        setCommButtonBackgroudResource(btn_C, "C");
                        setCommButtonBackgroudResource(btn_D, "D");
                        break;
                    case R.id.mul_btn_A: // 多选按钮A设置监听
                        // 按钮(未)选中状态设置
                        if (multiselect_btnA == false) {
                            setMulSelectButtonBackgroudResource(mul_btn_A);
                            multiselect_btnA = true;
                            multiselect_btnA_String = "A、";
                        } else {
                            setMulCommButtonBackgroundResource(mul_btn_A);
                            multiselect_btnA = false;
                            multiselect_btnA_String = "";
                        }
                        // 设置提交答案
                        if (httpUtils.isNetworkConnected(context)) {
                            MulChoiceTask mulTask = new MulChoiceTask();
                            mulTask.execute();
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mul_btn_B: // 多选按钮B设置监听
                        // 按钮(未)选中状态设置
                        if (multiselect_btnB == false) {
                            setMulSelectButtonBackgroudResource(mul_btn_B);
                            multiselect_btnB = true;
                            multiselect_btnB_String = "B、";
                        } else {
                            setMulCommButtonBackgroundResource(mul_btn_B);
                            multiselect_btnB = false;
                            multiselect_btnB_String = "";
                        }
                        // 设置提交答案
                        if (httpUtils.isNetworkConnected(context)) {
                            MulChoiceTask mulTask = new MulChoiceTask();
                            mulTask.execute();
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mul_btn_C: // 多选按钮C设置监听
                        // 按钮(未)选中状态设置
                        if (multiselect_btnC == false) {
                            setMulSelectButtonBackgroudResource(mul_btn_C);
                            multiselect_btnC = true;
                            multiselect_btnC_String = "C、";
                        } else {
                            setMulCommButtonBackgroundResource(mul_btn_C);
                            multiselect_btnC = false;
                            multiselect_btnC_String = "";
                        }
                        // 设置提交答案
                        if (httpUtils.isNetworkConnected(context)) {
                            MulChoiceTask mulTask = new MulChoiceTask();
                            mulTask.execute();
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mul_btn_D: // 多选按钮D设置监听
                        // 按钮(未)选中状态设置
                        if (multiselect_btnD == false) {
                            setMulSelectButtonBackgroudResource(mul_btn_D);
                            multiselect_btnD = true;
                            multiselect_btnD_String = "D、";
                        } else {
                            setMulCommButtonBackgroundResource(mul_btn_D);
                            multiselect_btnD = false;
                            multiselect_btnD_String = "";
                        }
                        // 设置提交答案
                        if (httpUtils.isNetworkConnected(context)) {
                            MulChoiceTask mulTask = new MulChoiceTask();
                            mulTask.execute();
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mul_btn_E: // 多选按钮E设置监听
                        // 按钮(未)选中状态设置
                        if (multiselect_btnE == false) {
                            setMulSelectButtonBackgroudResource(mul_btn_E);
                            multiselect_btnE = true;
                            multiselect_btnE_String = "E、";
                        } else {
                            setMulCommButtonBackgroundResource(mul_btn_E);
                            multiselect_btnE = false;
                            multiselect_btnE_String = "";
                        }
                        // 设置提交答案
                        if (httpUtils.isNetworkConnected(context)) {
                            MulChoiceTask mulTask = new MulChoiceTask();
                            mulTask.execute();
                        } else {
                            Toast.makeText(context, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 设置选中按钮颜色
     *
     * @param btn
     * @param str
     */
    private void setSelectedButtonBackgroundResource(Button btn, String str) {
        btn.setBackgroundResource(R.drawable.yuan_btn_bg);
        btn.setText(str);
        btn.setTextColor(Color.parseColor("#ffffff"));
    }

    /**
     * 设置未选中按钮颜色
     *
     * @param btn
     * @param str
     */
    private void setCommButtonBackgroudResource(Button btn, String str) {
        btn.setBackgroundResource(R.drawable.kongxinyuan_btn_bg);
        btn.setText(str);
        btn.setTextColor(Color.parseColor("#666666"));

    }

    /**
     * 多选按钮点击设置
     *
     * @param btn 选中按钮
     */
    private void setMulSelectButtonBackgroudResource(Button btn) {
        btn.setBackgroundResource(R.drawable.yuan_btn_bg);
        btn.setTextColor(Color.parseColor("#ffffff"));
    }

    private void setMulCommButtonBackgroundResource(Button btn) {
        btn.setBackgroundResource(R.drawable.kongxinyuan_btn_bg);
        btn.setTextColor(Color.parseColor("#333333"));
    }

    /**
     * webView进本设置
     *
     * @param webView
     * @param url
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void webViewSetting(final WebView webView, String url) {
        System.out.println("h5Str >>>:" + url);
        // 设置编码格式
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        // 设置
        webView.getSettings().setBuiltInZoomControls(false);
        // 支持js
        webView.getSettings().setJavaScriptEnabled(true);
        // 水平不显示
        webView.setHorizontalScrollBarEnabled(false);
        // 垂直不显示
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        // 添加webView
        if (httpUtils.isNetworkConnected(context)) {
            webView.loadUrl(url);
            webView.setWebViewClient(new TestWebViewClient(context, mHandler));
        } else {
            Toast.makeText(context, "加载本地图片", Toast.LENGTH_SHORT).show();
        }

        /**
         * 设置取消复制黏贴功能
         */
        webView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
    }

    /**
     * 单选题网络请求获取提交数据
     *
     * @author atom
     */
    class AsChoiceTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String UserQuesAnswer = params[0];
            System.out.println("UserQuesAnswer >>>:" + UserQuesAnswer);
            JSONObject obj = new JSONObject();
            try {
                CommParam commParam = new CommParam(mCtx);
                obj.put("userId", commParam.getUserId());
                obj.put("appName", commParam.getAppname());
                obj.put("oauth", commParam.getOauth());
                obj.put("client", commParam.getClient());
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("version", commParam.getVersion());
                obj.put("paperId", testPaKID);
                obj.put("quesId", questionPKID);
                obj.put("userQuesAnswer", UserQuesAnswer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            submitQuesAnswer = HttpUtils.getInstance().getSubmitQuesAnswer(
                    Const.URL + Const.SubmitPaperAPI, obj);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if ("100".equals(submitQuesAnswer.getCode())) {
                Message msg = new Message();
                msg.what = 0;
                int currentPage = currPage;
                msg.obj = currentPage;
                mHandler.sendMessage(msg);
                for (Card c : cardList) {
                    if (c.getPosition() == currentPage) {
                        c.setStatus(true);
                        cardList.set(currentPage, c);
                    }
                }
                if (currentPage == size - 1) {
                    System.out.println("最后一页");

                    Intent cardAswerIntent = new Intent(context,
                            CardAnswer.class);
                    cardAswerIntent.putExtra("pageStatus", cardList);
                    cardAswerIntent.putExtra("userId", userId);
                    cardAswerIntent.putExtra("TestPaperID", testPaKID);
                    // 传递标题
                    cardAswerIntent.putExtra("name", name);
                    cardAswerIntent.putExtra("time", time);
                    cardAswerIntent.putExtra("CardAnwserType", "My_Fragment");
                    context.startActivityForResult(cardAswerIntent, 1);
                    context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                }
            } else {
                Toast.makeText(context, "答题提交失败,请稍后再试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 多选按钮答案上传task
     *
     * @author atom
     */
    class MulChoiceTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String answerStr = multiselect_btnA_String
                    + multiselect_btnB_String + multiselect_btnC_String
                    + multiselect_btnD_String + multiselect_btnE_String
                    + multiselect_btnF_String;
            // 定义答案字符串
            String strAns = "";
            if (null != answerStr && !"".equals(answerStr)) {
                strAns = answerStr.substring(0, answerStr.lastIndexOf("、"));
                System.out.println("strAns >>>:" + strAns);
            }
            JSONObject obj = new JSONObject();
            try {
                CommParam commParam = new CommParam(mCtx);
                obj.put("userId", commParam.getUserId());
                obj.put("appName", commParam.getAppname());
                obj.put("oauth", commParam.getOauth());
                obj.put("client", commParam.getClient());
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("version", commParam.getVersion());
                // 试卷id
                obj.put("paperId", testPaKID);
                // 试题id
                obj.put("quesId", questionPKID);
                // 用户答案
                obj.put("userQuesAnswer", strAns);
            } catch (Exception e) {
                e.printStackTrace();
            }
            submitQuesAnswer = HttpUtils.getInstance().getSubmitQuesAnswer(
                    Const.URL + Const.SubmitPaperAPI, obj);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if ("100".equals(submitQuesAnswer.getCode())) {
                int currentPage = currPage;
                for (Card c : cardList) {
                    if (c.getPosition() == currentPage) {
                        c.setStatus(true);
                        cardList.set(currentPage, c);
                    }
                }
            } else {
                Toast.makeText(context, "答题提交失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != double_webView) {
            double_webView.destroy();
        }
        if (null != test_webView) {
            test_webView.destroy();
        }
        if (null != judge_webView) {
            judge_webView.destroy();
        }
        if (null != analysis_WebView) {
            analysis_WebView.destroy();
        }
        if (null != jianda_webView) {
            jianda_webView.destroy();
        }
        if (null != tKong_webView) {
            tKong_webView.destroy();
        }
        if (null != receiver) {
            context.unregisterReceiver(receiver);
        }
    }

    public interface TimeData {
        String getTime();
    }


}
