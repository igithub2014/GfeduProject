package jc.cici.android.atom.ui.tiku;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import jc.cici.android.R;
import jc.cici.android.atom.common.CommParam;

public class WrongAnalysisActivity extends FragmentActivity implements
        OnClickListener {

    // 试卷id
    private int testPPKID;
    // 标题名
    private String name;
    // 返回按钮
    private Button backBtn;
    // 收藏按钮
    private TextView txt_favor;
    // 答题卡按钮
    private TextView txt_card;
    // 纠错文字
    private TextView txt_error;
    // 标题文字
    private TextView title_txt;
    // 页面数
    private TextView tv_papersCount;
    // 创建viewPager 对象
    private ViewPager viewpager_wrongAnalysis;
    // 初始化fragmentList 对象
    private ArrayList<Fragment> FragmentList = new ArrayList<Fragment>();
    // 定义FragmentManager管理器对象
    private FragmentManager fm;
    // 自定义定义进度条
    private AlertDialog mDialog;
    // 初始化适配器对象
    private MyFragmentPageAdapter wAnalysisAdapter;
    // 创建获取解析数据list
    private ArrayList<WrongAnalysisContent> wAnalyList = new ArrayList<WrongAnalysisContent>();
    // 类型
    private String CardAnwserType;
    protected int pos;
    // 总个数
    private String count;
    private CommParam commParam;
    private WrongAnalysisContent wrongAnalysisBean;

    private Handler changeHandler = new Handler() {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrong_analysis_layout);
        testPPKID = getIntent().getIntExtra("testPPKID", 0);
//		name = getIntent().getStringExtra("name");
        CardAnwserType = getIntent().getStringExtra("CardAnwserType");
        // 初始化操作
        initSetting();
        // 加载数据
        showProcessDialog(WrongAnalysisActivity.this,
                R.layout.loading_process_dialog_color);
        // 按钮设置监听
        backBtn.setOnClickListener(this);
        txt_card.setOnClickListener(this);
        txt_favor.setOnClickListener(this);
        txt_error.setOnClickListener(this);

        // 获取加载数据
        getwAnalysisData();
    }

    /**
     * 异步task获取解析数据
     */
    private void getwAnalysisData() {
        // 创建任务对象
        WAnalysisTask task = new WAnalysisTask();
        task.execute();

    }

    /**
     * 自定义进度条
     */
    private void showProcessDialog(WrongAnalysisActivity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);

    }

    /**
     * 获取控件id
     */
    private void initSetting() {
        // 初始化按钮
        backBtn = (Button) findViewById(R.id.backBtn);
        // 笔记卡
        txt_error = (TextView) findViewById(R.id.txt_error);
        // 提问 id
        txt_favor = (TextView) findViewById(R.id.txt_favor);
        // 答题卡id
        txt_card = (TextView) findViewById(R.id.txt_card);
        // 标题名称
        title_txt = (TextView) findViewById(R.id.title_txt);

        Drawable drawable1 = getResources().getDrawable(R.drawable.tab_menu_card);
        drawable1.setBounds(0, 0, 36, 32);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_card.setCompoundDrawables(null, drawable1, null, null);//只放左边

        Drawable drawable2 = getResources().getDrawable(R.drawable.tab_menu_error);
        drawable2.setBounds(0, 0, 36, 36);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_error.setCompoundDrawables(null, drawable2, null, null);//只放左边

        Drawable drawable3 = getResources().getDrawable(R.drawable.tab_menu_favor);
        drawable3.setBounds(0, 0, 36, 35);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_favor.setCompoundDrawables(null, drawable3, null, null);//只放左边

        // 页个数
        tv_papersCount = (TextView) findViewById(R.id.tv_papersCount);
        // 获取viewpager id
        viewpager_wrongAnalysis = (ViewPager) findViewById(R.id.viewpagerAnalysis_wrong);
        // 获取fragmentManger管理器
        fm = getSupportFragmentManager();
        commParam = new CommParam(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn: // 返回按钮
                this.finish();
                break;
            case R.id.txt_favor: // 提问按钮

                break;
            case R.id.txt_card: // 答题卡按钮
                this.finish();
                break;
            case R.id.txt_error: // 笔记按钮

                break;
            default:
                break;
        }
    }

    class WAnalysisTask extends AsyncTask<Void, Void, Void> {
        // 获取试题总个数
        private int size;

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("UserId", commParam.getUserId());
                obj.put("oauth", commParam.getOauth());
                obj.put("client", commParam.getClient());
                obj.put("TimeStamp", commParam.getTimeStamp());
                obj.put("version", commParam.getVersion());
                obj.put("PaperId", testPPKID);
                obj.put("appName", commParam.getAppname());
            } catch (Exception e) {
                e.printStackTrace();
            }
            wrongAnalysisBean = HttpUtils.getInstance().getuserpapercarderror(
                    Const.URL + Const.GetUserPaperCardErrorAPI, obj);

            if ("100".equals(wrongAnalysisBean.getCode())) {
                if (wrongAnalysisBean.getBody() != null && !"".equals(wrongAnalysisBean)) {
                    wAnalyList.add(wrongAnalysisBean);
                }
                if (wAnalyList != null && wAnalyList.get(0).getBody().size() > 0) {
                    size = wAnalyList.get(0).getBody().size();
                    for (int i = 0; i < size; i++) {
                        // 创建fragment对象
                        /** update by atom 2016/12/22 **/
                        Fragment f = new TiKuContentFragment(
                                WrongAnalysisActivity.this, changeHandler);
                        Bundle b = new Bundle();
                        // 题库类型
                        b.putString("type", "zhenTiAnalysisType");
                        // 试题答案类型
                        b.putString("AnswerType", wAnalyList.get(0).getBody().get(i)
                                .getQuesType());
                        b.putInt("num", wAnalyList.get(0).getBody().get(i)
                                .getQuesId());
                        // 传递标题
                        b.putString("name", name);
                        // 传递H5
                        b.putString("title", wAnalyList.get(0).getBody().get(i).getQuesUrl());
                        // 试题总数
                        b.putInt("size", size);
                        b.putString("CardAnwserType", CardAnwserType);
                        f.setArguments(b);
                        FragmentList.add(f);
                    }
                }
            } else {
                Toast.makeText(WrongAnalysisActivity.this, "错题解析加载异常",
                        Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (null != wAnalyList && wAnalyList.get(0).getBody().size() > 0) {
                // fragement 添加适配器
                wAnalysisAdapter = new MyFragmentPageAdapter(fm, FragmentList);
                ViewPagerScroller scroller = new ViewPagerScroller(
                        WrongAnalysisActivity.this);
                scroller.initViewPagerScroll(viewpager_wrongAnalysis);
                viewpager_wrongAnalysis.setAdapter(wAnalysisAdapter);
                viewpager_wrongAnalysis
                        .setOnPageChangeListener(onPageChangeListener);

                // 设置标题名称
                title_txt.setText(name);
                // 获取总题数
                count = String.valueOf(size);

            } else {
                Toast.makeText(WrongAnalysisActivity.this, "没有错误内容",
                        Toast.LENGTH_SHORT).show();
                // 滚动条消失
                mDialog.dismiss();
                WrongAnalysisActivity.this.finish();
            }
        }

    }

    OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            pos = arg0;

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}
