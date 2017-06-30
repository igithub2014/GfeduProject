package jc.cici.android.atom.ui.tiku;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();

	public MyFragmentPageAdapter(FragmentManager fm,
			ArrayList<Fragment> FragmentList) {
		super(fm);
		this.mFragmentList = FragmentList;
	}

	@Override
	public int getCount() {
		return mFragmentList.size();

	}

	@Override
	public Fragment getItem(int posion) {
		return mFragmentList.get(posion);

	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
	}
	@Override
	public void startUpdate(ViewGroup container) {

		super.startUpdate(container);
	}
}