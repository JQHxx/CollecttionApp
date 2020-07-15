package com.huateng.phone.collection.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.huateng.phone.collection.R;

import java.util.ArrayList;
import java.util.List;

/**
  * author: Devin
  * createTime:2015年8月11日
  * desciprion:
  */
public class DynamicPicContainer extends LinearLayout{
	private final String TAG=getClass().getSimpleName();
	private int mVerticalSpace;
	private int mImageSize;
	private List<LinearLayout> mSubContainers=new ArrayList<LinearLayout>();
	private final int NUM_PER_LINE=3;
	private int mLineTotalCount;
	private List<ImageView> mImages=new ArrayList<ImageView>();
	private List<String> mImagePaths=new ArrayList<String>();
	
	public DynamicPicContainer(Context context) {
		super(context);
		init();
	}
	
	public DynamicPicContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		setOrientation(VERTICAL);
		mVerticalSpace=(int)(getResources().getDimension(R.dimen.reference_width)*0.04f);
//		mImageSize=(int) ((FmScreenUtil.getScaleWidth(getContext(),1)-mVerticalSpace*(NUM_PER_LINE+1))*(1f/NUM_PER_LINE));
		mImageSize=(int) ((getResources().getDimension(R.dimen.reference_width)-mVerticalSpace*(NUM_PER_LINE+1))*(1f/NUM_PER_LINE));

		Log.i(TAG, "mImageSize:"+mImageSize);
		setData(mImagePaths);
	}
	
	private ImageButton createAddButton(int id){
		ImageButton iv_add=new ImageButton(getContext());
		LayoutParams params=new LayoutParams(mImageSize,(int)(mImageSize*0.5f));
		params.leftMargin=mVerticalSpace;
		iv_add.setScaleType(ScaleType.FIT_CENTER);
		iv_add.setLayoutParams(params);
		iv_add.setImageResource(id);
		iv_add.setClickable(true);
		iv_add.setBackgroundResource(R.drawable.shape_bg_round_white);
		iv_add.setPadding(mImageSize/3,(int)(mImageSize*0.5f)/3,mImageSize/3,(int)(mImageSize*0.5f)/3);
		return iv_add;
	}
	
	private ImageView createImage(String path){
		DeletableImageView iv_image=new DeletableImageView(getContext());
		LayoutParams params=new LayoutParams(mImageSize,(int)(mImageSize*0.5f));
		params.leftMargin=mVerticalSpace;
		iv_image.setScaleType(ScaleType.CENTER_CROP);
		Glide.with(getContext()).load(path).into(iv_image);
		iv_image.setClickable(true);
		iv_image.setLayoutParams(params);
		return iv_image;
	}
	
	
	public void setData(List<String> mImagePaths){
		mImages.clear();
		mSubContainers.clear();
		removeAllViews();
		int totalSize=mImagePaths.size()+1;
		int mod=totalSize%NUM_PER_LINE;
		if (mod==0) {
			mLineTotalCount=totalSize/NUM_PER_LINE;
		}else {
			mLineTotalCount=totalSize/NUM_PER_LINE+1;
		}
		for (int i = 0; i < mLineTotalCount; i++) {
			LinearLayout ll=new LinearLayout(getContext());
			if (i!=0) {
				LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				params.topMargin=(int) (mVerticalSpace/2f);
				ll.setLayoutParams(params);
			}
			addView(ll);
			mSubContainers.add(ll);
		}
		if(mImagePaths!=null){
			for (int i = 0; i < mImagePaths.size(); i++) {
				int lineNum=i/NUM_PER_LINE+1;
				ImageView iv=createImage(mImagePaths.get(i));
				mImages.add(iv);
				mSubContainers.get(lineNum-1).addView(iv);
			}
		}
		ImageView iv_add=createAddButton(R.drawable.add_pic);
		mSubContainers.get(mSubContainers.size()-1).addView(iv_add);
		mImages.add(iv_add);
		for (int i = 0; i < mImages.size(); i++) {
			final ImageView iv=mImages.get(i);
			final int k=i;
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (mListener!=null) {
						if (k==mImages.size()-1) {
							mListener.onItemClicked(iv, k,true);
						}else {
							mListener.onItemClicked(iv, k,false);
						}
					}
				}
			});
			if (iv instanceof DeletableImageView) {
				((DeletableImageView) iv).setOnDeleteTouchListener(new DeletableImageView.OnDeleteTouchListener() {
					
					@Override
					public void onDeleteTouched() {
						if (mItemDeleteListener!=null) {
							mItemDeleteListener.onItemDeleted(k);
						}
					}
				});
			}
			
		}
		
	}
	
	public void addImage(String path){
		
	}
	private OnItemClickListener mListener;
	public interface OnItemClickListener{
		public void onItemClicked(ImageView v, int position, boolean isLast);
	}
	
	public void setOnItemClickListener(OnItemClickListener listener){
		this.mListener=listener;
	}
	
	private OnItemDeleteListener mItemDeleteListener;
	public interface OnItemDeleteListener{
		public void onItemDeleted(int position);
	}
	
	public void setOnItemDeleteListener(OnItemDeleteListener listener){
		this.mItemDeleteListener=listener;
	}

}
