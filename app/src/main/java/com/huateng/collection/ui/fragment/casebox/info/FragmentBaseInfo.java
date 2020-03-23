package com.huateng.collection.ui.fragment.casebox.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.api.RespCaseDetail;
import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.ui.base.BaseFragment;
import com.tools.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 基础信息
 */
public class FragmentBaseInfo extends BaseFragment {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_certNo)
    TextView tvCertNo;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_amout)
    TextView tvAmout;
    @BindView(R.id.tv_visitAddress)
    TextView tvVisitAddress;
    @BindView(R.id.iv_department)
    ImageView ivDepartment;

    private RespCaseDetail detail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_base_info, container, false);
        ButterKnife.bind(this, mContentView);
        return mContentView;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        detail = (RespCaseDetail) getArguments().getSerializable(Constants.CASE_DETAIL);
        refreshBaseInfo(detail);
    }

    public void refreshBaseInfo(final RespCaseDetail detail) {

        try {
            tvName.setText(detail.getName());
            tvAge.setText(detail.getAge() + "岁");
            tvCertNo.setText(detail.getCertNo());
            String dept = detail.getDeptOffice();
            if (StringUtils.isNotEmpty(dept)) {
                ivDepartment.setVisibility(View.VISIBLE);
                tvDepartment.setText(detail.getDeptOffice());
            }
            tvCompany.setText(detail.getCompName());
            tvDate.setText(detail.getIncollDate());
            tvAmout.setText(detail.getCaseAmt() + " (元)");
            ivSex.setImageResource(Dic.queryValue(Dic.SEX, detail.getGender()).equals("男") ? R.drawable.sex_male : R.drawable.sex_female);
            tvSex.setText(Dic.queryValue(Dic.SEX, detail.getGender()));

            tvVisitAddress.setText(Perference.getCurrentVisitAddress());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
