package jc.cici.android.atom.bean;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线选择课程实体类
 * Created by atom on 2017/6/8.
 */

public class OnLineBean implements Parent<String> {

    private String titleName;
    private ArrayList<String> childName;

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public ArrayList<String> getChildName() {
        return childName;
    }

    public void setChildName(ArrayList<String> childName) {
        this.childName = childName;
    }

    @Override
    public List<String> getChildList() {
        return childName;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
