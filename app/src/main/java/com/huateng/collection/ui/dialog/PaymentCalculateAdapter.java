package com.huateng.collection.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huateng.collection.R;
import com.huateng.collection.bean.api.RespPaymentCalItem;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentCalculateAdapter extends RecyclerView.Adapter<PaymentCalculateAdapter.VH> {



    private Context context;
    private List<RespPaymentCalItem> dataList;
    private LayoutInflater inflater;


    public PaymentCalculateAdapter(Context context, List<RespPaymentCalItem> dataList) {
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_payment_calculate, parent, false);
        VH holder = new VH(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {

        RespPaymentCalItem bean = dataList.get(position);
        holder.tvTag.setText(bean.getTag());
        holder.tvValue.setText(bean.getValue());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_tag)
        TextView tvTag;
        @BindView(R.id.tv_value)
        TextView tvValue;
        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
