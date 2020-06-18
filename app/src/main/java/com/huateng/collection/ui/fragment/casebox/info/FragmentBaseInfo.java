package com.huateng.collection.ui.fragment.casebox.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.CustInfoBean;
import com.huateng.collection.bean.orm.DictItemBean;
import com.huateng.collection.utils.DictUtils;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.orm.SugarRecord;
import com.trello.rxlifecycle3.LifecycleTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 客户信息
 */
public class FragmentBaseInfo extends BaseFragment {

    @BindView(R.id.tv_cust_name)
    TextView mTvCustName;
    @BindView(R.id.tv_gender)
    TextView mTvGender;
    @BindView(R.id.tv_cert_no)
    TextView mTvCertNo;
    @BindView(R.id.tv_native_place)
    TextView mTvNativePlace;
    @BindView(R.id.tv_ethnic_group)
    TextView mTvEthnicGroup;
    @BindView(R.id.tv_mobile_phone)
    TextView mTvMobilePhone;
    @BindView(R.id.tv_education)
    TextView mTvEducation;
    @BindView(R.id.tv_comp_name)
    TextView mTvCompName;
    @BindView(R.id.tv_comp_addr)
    TextView mTvCompAddr;
    @BindView(R.id.tv_comp_tel)
    TextView mTvCompTel;
    @BindView(R.id.tv_email_addr)
    TextView mTvEmailAddr;
    @BindView(R.id.tv_unit_establishment)
    TextView mTvUnitEstablishment;
    @BindView(R.id.tv_duty)
    TextView mTvDuty;
    @BindView(R.id.tv_card_blacklist_flag)
    TextView mTvCardBlacklistFlag;
    @BindView(R.id.tv_spouse_name)
    TextView mTvSpouseName;
    @BindView(R.id.tv_spouse_id_no)
    TextView mTvSpouseIdNo;
    @BindView(R.id.tv_spouse_mobile_phone)
    TextView mTvSpouseMobilePhone;
    @BindView(R.id.tv_spouse_company)
    TextView mTvSpouseCompany;
    @BindView(R.id.tv_age)
    TextView mTvAge;
    @BindView(R.id.tv_resi_addr)
    TextView mTvResiAddr;
    @BindView(R.id.iv_header)
    CircleImageView mIvHeader;
    @BindView(R.id.ll_head)
    LinearLayout mLlHead;
    @BindView(R.id.ll_update_version)
    LinearLayout mLlUpdateVersion;
    @BindView(R.id.ll_chage_password)
    LinearLayout mLlChagePassword;
    @BindView(R.id.ll_1)
    LinearLayout mLl1;
    @BindView(R.id.tv_resi_tel)
    TextView mTvResiTel;
    @BindView(R.id.tv_cert_type)
    TextView mTvCertType;
    @BindView(R.id.tv_census_addr)
    TextView mTvCensusAddr;
    @BindView(R.id.tv_has_children_flag)
    TextView mTvHasChildrenFlag;
    @BindView(R.id.tv_dept_office)
    TextView mTvDeptOffice;
    @BindView(R.id.tv_spouse_id_type)
    TextView mTvSpouseIdType;
    private String caseId;
    private String custNo;


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
        return R.layout.fragment_base_info;
    }

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
    }


    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        caseId = getArguments().getString(Constants.CASE_ID);
        custNo = getArguments().getString(Constants.CUST_ID);
        Map<String, String> map = new HashMap<>();
        map.put("caseId", caseId);
        map.put("tlrNo", Perference.getUserId());
        map.put("custNo", custNo);
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECTED_CUSTOM_INFO, map)
                .compose(getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<CustInfoBean>() {

                    @Override
                    public void onError(String code, String msg) {

                    }

                    @Override
                    public void onNextData(CustInfoBean custInfoBean) {
                        if (custInfoBean == null) {
                            return;
                        }
                        setCustData(custInfoBean);


                        setEthnicGroup(custInfoBean.getEthnicGroup());



                    }


                });


    }

    private void setEthnicGroup(String ethnicGroup) {
        if (TextUtils.isEmpty(ethnicGroup)) {
            mTvEthnicGroup.setText("");
            return;
        }

        List<DictItemBean> dictItemBeans = SugarRecord.find(DictItemBean.class, "DICT_CODE=?", "NATIONALITY");
        // Log.e("nb", dictItemBeans.size() + ":");
        if (dictItemBeans == null || dictItemBeans.size() == 0) {
            return;
        }


        for (DictItemBean dictItemBean : dictItemBeans) {
            if (ethnicGroup.endsWith(dictItemBean.getDataVal())) {
                mTvEthnicGroup.setText(dictItemBean.getDescription());
            }
        }


    }

    private void setCustData(CustInfoBean custInfoBean) {
        mTvCustName.setText(custInfoBean.getCustName());
        mTvAge.setText(custInfoBean.getAge() + "");
        if("1".equals(custInfoBean.getGender())) {
            mTvGender.setText("男");
        }else if("2".equals(custInfoBean.getGender())) {
            mTvGender.setText("女");
        }else {
            mTvGender.setText("");
        }
       mTvCertNo.setText(custInfoBean.getCertNo());

        mTvNativePlace.setText(TextUtils.isEmpty(custInfoBean.getNativePlace()) ? "" : custInfoBean.getNativePlace().replaceAll("\\s*", ""));
        mTvMobilePhone.setText(custInfoBean.getMobilePhone());
        mTvEducation.setText(DictUtils.getEducation(custInfoBean.getEducation()));
        mTvCompName.setText(custInfoBean.getCompName());
        mTvCompAddr.setText(TextUtils.isEmpty(custInfoBean.getCompAddr()) ? "" : custInfoBean.getCompAddr().replaceAll("\\s*", ""));
        mTvCompTel.setText(custInfoBean.getCompTel());
        mTvEmailAddr.setText(TextUtils.isEmpty(custInfoBean.getEmailAddr()) ? "" : custInfoBean.getEmailAddr().replaceAll("\\s*", ""));
        mTvUnitEstablishment.setText(DictUtils.getUnitsCompiled(custInfoBean.getUnitEstablishment()));
        mTvDuty.setText(custInfoBean.getHeadShip());
        if("Y".equals(custInfoBean.getCardBlacklistFlag())) {
            mTvCardBlacklistFlag.setText("是");
        }else if("N".equals(custInfoBean.getCardBlacklistFlag())) {
            mTvCardBlacklistFlag.setText("否");
        }else {
            mTvCardBlacklistFlag.setText("");
        }

        mTvSpouseName.setText(custInfoBean.getSpouseName());
        mTvSpouseIdNo.setText(custInfoBean.getSpouseIdno());
        mTvSpouseMobilePhone.setText(custInfoBean.getSpousePhoneno());
        mTvSpouseCompany.setText(custInfoBean.getSpouseCompany());
        mTvResiTel.setText(custInfoBean.getResiTel());
        mTvResiAddr.setText(TextUtils.isEmpty(custInfoBean.getResiAddr()) ? "" : custInfoBean.getResiAddr().replaceAll("\\s*", ""));
        mTvCertType.setText(DictUtils.getCertType(custInfoBean.getCertType()));
        mTvSpouseIdType.setText(DictUtils.getCertType(custInfoBean.getSpouseIdtype()));
        mTvCensusAddr.setText(TextUtils.isEmpty(custInfoBean.getCensusAddr()) ? "" : custInfoBean.getCensusAddr().replaceAll("\\s*", ""));
        if ("1".equals(custInfoBean.getHaveChildrenFlag())) {
            mTvHasChildrenFlag.setText("有");
        } else if ("2".equals(custInfoBean.getHaveChildrenFlag())) {
            mTvHasChildrenFlag.setText("无");
        }else {
            mTvHasChildrenFlag.setText("");
        }

        mTvDeptOffice.setText(custInfoBean.getDeptOffice());

    }

    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
