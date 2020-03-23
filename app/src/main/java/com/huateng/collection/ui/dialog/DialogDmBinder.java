package com.huateng.collection.ui.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huateng.collection.R;
import com.huateng.collection.ui.dialog.dm.AddAddressDM;
import com.huateng.collection.ui.dialog.dm.AddPhoneDM;
import com.huateng.collection.ui.dialog.dm.BaseDM;
import com.huateng.collection.ui.dialog.dm.CommonContentDM;
import com.huateng.collection.ui.dialog.dm.CorrentEndCaseDM;
import com.huateng.collection.ui.dialog.dm.PaymentCalculateDM;
import com.huateng.collection.ui.dialog.dm.SearchDM;
import com.huateng.collection.ui.dialog.dm.VisitSummarizeDM;
import com.huateng.collection.ui.dialog.dm.WrongEndCaseDM;
import com.huateng.collection.widget.UniversalInput;
import com.huateng.collection.widget.dialogplus.DialogPlus;
import com.huateng.fm.ui.widget.FmButton;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author dengzh
 * @description
 * @time 2016-12-05.
 */
public class DialogDmBinder {

    private View headerView, footerView, contentView;
    private DialogPlus dialog;

    public DialogDmBinder(DialogPlus dialog) {
        this.dialog = dialog;
        init();
    }

    private void init() {
        headerView = dialog.getHeaderView();
        footerView = dialog.getFooterView();
        contentView = dialog.getHolderView();
    }

    public void bindBaseDM(BaseDM dm) {
        dm.tv_title = (TextView) headerView.findViewById(R.id.tv_title);
        dm.btn_left = (FmButton) footerView.findViewById(R.id.btn_left);
        dm.btn_right = (FmButton) footerView.findViewById(R.id.btn_right);
        dm.iv_close = (ImageView) headerView.findViewById(R.id.iv_close);
        dm.setDialog(dialog);
    }

    //绑定案件搜索DM
    public void bindSearchDM(SearchDM dm) {
        dm.csv_custName = (UniversalInput) contentView.findViewById(R.id.csv_custName);
        bindBaseDM(dm);
        dm.init();
    }

    //绑定上门总结DM
    public void bindVisitSummarizeDM(VisitSummarizeDM dm) {
        dm.csv_visitAddress = (UniversalInput) contentView.findViewById(R.id.csv_visitAddress);
        dm.csv_visitDate = (UniversalInput) contentView.findViewById(R.id.csv_visitDate);
        dm.csv_visitInfo = (UniversalInput) contentView.findViewById(R.id.csv_visitInfo);
        dm.csv_visitOfficer = (UniversalInput) contentView.findViewById(R.id.csv_visitOfficer);
        dm.csv_visitOtherOfficer = (UniversalInput) contentView.findViewById(R.id.csv_visitOhterOfficer);
        dm.csv_visitResult = (UniversalInput) contentView.findViewById(R.id.csv_visitResult);
        bindBaseDM(dm);
        dm.init();
    }

    //绑定新增电话DM
    public void bindAddPhoneDM(AddPhoneDM dm) {
        dm.csv_caseNo = (UniversalInput) contentView.findViewById(R.id.csv_caseNo);
        dm.csv_custNo = (UniversalInput) contentView.findViewById(R.id.csv_custNo);
        dm.csv_custName = (UniversalInput) contentView.findViewById(R.id.csv_custName);
        dm.csv_custRel = (UniversalInput) contentView.findViewById(R.id.csv_custRel);
        dm.csv_phoneType = (UniversalInput) contentView.findViewById(R.id.csv_phoneType);
        dm.csv_phone = (UniversalInput) contentView.findViewById(R.id.csv_phone);
        bindBaseDM(dm);
        dm.init();
    }

    //绑定新增地址DM
    public void bindAddAddressDM(AddAddressDM dm) {
        dm.csv_caseNo = (UniversalInput) contentView.findViewById(R.id.csv_caseNo);
        dm.csv_custNo = (UniversalInput) contentView.findViewById(R.id.csv_custNo);
        dm.csv_addressType = (UniversalInput) contentView.findViewById(R.id.csv_addressType);
        dm.csv_city = (UniversalInput) contentView.findViewById(R.id.csv_city);
        dm.csv_postCode = (UniversalInput) contentView.findViewById(R.id.csv_postCode);
        dm.csv_address = (UniversalInput) contentView.findViewById(R.id.csv_address);
        dm.csv_name = (UniversalInput) contentView.findViewById(R.id.csv_name);
        dm.csv_custRel = (UniversalInput) contentView.findViewById(R.id.csv_custRel);
        bindBaseDM(dm);
        dm.init();
    }

    //绑定销卡付清计算DM
    public void bindPaymentCalDM(PaymentCalculateDM dm) {
        dm.recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        dm.csv_currencyType = (UniversalInput) contentView.findViewById(R.id.csv_currencyType);
        dm.csv_accountNo = (UniversalInput) contentView.findViewById(R.id.csv_accountNo);
        dm.csv_date = (UniversalInput) contentView.findViewById(R.id.csv_date);
        dm.btn_query = (Button) contentView.findViewById(R.id.btn_query);
        dm.tvTip = (TextView) contentView.findViewById(R.id.tv_tip);
        bindBaseDM(dm);
        dm.init();
    }

    public void bindCommonContentDM(CommonContentDM dm) {
        dm.tv_content = (TextView) contentView.findViewById(R.id.tv_content);
        dm.tv_description = (TextView) contentView.findViewById(R.id.tv_description);
        bindBaseDM(dm);
        dm.init();
    }

    //绑定正确处理案件DM
    public void bindCorrentEndCaseDM(CorrentEndCaseDM dm) {
        bindCommonContentDM(dm);
        dm.init();
    }

    //绑定错误处理案件DM
    public void bindWrongEndCaseDM(WrongEndCaseDM dm) {
        bindCommonContentDM(dm);
        dm.init();
    }
}
