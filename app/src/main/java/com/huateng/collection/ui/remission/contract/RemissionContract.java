package com.huateng.collection.ui.remission.contract;

import com.huateng.collection.base.IBaseView;
import com.huateng.collection.bean.BizAcctInfoBean;
import com.huateng.collection.bean.BizAcctItemBean;
import com.huateng.collection.bean.CustInfoBean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-13 20:08
 * description:
 */
public interface RemissionContract {

    interface View extends IBaseView {

        /**
         * 设置减免数据列表
         *
         * @param bizAcctInfoBean
         */
        void setBizAcctInfo(BizAcctInfoBean bizAcctInfoBean);

        void setCustData(CustInfoBean custInfoBean);

        List<BizAcctItemBean> getBizAcctAccountData();

        List<BizAcctItemBean> getBizAcctCardData();
    }

    interface Presenter {

        void loadData(String custId);

        void loadCustInfo(String custId);

        void reliefBatchExcute(String custId);

    }
}
