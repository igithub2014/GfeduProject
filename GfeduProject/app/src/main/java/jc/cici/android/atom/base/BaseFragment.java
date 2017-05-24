package jc.cici.android.atom.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment基类
 * Created by atom on 2017/3/28.
 */

public abstract class BaseFragment extends Fragment {

    protected BaseActivity baseActivity;

    protected abstract void initView(View view, Bundle savedInstanceState);

    // 布局文件
    protected abstract int getLayoutId();

    // 获取宿主Activity
    protected BaseActivity getHoldingActivity() {
        return baseActivity;
    }

    /**
     * 设置Activity baseActivity基类赋值，
     * 防止低内存回收fragment造成空指针
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * 添加fragment
     * @param baseFragment
     */
    public void addFragement(BaseFragment baseFragment){
        if(null != baseFragment){
            getHoldingActivity().addFragment(baseFragment);
        }
    }

    /**
     * 移除fragment
     */
    public void removeFragment(){
        getHoldingActivity().removeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }
}
