package com.huateng.collection.widget.navigator;

import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by aspsine on 16/3/30.
 */
public interface FragmentNavigatorAdapter {

    public Fragment onCreateFragment(int position);

    public String getTag(int position);

    public int getCount();

    public List<Fragment> getFragments();
}
