package com.huateng.fm.ui.view;

import com.huateng.flowMobile.R;
import com.huateng.fm.app.FmAttributeValues;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
  * @author devin
  *  2014-12-4 下午2:58:42
 */
public class FmListPopWindow extends PopupWindow {

    private Context context;
    private LinearLayout popView;
    private int popWidth;
	
    private ListView listView;
	private PopAdapter adapter;
	private OnMItemClickedListener onMItemClickedListener;
	private String[] textStrings;
	private int[] iconResIds;
	private boolean hideIcon;

    public FmListPopWindow(Context context,String[] textStrings,int[] iconResIds ) {
        super(context, null);
        this.context = context;
        this.textStrings=textStrings;
        this.iconResIds=iconResIds;
        init();
    }
    
    public FmListPopWindow(Context context,String[] textStrings ) {
        super(context, null);
        this.context = context;
        this.textStrings=textStrings;
        this.hideIcon=true;
        init();
    }

  


    private void init() {
        popWidth = context.getResources().getDimensionPixelSize(R.dimen.ht_list_pop_window_width);

        initPop();
    }

    private void initPop() {

    	   popView = new LinearLayout(context);
           popView.setBackgroundColor(context.getResources().getColor(R.color.transparent));

           popView.setOrientation(LinearLayout.VERTICAL);
           LinearLayout.LayoutParams lp_popView = new LinearLayout.LayoutParams(
                   popWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
           popView.setGravity(Gravity.CENTER);
           popView.setLayoutParams(lp_popView);


           setContentView(popView);
           setWidth(popWidth);
           setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

           setFocusable(true);
           setOutsideTouchable(false);

           ColorDrawable dw = new ColorDrawable(0xC0000000);
           this.setBackgroundDrawable(dw);


    }
    
    
    private ListView createListView(){
    	 listView=new ListView(context);
    	  LinearLayout.LayoutParams lp_listView = new LinearLayout.LayoutParams(
          		LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	  
    	  listView.setLayoutParams(lp_listView);
    	  listView.setDivider(new ColorDrawable(context.getResources().getColor(FmAttributeValues.THEME_COLOR)));
    	  listView.setDividerHeight(1);
    	  listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (onMItemClickedListener!=null) {
					onMItemClickedListener.onMItemClicked(parent, view, position, id);
				}
				dismiss();
			}
		});
    	  adapter=new PopAdapter();
      	listView.setAdapter(adapter);
    	  return listView;
    }
    
    public interface OnMItemClickedListener{
    	public void onMItemClicked(AdapterView<?> parent, View view,
				int position, long id);
    }
    
    public void setOnMItemClickedListener(OnMItemClickedListener onMItemClickedListener){
    	this.onMItemClickedListener=onMItemClickedListener;
    }
    
    private final class PopAdapter extends BaseAdapter {
    	
    	
        @Override
        public int getCount() {
            return textStrings.length;
        }

   

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.ht_list_pop_item, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.text = (TextView) convertView.findViewById(R.id.text);
                if (hideIcon) {
                    holder.icon.setVisibility(View.GONE);
				}
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (!hideIcon) {
                holder.icon.setImageResource(iconResIds[position]);
            }
            holder.text.setText(textStrings[position]);
            return convertView;
        }

        private final class ViewHolder {
            ImageView icon;
            TextView text;
        }

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
   
//    public void show(View anchor) {
//        popView.addView(createListView());
//        showAsDropDown(anchor);
//   }
    
    public void show(int yOffset) {
          popView.addView(createListView());
//  		showAsDropDown(v);
//  		showAsDropDown(v, xoff, yoff);
  		showAtLocation(((Activity)context).getWindow().getDecorView(), 
  				Gravity.RIGHT|Gravity.TOP, 10, yOffset);
//        this.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.CENTER, 0, 0); //设置layout在PopupWindow中显示的位置
      
    }
    
//    public void show(int gravity) {
//        popView.addView(createListView());
////		showAsDropDown(v);
////		showAsDropDown(v, xoff, yoff);
//        int yOffSet=(int)CommonUtils.getScaleHeight(context, 0.115f);
//		showAtLocation(((Activity)context).getWindow().getDecorView(), 
//				gravity, 0, yOffSet);
////      this.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.CENTER, 0, 0); //设置layout在PopupWindow中显示的位置
//    
//  }


}
