package com.huateng.collection.widget.navigator;


import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by shanyong on 2017/2/17.
 */

public class FragmentOrganizer {
    private List<Fragment> mFraments;
    private FragmentManager mFragmentManager;
    private int mContainerViewId;

    private int mCurrentPosition;

    public FragmentOrganizer(FragmentManager manager, List<Fragment> framents, int id) {
        this.mFragmentManager = manager;
        this.mFraments = framents;
        this.mContainerViewId = id;
    }

    /**
     * @see #showFragment(int, boolean)
     */
    public void showFragment(int position) {
        showFragment(position, false);
    }


    /**
     * Show fragment at given position
     *
     * @param position          fragment position
     * @param allowingStateLoss true if allowing state loss otherwise false
     */
    public void showFragment(int position, boolean allowingStateLoss) {
        this.mCurrentPosition = position;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        int count = mFraments.size();
        for (int i = 0; i < count; i++) {
            if (position == i) {
                show(i, transaction);
            } else {
                hide(i, transaction);
            }
        }

        if (allowingStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    private void removeAll(FragmentTransaction transaction) {
        int count = mFraments.size();
        for (int i = 0; i < count; i++) {
            remove(i, transaction);
        }
    }

    private void remove(int position, FragmentTransaction transaction) {
        Fragment fragment = mFraments.get(position);
        if (fragment != null && fragment.isAdded()) {
            transaction.remove(fragment);
        }
    }

    private void show(int position, FragmentTransaction transaction) {
        Fragment fragment = mFraments.get(position);
        if (!fragment.isAdded()) {
            add(position, transaction);
        } else {
            transaction.show(fragment);
        }
        fragment.setUserVisibleHint(true);
    }

    private void hide(int position, FragmentTransaction transaction) {
        Fragment fragment = mFraments.get(position);
        if (fragment != null && fragment.isAdded()) {
            transaction.hide(fragment);
            fragment.setUserVisibleHint(false);
        }
    }

    private void add(int position, FragmentTransaction transaction) {
        Fragment fragment = mFraments.get(position);
        transaction.add(mContainerViewId, fragment);
    }

    public void hideAll() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : mFraments) {
            if (!fragment.isAdded()) {
                transaction.add(mContainerViewId, fragment);
            }
            transaction.hide(fragment);
        }
        transaction.commit();
    }

}
