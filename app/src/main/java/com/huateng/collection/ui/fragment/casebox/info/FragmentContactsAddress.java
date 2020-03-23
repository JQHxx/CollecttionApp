package com.huateng.collection.ui.fragment.casebox.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.AddAddress;
import com.huateng.collection.bean.api.RespAddress;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.ui.adapter.ContactsAddressAdapter;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.AddAddressDM;
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
 * 地址簿
 */
public class FragmentContactsAddress extends BaseFragment {

    @BindView(R.id.recycle_contacts_address)
    RecyclerView recyclerView;
    @BindView(R.id.v_addAddress)
    LinearLayout vAddAddress;

    private ContactsAddressAdapter adapter;
    private List<RespAddress> addresses;
    private AddAddressDM dm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_contacts_address, container, false);
        ButterKnife.bind(this, mContentView);
        return mContentView;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        addresses = (List<RespAddress>) getArguments().getSerializable(Constants.CASE_CONTACT_ADDRESS);

        adapter = new ContactsAddressAdapter(R.layout.list_item_contacts_address, addresses, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        //添加地址
        vAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAddress();
            }
        });
    }


    public void addAddress() {
        dm = new DialogCenter(getActivity()).showAddAddressDialog();
        dm.setCustNo(Perference.getCurrentCustId());
        dm.setOnFooterButtonClickListener(new BaseDM.OnFooterButtonClickListener() {
            @Override
            public void onLeftClicked(View v) {
                AddAddress addAddress = dm.collectData();
                if (addAddress != null) {
                    requestAddAddress(addAddress);
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

    //网络请求
    public void requestAddAddress(final AddAddress respAddress) {
        final String caseId = respAddress.getCaseId();
        final String addrType = respAddress.getAddrType();
        final String relWithCust = respAddress.getRelWithCust();
        final String city = respAddress.getCity();
        final String address1 = respAddress.getAddress1();
        final String postcode = respAddress.getPostcode();
        final String name = respAddress.getName();

        Map<String, String> map = new HashMap<>();
        map.put("caseId", caseId);
        map.put("custNo", Perference.getCurrentCustId());
        map.put("name", name);
        map.put("addrType", addrType);
        map.put("relWithCust", relWithCust);
        map.put("city", city);
        map.put("address", address1);
        map.put("postcode", postcode);

        CommonInteractor.request(new RequestCallbackImpl<RespAddress>() {
            @Override
            public void beforeRequest() {
                showLoading();
            }

            @Override
            public void requestError(String code, String msg) {
                super.requestError(code, msg);
                RxToast.showToast(msg);
            }

            @Override
            public void response(RespAddress respBase) {
                RxToast.showToast(respBase.getResultDesc());

                RespAddress address = new RespAddress();
                address.setAddress(respBase.getAddress());
                address.setBizId(Perference.getCurrentVisitAddressId());
                address.setAddrId(respBase.getAddrId());
                address.setAddrType(addrType);
                address.setCity(city);
                address.setPostcode(postcode);
                address.setRelWithCust(relWithCust);
                address.setCaseId(caseId);

                dm.getDialog().dismiss();
                if (CaseManager.isCaseCached(Perference.getCurrentVisitAddressId())) {
                    SugarRecord.save(address);
                    addresses.add(address);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void end() {
                super.end();
                hideLoading();
            }
        }, ApiConstants.APP_ROOT, ApiConstants.METHOD_ADD_ADDRESS, map);

    }

}