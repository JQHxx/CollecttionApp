package com.huateng.collection.widget.dialogplus;

import android.widget.BaseAdapter;


public interface HolderAdapter extends Holder {

  void setAdapter(BaseAdapter adapter);

  void setOnItemClickListener(OnHolderListener listener);
}
