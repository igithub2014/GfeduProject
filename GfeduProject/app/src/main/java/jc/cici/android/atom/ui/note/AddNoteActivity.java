package jc.cici.android.atom.ui.note;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.ImageRecyclerAdapter;
import jc.cici.android.atom.adapter.RelAnswerRecyclerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.Answer;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.Question;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.FullGridLayoutManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 添加笔记activity
 * Created by atom on 2017/6/9.
 */

public class AddNoteActivity extends BaseActivity {

    private static final String TAG = "AddNoteActivity";
    private BaseActivity baseActivity;
    // 班级id
    private int classId;
    // 阶段id
    private int stageId;
    private int childClassTypeId;
    // 跳转来源标记
    private int jumpFlag;
    // 课程id
    private int lessonId;
    // 试卷id
    private int testPapgerId;
    // 试题id
    private int testQuesId;
    // 视频id
    private int videoId;
    // 问题id
    private int quesId;
    // 答案id
    private int answerId;
    //  回复id
    private int replyid;
    // 请求类型，1.评论  2.追问追答  3.回答  4.问题
    private int afterType;

    private Unbinder unbinder;
    // title布局
    @BindView(R.id.title_layout)
    Toolbar title_layout;
    // 返回按钮布局
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    @BindView(R.id.back_img)
    ImageView back_img;
    // 取消文字
    @BindView(R.id.cancel_txt)
    TextView cancel_txt;
    // 标题
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 搜索更多布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 搜索按钮
    @BindView(R.id.noteMore_Btn)
    Button noteMore_Btn;
    // 更多按钮
    @BindView(R.id.search_Btn)
    Button search_Btn;
    // 隐藏布局
    @BindView(R.id.register_txt)
    TextView register_txt;
    // 选择相关科目布局
    @BindView(R.id.select_item_layout)
    RelativeLayout select_item_layout;
    @BindView(R.id.subject_txt)
    TextView subject_txt;
    // 文字输入框
    @BindView(R.id.addNote_edit)
    EditText addNote_edit;
    // 添加图片布局
    @BindView(R.id.addImg_layout)
    LinearLayout addImg_layout;
    // 添加图片
    @BindView(R.id.addImg)
    ImageView addImg;
    // 添加图片RecyclerView 列表
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    // 相关问题布局
    @BindView(R.id.relativeAnswer_layout)
    RelativeLayout relativeAnswer_layout;
    // 相关问题列表
    @BindView(R.id.relativeRecyclerView)
    RecyclerView relativeRecyclerView;
    // GridViewLayoutManger 对象
    private FullGridLayoutManager gridLayoutManager;
    private List<LocalMedia> itemList = new ArrayList<>();
    // 上传图片列表
    private ArrayList<String> imgList = new ArrayList<>();
    // 默认最大选中个数
    private int maxSelectNum;
    // 图片适配器对象
    private ImageRecyclerAdapter adapter;
    private SweetAlertDialog dialog;
    // 科目id
    private int subjectId;
    // 笔记图片url
    private String imgUrl = "";
    // 科目名称
    private String strSubject;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addnote;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().addActivity(this);
        baseActivity = this;
        // 去标题
        baseActivity.requestNoTitle();
        // 班级id
        classId = getIntent().getIntExtra("classId", 0);
        // 阶段id
        stageId = getIntent().getIntExtra("stageId", 0);
        // 类型id
        childClassTypeId = getIntent().getIntExtra("childClassTypeId", 0);
        // 课程id
        lessonId = getIntent().getIntExtra("lessonId", 0);
        // 试卷id
        testPapgerId = getIntent().getIntExtra("testPapgerId", 0);
        // 试题id
        testQuesId = getIntent().getIntExtra("testQuesId", 0);
        // 视频id
        videoId = getIntent().getIntExtra("videoId", 0);
        quesId = getIntent().getIntExtra("quesId", 0);
        answerId = getIntent().getIntExtra("answerId", 0);
        replyid = getIntent().getIntExtra("replyid", 0);
        // 请求类型
        afterType = getIntent().getIntExtra("afterType", 0);
        // 跳转标记(笔记OR答疑)
        jumpFlag = getIntent().getIntExtra("jumpFlag", 0);
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
    }

    /**
     *
     */
    private void initData() {
        String strImg = null;
        if (!"".equals(subject_txt.getText().toString())) {
            // 笔记内容
            String noteContent = addNote_edit.getText().toString();
            if (!"".equals(noteContent) && (noteContent.length() > 0)) {
                // 压缩后图片url地址
                if (itemList.size() > 0) {
                    imgUrl = itemList.get(0).getCompressPath();
                    try {
                        strImg = ToolUtils.ioToBase64(imgUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
                dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                dialog.setTitleText("");
                dialog.setTitle("");
                HttpPostService httpPostService = retrofit.create(HttpPostService.class);
                JSONObject obj = new JSONObject();
                CommParam commParam = new CommParam(this);
                try {
                    obj.put("client", commParam.getClient());
                    obj.put("userId", commParam.getUserId());
                    // 默认
                    obj.put("nTBClassID", 0);
                    // 笔记内容
                    obj.put("nTBContent", noteContent);
                    // 笔记公开状态(0:公开，1:私有) 默认添加为私有
                    obj.put("nTBTempVal", 1);
                    // 班级id
                    obj.put("nTBClassTypePKID", classId);
                    // 子班型id
                    obj.put("nTBChildClassTypePKID", stageId);
                    // 课表id
                    obj.put("nTBClassSchedulePKID", lessonId);
                    // 视频id
                    obj.put("nTBVideoId", videoId);
                    // 试卷id
                    obj.put("nTBTestPaperId", testPapgerId);
                    // 试题id
                    obj.put("nTBTestQuesId", testQuesId);
                    obj.put("Base64Img", strImg);
                    // 科目id
                    obj.put("childClassType_LevelID", subjectId);
                    obj.put("timeStamp", commParam.getTimeStamp());
                    obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
                Observable<CommonBean> observable = httpPostService.getAddNotesInfo(body);
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Subscriber<CommonBean>() {
                                    @Override
                                    public void onCompleted() {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismissWithAnimation();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismissWithAnimation();
                                            Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onNext(CommonBean commonBean) {
                                        if (100 == commonBean.getCode()) {

                                            Toast.makeText(baseActivity, "笔记添加成功", Toast.LENGTH_SHORT).show();
                                            // 清除缓存
                                            PictureFileUtils.deleteCacheDirFile(baseActivity);
                                            Intent it = new Intent();
                                            it.setAction("com.jc.addnote");
                                            baseActivity.sendBroadcast(it);
                                            baseActivity.finish();
                                        } else {
                                            Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onStart() {
                                        super.onStart();
                                        if (null != dialog && !dialog.isShowing()) {
                                            dialog.show();
                                        }
                                    }
                                }
                        );
            } else {
                Toast.makeText(baseActivity, "笔记内容不能为空,请添加笔记内容", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(baseActivity, "请先选择笔记对应科目", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 初始化控件内容
     */
    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        back_img.setVisibility(View.GONE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        cancel_txt.setVisibility(View.VISIBLE);
        register_txt.setText("提交");
        cancel_txt.setText("取消");
        // 添加管理器
        relativeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        switch (jumpFlag) {
            case 0: // 表示笔记跳转
                title_txt.setText("添加笔记");
                maxSelectNum = 1;
                break;
            case 1:// 表示从答疑跳转
                title_txt.setText("新增提问");
                subject_txt.setHint("请先选择问题对应科目");
                addNote_edit.setHint("请输入问题内容");
                relativeAnswer_layout.setVisibility(View.VISIBLE);
                maxSelectNum = 3;
                break;
            case 2: // 来自我要作答跳转
                title_txt.setText("新增回答");
                subject_txt.setHint("请先选择问题对应科目");
                addNote_edit.setHint("请输入问题内容");
                maxSelectNum = 3;
                break;
            case 3: // 来自追问
                title_txt.setText("追问");
                subject_txt.setVisibility(View.GONE);
                select_item_layout.setVisibility(View.GONE);
                addNote_edit.setHint("你的问题");
                maxSelectNum = 1;
                break;
            default:
                break;
        }

        // 创建GridLayoutManger 管理器对象 并设置每行显示3个
        gridLayoutManager = new FullGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        // 添加管理器
        recyclerView.setLayoutManager(gridLayoutManager);
        // 创建适配器
        adapter = new ImageRecyclerAdapter(this, onPicClickListener);
        // 添加数据
        adapter.setList(itemList);
        if (0 == jumpFlag) {
            // 添加默认个数
            adapter.setSelectMax(1);
        } else if (1 == jumpFlag) {
            // 添加默认个数
            adapter.setSelectMax(3);
        } else {
            adapter.setSelectMax(1);
        }
        //recyclerView 设置适配器
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ImageRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position, View v) {

                if (itemList.size() > 0) {
                    LocalMedia localMedia = itemList.get(position);
                    String pictureType = localMedia.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1: // 预览照片
                            PictureSelector.create(AddNoteActivity.this).externalPicturePreview(position, itemList);
//                            // 长按保存图片
//                            PictureSelector.create(AddNoteActivity.this).externalPicturePreview(position, "/jc_school/pic", itemList);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    //     创建图片点击时间
    private ImageRecyclerAdapter
            .onAddPicClickListener onPicClickListener = new ImageRecyclerAdapter
            .onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(baseActivity)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(3)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(true)// 是否显示拍照按钮
                    .compress(true)// 是否压缩
                    .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .selectionMedia(itemList)// 是否传入已选图片
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    itemList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    adapter.setList(itemList);
                    adapter.notifyDataSetChanged();
                    DebugUtil.i(TAG, "onActivityResult:" + itemList.size());
                    break;
            }
        } else if (1 == requestCode) {
            if (2 == resultCode) {
                subjectId = data.getIntExtra("subjectId", 0);
                strSubject = data.getStringExtra("subjectName");
                System.out.println("subjectId >>>:" + subjectId + "strSubject >>>:" + strSubject);
                subject_txt.setText(strSubject);
            }
        }
    }

    @OnClick({R.id.back_layout, R.id.register_txt, R.id.select_item_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.register_txt: // 提交上传图片按钮
                switch (jumpFlag) {
                    case 0: // 新增笔记提交
                        // 网络请求
                        if (NetUtil.isMobileConnected(this)) {
                            initData();
                        } else {
                            Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1: // 新增问题提交
                        // 网络请求
                        if (NetUtil.isMobileConnected(this)) {
                            quesSubmit();
                        } else {
                            Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2: // 新增回答提交
                        // 网络请求
                        if (NetUtil.isMobileConnected(this)) {
                            // 提交我的回答内容
                            answerSubmit();
                        } else {
                            Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3: // 追问提交
                        if (NetUtil.isMobileConnected(this)) {
                            // 提交我的追问追答
                            zhuiWenSubmit();
                        } else {
                            Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case R.id.select_item_layout: // 相关科目选择
                Intent it = new Intent(this, ChapterContentAc.class);
                Bundle bundle = new Bundle();
                if (jumpFlag != 0) {
                    bundle.putInt("stageId", childClassTypeId);
                } else {
                    bundle.putInt("stageId", stageId);
                }
//                bundle.putInt("jumpFlag", jumpFlag);
                it.putExtras(bundle);
                startActivityForResult(it, 1);
                break;
            default:
                break;
        }
    }

    private void zhuiWenSubmit() {
        // 问题内容
        String quesContent = addNote_edit.getText().toString();
        if (!"".equals(quesContent) && (quesContent.length() > 0)) {
            // 压缩后图片url地址
            if (itemList.size() > 0) {
                imgList.clear();
                for (int i = 0; i < itemList.size(); i++) {
                    String imgUrl = itemList.get(i).getCompressPath();
                    try {
                        String str = ToolUtils.ioToBase64(imgUrl);
                        imgList.add(str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 添加网络请求
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            HttpPostService httpPostService = retrofit.create(HttpPostService.class);

            RequestBody body = commRequestBody(quesContent);
            // 具体请求
            Observable<CommonBean<Answer>> observable = httpPostService.getAddAnswerInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<CommonBean<Answer>>() {
                                @Override
                                public void onCompleted() {
                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.dismissWithAnimation();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.dismissWithAnimation();
                                        Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNext(CommonBean<Answer> answerCommonBean) {
                                    if (100 == answerCommonBean.getCode()) {
                                        Toast.makeText(baseActivity, "我的回答添加成功", Toast.LENGTH_SHORT).show();
                                        // 清除缓存
                                        PictureFileUtils.deleteCacheDirFile(baseActivity);
                                        Intent broadIt = new Intent();
                                        broadIt.putExtra("quesId", quesId);
                                        broadIt.setAction("com.jc.changeQues");
                                        baseActivity.sendBroadcast(broadIt);
                                        Intent it = new Intent();
                                        setResult(2, it);
                                        baseActivity.finish();
                                    } else {
                                        Toast.makeText(baseActivity, answerCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onStart() {
                                    super.onStart();
                                    if (null != dialog && !dialog.isShowing()) {
                                        dialog.show();
                                    }
                                }
                            }
                    );

        } else {
            Toast.makeText(baseActivity, "追问内容不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 我的回答提交
     */
    private void answerSubmit() {
        if (!"".equals(subject_txt.getText().toString())) {
            // 问题内容
            String quesContent = addNote_edit.getText().toString();
            if (!"".equals(quesContent) && (quesContent.length() > 0)) {
                // 压缩后图片url地址
                if (itemList.size() > 0) {
                    imgList.clear();
                    for (int i = 0; i < itemList.size(); i++) {
                        String imgUrl = itemList.get(i).getCompressPath();
                        try {
                            String str = ToolUtils.ioToBase64(imgUrl);
                            imgList.add(str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // 添加网络请求
                Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
                HttpPostService httpPostService = retrofit.create(HttpPostService.class);
                RequestBody body = commRequestBody(quesContent);
                // 具体请求
                Observable<CommonBean<Answer>> observable = httpPostService.getAddAnswerInfo(body);
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Subscriber<CommonBean<Answer>>() {
                                    @Override
                                    public void onCompleted() {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismissWithAnimation();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismissWithAnimation();
                                            Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onNext(CommonBean<Answer> answerCommonBean) {
                                        if (100 == answerCommonBean.getCode()) {
                                            Toast.makeText(baseActivity, "我的回答添加成功", Toast.LENGTH_SHORT).show();
                                            // 清除缓存
                                            PictureFileUtils.deleteCacheDirFile(baseActivity);
                                            Intent broadIt = new Intent();
                                            broadIt.putExtra("quesId", quesId);
                                            broadIt.setAction("com.jc.changeQues");
                                            baseActivity.sendBroadcast(broadIt);
                                            Intent it = new Intent();
                                            setResult(2, it);
                                            baseActivity.finish();
                                        } else {
                                            Toast.makeText(baseActivity, answerCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onStart() {
                                        super.onStart();
                                        if (null != dialog && !dialog.isShowing()) {
                                            dialog.show();
                                        }
                                    }
                                }
                        );

            } else {
                Toast.makeText(baseActivity, "问题内容不能为空,请添加问题内容", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(baseActivity, "请先选择问题对应科目", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 新增问题提交
     */
    private void quesSubmit() {

        if (!"".equals(subject_txt.getText().toString())) {
            // 问题内容
            String quesContent = addNote_edit.getText().toString();
            if (!"".equals(quesContent) && (quesContent.length() > 0)) {
                // 压缩后图片url地址
                if (itemList.size() > 0) {
                    imgList.clear();
                    for (int i = 0; i < itemList.size(); i++) {
                        String imgUrl = itemList.get(i).getCompressPath();
                        try {
                            imgList.add(ToolUtils.ioToBase64(imgUrl));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // 添加网络请求
                Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
                HttpPostService httpPostService = retrofit.create(HttpPostService.class);
                RequestBody body = commRequestBody(quesContent);
                // 具体请求
                Observable<CommonBean<Question>> observable = httpPostService.getAddClassQuesInfo(body);
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Subscriber<CommonBean<Question>>() {
                                    @Override
                                    public void onCompleted() {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismissWithAnimation();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismissWithAnimation();
                                            Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onNext(CommonBean<Question> quesInfoCommonBean) {
                                        if (100 == quesInfoCommonBean.getCode()) {
                                            Toast.makeText(baseActivity, "问题添加成功", Toast.LENGTH_SHORT).show();
                                            // 清除缓存
                                            PictureFileUtils.deleteCacheDirFile(baseActivity);
                                            Intent it = new Intent();
                                            it.setAction("com.jc.addQues");
                                            baseActivity.sendBroadcast(it);
                                            baseActivity.finish();
                                        } else {
                                            Toast.makeText(baseActivity, quesInfoCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onStart() {
                                        super.onStart();
                                        if (null != dialog && !dialog.isShowing()) {
                                            dialog.show();
                                        }
                                    }
                                }
                        );

            } else {
                Toast.makeText(baseActivity, "问题内容不能为空,请添加问题内容", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(baseActivity, "请先选择问题对应科目", Toast.LENGTH_SHORT).show();
        }
    }


//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        System.out.println("excute..");
//        subjectId = intent.getIntExtra("subjectId", 0);
//        String strSubject = intent.getStringExtra("subjectName");
//        System.out.println("subjectId >>>:" + subjectId + "strSubject >>>:" + strSubject);
//        subject_txt.setText(strSubject);
//
//    }

    /**
     * 公共请求参数
     *
     * @param quesContent
     * @return
     */
    private RequestBody commRequestBody(String quesContent) {

        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setTitle("");
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            obj.put("client", commParam.getClient());
            obj.put("userId", commParam.getUserId());
            // 请求类型，1.评论  2.追问追答  3.回答  4.问题
            if (4 == afterType) {
                // 班级id
                obj.put("classTypeId", classId);
                // 阶段id
                obj.put("childClassTypeId", childClassTypeId);
            } else if (3 == afterType) {
                //问题id
                obj.put("quesId", quesId);
            } else if (2 == afterType) {
                //问题id
                obj.put("answerId", answerId);
            } else if (1 == afterType) {
                //问题id
                obj.put("answerId", answerId);
                //问题id
                obj.put("replyId", replyid);
            }
            obj.put("afterType", afterType);
            JSONArray array = new JSONArray(imgList);
            obj.put("base64Img", array);
            obj.put("Content", quesContent);
            // 科目id
            obj.put("subjectId", subjectId);
            //时间戳
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }
}
