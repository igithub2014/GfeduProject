package jc.cici.android.atom.ui.tiku;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jc.cici.android.R;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.view.RoundProgressBarWidthNumber;


public class CardResultActivity extends Activity implements OnClickListener {

    private static final int MSG_PROGRESS_UPDATE = 0x110;
    // 返回按钮
    private RelativeLayout backResultImgBtn;
    private TextView reset_txt;

    // 做题用时
    private TextView sckd_tv;
    // 交卷时间
    private TextView tv_time;
    // 答题情况
    private GridView view_gridResult;
    // 错题解析
    private Button wrongAnalysisBtn;
    // 全部解析
    private Button allAnalysisBtn;

    // 试卷id
    private int testPPKID;
    // 创建工具类对象
    private HttpUtils httpUtils = new HttpUtils();
    // 返回答案及解析数据list
    private ArrayList<CardStatus> resultList;
    // 创建适配器对象
    private ResultAdaper adapter;
    // 创建hashMap 用于记录位置
    private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    private String name; // 当前学习标题
    // 自定义进度条
    private Dialog mDialog;
    // 进度
    private RoundProgressBarWidthNumber progress_bar;
    // 获取正确率字符串
    private String countRightStr;
    private int countRight;
    // 类型区分考研数学
    private String CardAnwserType;

    private String itemName;

    //新增
    private CardResultAdapter cardResultAdapter;
    private CommParam commonParams;
    private CardStatus cardStatus;
    private ArrayList<CardStatus> cardStatusList = new ArrayList<>();
    private ExpandableListView class_lv;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int roundProgress = progress_bar.getProgress();
            progress_bar.setProgress(++roundProgress);
            if (countRight >= 100) {
                mHandler.removeMessages(MSG_PROGRESS_UPDATE);
            }
            mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_card);
        testPPKID = getIntent().getIntExtra("TestPPKID", 0);
        CardAnwserType = getIntent().getStringExtra("CardAnwserType");
        System.out.println("提交答案之后的界面 : " + CardAnwserType);

        // 加载数据
        showProcessDialog(CardResultActivity.this,
                R.layout.loading_process_dialog_color);
        // 初始化操作
        initViewSetting();
        // 获取真题数据
        GetZhenTiData();
    }

    private void GetZhenTiData() {
        // 创建任务对象
        AnswerDataTask task = new AnswerDataTask();
        task.execute();
    }

    /**
     * 自定义进度条
     *
     * @param mContext
     * @param layout
     */

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    /**
     * 给控件设置id
     */
    private void initViewSetting() {
        class_lv = (ExpandableListView) findViewById(R.id.class_lv);
        // 返回按钮获取id
        backResultImgBtn = (RelativeLayout) findViewById(R.id.backLayout);
        progress_bar = (RoundProgressBarWidthNumber) findViewById(R.id.progress_bar);


        sckd_tv = (TextView) findViewById(R.id.sckd_tv);
        // 重做
        reset_txt = (TextView) findViewById(R.id.reset_txt);
        // 花费时间获取id
        tv_time = (TextView) findViewById(R.id.time_txt);
        // 错题解析获取id
        wrongAnalysisBtn = (Button) findViewById(R.id.wrongAnalytical_btn);
        // 全部解析获取id
        allAnalysisBtn = (Button) findViewById(R.id.allAnalytical_btn);
        // 设置监听
        backResultImgBtn.setOnClickListener(this);
        wrongAnalysisBtn.setOnClickListener(this);
        allAnalysisBtn.setOnClickListener(this);
        reset_txt.setOnClickListener(this);

        // 初始化公共参数
        commonParams = new CommParam(this);
    }

    class AnswerDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... param) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("userId", commonParams.getUserId());
                obj.put("oauth", commonParams.getOauth());
                obj.put("client", commonParams.getClient());
                obj.put("timeStamp", commonParams.getTimeStamp());
                obj.put("version", commonParams.getVersion());
                obj.put("paperId", testPPKID);
                obj.put("appName", commonParams.getAppname());
            } catch (Exception e) {
                e.printStackTrace();
            }
            cardStatus = HttpUtils.getInstance().getuserpaperreport(
                    Const.URL + Const.GetUserPaperReportAPI, obj);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if ("100".equals(cardStatus.getCode())) {
                cardStatusList.add(cardStatus);
                // 答题时间
                String time = cardStatus.getBody().getUserDoneTime();
                // 正确答案率
                countRightStr = cardStatus.getBody().getQuesTypeName();
                if (time != null && !time.equals("")) {
                    tv_time.setText(time);
                } else {
                    tv_time.setText("00:00");
                }
                // 测试数据
                progress_bar.setProgress(80);
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(progress_bar, "progress", 1, 80);
                objectAnimator.setDuration(2000);
                objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                objectAnimator.start();
                if (countRightStr != null) {
                    countRight = Integer.parseInt(countRightStr);

                    mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);
                }
                // 交卷时间
                String strSubmit = cardStatus.getBody().getUserSubTime();
                if (strSubmit != null && !strSubmit.equals("")) {
                    sckd_tv.setText("交卷时间:" + strSubmit);
                }

                cardResultAdapter = new CardResultAdapter(CardResultActivity.this, cardStatusList);
                class_lv.setAdapter(cardResultAdapter);
                // 设置全部展开
                for (int i = 0; i < cardStatus.getBody().getPaperQuesGroupList().size(); i++) {
                    class_lv.expandGroup(i);
                }
                // 设置不可点击收缩
                class_lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return true;
                    }
                });
                // 滚动条消失
                mDialog.dismiss();
            }
            super.onPostExecute(result);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout: // 返回按钮
                finish();
                break;
            case R.id.wrongAnalytical_btn: // 错题解析
                //TODO
			Intent wrongIntent = new Intent(CardResultActivity.this,
					WrongAnalysisActivity.class);
			// 试卷id
			wrongIntent.putExtra("testPPKID", testPPKID);
			wrongIntent.putExtra("name", name);
			// 类型
			wrongIntent.putExtra("CardAnwserType", CardAnwserType);
			CardResultActivity.this.startActivity(wrongIntent);
                break;
            case R.id.allAnalytical_btn: // 全部解析
			Intent allIntent = new Intent(CardResultActivity.this,
					AnalysisActivity.class);
			// 试卷id
			allIntent.putExtra("testPPKID", testPPKID);
			allIntent.putExtra("name", name);
			allIntent.putExtra("itemName", itemName);
			// 类型
			allIntent.putExtra("CardAnwserType", CardAnwserType);
			CardResultActivity.this.startActivity(allIntent);
                break;
            case R.id.reset_txt: // 重做按钮监听
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        super.onDestroy();
    }

}
