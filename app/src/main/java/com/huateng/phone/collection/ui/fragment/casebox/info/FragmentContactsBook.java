package com.huateng.phone.collection.ui.fragment.casebox.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.app.Constants;
import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.base.BaseFragment;
import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.bean.CustTelInfoBean;
import com.huateng.phone.collection.ui.adapter.ContactsPhoneAdapter;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 添加电话
 */
public class FragmentContactsBook extends BaseFragment {
    private String[] stringItems = {"拨号"};
    private ActionSheetDialog dialog;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private String custId;
    private String caseId;
    private int pageSize = 15;
    private int pageNum = 1;
    @BindView(R.id.recycle_contacts_phone)
    RecyclerView recyclerView;

    private ContactsPhoneAdapter adapter;
    // private AddPhoneDM dm;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts_book;
    }

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        adapter = new ContactsPhoneAdapter(R.layout.list_item_contacts_phone);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //  recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.rl_call) {
                    CustTelInfoBean.RecordsBean bean = (CustTelInfoBean.RecordsBean) adapter.getData().get(position);
                    if (TextUtils.isEmpty(bean.getContactPnhone())) {
                        RxToast.showToast("手机号码不能未空");
                        return;
                    }
                    //bean.getContactPnhone()
                    String tiltle = String.format("拨打%s电话\r\n%s", bean.getName(), bean.getContactPnhone());
                    dialog = new ActionSheetDialog(mContext, stringItems, null);
                    dialog.title(tiltle)
                            .titleTextSize_SP(14.5f)
                            .show();

                    dialog.setOnOperItemClickL(new OnOperItemClickL() {
                        @Override
                        public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String phoneNumber = bean.getContactPnhone();

                            Perference.setPrepareCallRecording(true);
                            Perference.setPrepareRecordingPhoneNumber(phoneNumber);

                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);

                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        custId = getArguments().getString(Constants.CUST_ID);
        caseId = getArguments().getString(Constants.CASE_ID);
        loadData();

    }

    private void loadData() {

        Map<String, Object> map = new HashMap<>();
        map.put("caseId", caseId);
        map.put("custNo", custId);
        map.put("tlrNo", Perference.getUserId());
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECT_CUST_TEL_INFO, map)
                .compose(getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<CustTelInfoBean>() {


                    @Override
                    public void onError(String code, String msg) {

                        mSwipeRefresh.setRefreshing(false);
                        adapter.setEmptyView(R.layout.layout_empty_view, recyclerView);
                    }

                    @Override
                    public void onNextData(CustTelInfoBean custTelInfoBean) {
                        if (custTelInfoBean == null) {
                            return;
                        }
                        mSwipeRefresh.setRefreshing(false);
                        if (custTelInfoBean.getRecords().size() == 0) {
                            adapter.setEmptyView(R.layout.layout_empty_view, recyclerView);
                            return;
                        }

                        adapter.setNewData(custTelInfoBean.getRecords());
                    }


                });


    }


    //添加电话
  /*  public void addPhone() {
        dm = new DialogCenter(getActivity()).showAddPhoneDialog();
        dm.setCaseNo(Perference.getCurrentCaseId());
        dm.setCustNo(Perference.getCurrentCustId());
        dm.setOnFooterButtonClickListener(new BaseDM.OnFooterButtonClickListener() {
            @Override
            public void onLeftClicked(View v) {
                final RespPhone respPhone = dm.collectData();

                if (respPhone != null) {
                    final String caseId = respPhone.getCaseId();
                    final String addrId = Perference.getCurrentVisitAddressId();

                    String name = respPhone.getName();
                    String relWithCust = respPhone.getRelWithCust();
                    String telNo = respPhone.getTelNo();
                    String telType = respPhone.getTelType();

                    Map<String, String> map = new HashMap<>();
                    map.put("telType", telType);
                    map.put("custNo", Perference.getCurrentCustId());
                    map.put("name", name);
                    map.put("telNo", telNo);
                    map.put("relWithCust", relWithCust);

                    CommonInteractor.request(new RequestCallbackImpl<RespPhone>() {
                        @Override
                        public void beforeRequest() {
                            showLoading();
                        }

                        @Override
                        public void requestError(String code, String msg) {
                            super.requestError(code, msg);
                            Toast.makeText(getActivity(), "新增电话失败", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void response(RespPhone respBase) {
                            RxToast.showToast(respBase.getResultDesc());
                            respPhone.setTelId(respBase.getTelId());
                            respPhone.setBizId(Perference.getCurrentVisitAddressId());

                            dm.getDialog().dismiss();
                            if (CaseManager.isCaseCached(addrId)) {
                                SugarRecord.save(respPhone);
                                phones.add(respPhone);
                                adapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void end() {
                            super.end();
                            hideLoading();
                        }
                    }, ApiConstants.APP_ROOT, ApiConstants.METHOD_ADD_TEL, map);


                } else {
                    RxToast.showToast(getString(R.string.info_empty_tips));
                }
            }

            @Override
            public void onRightClicked(View v) {
                dm.getDialog().dismiss();
            }
        });
    }
*/

    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
