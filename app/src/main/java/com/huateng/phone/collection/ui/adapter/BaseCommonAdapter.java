package com.huateng.phone.collection.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public abstract class BaseCommonAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> list;
    protected Context mContext;
    protected OnClickRecyclerItemListener onClickItemListener;

    public BaseCommonAdapter(Context context) {
        this.mContext = context;
    }

    public void addItem(T t) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.add(t);
        notifyItemInserted(this.list.size());
    }

    public void removeItem(int position) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.remove(position);
        notifyItemRemoved(position);
    }

    public void updateList(List<T> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list = list;
        notifyDataSetChanged();
    }


    public List<T> getList() {
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public void setOnClickItemListener(OnClickRecyclerItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }

    public interface OnClickRecyclerItemListener {
        void onClick(int position);
    }

}
