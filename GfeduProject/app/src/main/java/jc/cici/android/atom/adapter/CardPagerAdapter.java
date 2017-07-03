package jc.cici.android.atom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jc.cici.android.R;
import jc.cici.android.atom.bean.CardItem;
import jc.cici.android.atom.ui.note.NoteAllActivity;
import jc.cici.android.atom.ui.note.QuestionAllActivity;
import jc.cici.android.atom.ui.study.CourseDetialActivity;
import jc.cici.android.atom.utils.ToolUtils;


/**
 * 滑动卡片适配器
 * Created by atom on 2017/5/10.
 */

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;
    private Context mCtx;
    private int mClassID;

    public CardPagerAdapter(Context context,int classID) {
        mCtx = context;
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        mClassID = classID;
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.card_adapter, container, false);
        container.addView(view);
        bind(mData.get(position), view, position);

        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    /**
     * 获取id 填充数据
     *
     * @param cardItem
     * @param view
     */
    private void bind(CardItem cardItem, View view, int position) {
        // 是否开始标记
        ImageView flagCard_Img = (ImageView) view.findViewById(R.id.flagCard_Img);
        // 课程类型
        TextView courseType_Txt = (TextView) view.findViewById(R.id.courseType_Txt);
        // 课程学时阶段
        TextView courseDru_Txt = (TextView) view.findViewById(R.id.courseDru_Txt);
        // 课程总天数
        TextView courseDay_Txt = (TextView) view.findViewById(R.id.courseDay_Txt);
        // 课程类型
        TextView typeCard_Txt = (TextView) view.findViewById(R.id.typeCard_Txt);
        // 类型图标
        ImageView icon_typeCard = (ImageView) view.findViewById(R.id.icon_typeCard);
        // 课表图片
        ImageView icon_form_Imag = (ImageView) view.findViewById(R.id.icon_form_Imag);
        // 问题图片
        ImageView icon_ques_Imag = (ImageView) view.findViewById(R.id.icon_ques_Imag);
        // 笔记图片
        ImageView icon_note_Imag = (ImageView) view.findViewById(R.id.icon_note_Imag);
        // 资料图片
        ImageView icon_material_Imag = (ImageView) view.findViewById(R.id.icon_material_Imag);
        // 课表按钮
        ImageView goClickableFrom_Img = (ImageView) view.findViewById(R.id.goClickableFrom_Img);
        // 问题按钮
        ImageView goClickableQues_Img = (ImageView) view.findViewById(R.id.goClickableQues_Img);
        // 笔记按钮
        ImageView goClickableNote_Img = (ImageView) view.findViewById(R.id.goClickableNote_Img);
        // 资料按钮
        ImageView goClickableMat_Img = (ImageView) view.findViewById(R.id.goClickableMat_Img);

        // 填充内容
        courseType_Txt.setText(ToolUtils.replaceAllChar(cardItem.getStageName()));
        courseDru_Txt.setText(cardItem.getStageStartTime().replaceAll("-",".") + "-" + cardItem.getStageEndTime().replaceAll("-","."));
        courseDay_Txt.setText(cardItem.getStagePeriod() + "学时");
        typeCard_Txt.setText(cardItem.getStageType());

        if ("面授".equals(cardItem.getStageType())) {
            icon_typeCard.setBackgroundResource(R.drawable.icon_typecard_ms);
        } else if ("直播".equals(cardItem.getStageType())) {
            icon_typeCard.setBackgroundResource(R.drawable.icon_typecard_zb);
        } else if ("在线".equals(cardItem.getStageType())) {
            icon_typeCard.setBackgroundResource(R.drawable.icon_typecard_zx);
        }
        // 课表
        // 问题标记
        int problemFlag = cardItem.getStageProblem();
        if (problemFlag == 1) { // 已启用状态
            if ("面授".equals(cardItem.getStageType())) {
                icon_ques_Imag.setBackgroundResource(R.drawable.icon_problem_ms);
                goClickableQues_Img.setBackgroundResource(R.drawable.icon_goclick_ms);
                goClickableQues_Img.setEnabled(true);
                goClickableQues_Img.setClickable(true);
            } else if ("直播".equals(cardItem.getStageType())) {
                icon_ques_Imag.setBackgroundResource(R.drawable.icon_problem_zb);
                goClickableQues_Img.setBackgroundResource(R.drawable.icon_goclick_zb);
                goClickableQues_Img.setEnabled(true);
                goClickableQues_Img.setClickable(true);
            } else if ("在线".equals(cardItem.getStageType())) {
                icon_ques_Imag.setBackgroundResource(R.drawable.icon_problem_zx);
                goClickableQues_Img.setBackgroundResource(R.drawable.icon_goclick_zx);
                goClickableQues_Img.setEnabled(true);
                goClickableQues_Img.setClickable(true);
            }
        } else if (problemFlag == 0) { // 未启用状态
            goClickableQues_Img.setBackgroundResource(R.drawable.icon_go_normal);
            goClickableQues_Img.setEnabled(false);
            goClickableQues_Img.setClickable(false);
        }
        // 笔记标记
        int noteFlag = cardItem.getStageNote();
        if (noteFlag == 1) { // 已启用状态
            if ("面授".equals(cardItem.getStageType())) {
                icon_note_Imag.setBackgroundResource(R.drawable.icon_note_ms);
                goClickableNote_Img.setBackgroundResource(R.drawable.icon_goclick_ms);
                goClickableNote_Img.setEnabled(true);
                goClickableNote_Img.setClickable(true);
            } else if ("直播".equals(cardItem.getStageType())) {
                icon_note_Imag.setBackgroundResource(R.drawable.icon_note_zb);
                goClickableNote_Img.setBackgroundResource(R.drawable.icon_goclick_zb);
                goClickableNote_Img.setEnabled(true);
                goClickableNote_Img.setClickable(true);
            } else if ("在线".equals(cardItem.getStageType())) {
                icon_note_Imag.setBackgroundResource(R.drawable.icon_note_zx);
                goClickableNote_Img.setBackgroundResource(R.drawable.icon_goclick_zx);
                goClickableNote_Img.setEnabled(true);
                goClickableNote_Img.setClickable(true);
            }
        } else if (noteFlag == 0) { // 未启用状态
            goClickableNote_Img.setBackgroundResource(R.drawable.icon_go_normal);
            goClickableNote_Img.setEnabled(false);
            goClickableNote_Img.setClickable(false);
        }
        // 资料标记
        int materialFlag = cardItem.getStageInformation();
        if (materialFlag == 1) { // 已启用状态
            if ("面授".equals(cardItem.getStageType())) {
                icon_material_Imag.setBackgroundResource(R.drawable.icon_material_ms);
                goClickableMat_Img.setBackgroundResource(R.drawable.icon_goclick_ms);
                goClickableMat_Img.setEnabled(true);
                goClickableMat_Img.setClickable(true);
            } else if ("直播".equals(cardItem.getStageType())) {
                icon_material_Imag.setBackgroundResource(R.drawable.icon_material_zb);
                goClickableMat_Img.setBackgroundResource(R.drawable.icon_goclick_ms);
                goClickableMat_Img.setEnabled(true);
                goClickableMat_Img.setClickable(true);
            } else if ("在线".equals(cardItem.getStageType())) {
                icon_material_Imag.setBackgroundResource(R.drawable.icon_material_zx);
                goClickableMat_Img.setBackgroundResource(R.drawable.icon_goclick_ms);
                goClickableMat_Img.setEnabled(true);
                goClickableMat_Img.setClickable(true);
            }
        } else if (materialFlag == 0) { // 未启用状态
            goClickableMat_Img.setBackgroundResource(R.drawable.icon_go_normal);
            goClickableMat_Img.setEnabled(false);
            goClickableMat_Img.setClickable(false);
        }

        int status = cardItem.getStageStatus();
        switch (status) {
            case 1: // 正常状态
                if ("面授".equals(cardItem.getStageType())) {
                    flagCard_Img.setBackgroundResource(R.drawable.icon_start_ms);
                    icon_form_Imag.setBackgroundResource(R.drawable.icon_form_ms);
                    goClickableFrom_Img.setBackgroundResource(R.drawable.icon_goclick_ms);
                    goClickableFrom_Img.setEnabled(true);
                    goClickableFrom_Img.setClickable(true);
                } else if ("直播".equals(cardItem.getStageType())) {
                    flagCard_Img.setBackgroundResource(R.drawable.icon_start_zb);
                    icon_form_Imag.setBackgroundResource(R.drawable.icon_form_zb);
                    goClickableFrom_Img.setBackgroundResource(R.drawable.icon_goclick_zb);
                    goClickableFrom_Img.setEnabled(true);
                    goClickableFrom_Img.setClickable(true);
                } else if ("在线".equals(cardItem.getStageType())) {
                    flagCard_Img.setBackgroundResource(R.drawable.icon_start_zx);
                    icon_form_Imag.setBackgroundResource(R.drawable.icon_form_zx);
                    goClickableFrom_Img.setBackgroundResource(R.drawable.icon_goclick_zx);
                    goClickableFrom_Img.setEnabled(true);
                    goClickableFrom_Img.setClickable(true);
                }
                break;
            case 0: // 未开始状态
                flagCard_Img.setBackgroundResource(R.drawable.icon_nostart);
                goClickableFrom_Img.setBackgroundResource(R.drawable.icon_go_normal);
                goClickableFrom_Img.setEnabled(false);
                goClickableFrom_Img.setClickable(false);
                break;
            case 2: // 已结束状态
                flagCard_Img.setBackgroundResource(R.drawable.icon_end);
                goClickableFrom_Img.setBackgroundResource(R.drawable.icon_go_normal);
                goClickableFrom_Img.setEnabled(false);
                goClickableFrom_Img.setClickable(false);
                break;
            default:
                break;
        }

        // 设置监听
        setOnClick(goClickableFrom_Img, position);
        setOnClick(goClickableQues_Img, position);
        setOnClick(goClickableNote_Img, position);
        setOnClick(goClickableMat_Img, position);

    }

    /**
     * 设置监听
     *
     * @param view
     */
    public void setOnClick(View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.goClickableFrom_Img: // 课表按钮
                        if("在线".equals(mData.get(position).getStageType())){
//                            Intent it = new Intent(mCtx, OnlineClassActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("titleName",mData.get(position).getStageName());
//                            bundle.putString("classId",String.valueOf(mClassID));
//                            bundle.putString("stageId", String.valueOf(mData.get(position).getStageId()));
//                            it.putExtras(bundle);
//                            mCtx.startActivity(it);
                        }else if("面授".equals(mData.get(position).getStageType())){
                            Intent it = new Intent(mCtx, CourseDetialActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("titleName",mData.get(position).getStageName());
                            bundle.putInt("classId",mClassID);
                            bundle.putInt("stageId", mData.get(position).getStageId());
                            it.putExtras(bundle);
                            mCtx.startActivity(it);
                        }else{
                            Toast.makeText(mCtx,"暂无直播内容",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.goClickableQues_Img: // 问题按钮
                        Intent quesitonIt = new Intent(mCtx, QuestionAllActivity.class);
                        Bundle quesBundle = new Bundle();
                        quesBundle.putString("titleName",mData.get(position).getStageName());
                        quesBundle.putInt("classId",mClassID);
                        quesBundle.putInt("stageId", mData.get(position).getStageId());
                        quesitonIt.putExtras(quesBundle);
                        mCtx.startActivity(quesitonIt);
                        break;
                    case R.id.goClickableNote_Img: // 笔记按钮
                        Intent noteIt = new Intent(mCtx, NoteAllActivity.class);
                        Bundle noteBundle = new Bundle();
                        noteBundle.putString("titleName",mData.get(position).getStageName());
                        noteBundle.putInt("classId",mClassID);
                        noteBundle.putInt("stageId", mData.get(position).getStageId());
                        noteIt.putExtras(noteBundle);
                        mCtx.startActivity(noteIt);
                        break;
                    case R.id.goClickableMat_Img: // 资料按钮
                        Toast.makeText(mCtx, "item被点击了", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
