package jc.cici.android.atom.ui.tiku;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import jc.cici.android.R;
import jc.cici.android.atom.common.CommParam;


public class MyQuestionActivity extends BaseActivity implements
        OnClickListener, TiKuContentFragment.TimeData {

    private MyFragmentPageAdapter mAdapter;
    // 数据请求task
    private MyQuestionTask myQuestionmTask;

    // 真题内容list
    private ContentZhenTi ContentZhenTi;
    private ArrayList<ContentZhenTi> contentZTList = new ArrayList<ContentZhenTi>();
    //
    private ArrayList<Fragment> FragmentList = new ArrayList<Fragment>();
    // 工具类
    private HttpUtils httpUtils = new HttpUtils();
    // fragment管理器
    private FragmentManager fm;

    // 创建计时器对象
    private Timer timter = new Timer();
    TimerTask timerTask;
    // 创建Handler 对象
    private Handler handler;
    private long i = 0;
    private boolean isStop = false;
    private boolean isGoing = false;
    private Dialog dialog;

    private String itemName;
    // 用户id
    private int userId;
    // 试卷id
    private int paperId;

    private Dialog mDialog;

    // 返回按钮
    private Button backBtn;
    // 标题文字
    private TextView title_txt;
    // 网络提示文字
    private TextView netMobileTV;
    //    // 试题数量
//    private TextView tv_papersCount;
    // viewPgaer 内容
    private ViewPager viewPager;
    // 标记按钮
    private TextView txt_mark;
    // 答题卡按钮
    private TextView txt_card;
    // 时间按钮
    private TextView txt_time;
    // 填空题的当前位置
    private int currPos;

    private Handler changeHandler = new Handler() {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int nextPage = (Integer) msg.obj;
                    if (viewPager != null && nextPage >= 0) {
                        viewPager.setCurrentItem(nextPage + 1);
                    }
                    break;
                case 1:// 点击跳转
                    int jumpPage = (Integer) msg.obj;
                    if (viewPager != null && jumpPage > 0) {
                        viewPager.setCurrentItem(jumpPage);
                    } else if (jumpPage == 0) {
                        viewPager.setCurrentItem(0);
                    }
                    break;
                case 2: // 设置时间戳
                    if (haveTime != null) {
                        t--;
                        int totalSec = (int) t;
                        int min = (totalSec / 60);
                        int sec = (totalSec % 60);
                        time = String.format("%1$02d:%2$02d", min, sec);
                        txt_time.setText(time);
                    } else {
                        i++;
                        int totalSec = (int) i;
                        int min = (totalSec / 60);
                        int sec = (totalSec % 60);
                        time = String.format("%1$02d:%2$02d", min, sec);
                        txt_time.setText(time);
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

    private ArrayList<Card> cardList = new ArrayList<Card>();
    // 当前页面位置
    private int pos;
    // 是否添加标记 默认未添加
    private boolean flag = false;
    public String time;
    // 答题时间字符串
    private String title;
    // 提交答题时间
    private static String mTime;
    private String CardAnwserType;
    // 获取考卷时间
    private String haveTime;
    // 考卷时间转化为时间值
    private long t;
    // 试卷总题数
    private String count;
    // 填空题提价返回结果
    private String callBackStr;
    // 试题id
    private int questionPKID;
    private CommParam param;
    // 判断用户是否滑动过
    private boolean isTouchPager = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startdo_papers);
        Bundle startBundle = getIntent().getExtras();
//        // 获取试卷id
//        paperId = startBundle.getInt("paperId");
        // 测试数据
        paperId = 58;
//        // 获取标题
//        title = (String) startBundle.get("title");
        // 测试数据
        title = "知识点测试";

//        // 获取试卷名称
//        itemName = (String) startBundle.get("itemName");
//        userId = (String) startBundle.get("userId");
//        testPaKID = (String) startBundle.get("testPaKID");
//        CardAnwserType = getIntent().getStringExtra("CardAnwserType");
//        // 获取时间
//        haveTime = getIntent().getStringExtra("TestPaAllLong");
//        System.out.println(" ---- : " + haveTime);
//        if (haveTime != null) {
//            t = Long.parseLong(haveTime);
//        }
        // 加载数据
        showProcessDialog(MyQuestionActivity.this,
                R.layout.loading_process_dialog_color);
        // 初始化view
        initSetting();
        // 获取真题数据
        GetZhenTiData();
    }

    /**
     * 获取view中各个控件id
     */
    private void initSetting() {
        backBtn = (Button) findViewById(R.id.backBtn);
        title_txt = (TextView) findViewById(R.id.title_txt);
        // 网络提示文字
        netMobileTV = (TextView) findViewById(R.id.netMobileTV);
//        tv_papersCount = (TextView) findViewById(R.id.tv_papersCount);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        txt_mark = (TextView) findViewById(R.id.txt_mark);
        txt_card = (TextView) findViewById(R.id.txt_card);
        txt_time = (TextView) findViewById(R.id.txt_time);

        Drawable drawable1 = getResources().getDrawable(R.drawable.ic_time_nomal);
        drawable1.setBounds(0, 0, 34, 38);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_time.setCompoundDrawables(null, drawable1, null, null);//只放左边

        Drawable drawable2 = getResources().getDrawable(R.drawable.ic_card_nomal);
        drawable2.setBounds(0, 0, 36, 32);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_card.setCompoundDrawables(null, drawable2, null, null);//只放左边

        Drawable drawable3 = getResources().getDrawable(R.drawable.tab_menu_bg);
        drawable3.setBounds(0, 0, 36, 36);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_mark.setCompoundDrawables(null, drawable3, null, null);//只放左边

        // 关闭预加载，默认一次只加载一个Fragment
        viewPager.setOffscreenPageLimit(0);
        // 获取fragment管理器
        fm = getSupportFragmentManager();
        // 设置监听
        backBtn.setOnClickListener(this);
        txt_mark.setOnClickListener(this);
        txt_card.setOnClickListener(this);
        txt_time.setOnClickListener(this);
        // 创建参数对象
        param = new CommParam(this);
        userId = param.getUserId();

    }

    /**
     * 重新启动计时
     */
    private void restart() {
        if (timter != null) {
            timter.cancel();
            timter = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        isStop = false;
        isGoing = false;
        start();
    }

    /**
     * 计时器暂停
     */
    private void stop() {
        isStop = !isStop;
    }

    /**
     * 开始计时
     */
    private void start() {
        if (isGoing) {
            isStop = false;
            return;
        }
        if (timter == null) {
            timter = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                public void run() {
                    if (isStop) {
                        return;
                    }
                    changeHandler.sendEmptyMessage(2);
                }
            };
        }
        if (timter != null && timerTask != null) {
            timter.schedule(timerTask, 0, 1000);

        }
        isGoing = true;
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

    public void GetZhenTiData() {
        if (httpUtils.isNetworkConnected(this)) {
            myQuestionmTask = new MyQuestionTask();
            myQuestionmTask.execute();
        } else {
            netMobileTV.setVisibility(View.VISIBLE);
        }

    }

    class MyQuestionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            try {

                obj.put("userId", param.getUserId());
                obj.put("oauth", param.getOauth());
                obj.put("client", param.getClient());
                obj.put("timeStamp", param.getTimeStamp());
                obj.put("version", param.getVersion());
                obj.put("paperId", paperId);
                obj.put("appName", param.getAppname());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ContentZhenTi = HttpUtils.getInstance().getTestResultInfo(
                    Const.URL + Const.ExamPaperInfo, obj);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(ContentZhenTi.getCode())) {
                contentZTList.add(ContentZhenTi);
                int size = contentZTList.get(0).getBody().getPaperQuesCardList().size();
                for (int i = 0; i < size; i++) {
                    Card card = new Card();
                    // 设置答题卡个数
                    // 如果已经答题了，设置状态值为true
                    if (!"".equals(contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesUserAnswer())) {
                        card.setStatus(true);
                    } else {// 否则设置为false
                        // 设置点击状态，默认为未点击
                        card.setStatus(false);
                    }
                    card.setSize(i);
                    // 设置标记，默认为未标记
                    card.setFlag(false);
                    // 设置初始状态位置
                    card.setPosition(i);
                    cardList.add(card);
                    /** update by atom 2016/12/21 **/
                    Fragment f = new TiKuContentFragment(
                            MyQuestionActivity.this, changeHandler);
                    /** update by atom 2016/12/21 **/
                    Bundle b = new Bundle();
                    b.putString("type", "zhenTiType");
                    b.putString("AnswerType", contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesType());
                    questionPKID = contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesId();
                    // 问题id
                    b.putInt("num", questionPKID);
                    // 传递标题
                    b.putString("name", title);
                    /** update by atom 2016/12/21 **/
                    b.putString("title", contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesUrl());
                    b.putInt("optionCount", contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesOptionCount());
                    /** update by atom 2016/12/21 **/
//                     b.putString("content", contentZTList.get(0).getBody().getQuesAnswerChoice());
                    // 传递图片列表
//                    b.putStringArrayList("mImageList", contentZTList.get(i).getImgList());
                    b.putInt("userId", userId);
                    b.putInt("testPaKID", paperId);
                    b.putInt("currPage", i);
                    b.putInt("size", size);
                    b.putSerializable("cardList", cardList);
                    b.putSerializable("CardAnwserType", CardAnwserType);
                    // 传递用户答案
                    b.putString("QuesUserAnswer", contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesUserAnswer());
                    f.setArguments(b);
                    FragmentList.add(f);
                }

                String jumpStr = contentZTList.get(0).getBody().getLastDoNo();
                int jAns = Integer.parseInt(jumpStr);
                mAdapter = new MyFragmentPageAdapter(fm, FragmentList);
                ViewPagerScroller scroller = new ViewPagerScroller(
                        MyQuestionActivity.this);
                scroller.initViewPagerScroll(viewPager);
                viewPager.setAdapter(mAdapter);
                viewPager.setOnPageChangeListener(onPageChangeListener);
                // 获取试题总数量
                count = String.valueOf(size);
                title_txt.setText(itemName);
//                tv_papersCount.setText("1/" + count);
                // 启动时间戳
                start();
                if (jAns != 0) {
                    viewPager.setCurrentItem(jAns - 1);
                }

            } else {
                // 滚动条消失
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), "试题暂未上传",
                        Toast.LENGTH_SHORT).show();
            }

        }

        /**
         * vp滑动监听
         */
        OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
            private boolean flag;

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        //拖的时候才进入下一页
                        flag = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        flag = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        /**
                         * 判断是不是最后一页，同是是不是拖的状态
                         */
                        if (viewPager.getCurrentItem() == mAdapter.getCount() - 1 && !flag) {
                            goToCardAnswerActivity();
                        }
                        flag = true;
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int arg0) {

                isTouchPager = true;
                pos = arg0 + 1;
//                String per = String.valueOf(pos);
//                tv_papersCount.setText(per + "/" + count);
                // 答题时间赋值给mTime用于提交后台
                if (haveTime != null) {
                    long aTime = Long.parseLong(haveTime);
                    long s = aTime - t;
                    int totalSec = (int) s;
                    int min = (totalSec / 60);
                    int sec = (totalSec % 60);
                    String submitTime = String
                            .format("%1$02d:%2$02d", min, sec);
                    mTime = submitTime;
                    System.out.println("mTime >>>:" + mTime);
                } else {
                    mTime = time;
                }
                // 设置标记图片
                if (cardList.get(arg0).isFlag()) { // 标记情况
                    txt_mark.setSelected(true);
                } else { // 未标记情况
                    txt_mark.setSelected(false);
                }

//                String asType = null;
//                if (arg0 == 0) {
//                    // 设置当前位置
//                    currPos = 0;
//                    asType = contentZTList.get(0).getBody().getPaperQuesCardList().get(arg0).getQuesType();
//                    questionPKID = contentZTList.get(0).getBody().getPaperQuesCardList().get(arg0).getQuesId();
//                } else {
//                    // 设置当前位置
//                    currPos = arg0 - 1;
//                    asType = contentZTList.get(0).getBody().getPaperQuesCardList().get(arg0).getQuesType();
//                    questionPKID = contentZTList.get(0).getBody().getPaperQuesCardList().get(arg0).getQuesId();
//                }
//                if ("4".equals(asType)) { // 判断当前滑动类型为填空题类型情况
//                    Intent it = new Intent();
//                    it.setAction("com.tiankong.braodcast");
//                    MyQuestionActivity.this.sendBroadcast(it);
//                    // 设置选中
//                    for (Card c : cardList) {
//                        if (c.getPosition() == currPos) {
//                            c.setStatus(true);
//                            cardList.set(currPos, c);
//                        }
//                    }
//
//                }
            }

        };
    }

    /**
     * 最后一页跳转答题卡
     */
    private void goToCardAnswerActivity() {

        Intent cardAswerIntent = new Intent(this,
                CardAnswer.class);
        cardAswerIntent.putExtra("pageStatus", cardList);
        cardAswerIntent.putExtra("userId", userId);
        cardAswerIntent.putExtra("testPaperID", paperId);
        // 传递标题
        cardAswerIntent.putExtra("name", title);
        cardAswerIntent.putExtra("time", time);
        cardAswerIntent.putExtra("CardAnwserType", "My_Fragment");
        startActivityForResult(cardAswerIntent, 1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
    }

    /**
     * 重置所有文本选中状态
     */
    public void selected() {
        txt_mark.setSelected(false);
        txt_card.setSelected(false);
        txt_time.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:// 返回按钮
                stop();
                final Dialog dialog_back = new Dialog(MyQuestionActivity.this,
                        R.style.backDialog);
                dialog_back.setContentView(R.layout.dialog_back);
                Button miss_Btn = (Button) dialog_back
                        .findViewById(R.id.miss_button_back);
                Button sure_Btn = (Button) dialog_back
                        .findViewById(R.id.sumbit_btn_back);
                dialog_back.show();
                sure_Btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        restart();
                        dialog_back.dismiss();
                        MyQuestionActivity.this.finish();
                    }
                });
                miss_Btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        restart();
                        dialog_back.dismiss();
                    }
                });
                break;
            case R.id.txt_mark:// 标记按钮
                for (Card card : cardList) {
                    boolean f = cardList.get(card.getPosition()).isFlag();
                    if (isTouchPager == false) { // 未滑动情况
                        if (card.getPosition() == 0 && pos == 0) {
                            if (f) {
                                card.setFlag(false);
                                Toast.makeText(this, "取消标记", Toast.LENGTH_LONG)
                                        .show();
                                txt_mark.setSelected(false);
                            } else {
                                Toast.makeText(this, "标记成功", Toast.LENGTH_LONG)
                                        .show();
                                card.setFlag(true);
                                txt_mark.setSelected(true);
                            }
                            cardList.set(0, card);

                        }
                        if (card.getPosition() == pos - 1) {

                            if (f) {
                                Toast.makeText(this, "取消标记", Toast.LENGTH_LONG)
                                        .show();
                                card.setFlag(false);
                                txt_mark.setSelected(false);
                            } else {
                                Toast.makeText(this, "标记成功", Toast.LENGTH_LONG)
                                        .show();
                                card.setFlag(true);
                                txt_mark.setSelected(true);
                            }
                            cardList.set(pos - 1, card);
                        }
                    } else { // 滑动情况
                        if (card.getPosition() == 0 && pos == 1) {
                            if (f) {
                                card.setFlag(false);
                                Toast.makeText(this, "取消标记", Toast.LENGTH_LONG)
                                        .show();
                                txt_mark.setSelected(false);
                            } else {
                                Toast.makeText(this, "标记成功", Toast.LENGTH_LONG)
                                        .show();
                                card.setFlag(true);
                                txt_mark.setSelected(true);
                            }
                            cardList.set(0, card);
                        }

                        if (card.getPosition() == pos - 1) {

                            if (f) {
                                card.setFlag(false);
                                Toast.makeText(this, "取消标记", Toast.LENGTH_LONG)
                                        .show();
                                txt_mark.setSelected(false);
                            } else {
                                Toast.makeText(this, "标记成功", Toast.LENGTH_LONG)
                                        .show();
                                card.setFlag(true);
                                txt_mark.setSelected(true);
                            }
                            cardList.set(pos - 1, card);

                        }
                    }

                }

                break;
            case R.id.txt_card:// 答题卡按钮
                if (cardList != null && cardList.size() > 0) {
                    Intent asIntent = new Intent(this, CardAnswer.class);
                    asIntent.putExtra("pageStatus", cardList);
                    asIntent.putExtra("userId", userId);
                    asIntent.putExtra("TestPaperID", paperId);
                    if (haveTime != null) {
                        asIntent.putExtra("time", mTime);
                    } else {
                        asIntent.putExtra("time", time);
                    }
                    // 传递学习标题
                    asIntent.putExtra("name", title);
                    if ("KY_Specia_Fragment".equals(CardAnwserType)) {
                        asIntent.putExtra("CardAnwserType", "KY_Specia_Fragment");
                    } else {
                        asIntent.putExtra("CardAnwserType", "My_Fragment");
                    }

                    System.out.println("答题卡传递 : " + CardAnwserType);
                    // 启动带返回值得Intent 其中 1表示请求的requsetCode
                    startActivityForResult(asIntent, 2);
                    overridePendingTransition(R.anim.slide_bottom_in, 0);
                }
                break;
            case R.id.txt_time: // 计时按钮监听
                txt_time.setSelected(true);
                stop();
                dialog = new Dialog(MyQuestionActivity.this,
                        R.style.SubmitDialogStyle);
                dialog.setContentView(R.layout.pause_time_dialog);
                dialog.setCanceledOnTouchOutside(false);
                // 继续做题
                Button go_Btn = (Button) dialog.findViewById(R.id.go_on);
                go_Btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        restart();
                        dialog.dismiss();
                        txt_time.setSelected(false);

                    }
                });
                dialog.show();
                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("请求的requsetCode相同 ");
        if (resultCode == 1) {// 获取返回结果
            if (requestCode == 2) { // 请求的requsetCode相同
                int pageId = data.getIntExtra("pageID", 0);
                flag = data.getBooleanExtra("flag", false);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = pageId;
                changeHandler.sendMessage(msg);
            } else if (requestCode == 1) { // 请求的requsetCode相同
                int pageId = data.getIntExtra("pageID", 0);
                flag = data.getBooleanExtra("flag", false);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = pageId;
                changeHandler.sendMessage(msg);
            }
        }
    }


    public String getTime() {
        if (mTime != null) { // 答题时间不为0情况
            return mTime;
        } else {
            return "00:00";
        }
    }

    protected void onDestroy() {
        timter.cancel();
        this.finish();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 返回按钮设置监听
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            stop();
            final Dialog dialog_back = new Dialog(MyQuestionActivity.this,
                    R.style.backDialog);
            dialog_back.setContentView(R.layout.dialog_back);
            Button miss_Btn = (Button) dialog_back
                    .findViewById(R.id.miss_button_back);
            Button sure_Btn = (Button) dialog_back
                    .findViewById(R.id.sumbit_btn_back);
            dialog_back.show();
            sure_Btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    restart();
                    dialog_back.dismiss();

                    MyQuestionActivity.this.finish();
                }
            });
            miss_Btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    restart();
                    dialog_back.dismiss();
                }
            });
            return true;
        }
        return true;
    }

    @Override
    public void onNetChange(int netMobile) {
        // 网络状态变化时的操作
        if (netMobile == NetUtil.NETWORK_NONE) {
            netMobileTV.setVisibility(View.VISIBLE);
        } else {
            netMobileTV.setVisibility(View.GONE);

        }
    }
}
