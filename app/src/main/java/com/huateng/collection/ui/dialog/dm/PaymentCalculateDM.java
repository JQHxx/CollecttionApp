package com.huateng.collection.ui.dialog.dm;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.widget.UniversalInput;

import java.util.HashMap;
import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-12-05.
 */
public class PaymentCalculateDM extends  BaseDM{

    public RecyclerView recyclerView;
    public UniversalInput csv_currencyType;
    public UniversalInput csv_accountNo;
    public UniversalInput csv_date;
    public Button btn_query;
    public TextView tvTip;


    public PaymentCalculateDM(){
    }

    @Override
    public void init() {
        super.init();
        tv_title.setText("销卡付清");
        btn_left.setText("提交");
        btn_right.setText("取消");
        getDialog().getFooterView().setVisibility(View.GONE);
        GridLayoutManager mgr = new GridLayoutManager(getDialog().getHolderView().getContext(),7);
        mgr.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mgr);
        csv_currencyType.setDataSource(Dic.getOptions(Dic.CURRENCY));
        tvTip.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

    }

    public void setRecyclerAdapter(RecyclerView.Adapter adapter){
        recyclerView.setAdapter(adapter);
        if (adapter.getItemCount()!=0){
            tvTip.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            tvTip.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void setAccountDataSource(List<String> data){
        csv_accountNo.setDataSource(data);
    }

    public void notifyRecyclerData(){
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    public void setOnQueryListener(View.OnClickListener listener){
        btn_query.setOnClickListener(listener);
    }

    public HashMap<String,String> collectData(){
        HashMap<String,String> map=new HashMap<>();
        map.put("acctNo",csv_accountNo.getNoneNullText());
        map.put("currency",Dic.queryKey(Dic.CURRENCY,csv_currencyType.getNoneNullText()));
        map.put("payoffDate",csv_date.getNoneNullText());
        return  map;
    }

}
