package com.huateng.fm.ui.toolbar;


import androidx.appcompat.widget.Toolbar;

/**
  * author: Devin
  * createTime:2015年7月10日
  * desciprion:
  */
public abstract class ToolBarPrototype {
	
	private Toolbar mToolbar;
	
	public ToolBarPrototype(){
	}
	
	public ToolBarPrototype(Toolbar toolbar){
		this.mToolbar=toolbar;
	}
	
	
	public Toolbar getToolbar(){
		return mToolbar;
	}
}
