package com.huateng.fm.ui.widget.common;

import com.huateng.fm.ui.widget.FmAlertDialog;
import com.huateng.fm.ui.widget.FmAlertDialog.OnCenterBtnClickListener;
import com.huateng.fm.ui.widget.FmAlertDialog.OnLeftBtnClickListener;
import com.huateng.fm.ui.widget.FmAlertDialog.OnRightBtnClickListener;

import android.content.Context;

public class DialogFactory {
	

	public  static FmAlertDialog showOneBtnAlert(Context context,String tittleText ,String message,String text  ,
			 OnCenterBtnClickListener centerBtnClickListener){
		FmAlertDialog dialog=getOneBtnAlertBuilder( context, tittleText , message, text  ,
				  centerBtnClickListener).create();
		dialog.show();
		return dialog;
	}
	
	public  static FmAlertDialog.Builder getOneBtnAlertBuilder(Context context,String tittleText ,String message,String text  ,
			 OnCenterBtnClickListener centerBtnClickListener){
		return getThreeBtnAlertBuilder(context, 0, tittleText,message, null,text, null, null,centerBtnClickListener, null);
	}
	
	
	public  static FmAlertDialog showOneBtnAlert(Context context,String tittleText ,String message,int textId 
			,OnCenterBtnClickListener centerBtnClickListener ){
		FmAlertDialog dialog=getOneBtnAlertBuilder( context, tittleText , message, textId 
				, centerBtnClickListener ).create();
		dialog.show();
		return dialog;
	}
	public  static FmAlertDialog.Builder getOneBtnAlertBuilder(Context context,String tittleText ,String message,int textId 
			,OnCenterBtnClickListener centerBtnClickListener ){
		
		return getThreeBtnAlertBuilder(context, 0, tittleText,message,  0,textId, 0, null,centerBtnClickListener, null);
	}
	
	public  static FmAlertDialog showOneBtnAlert(Context context,int iconResId,String tittleText,String message ,
			String text,OnCenterBtnClickListener onCenterBtnClickListener){
		FmAlertDialog dialog=getOneBtnAlertBuilder( context, iconResId, tittleText, message ,
				 text, onCenterBtnClickListener).create();
		dialog.show();
		return dialog;
	}
	public  static FmAlertDialog.Builder getOneBtnAlertBuilder(Context context,int iconResId,String tittleText,String message ,
			String text,OnCenterBtnClickListener onCenterBtnClickListener){
	    
		return getThreeBtnAlertBuilder(context, iconResId,tittleText, message,  null,text, null, null,onCenterBtnClickListener, null);
	}
	
	public  static FmAlertDialog showOneBtnAlert(Context context,int iconResId,String tittleText,String message ,
			int textId,OnCenterBtnClickListener onCenterBtnClickListener){
		FmAlertDialog dialog=getOneBtnAlertBuilder( context, iconResId, tittleText, message ,
				textId, onCenterBtnClickListener).create();
		dialog.show();
		return dialog;
	}
	
	public  static FmAlertDialog.Builder getOneBtnAlertBuilder(Context context,int iconResId,String tittleText,String message,int textId 
		 ,OnCenterBtnClickListener centerBtnClickListener){
		return getThreeBtnAlertBuilder(context, iconResId, tittleText,message,  0,textId, 0, null,centerBtnClickListener, null);
	}
	

	
	
	
	/**
	 * 2个按钮
	 * @param context
	 * @param message
	 * @param tittleText
	 * @param leftText
	 * @param rightText
	 * @param leftBtnClickListener
	 * @param rightBtnClickListener
	 * @return
	 */
	public  static FmAlertDialog showTwoBtnAlert(Context context,String tittleText ,String message,String leftText ,
			String rightText,OnLeftBtnClickListener leftBtnClickListener ,
			OnRightBtnClickListener rightBtnClickListener){
		
		FmAlertDialog dialog=getTwoBtnAlertBuilder( context, tittleText , message, leftText ,
				 rightText, leftBtnClickListener , rightBtnClickListener).create();
		dialog.show();
		return dialog;
	}
	
	public  static FmAlertDialog.Builder getTwoBtnAlertBuilder(Context context,String tittleText ,String message,String leftText ,
			String rightText,OnLeftBtnClickListener leftBtnClickListener ,
			OnRightBtnClickListener rightBtnClickListener){
	    
		return getThreeBtnAlertBuilder(context, 0, tittleText, message,  leftText,null, rightText, leftBtnClickListener,null, rightBtnClickListener);
	}
	
	public  static FmAlertDialog showTwoBtnAlert(Context context ,String tittleText,String message,int leftTextId 
			,int rightTextId,OnLeftBtnClickListener leftBtnClickListener ,
			OnRightBtnClickListener rightBtnClickListener){
		
		FmAlertDialog dialog=getTwoBtnAlertBuilder( context , tittleText, message, leftTextId 
				, rightTextId, leftBtnClickListener ,rightBtnClickListener).create();
		dialog.show();
		return dialog;
	}
	
	public  static FmAlertDialog.Builder getTwoBtnAlertBuilder(Context context ,String tittleText,String message,int leftTextId 
			,int rightTextId,OnLeftBtnClickListener leftBtnClickListener ,
			OnRightBtnClickListener rightBtnClickListener){
		
		return getThreeBtnAlertBuilder(context, 0, tittleText, message, leftTextId,0, rightTextId, leftBtnClickListener,null, rightBtnClickListener);
	}
	
	public  static FmAlertDialog showTwoBtnAlert(Context context,int iconResId,String tittleText,String message,String leftText ,
			String rightText,OnLeftBtnClickListener leftBtnClickListener ,
			OnRightBtnClickListener rightBtnClickListener){
		
		FmAlertDialog dialog=getTwoBtnAlertBuilder( context, iconResId, tittleText, message, leftText ,
				 rightText, leftBtnClickListener ,
				 rightBtnClickListener).create();
		dialog.show();
		return dialog;
	}

	public  static FmAlertDialog.Builder getTwoBtnAlertBuilder(Context context,int iconResId,String tittleText,String message,String leftText ,
			String rightText,OnLeftBtnClickListener leftBtnClickListener ,
			OnRightBtnClickListener rightBtnClickListener){
	    
		return getThreeBtnAlertBuilder(context, iconResId, tittleText,message,  leftText,null, rightText, leftBtnClickListener,null, rightBtnClickListener);
	}
	
	public  static FmAlertDialog showTwoBtnAlert(Context context,int iconResId,String tittleText,String message,int leftTextId 
			,int rightTextId,OnLeftBtnClickListener leftBtnClickListener ,
			OnRightBtnClickListener rightBtnClickListener){
		
		FmAlertDialog dialog=getTwoBtnAlertBuilder( context, iconResId, tittleText, message, leftTextId 
				, rightTextId, leftBtnClickListener ,
				 rightBtnClickListener).create();
		dialog.show();
		return dialog;
	}
	
	public  static FmAlertDialog.Builder getTwoBtnAlertBuilder(Context context,int iconResId,String tittleText,String message,int leftTextId 
			,int rightTextId,OnLeftBtnClickListener leftBtnClickListener ,
			OnRightBtnClickListener rightBtnClickListener){
		
		return getThreeBtnAlertBuilder(context, iconResId, tittleText,message,  leftTextId,0, rightTextId, leftBtnClickListener,null, rightBtnClickListener);
	}
	
	public  static FmAlertDialog showTwoBtnAlert(Context context,int iconResId,int tittleTextId,int messageId,int leftTextId,
			int rightTextId,OnLeftBtnClickListener leftBtnClickListener ,
			OnRightBtnClickListener rightBtnClickListener){
		
		FmAlertDialog dialog=getTwoBtnAlertBuilder( context, iconResId, tittleTextId, messageId, leftTextId,
				 rightTextId, leftBtnClickListener ,
				 rightBtnClickListener).create();
		dialog.show();
		return dialog;
	}
	
	public  static FmAlertDialog.Builder getTwoBtnAlertBuilder(Context context,int iconResId,int tittleTextId,int messageId,int leftTextId,
			int rightTextId,OnLeftBtnClickListener leftBtnClickListener ,
			OnRightBtnClickListener rightBtnClickListener){
		FmAlertDialog.Builder builder = new FmAlertDialog.Builder(context);
		builder.setMessage(messageId)
		.setIcon(iconResId)
		.setTitle(tittleTextId)
		.setLeftButton(leftTextId, leftBtnClickListener)
		.setRightButton(rightTextId, rightBtnClickListener);
		return builder;

	}
	
	public  static FmAlertDialog showThreeBtnAlert(Context context,int iconResId,String tittleText,String message,String leftText,String centerText
			,String rightText,OnLeftBtnClickListener leftBtnClickListener,OnCenterBtnClickListener centerBtnClickListener,
			OnRightBtnClickListener rightBtnClickListener){
		
		FmAlertDialog dialog=getThreeBtnAlertBuilder( context, iconResId, tittleText, message, leftText, centerText
				, rightText, leftBtnClickListener, centerBtnClickListener,
				 rightBtnClickListener).create();
		dialog.show();
		return dialog;
	}
	
	public  static FmAlertDialog.Builder getThreeBtnAlertBuilder(Context context,int iconResId,String tittleText,String message,String leftText,String centerText
				,String rightText,OnLeftBtnClickListener leftBtnClickListener,OnCenterBtnClickListener centerBtnClickListener,
				OnRightBtnClickListener rightBtnClickListener){
		FmAlertDialog.Builder builder = new FmAlertDialog.Builder(context);
		builder.setMessage(message)
		.setIcon(iconResId)
		.setTitle(tittleText)
		.setLeftButton(leftText, leftBtnClickListener)
		.setCenterButton(centerText, centerBtnClickListener)
		.setRightButton(rightText, rightBtnClickListener);
		return builder;

	}
	
	public  static FmAlertDialog showThreeBtnAlert(Context context,int iconResId,String tittleText,String message,int leftTextId,int centerTextId
			,int rightTextId,OnLeftBtnClickListener leftBtnClickListener,OnCenterBtnClickListener centerBtnClickListener,
			OnRightBtnClickListener rightBtnClickListener){
		
		FmAlertDialog dialog=getThreeBtnAlertBuilder( context, iconResId, tittleText, message, leftTextId, centerTextId
				, rightTextId, leftBtnClickListener, centerBtnClickListener,
				 rightBtnClickListener).create();
		dialog.show();
		return dialog;
	}
	
	public  static FmAlertDialog.Builder getThreeBtnAlertBuilder(Context context,int iconResId,String tittleText,String message,int leftTextId,int centerTextId
			,int rightTextId,OnLeftBtnClickListener leftBtnClickListener,OnCenterBtnClickListener centerBtnClickListener,
			OnRightBtnClickListener rightBtnClickListener){
		FmAlertDialog.Builder builder = new FmAlertDialog.Builder(context);
		builder.setMessage(message)
		.setIcon(iconResId)
		.setTitle(tittleText)
		.setLeftButton(leftTextId, leftBtnClickListener)
		.setCenterButton(centerTextId, centerBtnClickListener)
		.setRightButton(rightTextId, rightBtnClickListener);
		return builder;
	}
	
	/**
	 * 三按钮消息提示框
	 * @param context00
	 * @param iconResId  图片资源ID
	 * @param messageId  消息文字ID
	 * @param tittleTextId 标题文字ID
	 * @param leftTextId  左按钮文字ID
	 * @param centerTextId  中间按钮文字ID
	 * @param rightTextId  右按钮文字ID
	 * @param leftBtnClickListener  左按钮事件监听器00000000000
	 * @param centerBtnClickListener 中间按钮事件监听器
	 * @param rightBtnClickListener  左右按钮事件监听器
	 * @return
	 */
	public  static FmAlertDialog.Builder getThreeBtnAlertBuilder(Context context,int iconResId,int tittleTextId,int messageId,int leftTextId,int centerTextId
			,int rightTextId,OnLeftBtnClickListener leftBtnClickListener,OnCenterBtnClickListener centerBtnClickListener,
			OnRightBtnClickListener rightBtnClickListener){
		FmAlertDialog.Builder builder = new FmAlertDialog.Builder(context);
		builder.setMessage(messageId)
		.setIcon(iconResId)
		.setTitle(tittleTextId)
		.setLeftButton(leftTextId, leftBtnClickListener)
		.setCenterButton(centerTextId, centerBtnClickListener)
		.setRightButton(rightTextId, rightBtnClickListener);
	
		return builder;
	}
	
	public  static FmAlertDialog showThreeBtnAlert(Context context,int iconResId,int tittleTextId,int messageId,int leftTextId,int centerTextId
			,int rightTextId,OnLeftBtnClickListener leftBtnClickListener,OnCenterBtnClickListener centerBtnClickListener,
			OnRightBtnClickListener rightBtnClickListener){
		
		FmAlertDialog dialog=getThreeBtnAlertBuilder( context, iconResId, tittleTextId, messageId, leftTextId, centerTextId
				, rightTextId, leftBtnClickListener, centerBtnClickListener,
				 rightBtnClickListener).create();
		dialog.show();
		return dialog;
	}
	
	
	
}
