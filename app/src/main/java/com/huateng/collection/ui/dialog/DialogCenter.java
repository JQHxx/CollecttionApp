package com.huateng.collection.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.huateng.collection.R;
import com.huateng.collection.ui.dialog.dm.AddAddressDM;
import com.huateng.collection.ui.dialog.dm.AddPhoneDM;
import com.huateng.collection.ui.dialog.dm.CommonContentDM;
import com.huateng.collection.ui.dialog.dm.CorrentEndCaseDM;
import com.huateng.collection.ui.dialog.dm.PaymentCalculateDM;
import com.huateng.collection.ui.dialog.dm.SearchDM;
import com.huateng.collection.ui.dialog.dm.VisitSummarizeDM;
import com.huateng.collection.ui.dialog.dm.WrongEndCaseDM;
import com.huateng.collection.widget.dialogplus.DialogPlus;
import com.huateng.collection.widget.dialogplus.DialogPlusBuilder;
import com.huateng.collection.widget.dialogplus.ViewHolder;

/**
 * @author dengzh
 * @description
 * @time 2016-12-02.
 */
public class DialogCenter {


    private Context context;

    public interface OnButtonClickListener {
        public void onButtonClicked(DialogPlus dialog, View v, int position);
    }

    public DialogCenter(Context context) {
        this.context = context;
    }

    //搜索案件对话框
    public SearchDM showSearchCaseDialog() {
        DialogPlusBuilder builder = createGenericBuilder(R.layout.dialog_content_search_case);
        DialogPlus dialogPlus = builder.create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        SearchDM dm = new SearchDM();
        binder.bindSearchDM(dm);
        return dm;
    }

    //上门总结对话框
    public VisitSummarizeDM showVisitSummarizeDialog() {
        DialogPlusBuilder builder = createGenericBuilder(R.layout.dialog_content_visit_summarize);
        DialogPlus dialogPlus = builder.create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        VisitSummarizeDM dm = new VisitSummarizeDM();
        binder.bindVisitSummarizeDM(dm);
        return dm;
    }

    //新增电话对话框
    public AddPhoneDM showAddPhoneDialog() {
        DialogPlusBuilder builder = createGenericBuilder(R.layout.dialog_content_add_phone);
        DialogPlus dialogPlus = builder.create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        AddPhoneDM dm = new AddPhoneDM();
        binder.bindAddPhoneDM(dm);
        return dm;
    }

    //新增地址对话框
    public AddAddressDM showAddAddressDialog() {
        DialogPlusBuilder builder = createGenericBuilder(R.layout.dialog_content_add_address);
        DialogPlus dialogPlus = builder.create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        AddAddressDM dm = new AddAddressDM();
        binder.bindAddAddressDM(dm);
        return dm;
    }

    //正确处理案件对话框
    public CorrentEndCaseDM showCorrentEndCaseDialog() {
        DialogPlus dialogPlus = createCommonContentDialog().create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        CorrentEndCaseDM dm = new CorrentEndCaseDM();
        binder.bindCorrentEndCaseDM(dm);
        return dm;
    }

    //错误处理案件对话框
    public WrongEndCaseDM showWrongEndCaseDialog() {
        DialogPlus dialogPlus = createCommonContentDialog().create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        WrongEndCaseDM dm = new WrongEndCaseDM();
        binder.bindWrongEndCaseDM(dm);
        return dm;
    }

    //销卡付清计算对话框
    public PaymentCalculateDM showPaymentCalculateDialog() {
        DialogPlus dialogPlus = createGenericBuilder(R.layout.dialog_content_payment_calculate).create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        PaymentCalculateDM dm = new PaymentCalculateDM();
        binder.bindPaymentCalDM(dm);
        return dm;
    }

    //退出应用
    public CommonContentDM showQuitDialog() {
        DialogPlus dialogPlus = createCommonContentDialog().create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        CommonContentDM dm = new CommonContentDM();
        binder.bindCommonContentDM(dm);
        dm.setTitle("退出应用?");
        dm.setContent("程序将结束!");
        dm.showDescription(false);
        return dm;
    }


    //退出应用
    public CommonContentDM showLogoutDialog() {
        DialogPlus dialogPlus = createCommonContentDialog().create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        CommonContentDM dm = new CommonContentDM();
        binder.bindCommonContentDM(dm);
        dm.setTitle("退出登录?");
        dm.setContent("登出当前用户!");
        dm.showDescription(false);
        return dm;
    }


    //登录超时提示
    public CommonContentDM showLoginTimeout() {
        DialogPlus dialogPlus = createCommonContentDialog().create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        CommonContentDM dm = new CommonContentDM();
        binder.bindCommonContentDM(dm);
        dm.setTitle("提示");
        dm.setContent("登录状态失效,重新登录？");
        dm.showDescription(false);
        return dm;
    }


    public CommonContentDM showCurrentCaseOnOperationDialog() {
        DialogPlus dialogPlus = createCommonContentDialog().create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        CommonContentDM dm = new CommonContentDM();
        binder.bindCommonContentDM(dm);
        dm.setTitle("当前案件正在处理中");
        dm.setContent("请检查是否有未结束录音或其他流程");
        dm.showDescription(false);
        return dm;
    }

    public CommonContentDM showOfflineModeDialog() {
        DialogPlus dialogPlus = createCommonContentDialog().create();
        dialogPlus.show();
        DialogDmBinder binder = new DialogDmBinder(dialogPlus);
        CommonContentDM dm = new CommonContentDM();
        binder.bindCommonContentDM(dm);
        dm.setTitle("网络状况不佳");
        dm.setContent("因网络原因无法登录，点击确定重新登录，点击取消进入离线模式");
        dm.showDescription(false);
        return dm;
    }

    private DialogPlusBuilder createCommonContentDialog() {
        return createGenericBuilder(R.layout.dialog_content_common_content);
    }

    private DialogPlusBuilder createGenericBuilder(int contentId) {
        DialogPlusBuilder builder = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(contentId))
                .setHeader(R.layout.dialog_header_default)
                .setFooter(R.layout.dialog_footer_default)
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
//                .setAdapter(adapter)
//                .setOnDismissListener(dismissListener)
//                .setExpanded(expanded)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                .setOnCancelListener(cancelListener)
                .setOverlayBackgroundResource(R.color.transelent)
//        .setContentBackgroundResource(R.drawable.corner_background)
                .setMargin(36, 0, 36, 0);
        return builder;
    }


}
