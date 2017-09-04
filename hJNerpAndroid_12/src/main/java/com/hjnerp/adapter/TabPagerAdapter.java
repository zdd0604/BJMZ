package com.hjnerp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	private List<Fragment> list;

	// 构造函数
	public TabPagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		if (list == null) 
			list = new ArrayList<Fragment>();
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}