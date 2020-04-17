package com.huateng.collection.ui.fragment.casebox.info;

import android.os.Bundle;

import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.ProductMsgBean;
import com.huateng.collection.bean.RepaymentBean;
import com.huateng.collection.bean.api.RespCaseDetail;
import com.huateng.collection.ui.adapter.ProductMsgAdater;
import com.huateng.collection.ui.adapter.RepaymentAdapter;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;


/**
 * 客户信息
 */
public class FragmentBaseInfo extends BaseFragment {
    @BindView(R.id.recycler_product_msg)
    RecyclerView mRecyclerProductMsg;
    @BindView(R.id.recycler_repayment)
    RecyclerView mRecyclerRepayment;

   /* @BindView(R.id.tv_name)
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
    @BindView(R.id.text)
    TextView mText;*/

    private RespCaseDetail detail;
    private ProductMsgAdater mProductMsgAdater;
    private RepaymentAdapter mRepaymentAdapter;

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

        detail = (RespCaseDetail) getArguments().getSerializable(Constants.CASE_DETAIL);
        // refreshBaseInfo(detail);
        initRecyclerview();
    }

    /**
     * 初始化Recyclerview控件
     */
    private void initRecyclerview() {
        // mRecyclerProductMsg
        mProductMsgAdater = new ProductMsgAdater();
        mRecyclerProductMsg.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerProductMsg.setAdapter(mProductMsgAdater);

        mRepaymentAdapter = new RepaymentAdapter();
        mRecyclerRepayment.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerRepayment.setAdapter(mRepaymentAdapter);
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        List<ProductMsgBean> list = new ArrayList<>();
        List<RepaymentBean> repaymentBeanList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            list.add(new ProductMsgBean());
            repaymentBeanList.add(new RepaymentBean());
        }
        mProductMsgAdater.setNewData(list);
        mRepaymentAdapter.setNewData(repaymentBeanList);

    }


    /*  public void refreshBaseInfo(final RespCaseDetail detail) {

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

  */
    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
