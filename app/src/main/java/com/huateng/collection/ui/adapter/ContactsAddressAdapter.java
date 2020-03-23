package com.huateng.collection.ui.adapter;

import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.api.RespAddress;
import com.huateng.collection.bean.api.RespBase;
import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.BaseDM;
import com.huateng.collection.ui.dialog.dm.VisitSummarizeDM;
import com.huateng.network.ApiConstants;
import com.tools.view.RxToast;

import java.util.List;
import java.util.Map;

import androidx.annotation.LayoutRes;


/**
 * Created by shanyong on 2016/11/29.
 * 地址簿
 */

public class ContactsAddressAdapter extends BaseQuickAdapter<RespAddress, BaseViewHolder> {

    private VisitSummarizeDM dm;

    private String[] stringItems = {"添加上门总结"};
    private ActionSheetDialog dialog;
    private BaseFragment baseFragment;

    public ContactsAddressAdapter(@LayoutRes int layoutResId, List<RespAddress> dataList, BaseFragment fragment) {
        super(layoutResId, dataList);
        this.baseFragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, final RespAddress bean) {

        String addressType = Dic.queryValue(Dic.ADDRESS, bean.getAddrType());

        helper.setText(R.id.tv_addressType, addressType);
        helper.setText(R.id.tv_address, bean.getAddress());

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ActionSheetDialog(mContext, stringItems, null);
                dialog.title(bean.getAddress())
                        .titleTextSize_SP(14.5f)
                        .show();

                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();

                        dm = new DialogCenter(mContext).showVisitSummarizeDialog();
                        dm.setVisitAddress(bean.getAddress());
                        dm.setVisitOfficer(Perference.getUserId());
                        dm.setAddrId(bean.getAddrId());

                        dm.setOnFooterButtonClickListener(new BaseDM.OnFooterButtonClickListener() {
                            @Override
                            public void onLeftClicked(View v) {
                                Map<String, String> map = dm.collectData();
                                if (map != null) {
                                    map.put("visitAddress",bean.getAddress());
                                    map.put("addrType",bean.getAddrType());

                                    CommonInteractor.request(new RequestCallbackImpl<RespBase>() {
                                        @Override
                                        public void beforeRequest() {
                                            baseFragment.showLoading();
                                        }

                                        @Override
                                        public void requestError(String code, String msg) {
                                            super.requestError(code, msg);
                                            RxToast.showToast("新增总结失败");
                                        }

                                        @Override
                                        public void response(RespBase respBase) {
                                            if (respBase.getResultCode().equals("0000")) {
                                                RxToast.showToast(respBase.getResultDesc());
                                                dm.getDialog().dismiss();
                                            }
                                        }

                                        @Override
                                        public void end() {
                                            super.end();
                                            baseFragment.hideLoading();
                                        }
                                    }, ApiConstants.APP_ROOT, ApiConstants.METHOD_ADD_VISIT_LOG, map);

                                } else {
                                    RxToast.showToast("信息未填写完整!");
                                }
                            }

                            @Override
                            public void onRightClicked(View v) {
                                dm.getDialog().dismiss();
                            }
                        });
                    }
                });
            }
        });

    }

    public VisitSummarizeDM getDm() {
        return dm;
    }

}