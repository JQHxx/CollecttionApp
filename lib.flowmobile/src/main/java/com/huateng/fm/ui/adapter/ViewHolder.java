package com.huateng.fm.ui.adapter;
 /**
  * author: Devin
  * createTime:2015年8月10日
  * desciprion:
  */
import android.util.SparseArray;
import android.view.View;


public class ViewHolder {
  
  private ViewHolder() {}

  public static <T extends View>T getChildView(View convertView,int id) {
    
    @SuppressWarnings("unchecked")
	SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
    
    if (viewHolder == null) {
      viewHolder = new SparseArray<View>();
      convertView.setTag(viewHolder);
    }
    
    View childView = viewHolder.get(id);
    
    if (childView == null) {
      childView = convertView.findViewById(id);
      viewHolder.put(id, childView);
    }
    
    return (T) childView;
  }
  
}
