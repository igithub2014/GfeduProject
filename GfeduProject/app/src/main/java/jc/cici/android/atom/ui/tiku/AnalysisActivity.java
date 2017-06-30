package jc.cici.android.atom.ui.tiku;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import jc.cici.android.R;
import jc.cici.android.atom.common.CommParam;

/**
 * 全部解析
 * Created by atom on 2017/6/29.
 */

public class AnalysisActivity extends FragmentActivity implements
        View.OnClickListener {
    // 用户id
    private String userId;
    // 试卷id
    private int testPPKID;
    // 标题名
    private String name;
    // 返回按钮
    private Button backBtn;
    // 所做题目名称
    private TextView title_txt;
    // 当前页码
    private TextView tv_papersCount;
    private TextView txt_error;
    private TextView txt_card;
    private TextView txt_favor;
    // viewPager
    private ViewPager analysisViewPager;
    // 初始化适配器对象
    private MyFragmentPageAdapter analysicAdapter;
    // 自定义进度条
    private AlertDialog mDialog;
    // 创建工具类对象
    private HttpUtils httpUtils = new HttpUtils();
    // 创建获取数据list
    private ArrayList<WrongAnalysisContent> analysisList = new ArrayList<WrongAnalysisContent>();
    private ArrayList<Fragment> FragmentList = new ArrayList<Fragment>();
    // 定义FragmentManager管理器对象
    private FragmentManager fm;
    // 定义当前位置信息
    private int pos;
    // 点击跳转位置
    private int currPos;
    private String jsonPara;

    // 类型
    private String CardAnwserType;

    private String parStr;
    private String itemName;

    // 答题卡
    private ArrayList<Card> cardList = new ArrayList<Card>();
    // 整數量
    private String count;

    private CommParam commParam;
    private WrongAnalysisContent analysisContent;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis_content);

        testPPKID = getIntent().getIntExtra("testPPKID", 0);
        name = getIntent().getStringExtra("name");
        itemName = getIntent().getStringExtra("itemName");
        currPos = getIntent().getIntExtra("currPosition", 0);
        // 初始化操作
        initSetting();
        // 加载数据
        showProcessDialog(AnalysisActivity.this,
                R.layout.loading_process_dialog_color);
        // 获取加载数据
        getAnalysisData();

    }

    /**
     * 通过task任务获取解析数据
     */
    private void getAnalysisData() {
        AnalysisTask analysisTask = new AnalysisTask();
        analysisTask.execute();
    }

    /**
     * 自定义进度条
     *
     * @param mContext
     * @param layout
     */
    private void showProcessDialog(AnalysisActivity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext,R.style.showdialog).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);

    }

    /**
     * 初始化view
     */
    private void initSetting() {
        // 初始化按钮
        backBtn = (Button) findViewById(R.id.backBtn);
        title_txt = (TextView) findViewById(R.id.title_txt);
//        tv_papersCount = (TextView) findViewById(R.id.tv_papersCount);
        // 初始化viewPager
        analysisViewPager = (ViewPager) findViewById(R.id.viewpagerAnalysis);
        txt_error = (TextView) findViewById(R.id.txt_error);
        txt_card = (TextView) findViewById(R.id.txt_card);
        txt_favor = (TextView) findViewById(R.id.txt_favor);

        Drawable drawable1 = getResources().getDrawable(R.drawable.tab_menu_card);
        drawable1.setBounds(0, 0, 36, 32);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_card.setCompoundDrawables(null, drawable1, null, null);//只放左边

        Drawable drawable2 = getResources().getDrawable(R.drawable.tab_menu_error);
        drawable2.setBounds(0, 0, 36, 36);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_error.setCompoundDrawables(null, drawable2, null, null);//只放左边

        Drawable drawable3 = getResources().getDrawable(R.drawable.tab_menu_favor);
        drawable3.setBounds(0, 0, 36, 35);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_favor.setCompoundDrawables(null, drawable3, null, null);//只放左边

        // 获取fragmentManger管理器
        fm = getSupportFragmentManager();

        commParam = new CommParam(this);

        backBtn.setOnClickListener(this);
        txt_error.setOnClickListener(this);
        txt_card.setOnClickListener(this);
        txt_favor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backBtn:// 返回按钮操作
                this.finish();
                break;
            case R.id.txt_favor:// 收藏按钮操作

                break;
            case R.id.txt_card:// 返回答题卡按钮操作
                this.finish();
                break;
            case R.id.txt_error: // 纠错按钮监听

                break;
            default:
                break;
        }
    }


    /**
     * 创建任务线程
     *
     * @author atom
     */
    class AnalysisTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            JSONObject obj = new JSONObject();
            try {
                obj.put("userId", commParam.getUserId());
                obj.put("oauth", commParam.getOauth());
                obj.put("client", commParam.getClient());
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("version", commParam.getVersion());
                obj.put("paperId", testPPKID);
                obj.put("appName", commParam.getAppname());
            } catch (Exception e) {
                e.printStackTrace();
            }
            analysisContent = HttpUtils.getInstance().getuserpapercarderror(
                    Const.URL + Const.GetUserPaperCardAllAPI, obj);
            if ("100".equals(analysisContent.getCode())) {
                if (analysisContent.getBody() != null && !"".equals(analysisContent)) {
                    analysisList.add(analysisContent);
                }
                if (analysisList != null && analysisList.get(0).getBody().size() > 0) {
                    int size = analysisList.get(0).getBody().size();
                    for (int i = 0; i < analysisList.size(); i++) {
                        /** update by atom 2016/12/22 **/
                        Fragment f = new TiKuContentFragment(
                                AnalysisActivity.this, changeHandler);
                        Bundle b = new Bundle();
                        // 题库类型
                        b.putString("type", "zhenTiAnalysisType");
                        // 试题答案类型
                        b.putString("AnswerType", analysisList.get(0).getBody().get(i)
                                .getQuesType());
                        b.putInt("num", analysisList.get(0).getBody().get(i)
                                .getQuesId());
                        // 传递标题
                        b.putString("name", name);
                        // 传递H5
                        b.putString("title", analysisList.get(0).getBody().get(i).getQuesUrl());
                        // 试题总数
                        b.putInt("size", size);
                        b.putString("CardAnwserType", CardAnwserType);
                        f.setArguments(b);
                        FragmentList.add(f);
                    }
                }
            } else {
                Toast.makeText(AnalysisActivity.this, analysisContent.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (analysisList != null && analysisList.size() > 0) {
                // 获取list大小
                int size = analysisList.size();
                // 添加适配器
                analysicAdapter = new MyFragmentPageAdapter(fm, FragmentList);
                ViewPagerScroller scroller = new ViewPagerScroller(
                        AnalysisActivity.this);
                scroller.initViewPagerScroll(analysisViewPager);
                analysisViewPager.setAdapter(analysicAdapter);
                analysisViewPager.setOnPageChangeListener(onPageChangeListener);

                // 设置标题名称
                title_txt.setText(name);
                // 获取总题数
                count = String.valueOf(size);

                if (analysisViewPager != null && currPos > 0) {
                    analysisViewPager.setCurrentItem(currPos);
                }
            } else {
                Toast.makeText(AnalysisActivity.this, "试题暂未上传",
                        Toast.LENGTH_SHORT).show();
                // 滚动条消失
                mDialog.dismiss();
                AnalysisActivity.this.finish();
            }

        }

    }

    /**
     * vp滑动监听
     */
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            // 获取当前页面标示
            pos = arg0;
//            // 转换为字符串
//            String per = String.valueOf(pos);
//            // 试题id
//            if (!"".equals(analysisList.get(arg0).getQuestionPKID())
//                    && analysisList.get(arg0).getQuestionPKID() != null) {
//                questionPKID = analysisList.get(arg0).getQuestionPKID();
//            }
        }
    };

    @Override
    protected void onResume() {

        super.onResume();
    }


    private Handler changeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int nextPage = (Integer) msg.obj;
                    if (analysisViewPager != null && nextPage >= 0) {
                        analysisViewPager.setCurrentItem(nextPage + 1);
                    }

                    break;
                case 1:
                    int jumpPage = (Integer) msg.obj;
                    if (analysisViewPager != null && jumpPage > 0) {
                        analysisViewPager.setCurrentItem(jumpPage);
                    } else if (jumpPage == 0) {
                        analysisViewPager.setCurrentItem(0);
                    }

                    break;
                // 多选题状态
                case 100:
                    int nextPage2 = (Integer) msg.obj;
                    if (analysisViewPager != null && nextPage2 >= 0) {
                        analysisViewPager.setCurrentItem(nextPage2 + 1);
                    }
                    break;
                case 10:
                    if (null != mDialog && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };
}
