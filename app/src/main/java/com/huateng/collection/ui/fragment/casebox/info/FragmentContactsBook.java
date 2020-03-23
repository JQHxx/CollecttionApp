package com.huateng.collection.ui.fragment.casebox.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.api.RespPhone;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.ui.adapter.ContactsPhoneAdapter;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.AddPhoneDM;
import com.huateng.collection.ui.dialog.dm.BaseDM;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.widget.DividerItemDecoration;
import com.huateng.network.ApiConstants;
import com.orm.SugarRecord;
import com.tools.view.RxToast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 添加电话
 */
public class FragmentContactsBook extends BaseFragment {

    @BindView(R.id.recycle_contacts_phone)
    RecyclerView recyclerView;
    @BindView(R.id.v_addPhone)
    View vAddPhone;

    private ContactsPhoneAdapter adapter;
    private List<RespPhone> phones;
    private AddPhoneDM dm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_contacts_book, container, false);
        ButterKnife.bind(this, mContentView);
        return mContentView;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        phones = (List<RespPhone>) getArguments().getSerializable(Constants.CASE_CONTACT_BOOK);

        adapter = new ContactsPhoneAdapter(R.layout.list_item_contacts_phone, phones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        vAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhone();
            }
        });
    }


    //添加电话
    public void addPhone() {
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


}
