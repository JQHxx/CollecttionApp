package com.huateng.fm.ui.toolbar;
import java.lang.reflect.Field;

import com.huateng.flowMobile.R;
import com.huateng.fm.core.util.FmScreenUtil;

/**  
 * @author Devin
 * @date 2016年1月11日 下午6:24:43 
 * @Description
 * @version 
*/
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by moon.zhong on 2015/6/12.
 * time : 10:45
 */
public class ToolbarHelper {
	private final String TAG=getClass().getSimpleName();
    /*上下文，创建view的时候需要用到*/
    private Context mContext;

    /*base view*/
    private FrameLayout mContentView;

    /*用户定义的view*/
    private View mUserView;

    /*toolbar*/
    private Toolbar mToolbar;

    /*视图构造器*/
    private LayoutInflater mInflater;

    /*
    * 两个属性
    * 1、toolbar是否悬浮在窗口之上
    * 2、toolbar的高度获取
    * */
    private static int[] ATTRS = {
            android.R.attr.windowActionBarOverlay,
            android.R.attr.actionBarSize
    };

    public ToolbarHelper(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        /*初始化整个内容*/
        initContentView();
        /*初始化用户定义的布局*/
        initUserView(layoutId);
        /*初始化toolbar*/
        initToolBar();
    }

    private void initContentView() {
        /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new FrameLayout(mContext);
//        mContentView.setClipToPadding(true);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);

    }

    private void initToolBar() { 
        /*通过inflater获取toolbar的布局文件*/
        View toolbar = mInflater.inflate(R.layout.ht_toolbar, mContentView);
        mToolbar = (Toolbar) toolbar.findViewById(R.id.id_tool_bar);
//                mToolbar.setPadding(mToolbar.getPaddingLeft(), getStatusBarHeight(mContext),mToolbar.getPaddingRight(), mToolbar.getPaddingBottom());
        mToolbar.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
			
			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				if (visibility==View.GONE) {
			        mUserView.setPadding(mUserView.getPaddingLeft(), statusBarHeight, mUserView.getPaddingRight(), mUserView.getPaddingBottom());

					Log.e("111", "visibility==View.GONE");
//					LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) mToolbar.getLayoutParams();
//	                params.topMargin=0;
//	                mToolbar.setLayoutParams(params);
				}else {
			        mUserView.setPadding(mUserView.getPaddingLeft(), toolBarHeight+statusBarHeight, mUserView.getPaddingRight(), mUserView.getPaddingBottom());

//					LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) mToolbar.getLayoutParams();
//	                params.topMargin=getStatusBarHeight(mContext);
//	                mToolbar.setLayoutParams(params);
					Log.e("111", "visibility==View.visible");
				}
			}
		});      
          
    }
    
    /**
     * 获状态栏高度
     *
     * @param context 上下文
     * @return 通知栏高度
     */
    public  int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int temp = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("11222", "statusBarHeight:"+statusBarHeight);
        return statusBarHeight;
    }
    int toolBarHeight,statusBarHeight;
    private void initUserView(int id) {
    	statusBarHeight=getStatusBarHeight(mContext);
    	TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);
        /*获取主题中定义的悬浮标志*/
        boolean overly = typedArray.getBoolean(0, false);
        /*获取主题中定义的toolbar的高度*/
        //devin
        toolBarHeight = (int) typedArray.getDimension(1,56);
        typedArray.recycle();
//        int height=FmScreenUtil.getScreenHeight(mContext)-toolBarHeight        Log.e(TAG, "initUserView-height："+height);
        mUserView = mInflater.inflate(id, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        /*如果是悬浮状态，则不需要设置间距*/
//        params.topMargin = overly ? 0 : toolBarSize;
        mContentView.addView(mUserView, params);

    }

    public FrameLayout getContentView() {
        return mContentView;
    }

    public Toolbar getToolBar() {
        return mToolbar;
    }
}


