package com.huateng.phone.collection.ui.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.bean.CustTelInfoBean;

import androidx.annotation.LayoutRes;


/**
 * Created by shanyong on 2016/11/29.
 */

public class ContactsPhoneAdapter extends BaseQuickAdapter<CustTelInfoBean.RecordsBean, BaseViewHolder> {

    private String[] stringItems = {"拨号"};
    private ActionSheetDialog dialog;

    public ContactsPhoneAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CustTelInfoBean.RecordsBean bean) {

        helper.setText(R.id.tv_name, bean.getName())
                .setText(R.id.tv_contact_conpany, bean.getContactCompany())
                .setText(R.id.tv_contact_id_no, bean.getContactIdno())
                .setText(R.id.tv_contact_phone, bean.getContactPnhone())
                .setText(R.id.tv_rel_with_cust, bean.getRelWithCust());

        TextView mTvSex = helper.getView(R.id.tv_contact_sex);
        TextView mTvFamilyFlag = helper.getView(R.id.tv_family_flag);
        TextView mTvEffectiveFlag = helper.getView(R.id.tv_effective_flag);
        if ("M".equals(bean.getContactSex())) {
            mTvSex.setText("男");
        } else if ("F".equals(bean.getContactSex())) {
            mTvSex.setText("女");
        } else {
            mTvSex.setText("");
        }

        if (TextUtils.isEmpty(bean.getContactPnhone())) {
            helper.setVisible(R.id.rl_call, false);
        } else {
            helper.setVisible(R.id.rl_call, true);
        }

        if ("Y".equals(bean.getContactSex())) {
            mTvFamilyFlag.setText("是");
        } else if ("N".equals(bean.getContactSex())) {
            mTvFamilyFlag.setText("否");
        } else {
            mTvFamilyFlag.setText("");
        }

        if ("Y".equals(bean.getEffectiveFlag())) {
            mTvEffectiveFlag.setText("是");
        } else if ("N".equals(bean.getEffectiveFlag())) {
            mTvEffectiveFlag.setText("否");
        } else {
            mTvEffectiveFlag.setText("");
        }

        helper.addOnClickListener(R.id.rl_call);

       /* helper.itemView.setOnClickListener(new View.OnClickListener() {
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

*/
    }

}
