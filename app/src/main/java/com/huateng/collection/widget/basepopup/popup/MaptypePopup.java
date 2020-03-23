package com.huateng.collection.widget.basepopup.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.huateng.collection.R;
import com.huateng.collection.utils.map.Constants;
import com.huateng.collection.widget.basepopup.BasePopupWindow;


/**
 * Created by Lenovo on 2017/7/25.
 */

public class MaptypePopup extends BasePopupWindow implements View.OnClickListener {
    private View popupView;

    private View tencentMap;
    private View cancel;

    private onMaptypeClickListener mListener;

    public MaptypePopup(Activity context) {
        super(context);
        initViews();
    }

    private void initViews() {
        tencentMap = popupView.findViewById(R.id.layout_maptencent);
        cancel = popupView.findViewById(R.id.iv_cancel);

        if (popupView != null) {
            tencentMap.setOnClickListener(this);
            cancel.setOnClickListener(this);
        }
    }


    public void setOnMaptypeClickListener(onMaptypeClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected Animation getShowAnimation() {
//        return getTranslateAnimation(250 * 2, 0, 300);
        return null;
    }

    @Override
    protected View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View getPopupView() {
        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_map_type, null);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_maptencent:
                if (mListener != null) {
                    mListener.onClick(Constants.MapType.MAP_TENCENT);
                }
                break;
            case R.id.iv_cancel:
                this.dismissWithOutAnima();
                break;
            default:
                break;
        }

    }

    public interface onMaptypeClickListener {
        void onClick(Constants.MapType type);
    }

}
