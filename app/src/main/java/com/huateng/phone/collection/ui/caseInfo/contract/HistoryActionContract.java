package com.huateng.phone.collection.ui.caseInfo.contract;

import com.huateng.phone.collection.base.IBaseView;
import com.huateng.phone.collection.bean.LogActActionBean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-12 11:15
 * description:
 */
public interface HistoryActionContract {

    interface View extends IBaseView {
      void   showEmptyView();

        void setLogActionData(int status, List<LogActActionBean.RecordsBean> records);
    }

    interface Presenter {
        /**
         * 加载数据
         * @param status  类型 刷新 还是加载更多
         */
       void loadData(int status,String caseId,String custId);
    }

}
