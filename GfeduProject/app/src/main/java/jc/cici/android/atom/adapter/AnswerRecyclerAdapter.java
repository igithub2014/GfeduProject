package jc.cici.android.atom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.Answer;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.QuesDetailBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.note.AnswerDetailActivity;
import jc.cici.android.atom.ui.note.CommentActivity;
import jc.cici.android.atom.ui.note.GalleryActivity;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.GlideCircleTransform;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 回答列表适配器
 * Created by atom on 2017/6/15.
 */

public class AnswerRecyclerAdapter extends BaseRecycleerAdapter<Answer, AnswerRecyclerAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<Answer> mItems;
    private int mStatus;
    private ArrayList<String> imgList;
    private int mQuesId;

    public AnswerRecyclerAdapter(Context context, ArrayList<Answer> items, int status, int quesId) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
        this.mStatus = status;
        this.mQuesId = quesId;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, Answer item, final int position) {

        // 用户图像
        Glide.with(mCtx).load(item.getHeadImg())
                .placeholder(R.drawable.icon_avatar) //加载中显示的图片
                .error(R.drawable.icon_avatar) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(280, 186) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .transform(new GlideCircleTransform(mCtx)) // 设置圆角
                .into(holder.userImg_answer);
        // 用户名
        holder.userName_ques.setText(ToolUtils.strReplaceAll(item.getNickName()));
        holder.userName_ques.setVisibility(View.VISIBLE);

        if (mStatus == 2) { // 大家的答疑
            holder.QuesDel_txt.setText("删除");
        } else { // 我的答疑
            if (item.getAnswerStatus() != 1) {
                holder.QuesDel_txt.setText("设为最佳答案");
                holder.QuesDel_txt.setVisibility(View.VISIBLE);
            } else {
                holder.QuesDel_txt.setVisibility(View.GONE);
            }
        }
        // 上传时间
        holder.answerDate_txt.setText(item.getAnswerAddTime());
        // 答案内容
        holder.answer_txt.setText(item.getAnswerContent());
        // 获取上传图片
        imgList = item.getAnswerImageUrl();
        if (null != imgList && imgList.size() > 0) {
            // 获取图片个数
            int size = imgList.size();
            switch (size) {
                case 1: // 一张图片
                    // 加载显示第一张图片
                    loadImg(holder.answer1_img, imgList.get(0));
                    setImageVisible(holder.answer1_img, true);
                    setImageVisible(holder.answer2_img, false);
                    setImageVisible(holder.answer3_img, false);
                    // 第一张图片设置监听
                    setOnclick(holder.answer1_img, imgList, 0);
                    break;
                case 2: // 两张图片
                    // 加载显示第一张图片
                    loadImg(holder.answer1_img, imgList.get(0));
                    // 加载显示第二张图片
                    loadImg(holder.answer2_img, imgList.get(1));
                    setImageVisible(holder.answer1_img, true);
                    setImageVisible(holder.answer2_img, true);
                    setImageVisible(holder.answer3_img, false);

                    // 第一张图片设置监听
                    setOnclick(holder.answer1_img, imgList, 0);
                    // 第二张图片设置监听
                    setOnclick(holder.answer2_img, imgList, 1);
                    break;
                case 3: // 三张图片
                    // 加载显示第一张图片
                    loadImg(holder.answer1_img, imgList.get(0));
                    // 加载显示第二张图片
                    loadImg(holder.answer2_img, imgList.get(1));
                    // 加载显示第三张图片
                    loadImg(holder.answer3_img, imgList.get(2));
                    setImageVisible(holder.answer1_img, true);
                    setImageVisible(holder.answer2_img, true);
                    setImageVisible(holder.answer3_img, true);

                    // 第一张图片设置监听
                    setOnclick(holder.answer1_img, imgList, 0);
                    // 第二张图片设置监听
                    setOnclick(holder.answer2_img, imgList, 1);
                    // 第三张图片设置监听
                    setOnclick(holder.answer3_img, imgList, 2);
                    break;
                default:
                    break;
            }
        } else {
            setImageVisible(holder.answer1_img, false);
            setImageVisible(holder.answer2_img, false);
            setImageVisible(holder.answer3_img, false);
        }
        // 追问数设置
        holder.answerCount_txt.setText("(" + item.getAfterQuesCount() + ")追问");

        if (1 == item.getUserPraiseStatus()) {
            holder.zanBtn.setBackgroundResource(R.drawable.icon_zan_clickable);
        } else {
            holder.zanBtn.setBackgroundResource(R.drawable.icon_zan);
        }
        // 点赞数
        holder.zanResponseCount.setText("(" + item.getPraiseCount() + ")");
        // 评论数
        holder.commitCount_txt.setText("(" + item.getCommentsCount() + ")");

        // 删除或推荐答案设置监听
        holder.QuesDel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStatus == 1) { // 我的答疑此刻用于设置推荐答案
                    if (NetUtil.isMobileConnected(mCtx)) {
                        addClick(position, 2, holder);
                    } else {
                        Toast.makeText(mCtx, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                } else { // 大家的答疑 用于删除有关我的回答，评论，追问追答的删除操作
                    // TODO 删除操作
                }
            }
        });

        /**
         * 评论按钮监听
         */
        holder.commit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mCtx, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("answerId", mItems.get(position).getAnswerId());
                bundle.putInt("afterType", 1);
                it.putExtras(bundle);
                mCtx.startActivity(it);
            }
        });

        holder.zan_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtil.isMobileConnected(mCtx)) {
                    addClick(position, 1, holder);
                } else {
                    Toast.makeText(mCtx, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addClick(final int position, final int flag, final MyHolder holder) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("appname", commParam.getAppname());
            obj.put("userId", commParam.getUserId());
            // 答案id
            obj.put("answerId", mItems.get(position).getAnswerId());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean> observable = null;
        if (1 == flag) { // 点赞请求
            observable = httpPostService.addUserPraiseInfo(body);
        } else if (2 == flag) { // 设置最佳答案监听
            observable = httpPostService.setClassQuesStatusInfo(body);
        }

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mCtx, "网络请求错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean BeanCommonBean) {
                                if (100 == BeanCommonBean.getCode()) {
                                    switch (flag) {
                                        case 1: // 点赞成功
                                            Toast.makeText(mCtx, "点赞成功", Toast.LENGTH_SHORT).show();
                                            holder.zanBtn.setBackgroundResource(R.drawable.icon_zan_clickable);
                                            int count = mItems.get(position).getPraiseCount() + 1;
                                            holder.zanResponseCount.setText("(" + count + ")");
                                            break;
                                        case 2: // 最佳答案设置成功
                                            holder.bestAnswer_Img.setVisibility(View.VISIBLE);
                                            holder.QuesDel_txt.setVisibility(View.GONE);
                                            // 发送广播
                                            Intent it = new Intent();
                                            it.setAction("com.jc.setBest");
                                            it.putExtra("quesId", mQuesId);
                                            mCtx.sendBroadcast(it);
                                            break;
                                        default:
                                            break;
                                    }
                                } else {
                                    Toast.makeText(mCtx, BeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                            }
                        }
                );
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_answer_view;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 用户图像
        @BindView(R.id.userImg_answer)
        ImageView userImg_answer;
        // 用户名
        @BindView(R.id.userName_ques)
        TextView userName_ques;
        //  删除or推荐为最佳
        @BindView(R.id.QuesDel_txt)
        TextView QuesDel_txt;
        // 上传日期
        @BindView(R.id.answerDate_txt)
        TextView answerDate_txt;
        // 答案内容
        @BindView(R.id.answer_txt)
        TextView answer_txt;
        // 回答图片1
        @BindView(R.id.answer1_img)
        ImageView answer1_img;
        // 回答图片2
        @BindView(R.id.answer2_img)
        ImageView answer2_img;
        // 回答问题3
        @BindView(R.id.answer3_img)
        ImageView answer3_img;
        // 追问布局
        @BindView(R.id.zhuiWen_layout)
        RelativeLayout zhuiWen_layout;
        // 追问数目
        @BindView(R.id.answerCount_txt)
        TextView answerCount_txt;
        // 点赞布局
        @BindView(R.id.zan_layout)
        LinearLayout zan_layout;
        // 点赞按钮
        @BindView(R.id.zanBtn)
        Button zanBtn;
        // 点赞数量
        @BindView(R.id.zanResponseCount)
        TextView zanResponseCount;
        // 评论布局
        @BindView(R.id.commit_layout)
        LinearLayout commit_layout;
        // 评论数目
        @BindView(R.id.commitCount_txt)
        TextView commitCount_txt;
        @BindView(R.id.bestAnswer_Img)
        ImageView bestAnswer_Img;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 设置监听
     *
     * @param img
     * @param list
     * @param currentPos
     */
    private void setOnclick(ImageView img, final ArrayList<String> list, final int currentPos) {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mCtx, GalleryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", currentPos);
                bundle.putStringArrayList("imgList", list);
                it.putExtras(bundle);
                mCtx.startActivity(it);
            }
        });
    }

    /**
     * 加载显示图片
     *
     * @param img
     * @param url
     */
    private void loadImg(ImageView img, String url) {
        Glide.with(mCtx).load(url)
                .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(280, 186) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .into(img);
    }

    /**
     * 设置图片显示隐藏
     *
     * @param img
     * @param flag
     */
    private void setImageVisible(ImageView img, boolean flag) {
        if (flag) {
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.GONE);
        }
    }
}
