package jc.cici.android.atom.ui.tiku;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jc.cici.android.R;
import jc.cici.android.atom.common.CommParam;

public class CardAnswer extends Activity implements OnClickListener {

    private GridView cardGrid;
    private Button backBtn_card;
    private Button sumbitBtn;
    private ArrayList<Card> mCardList;
    private AnswerAdapter adapter;
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    private int count = 0;

    private int userId;// 用户id
    private int testPaperID; // 试卷id
    private String testTime; // 测试时间
    private HttpUtils httpUtils = new HttpUtils();
    private String name; // 当前学习标题

    // 传递过来的类型
    private String CardAnwserType;
    private Dialog mDialog;
    private SubmitQuesAnswer submitQuesAnswer;
    private CommParam commParam;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view);
        mCardList = (ArrayList<Card>) getIntent().getSerializableExtra(
                "pageStatus");
        userId = getIntent().getIntExtra("userId", 0);
        testPaperID = getIntent().getIntExtra("TestPaperID", 0);
        testTime = getIntent().getStringExtra("time");
        CardAnwserType = getIntent().getStringExtra("CardAnwserType");

        System.out.println("答题卡获取的 : " + CardAnwserType);
        // 获取传递当前标题
        name = getIntent().getStringExtra("name");
        cardGrid = (GridView) findViewById(R.id.gridBtn_view);
        backBtn_card = (Button) findViewById(R.id.backbtn_card);
        sumbitBtn = (Button) findViewById(R.id.submit_card);
        commParam = new CommParam(this);
        backBtn_card.setOnClickListener(this);
        sumbitBtn.setOnClickListener(this);
        // 加载数据
        showProcessDialog(CardAnswer.this,
                R.layout.loading_process_dialog_color);
    }

    /**
     * 自定义进度条
     */
    private void showProcessDialog(Context ctx, int layout) {
        mDialog = new AlertDialog.Builder(ctx, R.style.showdialog).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
        adapter = new AnswerAdapter();
        cardGrid.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backbtn_card: // 返回按钮监听
                this.finish();

                break;
            case R.id.submit_card: // 提交按钮监听
                final Dialog dialog = new Dialog(CardAnswer.this,
                        R.style.SubmitDialogStyle);
                dialog.setContentView(R.layout.answerz_submit_dialog);
                TextView txt = (TextView) dialog.findViewById(R.id.txt_dialog);
                Button go_Btn = (Button) dialog.findViewById(R.id.go_on);
                Button ensure_Btn = (Button) dialog.findViewById(R.id.sumbit_btn);

                for (Card c : mCardList) {
                    if (c.isStatus() == true) {
                        count++;
                    }
                }
                if (count < mCardList.size()) {
                    txt.setText("您还有题目未做完,您确定要交卷吗？");
                } else {
                    txt.setText("您确定要交卷吗？");
                }

                dialog.show();
                go_Btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                ensure_Btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (httpUtils.isNetworkConnected(CardAnswer.this)) {
                            SubmitTask task = new SubmitTask();
                            task.execute();
                        } else {
                            Toast.makeText(CardAnswer.this, "提交失败，请检查网络设置！",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                break;

            default:
                break;
        }
    }

    class SubmitTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... param) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("userId", commParam.getUserId());
                obj.put("appName", commParam.getAppname());
                obj.put("oauth", commParam.getOauth());
                obj.put("client", commParam.getClient());
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("version", commParam.getVersion());
                obj.put("paperId", testPaperID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            submitQuesAnswer = HttpUtils.getInstance().getSubmitQuesAnswer(
                    Const.URL + Const.SubmitQuesanswerAPI, obj);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if ("100".equals(submitQuesAnswer.getCode())) {
                // 答案提交成功
                Toast.makeText(CardAnswer.this, "答案提交成功", Toast.LENGTH_SHORT)
                        .show();
                Intent cIntent = new Intent(CardAnswer.this,
                        CardResultActivity.class);
                // 传递试卷id
                cIntent.putExtra("TestPPKID", testPaperID);
                cIntent.putExtra("CardAnwserType", CardAnwserType);
                TiKuContentFragment.handler.sendEmptyMessage(0);
                CardAnswer.this.startActivity(cIntent);
                finish();
            } else {
                // 答案提交失败
                Toast.makeText(CardAnswer.this, "答案提交失败", Toast.LENGTH_SHORT)
                        .show();
            }
            super.onPostExecute(result);
        }

    }

    class AnswerAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mCardList.size();
        }

        @Override
        public Object getItem(int postion) {

            return postion;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        class ViewHolder {
            private ImageView flagImg;
            private Button sButton;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder viewHolder;
            if (!viewMap.containsKey(position) || viewMap.get(position) == null) {
                convertView = LayoutInflater.from(CardAnswer.this).inflate(
                        R.layout.card_btn, null);
                viewHolder = new ViewHolder();
                viewHolder.flagImg = (ImageView) convertView
                        .findViewById(R.id.flag_img);
                viewHolder.sButton = (Button) convertView
                        .findViewById(R.id.asbtn_card);
                convertView.setTag(viewHolder);
                viewMap.put(position, convertView);
            } else {
                convertView = viewMap.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (viewMap.size() > 20) {
                synchronized (convertView) {
                    for (int i = 1; i < cardGrid.getFirstVisiblePosition() - 3; i++) {
                        viewMap.remove(i);
                    }
                    for (int i = cardGrid.getLastVisiblePosition() + 3; i < getCount(); i++) {
                        viewMap.remove(i);
                    }
                }
            }
            // 获取按钮文字
            String txt = position + 1 + "";
            // 设置文字
            viewHolder.sButton.setText(txt);
            // 设置文字大小
            viewHolder.sButton.setTextSize(21);
            // 获取按钮点击状态
            if (position == mCardList.get(position).getPosition()) {// 当前positon与点击position相同
                // 获取选项是否选择
                boolean status = mCardList.get(position).isStatus();
                // 获取是否设置标记
                final boolean flag = mCardList.get(position).isFlag();
                if (status) { // 选择(已作答)
                    viewHolder.sButton
                            .setBackgroundResource(R.drawable.btn_yizuo_weibijiao);
                    viewHolder.sButton
                            .setTextColor(Color.parseColor("#ffffff"));
                }
                if (flag & status) { // 已标记
                    viewHolder.sButton
                            .setBackgroundResource(R.drawable.btn_yizuo_bijiao);
                    viewHolder.sButton
                            .setTextColor(Color.parseColor("#ffffff"));
                } else if (flag) {
                    viewHolder.sButton
                            .setBackgroundResource(R.drawable.btn_weizuo_yibijiao);
                    viewHolder.sButton
                            .setTextColor(Color.parseColor("#dd5555"));
                }
                viewHolder.sButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("My_Fragment".equals(CardAnwserType)) {
                            Intent cIntent = new Intent(CardAnswer.this,
                                    MyQuestionActivity.class);
                            cIntent.putExtra("pageID", position);
                            cIntent.putExtra("flag", flag);
                            setResult(1, cIntent);
                            finish();
                        }
                    }
                });
            }
            mDialog.dismiss();
            return convertView;
        }

    }
}
