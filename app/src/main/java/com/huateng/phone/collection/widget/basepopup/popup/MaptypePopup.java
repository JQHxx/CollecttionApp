package com.huateng.phone.collection.widget.basepopup.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.huateng.phone.collection.R;
import com.huateng.phone.collection.utils.map.Constants;
import com.huateng.phone.collection.widget.basepopup.BasePopupWindow;


/**
 * Created by Lenovo on 2017/7/25.
 */

public class MaptypePopup extends BasePopupWindow implements View.OnClickListener {
    private View popupView;

    private View tencentMap;
    private View cancel;
    private View baiduMap;
    private View gaodeMap;

    private onMaptypeClickListener mListener;

    public MaptypePopup(Activity context) {
        super(context);
        initViews();
    }

    private void initViews() {
        tencentMap = popupView.findViewById(R.id.layout_maptencent);
        baiduMap = popupView.findViewById(R.id.layout_mapbaidu);
        gaodeMap = popupView.findViewById(R.id.layout_mapgaode);
        cancel = popupView.findViewById(R.id.iv_cancel);

        if (popupView != null) {
            tencentMap.setOnClickListener(this);
            baiduMap.setOnClickListener(this);
            gaodeMap.setOnClickListener(this);
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
            case R.id.layout_mapbaidu:
                if (mListener != null) {

                    mListener.onClick(Constants.MapType.MAP_BAIDU);
                }
                break;
            case R.id.layout_mapgaode:
                if (mListener != null) {
                    mListener.onClick(Constants.MapType.MAP_GAODE);
                }
                break;
            default:
                break;
        }

    }

    public interface onMaptypeClickListener {
        void onClick(Constants.MapType type);
    }

}
