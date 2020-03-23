package com.huateng.collection.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.api.RespPhone;
import com.huateng.collection.bean.orm.Dic;

import java.util.List;

import androidx.annotation.LayoutRes;


/**
 * Created by shanyong on 2016/11/29.
 */

public class ContactsPhoneAdapter extends BaseQuickAdapter<RespPhone, BaseViewHolder> {

    private String[] stringItems = {"拨号"};
    private ActionSheetDialog dialog;

    public ContactsPhoneAdapter(@LayoutRes int layoutResId, List<RespPhone> dataList) {
        super(layoutResId, dataList);
    }

    @Override
    protected void convert(BaseViewHolder helper, final RespPhone bean) {

        String phoneType = Dic.queryValue(Dic.TELIPHONE, bean.getTelType());
        String relationType = Dic.queryValue(Dic.RELATION, bean.getRelWithCust());

        helper.setText(R.id.tv_phoneType, phoneType);
        helper.setText(R.id.tv_phoneNo, bean.getTelNo());
        helper.setText(R.id.tv_userName, bean.getName());
        helper.setText(R.id.tv_relationship, relationType);


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tiltle = String.format("拨打%s电话\r\n%s", bean.getName(), bean.getTelNo());
                dialog = new ActionSheetDialog(mContext, stringItems, null);
                dialog.title(tiltle)
                        .titleTextSize_SP(14.5f)
                        .show();

                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String phoneNumber = bean.getTelNo();

                        Perference.setPrepareCallRecording(true);
                        Perference.setPrepareRecordingPhoneNumber(phoneNumber);

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);

                        dialog.dismiss();
                    }
                });
            }
        });


    }

}
