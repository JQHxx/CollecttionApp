package com.huateng.phone.collection.widget.tab;




import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huateng.fm.util.FmValueUtil;
import com.huateng.fm.util.UiValuesUtil;
import com.huateng.phone.collection.R;

public class VerticalTabItem extends LinearLayout{

	private ImageView iv_icon;
	private TextView tv_text;
	private TextView tv_msg;
	private int mIconResId,mIconResIdChecked,mSelectedTextColorId,mUnSelectedTextColorId;
	private String mText;
	private String mMsgCount;
	private int mTextId;
	private int mSelectedSectionColor;

	public VerticalTabItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public VerticalTabItem(Context context, int iconResId, int iconResIdChecked, String text ) {
		super(context);
		mIconResId=iconResId;
		mIconResIdChecked=iconResIdChecked;
		mText=text;
		init();
	}

	public VerticalTabItem(Context context, int iconResId, int iconResIdChecked  ) {
		this(context,iconResId,iconResIdChecked,"");
	}

	public VerticalTabItem(Context context, int iconResId, int iconResIdChecked, int textId ) {
		super(context);
		mIconResId=iconResId;
		mIconResIdChecked=iconResIdChecked;
		mTextId=textId;
		init();
	}
	private int mVerticalPadding;

	public void setVerticalPadding(int verticalPadding){
		mVerticalPadding=verticalPadding;
	}
	private void init(){
		setOrientation(VERTICAL);
		mSelectedSectionColor=getResources().getColor(R.color.transparent);
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);

		setLayoutParams(params);
//		setPadding(0,mVerticalPadding,0,mVerticalPadding);
		ViewGroup vg=(ViewGroup) LayoutInflater.from(getContext())
				.inflate(R.layout.ht_tab_item_view, null);
		 iv_icon=(ImageView) vg.findViewById(R.id.icon);
		 tv_text=(TextView)vg.findViewById(R.id.text);
		if (mTextId==-1|| FmValueUtil.isStrEmpty(mText)){
			tv_text.setVisibility(View.GONE);
		}else if (!TextUtils.isEmpty(mText)) {
			tv_text.setText(mText);
		}else  if (mTextId!=-1){
			tv_text.setText(UiValuesUtil.getString(mTextId));
		}
		 tv_msg=(TextView)vg.findViewById(R.id.msg);
		 if (mIconResId!=0) {
			 iv_icon.setImageResource(mIconResId);
		 }

		 if (TextUtils.isEmpty(mMsgCount)) {
			 tv_msg.setVisibility(View.GONE);
		}else {
			tv_msg.setVisibility(View.VISIBLE);
		}
		addView(vg);
	}

	public void setSelectedSectionColor(int id){
		mSelectedSectionColor=id;
	}

	public int getIconResId() {
		return mIconResId;
	}

	public void setIconResId(int mIconResId) {
		this.mIconResId = mIconResId;
	}

	public String getText() {
		return mText;
	}

	public void setText(String mText) {
		this.mText = mText;
	}
	
	public void setText(int id){
		this.mTextId=id;
	}

	public String getMsgCount() {
		return mMsgCount;
	}
	
	public void setSelectedTextColor(int selId,int unselId){
		mSelectedTextColorId=selId;
		mUnSelectedTextColorId=unselId;
	}
	
	public void setChecked(boolean checked){
		if (checked) {
			iv_icon.setImageResource(mIconResIdChecked);
			tv_text.setTextColor(getResources().getColor(mSelectedTextColorId));
			setBackgroundResource(mSelectedSectionColor);
		}else {
			iv_icon.setImageResource(mIconResId);
			tv_text.setTextColor(getResources().getColor(mUnSelectedTextColorId));
			setBackgroundResource(R.color.transparent);

		}
		
	}

	public void setMsgCount(String mMsgCount) {
		this.mMsgCount = mMsgCount;
		 if (TextUtils.isEmpty(mMsgCount)||"0".equals(mMsgCount)) {
			 tv_msg.setVisibility(View.GONE);
		}else {
			tv_msg.setVisibility(View.VISIBLE);
			tv_msg.setText(mMsgCount);
		}
	}
	
	
	
}
