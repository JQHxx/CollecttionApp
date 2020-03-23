package com.huateng.collection.widget.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.huateng.fm.util.UiValuesUtil;

import java.util.ArrayList;
import java.util.List;

public class VerticalTabContainer extends LinearLayout {

    private int mSelectedTextColorId, mUnSelectedTextColorId, mSelectedSectionColor;

    @SuppressLint("NewApi")
    public VerticalTabContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public VerticalTabContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public VerticalTabContainer(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

    }

    public void setSelectedSectionColor(int id) {
        mSelectedSectionColor = id;
    }

    private int mVerticalPadding;

    public void setVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
    }

    private List<VerticalTabItem> tabs = new ArrayList<VerticalTabItem>();

    public VerticalTabContainer addItem(int iconResId, int iconResIdUnchecked, String text) {
        VerticalTabItem VerticalTabItemView = new VerticalTabItem(getContext(), iconResIdUnchecked,iconResId, text);
        VerticalTabItemView.setVerticalPadding(mVerticalPadding);
        VerticalTabItemView.setSelectedSectionColor(mSelectedSectionColor);
        VerticalTabItemView.setSelectedTextColor(mSelectedTextColorId, mUnSelectedTextColorId);
        VerticalTabItemView.setTag(getChildCount());
        VerticalTabItemView.setClickable(true);
        VerticalTabItemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("111", v.getTag() + "");
                if (listener != null) {
                    setSelection((Integer) (v.getTag()));
                    listener.onChecked((Integer) (v.getTag()), v);
                }
            }
        });
        tabs.add(VerticalTabItemView);
        addView(VerticalTabItemView);
        return this;
    }

    public VerticalTabContainer addItem(int iconResId, int iconResIdUnchecked) {
        addItem(iconResId, iconResIdUnchecked, "");
        return this;
    }

    public VerticalTabContainer addItem(int iconResId, int iconResIdUnchecked, int textId) {
        return addItem(iconResId, iconResIdUnchecked, UiValuesUtil.getString(textId));
    }

    public void setSelection(int position) {
        for (int i = 0; i < tabs.size(); i++) {
            if (i == position) {
                tabs.get(i).setChecked(true);
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
        public void onChecked(int position, View v);
    }

    private OnCheckedListener listener;

    public void setOnCheckedListener(OnCheckedListener listener) {
        this.listener = listener;
    }


}
