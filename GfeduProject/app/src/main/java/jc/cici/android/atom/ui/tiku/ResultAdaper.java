package jc.cici.android.atom.ui.tiku;


import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;

import jc.cici.android.R;

public class ResultAdaper extends BaseAdapter {
    ArrayList<CardStatus.Body.PaperQuesGroupList.PaperQuesList> resultList;
    private Activity ctx;
    // 创建hashMap 用于记录位置
    private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    private GridView view_gridResult;

    public ResultAdaper
            (Activity ctx,
             ArrayList<CardStatus.Body.PaperQuesGroupList.PaperQuesList> resultList,
             GridView view_gridResult) {
        this.ctx = ctx;
        this.resultList = resultList;
        this.view_gridResult = view_gridResult;
    }

    @Override
    public int getCount() {
        Log.d("resultList.size()","" + resultList.size());
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        private Button btnResult;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        ViewHolder viewHolder;
            convertView = LayoutInflater.from(ctx)
                    .inflate(R.layout.result_item, null);
            viewHolder = new ViewHolder();
            viewHolder.btnResult = (Button) convertView
                    .findViewById(R.id.resultbtn_item);
        // 获取按钮文字
        String txt = String.valueOf(position + 1);
        // 设置文字
        viewHolder.btnResult.setText(txt);
        // 用户选择答案
        int testPaperAns = resultList.get(position).getQuesStatus();
        switch (testPaperAns) {
            case 1: // 答案正确
                viewHolder.btnResult.setTextColor(Color.parseColor("#ffffff"));
                viewHolder.btnResult
                        .setBackgroundResource(R.drawable.btn_yizuo_zhengque);
                break;
            case -1: // 答案错误
                viewHolder.btnResult.setTextColor(Color.parseColor("#ffffff"));
                viewHolder.btnResult
                        .setBackgroundResource(R.drawable.btn_yizuo_weibijiao);
                break;
            case 0: // 答案無法判斷
                viewHolder.btnResult.setTextColor(Color.parseColor("#dd5555"));
                viewHolder.btnResult
                        .setBackgroundResource(R.drawable.btn_weizuo_weibijiao);
                break;
            default:
                break;
        }
        viewHolder.btnResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                // gridView 按钮点击跳转
//                Intent jumpIntent = new Intent(ctx,
//                        AnalysisActivity.class);
//                // 传递当前跳转页面位置
//                jumpIntent.putExtra("currPosition", position);
//                // 用户id
//                jumpIntent.putExtra("userId", userId);
//                // 试卷id
//                jumpIntent.putExtra("testPPKID", testPPKID);
//                // 试卷名称
//                jumpIntent.putExtra("name", name);
//                jumpIntent.putExtra("itemName", itemName);
//                // 类型
//                jumpIntent.putExtra("CardAnwserType", CardAnwserType);
//                // 答案list
//                jumpIntent.putExtra("result", resultList);
//                CardResultActivity.this.startActivity(jumpIntent);
            }
        });
        return convertView;
    }

}
