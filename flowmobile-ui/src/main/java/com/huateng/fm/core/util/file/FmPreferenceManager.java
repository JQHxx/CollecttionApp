package com.huateng.fm.core.util.file;


import com.huateng.fm.core.app.FmApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;



/**
 * @author: Devin
 * on: 14-5-15.
 */
public class FmPreferenceManager {
	/**
	 * 在指定的文件中保存数据
	 * 
	 * @param fileName
	 *            文件名称
	 * @param objs
	 *            数组{key,value}
	 */
	public static void save(String fileName, Object[] objs) {
		try {
			SharedPreferences sp = FmApplication.getInstance().getSharedPreferences(fileName, Context.MODE_APPEND);
			Editor editor = sp.edit();
			if (objs[1] instanceof String) {
				editor.putString(objs[0].toString(), objs[1].toString());
			} else if (objs[1] instanceof Integer) {
				editor.putInt(objs[0].toString(),Integer.parseInt(objs[1].toString()));
			} else if (objs[1] instanceof Long) {
				editor.putLong(objs[0].toString(),Long.parseLong((objs[1].toString())));
			} else if (objs[1] instanceof Float) {
				editor.putFloat(objs[0].toString(),Float.parseFloat((objs[1].toString())));
			} else if (objs[1] instanceof Boolean) {
				editor.putBoolean(objs[0].toString(),Boolean.parseBoolean((objs[1].toString())));
			}
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * 在指定的文件中读取数据
	 * 
	 * @param fileName
	 *            文件名称
	 * @param objs
	 *            数组{key,defaultValue}
	 */
	public static Object get(String fileName, Object[] objs) {
		try {
			SharedPreferences sp = FmApplication.getInstance().getSharedPreferences(fileName,Context.MODE_APPEND);
			if (objs[1] instanceof String) {
				return sp.getString(objs[0].toString(), objs[1].toString());
			} else if (objs[1] instanceof Integer) {
				return sp.getInt(objs[0].toString(),Integer.parseInt(objs[1].toString()));
			} else if (objs[1] instanceof Long) {
				return sp.getLong(objs[0].toString(),Long.parseLong((objs[1].toString())));
			} else if (objs[1] instanceof Float) {
				return sp.getFloat(objs[0].toString(),Float.parseFloat((objs[1].toString())));
			} else if (objs[1] instanceof Boolean) {
				return sp.getBoolean(objs[0].toString(),Boolean.parseBoolean((objs[1].toString())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String getString(String fileName,String key){
		SharedPreferences sp = FmApplication.getInstance().getSharedPreferences(fileName,Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}
    public static int getInt(String fileName,String key){
        SharedPreferences sp = FmApplication.getInstance().getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }
	
	public static Boolean getBoolean(String fileName,String key) {
		SharedPreferences sp = FmApplication.getInstance().getSharedPreferences(fileName,Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}

    public static void saveString(String fileName,String key,String value){
        SharedPreferences sp = FmApplication.getInstance().getSharedPreferences(fileName,Context.MODE_PRIVATE);
        Editor editor=sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static void saveBoolean(String fileName,String key,boolean value){
        SharedPreferences sp = FmApplication.getInstance().getSharedPreferences(fileName,Context.MODE_PRIVATE);
        Editor editor=sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void saveInt(String fileName,String key,int value){
        SharedPreferences sp = FmApplication.getInstance().getSharedPreferences(fileName,Context.MODE_PRIVATE);
        Editor editor=sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void remove(String fileName,String key){
        SharedPreferences sp = FmApplication.getInstance().getSharedPreferences(fileName,Context.MODE_PRIVATE);
        Editor editor=sp.edit();
        editor.remove(key);
        editor.commit();
    }
}
