package com.huateng.collection.ui.fragment.casebox.casefill;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.ReportSegmentRecyclerItem;
import com.huateng.collection.bean.api.RespAddress;
import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.bean.orm.PendingReportData;
import com.huateng.collection.event.BusEvent;
import com.huateng.collection.ui.adapter.ReportSegmentAdapter;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.fm.util.FmValueUtil;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tools.view.RxToast;

import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.anim.DefaultNoAnimator;

/**
 * 外访报告
 */
public class FragmentReportSegment extends BaseFragment {
    private String TAG = getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_title)
    ImageView ivTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private ReportSegmentAdapter adapter;
    private List<ReportSegmentRecyclerItem> items = new ArrayList<>();

    private String title;
    private int titleIconResId;
    private List<PendingReportData> pendingReportDataList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_report_segment, container, false);
        ButterKnife.bind(this, mContentView);
        EventBusActivityScope.getDefault(_mActivity).register(this);
        return mContentView;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            title = getArguments().getString("TITLE");
            titleIconResId = getArguments().getInt("TITLE_ICON");
            items.clear();
            items.addAll((ArrayList<ReportSegmentRecyclerItem>) getArguments().getSerializable("RECYCLER_ITEMS"));
        }
        TAG = title;

        initReportList();

        ivTitle.setImageResource(titleIconResId);
        tvTitle.setText(title);
    }


    private void initReportList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ReportSegmentAdapter(R.layout.list_item_report_segment, items);
        recyclerView.setAdapter(adapter);

        updateContent();
    }


    //刷新内容
    public void updateContent() {

        adapter.clearHistoryInput();
        List<PendingReportData> list = SugarRecord.find(PendingReportData.class, "BIZ_ID = ?", Perference.getCurrentVisitAddressId());

        if (FmValueUtil.isListNotEmpty(list)) {
            Log.e(TAG, "update  Content-list-key: " + list.get(0).getContent());
            pendingReportDataList.clear();
            pendingReportDataList.addAll(list);
            for (int i = 0; i < items.size(); i++) {
                ReportSegmentRecyclerItem item = items.get(i);
                for (int j = 0; j < pendingReportDataList.size(); j++) {
                    PendingReportData reportData = pendingReportDataList.get(j);
                    if (reportData.getKey().equals(title + "_" + item.getLabel())) {
//                        imitateDic(item);
                        item.setContent(reportData.getContent());
                    }
                }
                items.set(i, item);
            }
        } else {
            //未编辑 需设置内容为空
            for (int i = 0; i < items.size(); i++) {
                ReportSegmentRecyclerItem item = items.get(i);
                if (item.getLabel().equals("案件号")) {
                    item.setContent(Perference.getCurrentCaseId());
                } else if (item.getLabel().equals("客户姓名")) {
                    item.setContent(Perference.getCurrentCustName());
                } else if (item.getLabel().equals("地址")) {
                    item.setContent(Perference.getCurrentVisitAddress());
                } else {
                    item.setContent("");
                }
                items.set(i, item);
            }
        }
        Log.i(TAG, "items.size" + items.size());
        adapter.notifyDataSetChanged();
    }


    private String queryKey(List<Dic> dics, String value) {
        for (Dic dic : dics) {
            if (dic.getValue().equals(value)) {
                return dic.getKey();
            }
        }
        return "";
    }

    @Subscribe
    public void saveReport(String event) {
        if (event.equals(BusEvent.SAVE_REPORT)) {
            Logger.i("saveReport :%s", title);
            collectData();
        }
    }

    //保存已填信息
    private void collectData() {
        pendingReportDataList.clear();

        List<String> values = adapter.getValues();

        for (int i = 0; i < items.size(); i++) {
            String value = values.get(i);
            PendingReportData data = new PendingReportData();
            ReportSegmentRecyclerItem item = items.get(i);
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

    //将可选的东西 模仿成dic
    public void imitateDic(ReportSegmentRecyclerItem item) {
        String currentCaseId = Perference.getCurrentCaseId();

        if (item.getLabel().equals("地址编号")) {
            List<RespAddress> addresses = SugarRecord.find(RespAddress.class, "CASE_ID = ?", currentCaseId);
            List<Dic> dics = new ArrayList<>();
            for (int j = 0; j < addresses.size(); j++) {
                RespAddress address = addresses.get(j);
                dics.add(Dic.objectToDic(address.getAddrId(), address.getAddress(), address.getAddrType()));
            }
            item.setDicItems(dics);
            item.setImitate(true);
        } else if (item.getLabel().equals("录音编号")) {
            List<FileData> fileDatas = SugarRecord.find(FileData.class, "CASE_ID=? and TYPE=? and EXIST=1", currentCaseId, FileData.TYPE_AUDIO);
            List<Dic> dics = new ArrayList<>();
            for (int k = 0; k < fileDatas.size(); k++) {
                FileData fileData = fileDatas.get(k);
                dics.add(Dic.objectToDic(fileData.getFileId(), fileData.getFileId(), FileData.TYPE_AUDIO));
            }
            item.setDicItems(dics);
            item.setImitate(true);
        } else if (item.getLabel().equals("照片编号")) {
            List<FileData> fileDatas = SugarRecord.find(FileData.class, "CASE_ID=?  and TYPE=? and EXIST=1", currentCaseId, FileData.TYPE_PHOTO);
            //Log.i(" sugar exist photo size", String.format("-------->case id :%s  size: %s",GlobalConstants.currentCaseId,fileDatas.size()));
            List<Dic> dics = new ArrayList<>();
            for (int k = 0; k < fileDatas.size(); k++) {
                FileData fileData = fileDatas.get(k);
                dics.add(Dic.objectToDic(fileData.getFileId(), fileData.getFileId(), FileData.TYPE_PHOTO));
            }
            item.setImitate(true);
            item.setDicItems(dics);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }

    public static FragmentReportSegment newInstance(String title, int titleIconResId, List<ReportSegmentRecyclerItem> items) {
        FragmentReportSegment fragment = new FragmentReportSegment();
        Bundle args = new Bundle();
        args.putString("TITLE", title);
        args.putInt("TITLE_ICON", titleIconResId);
        args.putSerializable("RECYCLER_ITEMS", (ArrayList<ReportSegmentRecyclerItem>) items);
        fragment.setArguments(args);
        return fragment;
    }

}
