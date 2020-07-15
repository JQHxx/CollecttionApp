package com.huateng.phone.collection.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * author: yichuan
 * Created on: 2020/5/26 15:12
 * description:
 */

public class AlertDialogFragment extends DialogFragment {


    private String mTitle, mMessage, mLeftMessage, mRightMessage;
    private AlertDialog.Builder builder;
    private OnDialogButtonClickListener clickListener;
    private static final String TAG = "AlertDialogFragment";
    private static final String TITLE = "温馨提示";
    private static final String LEFT_MESSAGE = "取消";
    private static final String RIGHT_MESSAGE = "确定";
    private Context context;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public static AlertDialogFragment getInstance(String mMessage) {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mTitle", TITLE);
        bundle.putString("mMessage", mMessage);
        bundle.putString("mLeftMessage", LEFT_MESSAGE);
        bundle.putString("mRightMessage", RIGHT_MESSAGE);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    public static AlertDialogFragment getInstance(String mTitle, String mMessage, String mLeftMessage, String mRightMessage) {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mTitle", mTitle);
        bundle.putString("mMessage", mMessage);
        bundle.putString("mLeftMessage", mLeftMessage);
        bundle.putString("mRightMessage", mRightMessage);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    private void initData() {
        Bundle bundle = getArguments();
        mTitle = bundle.getString("mTitle", TITLE);
        mMessage = bundle.getString("mMessage", "");
        mLeftMessage = bundle.getString("mLeftMessage", LEFT_MESSAGE);
        mRightMessage = bundle.getString("mRightMessage", RIGHT_MESSAGE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    public void setOnButtonClickListener(OnDialogButtonClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(mTitle).setMessage(mMessage)
                .setNegativeButton(mLeftMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (clickListener != null) {
                            clickListener.onClickLeft();
                        }
                    }
                }).setPositiveButton(mRightMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (clickListener != null) {
                    clickListener.onClickRight();
                }
            }
        });
        setCancelable(false);
        return builder.create();
    }

    public interface OnDialogButtonClickListener {
        void onClickLeft();

        void onClickRight();
    }

}
