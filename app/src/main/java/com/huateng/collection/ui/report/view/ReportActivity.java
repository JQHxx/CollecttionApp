package com.huateng.collection.ui.report.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.bean.ReportSegmentRecyclerItem;
import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.bean.orm.PendingReportData;
import com.huateng.collection.ui.adapter.ReportSegmentAdapter;
import com.huateng.collection.ui.report.contract.ReportContract;
import com.huateng.collection.ui.report.presenter.ReportPresenter;
import com.huateng.collection.widget.UniversalInputType;
import com.huateng.collection.widget.Watermark;
import com.huateng.fm.util.FmValueUtil;
import com.orm.SugarRecord;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * author: yichuan
 * Created on: 2020-04-01 18:03
 * description:
 */
public class ReportActivity extends BaseActivity<ReportPresenter> implements ReportContract.View {
    private String TAG = getClass().getSimpleName();
    private String title;
    // private String title;
    private List<PendingReportData> pendingReportDataList = new ArrayList<>();
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    private List<ReportSegmentRecyclerItem> datalist = new ArrayList<>();
    private ReportSegmentAdapter adapter;

    @Override
    protected ReportPresenter createPresenter() {
        return new ReportPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Watermark.getInstance()
                .setTextColor(getResources().getColor(R.color.dialogplus_card_shadow))
                .setTextSize(12.0f)
                .setText("测试文本")
                .show(this);

        initRecyclerItemsAnswerDoorInfo();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());

        adapter = new ReportSegmentAdapter(R.layout.list_item_report_segment, datalist);
        mRecyclerview.setAdapter(adapter);
        initListener();
    }

    private void initListener() {
        mRxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRxTitle.getLlRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectData();
                RxToast.showToast("保存");
            }
        });
    }


    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_report;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        //  adapter.setNewData(datalist);
        // adapter.notifyDataSetChanged();
        title = getIntent().getStringExtra("title");
        updateContent();
    }


    //刷新内容
    public void updateContent() {

        adapter.clearHistoryInput();
        List<PendingReportData> list = SugarRecord.find(PendingReportData.class, "BIZ_ID = ?", Perference.getCurrentVisitAddressId());

        if (FmValueUtil.isListNotEmpty(list)) {
            Log.e("nb", "设置内容");
            Log.e("nb", "update  Content-list-key: " + list.get(0).getContent());
            pendingReportDataList.clear();
            pendingReportDataList.addAll(list);
            for (int i = 0; i < pendingReportDataList.size(); i++) {
                Log.e("nb", ":" + pendingReportDataList.get(i).getContent());
            }

            for (int i = 0; i < datalist.size(); i++) {
                ReportSegmentRecyclerItem item = datalist.get(i);
                for (int j = 0; j < pendingReportDataList.size(); j++) {

                    PendingReportData reportData = pendingReportDataList.get(j);

                    if (reportData.getKey().equals(title + "_" + item.getLabel())) {
                        //                       imitateDic(item);
                        item.setContent(reportData.getContent());
                    }
                }
                datalist.set(i, item);
            }
        } else {
            Log.e("nb", "未编辑 需设置内容为空");
            //未编辑 需设置内容为空
            for (int i = 0; i < datalist.size(); i++) {
                ReportSegmentRecyclerItem item = datalist.get(i);
                if (item.getLabel().equals("案件号")) {
                    item.setContent(Perference.getCurrentCaseId());
                } else if (item.getLabel().equals("客户姓名")) {
                    item.setContent(Perference.getCurrentCustName()+"111");
                } else if (item.getLabel().equals("地址")) {
                    item.setContent(Perference.getCurrentVisitAddress());
                } else {
                    item.setContent("");
                }
                datalist.set(i, item);
            }
        }
        Log.i(TAG, "items.size" + datalist.size());
        adapter.notifyDataSetChanged();
    }


    private void initRecyclerItemsAnswerDoorInfo() {
        datalist.add(getRecyclerItem("案件号", UniversalInputType.TEXT, 1));
        datalist.add(getRecyclerItem("客户姓名", UniversalInputType.TEXT, 2));
        datalist.add(getRecyclerItem("外访日期", UniversalInputType.DATE.DATE_SINGEL, Dic.dics(Dic.RELATION), 3));
        datalist.add(getRecyclerItem("逾期原因", UniversalInputType.EDIT.TEXT, 4));
        datalist.add(getRecyclerItem("单位在职情况", UniversalInputType.SPINNER, getWordType(), 5));//Dic.dics(Dic.SEX)
        datalist.add(getRecyclerItem("其他有效住址", UniversalInputType.EDIT.TEXT, 6));
        datalist.add(getRecyclerItem("是否见到本人", UniversalInputType.SPINNER,isSelf() , 18));
        datalist.add(getRecyclerItem("婚姻状况", UniversalInputType.SPINNER,isMarriage() ,19));
        datalist.add(getRecyclerItem("车辆信息", UniversalInputType.EDIT.TEXT, Dic.dics(Dic.WEATHER), 7));
        datalist.add(getRecyclerItem("房屋信息", UniversalInputType.EDIT.TEXT, Dic.dics(Dic.RELATION), 8));
        datalist.add(getRecyclerItem("公积金信息", UniversalInputType.EDIT.TEXT, 9));
        datalist.add(getRecyclerItem("投资信息", UniversalInputType.EDIT.TEXT, Dic.dics(Dic.SEX), 10));
        datalist.add(getRecyclerItem("外债信息", UniversalInputType.EDIT.TEXT, 11));
        datalist.add(getRecyclerItem("投资信息", UniversalInputType.EDIT.TEXT, Dic.dics(Dic.SEX), 12));
        datalist.add(getRecyclerItem("外债信息", UniversalInputType.EDIT.TEXT, 13));
        datalist.add(getRecyclerItem("其他财产信息", UniversalInputType.EDIT.TEXT, Dic.dics(Dic.SEX), 14));
        datalist.add(getRecyclerItem("详细内容", UniversalInputType.EDIT.TEXT, 15));
        datalist.add(getRecyclerItem("外访人员", UniversalInputType.EDIT.TEXT, Dic.dics(Dic.SEX), 16));
        datalist.add(getRecyclerItem("外访地址", UniversalInputType.EDIT.TEXT, 17));

    }

    private List<Dic> isMarriage() {
        List<Dic> list = new ArrayList<>();
        Dic dic = new Dic();
        dic.setKey("marriage");
        dic.setValue("已婚");
        Dic dic2 = new Dic();
        dic2.setKey("marriage");
        dic2.setValue("未婚");
        Dic dic3 = new Dic();
        dic3.setKey("marriage");
        dic3.setValue("离异");
        list.add(dic);
        list.add(dic2);
        list.add(dic3);
        return list;
    }

    /**
     *是否见到本人
     * @return
     */
    private List<Dic> isSelf() {
        List<Dic> list = new ArrayList<>();
        Dic dic = new Dic();
        dic.setKey("self");
        dic.setValue("是");
        Dic dic2 = new Dic();
        dic2.setKey("self");
        dic2.setValue("否");
        list.add(dic);
        list.add(dic2);
        return list;
    }

    /**
     * 是否在职
     * @return
     */
    private List<Dic> getWordType() {
        List<Dic> list = new ArrayList<>();
        Dic dic = new Dic();
        dic.setKey("work1");
        dic.setValue("在职");
        Dic dic2 = new Dic();
        dic2.setKey("work2");
        dic2.setValue("离职");
        list.add(dic);
        list.add(dic2);
        return list;

    }

    private ReportSegmentRecyclerItem getRecyclerItem(String label, int inputType, List<Dic> dicItems, int index) {
        ReportSegmentRecyclerItem item = new ReportSegmentRecyclerItem();
        item.setLabel(label);
        item.setInputType(inputType);
        item.setDicItems(dicItems);
        item.setPosition(index);
        return item;
    }

    private ReportSegmentRecyclerItem getRecyclerItem(String label, int inputType, int index) {
        return getRecyclerItem(label, inputType, null, index);
    }

    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, mRxTitle);

    }

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }



    //保存已填信息
    private void collectData() {
        pendingReportDataList.clear();

        List<String> values = adapter.getValues();

        for (int i = 0; i < datalist.size(); i++) {
            String value = values.get(i);
            PendingReportData data = new PendingReportData();
            ReportSegmentRecyclerItem item = datalist.get(i);
            data.setBizId(Perference.getCurrentVisitAddressId());
            data.setCaseId(Perference.getCurrentCaseId());
            data.setUserId(Perference.getUserId());
            data.setKey(title + "_" + item.getLabel());
            data.setValue(value);
            data.setPosition(item.getPosition());
            data.setTag(getMD5(data.getBizId() + data.getKey()));

            boolean isDic = !FmValueUtil.isListEmpty(item.getDicItems());
            data.setDic(isDic);
            if (isDic) {
                data.setContent(queryKey(item.getDicItems(), value));
            } else {
                data.setContent(value);
            }
            pendingReportDataList.add(data);
        }
        SugarRecord.saveInTx(pendingReportDataList);
    }

    public String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }


    private String queryKey(List<Dic> dics, String value) {
        for (Dic dic : dics) {
            if (dic.getValue().equals(value)) {
                return dic.getKey();
            }
        }
        return "";
    }
}
