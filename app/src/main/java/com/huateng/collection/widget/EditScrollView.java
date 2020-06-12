package com.huateng.collection.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * author: yichuan
 * Created on: 2020/6/11 14:22
 * description:
 */
public class EditScrollView extends ScrollView {
        public EditScrollView(Context context)
        {
            super(context);
            // TODO Auto-generated constructor stub
        }
        public EditScrollView(Context context, AttributeSet attrs)
        {
            super(context, attrs);
            // TODO Auto-generated constructor stub
        }
        public EditScrollView(Context context, AttributeSet attrs, int defStyle)
        {
            super(context, attrs, defStyle);
            // TODO Auto-generated constructor stub
        }
        //重写该方法，让ScrollView不进行事件拦截
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev)
        {

            return false;
        }

    }
