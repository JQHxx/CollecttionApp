package com.huateng.collection.widget.navigator;


import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * Created by aspsine on 16/3/30.
 */
public interface FragmentNavigatorAdapter {

    public Fragment onCreateFragment(int position);

    public String getTag(int position);

    public int getCount();

    public List<Fragment> getFragments();
}
