package com.tools.view.dialog;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.tools.RxPhotoUtils;
import com.tools.interfaces.onRequestPermissionsListener;
import com.tools.R;
import com.tools.view.RxToast;

import rx.functions.Action1;


/**
 * Created by vondear on 2017/3/20.
 * 封装了从相册/相机 获取 图片 的Dialog.
 */
public class RxDialogChooseImage extends RxDialog {

    private LayoutType mLayoutType = LayoutType.TITLE;
    private TextView mTvCamera;
    private TextView mTvFile;
    private TextView mTvCancel;
    private RxPermissions rxPermissions;

    public RxDialogChooseImage(Activity context) {
        super(context);
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment) {
        super(fragment.getContext());
        initView(fragment);
    }

    public RxDialogChooseImage(Activity context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment, int themeResId) {
        super(fragment.getContext(), themeResId);
        initView(fragment);
    }

    public RxDialogChooseImage(Activity context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment, float alpha, int gravity) {
        super(fragment.getContext(), alpha, gravity);
        initView(fragment);
    }

    public RxDialogChooseImage(Fragment fragment, LayoutType layoutType) {
        super(fragment.getContext());
        mLayoutType = layoutType;
        initView(fragment);
    }


    public RxDialogChooseImage(Activity context, LayoutType layoutType) {
        super(context);
        mLayoutType = layoutType;
        initView(context);
    }

    public RxDialogChooseImage(Activity context, int themeResId, LayoutType layoutType) {
        super(context, themeResId);
        mLayoutType = layoutType;
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment, int themeResId, LayoutType layoutType) {
        super(fragment.getContext(), themeResId);
        mLayoutType = layoutType;
        initView(fragment);
    }

    public RxDialogChooseImage(Activity context, float alpha, int gravity, LayoutType layoutType) {
        super(context, alpha, gravity);
        mLayoutType = layoutType;
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment, float alpha, int gravity, LayoutType layoutType) {
        super(fragment.getContext(), alpha, gravity);
        mLayoutType = layoutType;
        initView(fragment);
    }

    public TextView getTvCamera() {
        return mTvCamera;
    }

    public TextView getTvFile() {
        return mTvFile;
    }

    public TextView getTvCancel() {
        return mTvCancel;
    }

    public LayoutType getLayoutType() {
        return mLayoutType;
    }

    private void initView(final Activity activity) {
        rxPermissions = new RxPermissions(activity);

        View dialog_view = null;
        switch (mLayoutType) {
            case TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_picker_pictrue, null);
                break;
            case NO_TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_camero_show, null);
                break;
        }


        mTvCamera = (TextView) dialog_view.findViewById(R.id.tv_camera);
        mTvFile = (TextView) dialog_view.findViewById(R.id.tv_file);
        mTvCancel = (TextView) dialog_view.findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancel();
            }
        });
        mTvCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //请求Camera权限
                rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (!granted) {
                            RxToast.showToast("相机权限被禁止,请在系统设置页面设置");
                        } else {
                            RxPhotoUtils.openCameraImage(activity);
                            cancel();
                        }
                    }
                });
            }
        });
        mTvFile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (!granted) {
                            RxToast.showToast("请先获取读取SDCard权限");
                        } else {
                            RxPhotoUtils.openLocalImage(activity);
                            cancel();
                        }
                    }
                });

            }
        });
        setContentView(dialog_view);
        mLayoutParams.gravity = Gravity.BOTTOM;
    }

    private void initView(final Fragment fragment) {
        View dialog_view = null;
        switch (mLayoutType) {
            case TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_picker_pictrue, null);
                break;
            case NO_TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_camero_show, null);
                break;
        }

        mTvCamera = (TextView) dialog_view.findViewById(R.id.tv_camera);
        mTvFile = (TextView) dialog_view.findViewById(R.id.tv_file);
        mTvCancel = (TextView) dialog_view.findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancel();
            }
        });
        mTvCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //请求Camera权限
                rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (!granted) {
                            RxToast.showToast("相机权限被禁止,请在系统设置页面设置");
                        } else {
                            RxPhotoUtils.openCameraImage(fragment);
                            cancel();
                        }
                    }
                });

            }
        });
        mTvFile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                RxPhotoUtils.openLocalImage(fragment);
                cancel();
            }
        });

        setContentView(dialog_view);
        mLayoutParams.gravity = Gravity.BOTTOM;
    }

    public enum LayoutType {
        TITLE, NO_TITLE
    }
}
