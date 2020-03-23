package com.huateng.fm.ui.complex.timepicker.loopview;

import java.util.ArrayList;

/**
 * 
 * 管理loopview 
 * 
 * @author shanyong creatd on 2015/10/23
 *
 */
public class LoopViewManager {
	/**
	 * 
	 * @param loopView
	 *            loopview
	 * @param textSize
	 *            字体大小
	 * @param loop
	 *            是否循环
	 * @param listdata
	 *            数据
	 * @param position
	 *            初始位置
	 */
	public static void initLoopView(LoopView loopView, int textSize, boolean isLoop, ArrayList<String> dataList, int position) {
		if (!isLoop) {
			loopView.setNotLoop();
		}
		loopView.setArrayList(dataList);
		loopView.setPosition(position);
		loopView.setTextSize(textSize);
	}
}
