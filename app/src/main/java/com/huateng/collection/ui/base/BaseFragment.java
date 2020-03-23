package com.huateng.collection.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.widget.LoadingDialog;
import com.huateng.collection.widget.appmsg.AppMsg;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import me.yokeyword.fragmentation.SupportFragment;

import static android.view.Gravity.BOTTOM;

/**
 * Created by sumincy on 2018/9/30.
 * fragment
 */

public abstract class BaseFragment extends SupportFragment {

    protected View mContentView;
    protected Context mContext;

    private LoadingDialog dialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(savedInstanceState);
    }

    protected abstract void init(Bundle savedInstanceState);

    protected void immersiveStatusBar(View title) {
        SystemBarHelper.immersiveStatusBar((Activity) mContext, 0);
        SystemBarHelper.setHeightAndPadding(mContext, title);
    }

    protected View findViewById(@IdRes int id) {
        return mContentView.findViewById(id);
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragmentForResult(SupportFragment targetFragment, int requestCode) {
        startForResult(targetFragment, requestCode);
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment, @LaunchMode int mode) {
        start(targetFragment, mode);
    }


    @Override
    public void pop() {
        super.pop();
        hideSoftInput();
    }

    public void showLoading() {
        if (dialog == null) {
            dialog = new LoadingDialog(getActivity());
        }
        dialog.show();
    }

    public void hideLoading() {
        if (dialog != null) {
            dialog.hide();
        }
    }

    public void showMessage(String msg) {
        showMessage(msg, null);
    }

    public void showMessage(String msg, AppMsg.Style style) {
        AppMsg.Style mStyle = style != null ? style : AppMsg.STYLE_NORMAL;
        AppMsg appMsg = AppMsg.makeText(_mActivity, msg, mStyle);
        appMsg.setLayoutGravity(BOTTOM);
        appMsg.show();
    }


    public void showConfirmMessage(String msg, final AppMsgConfirmListener listener) {
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_STICKY, R.color.theme_color_content);
        final AppMsg appMsg = AppMsg.makeText(_mActivity, msg, style, R.layout.msg_confirm_layout);
        appMsg.setLayoutGravity(BOTTOM);
        View ConfirmButton = appMsg.getView().findViewById(R.id.confirm_btn);
        View CancelButton = appMsg.getView().findViewById(R.id.cancel_btn);
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appMsg.cancel();
                listener.OnConfirm();
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appMsg.cancel();
                listener.OnCancel();
            }
        });
        appMsg.show();
    }

    public interface AppMsgConfirmListener {
        void OnConfirm();

        void OnCancel();
    }


    public static BaseFragment newInstance(Class<?> clazz) {
        Bundle args = new Bundle();
        BaseFragment fragment = null;
        try {
            fragment = (BaseFragment) clazz.newInstance();
            fragment.setArguments(args);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    public static BaseFragment newInstance(Class<?> clazz, Bundle args) {
        BaseFragment fragment = null;
        try {
            fragment = (BaseFragment) clazz.newInstance();
            fragment.setArguments(args);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideSoftInput();
    }
}
