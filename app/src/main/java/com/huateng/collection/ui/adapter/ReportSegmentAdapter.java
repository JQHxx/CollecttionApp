package com.huateng.collection.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.ReportSegmentRecyclerItem;
import com.huateng.collection.bean.api.RespAddress;
import com.huateng.collection.bean.api.RespBase;
import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.BaseDM;
import com.huateng.collection.ui.dialog.dm.VisitSummarizeDM;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.widget.UniversalInput;
import com.huateng.collection.widget.UniversalInputType;
import com.huateng.fm.util.FmValueUtil;
import com.huateng.network.ApiConstants;
import com.orhanobut.logger.Logger;
import com.tools.view.RxToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 外访报告item adapter
 */
public class ReportSegmentAdapter extends BaseQuickAdapter<ReportSegmentRecyclerItem, BaseViewHolder> {

    private VisitSummarizeDM dm;

    //所填信息保存
    private List<String> values = new ArrayList<>();

    public ReportSegmentAdapter(@LayoutRes int layoutResId, @Nullable List<ReportSegmentRecyclerItem> data) {
        super(layoutResId, data);
        for (int i = 0; i < data.size(); i++) {
            values.add("");
        }
    }

    public void clearHistoryInput() {
        values.clear();
        for (int i = 0; i < getData().size(); i++) {
            values.add("");
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, ReportSegmentRecyclerItem bean) {
        final int position = helper.getPosition();

        UniversalInput csvInput = helper.getView(R.id.csv_input);
        csvInput.setInputType(bean.getInputType());

//        Logger.i("%s : %s",bean.getLabel(),bean.getInputType());


        helper.setText(R.id.tv_label, bean.getLabel());

        csvInput.setOnValueChangeListener(new UniversalInput.OnValueChangeListener() {
            @Override
            public void onValueChanged(String value) {
                values.set(position, value);
            }
        });


        if (bean.getInputType() == UniversalInputType.SPINNER) {
            if (null != bean.getDicItems()) {
                csvInput.setDataSource(getOptions(bean.getDicItems()));
                //未编辑过，则显示历史保存内容
                if (FmValueUtil.isStrEmpty(values.get(position))) {
                    values.set(position, queryValue(bean.getDicItems(), bean.getContent()));
                }
            }
        } else {
            //未编辑过，则显示历史保存内容
            if (FmValueUtil.isStrEmpty(values.get(position))) {
                values.set(position, bean.getContent());
            }
        }

        csvInput.setText(values.get(position));

        if (bean.getLabel().equals("地址编号")) {
            Logger.i("地址:   %s->%s", bean.getContent(), values.get(position));
        }

        //点击地址添加icon
        if (bean.getLabel().equals("地址")) {
            csvInput.setOptionIcon(R.drawable.ic_add);
            csvInput.setOnHtOptionTouchListener(new UniversalInput.OnHtOptionTouchListener() {
                @Override
                public void onHtOptionTouched() {
                    Logger.i("on option");
                    addVisitSummary();
                }
            });
        }
    }


    public List<String> getValues() {
        return values;
    }

    private String queryValue(List<Dic> dics, String key) {
        for (Dic dic : dics) {
            if (dic.getKey().equals(key)) {
                return dic.getValue();
            }
        }
        return "";
    }

    private List<String> getOptions(List<Dic> dics) {
        List<String> options = new ArrayList<>();
        for (Dic dic : dics) {
            options.add(dic.getValue());
        }
        return options;
    }

    public void addVisitSummary() {
        dm = new DialogCenter(mContext).showVisitSummarizeDialog();
        String addrId = Perference.getCurrentVisitAddressId();
        final RespAddress respAddress = CaseManager.obtainCachedAddress(addrId);

        String address = respAddress.getAddress();

        dm.setVisitAddress(address);
        dm.setVisitOfficer(Perference.getUserId());
        dm.setAddrId(addrId);

        Logger.i(address);

        dm.setOnFooterButtonClickListener(new BaseDM.OnFooterButtonClickListener() {
            @Override
            public void onLeftClicked(View v) {
                Map<String, String> map = dm.collectData();
                if (map != null) {
                    map.put("visitAddress", respAddress.getAddress());
                    map.put("addrType", respAddress.getAddrType());

                    CommonInteractor.request(new RequestCallbackImpl<RespBase>() {


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
                                RxToast.showToast("新增总结成功");
                            }
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

}
