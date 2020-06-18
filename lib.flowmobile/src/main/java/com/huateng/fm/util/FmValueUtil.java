package com.huateng.fm.util;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * @author Devin
 * 2015年1月8日  下午12:36:37
 */
public class FmValueUtil {

	
	public static boolean isListNotEmpty(List<?> noteList) {
		return null != noteList && noteList.size() > 0;
	}


	public static boolean isListEmpty(List<?> noteList) {
		return null == noteList || noteList.size() == 0;
	}

	

	public static boolean isStrEmpty(String value) {
		if (null == value || "".equals(value.trim())) {
			return true;
		} 
		return false;
	}

	public static boolean isNotEmpty(Object object) {
		return null != object;
	}

	public static boolean isEmpty(Object object) {
		return null == object;
	}

	public static boolean isStrNotEmpty(String value) {
		return !isStrEmpty(value);
	}



	/**
	 * 判断在多个EditText或者TextView的内容中有一个为空就返回true
	 * 
	 * @param ets
	 * @return
	 */
	public static boolean hasEmptyView(View... views) {
		for (View v : views) {
			if (!v.isShown()) {// 不可见的不做判断
				continue;
			}
			if (v instanceof EditText) {
				EditText et = (EditText) v;
				if (TextUtils.isEmpty(et.getText().toString().trim())) {
					return true;
				}
			} else if (v instanceof TextView) {
				TextView tv = (TextView) v;
				if (TextUtils.isEmpty(tv.getText().toString().trim())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获得EditText 或者 TextView的内容
	 * 
	 * @param et
	 * @return
	 */
	public static String ViewText2Str(View v) {
		String str = "";
		if (v != null) {
			if (v instanceof EditText) {
				String tempStr = ((EditText) v).getText().toString().trim();
				str = TextUtils.isEmpty(tempStr) ? "" : tempStr;
			} else if (v instanceof TextView) {
				String tempStr = ((TextView) v).getText().toString().trim();
				str = TextUtils.isEmpty(tempStr) ? "" : tempStr;
			}
		}
		return str;
	}

	/**
	 * 将boolean true变成"1" false变成"0"
	 * 
	 * @param b
	 * @return
	 */
	public static String bolean2String(boolean b) {
		return b ? "1" : "0";
	}

	
	public static void setText(Activity activity, int resId, String content) {
		View tvText = activity.findViewById(resId);
		if (null != tvText && (tvText instanceof TextView)) {
			((TextView) tvText).setText(content);
		}
	}


	/**
	 * 判断九宫格结构体的图片url是否为空
	 */
	public static boolean isGridImageUrlStrEmpty(String pressedBgName) {
		if (FmValueUtil.isStrEmpty(pressedBgName)
				|| pressedBgName.lastIndexOf("/") == pressedBgName.length() - 1) {
			return true;
		}
		return false;
	}


	
}