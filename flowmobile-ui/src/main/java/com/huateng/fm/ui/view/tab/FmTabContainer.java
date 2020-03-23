package com.huateng.fm.ui.view.tab;

import java.util.ArrayList;
import java.util.List;

import com.huateng.fm.util.UiValuesUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class FmTabContainer extends LinearLayout {

    private int mSelectedTextColorId, mUnSelectedTextColorId, mSelectedSectionColor;

    @SuppressLint("NewApi")
    public FmTabContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FmTabContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public FmTabContainer(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);

    }

    public void setSelectedSectionColor(int id) {
        mSelectedSectionColor = id;
    }

    private int mVerticalPadding;

    public void setVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
    }

    private List<TabItem> tabs = new ArrayList<TabItem>();

    public FmTabContainer addItem(int iconResId, int iconResIdUnchecked, String text) {
        TabItem tabItemView = new TabItem(getContext(), iconResIdUnchecked,iconResId, text);
        tabItemView.setVerticalPadding(mVerticalPadding);
        tabItemView.setSelectedSectionColor(mSelectedSectionColor);
        tabItemView.setSelectedTextColor(mSelectedTextColorId, mUnSelectedTextColorId);
        tabItemView.setTag(getChildCount());
        tabItemView.setClickable(true);
        tabItemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("111", v.getTag() + "");
                if (listener != null) {
                    setSelection((Integer) (v.getTag()));
                    listener.onChecked((Integer) (v.getTag()));
                }
            }
        });
        tabs.add(tabItemView);
        addView(tabItemView);
        return this;
    }

    public FmTabContainer addItem(int iconResId, int iconResIdUnchecked) {
        addItem(iconResId, iconResIdUnchecked, "");
        return this;
    }

    public FmTabContainer addItem(int iconResId, int iconResIdUnchecked, int textId) {
        return addItem(iconResId, iconResIdUnchecked, UiValuesUtil.getString(textId));
    }

    public void setSelection(int position) {
        for (int i = 0; i < tabs.size(); i++) {
            if (i == position) {
                tabs.get(i).setChecked(true);
                if (listener != null) {
                    listener.onChecked(position);
                }
            } else {
                tabs.get(i).setChecked(false);
            }
        }
    }

    public void clearSelection(){
        setSelection(-1);
    }

    public void setSelectedTextColor(int selId, int unselId) {
        mSelectedTextColorId = selId;
        mUnSelectedTextColorId = unselId;
    }

    public void setHasMsgPosition(int position, int count) {
        for (int i = 0; i < tabs.size(); i++) {
            if (i == position) {
                if (count > 99) {
                    count = 99;
                }
                tabs.get(i).setMsgCount("" + count);
            } else {
                tabs.get(i).setMsgCount("");
            }
        }
    }

    public interface OnCheckedListener {
        public void onChecked(int position);
    }

    private OnCheckedListener listener;

    public void setOnCheckedListener(OnCheckedListener listener) {
        this.listener = listener;
    }


}
