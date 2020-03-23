package com.huateng.fm.ui.complex.timepicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.huateng.flowMobile.R;
import com.huateng.fm.ui.complex.timepicker.loopview.LoopListener;
import com.huateng.fm.ui.complex.timepicker.loopview.LoopView;
import com.huateng.fm.util.FmValueUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
  * author: Devin
  * createTime:2015年11月11日
  * desciprion:
  */
public class DateTimeView extends LinearLayout implements LoopListener{
	
	private final String TAG=getClass().getSimpleName();
	private View v;
	private Context mContext;
	private LoopView v_year,v_month,v_day,v_hour,v_min;
//	private final int[] mMonthRange={1,2,3,4,5,6,7,8,9,10,11,12};
	private String mResult;
	
	private String mSeparator="-";
	private String mYearLabel="年";
	private String mMonthLabel="月";
	private String mDayLabel="日";
	private String mHourLabel="时";
	private String mMinLabel="分";

	private int mStartYear;
	private int mStartMonth;
	private int mStartDay;
	private int mEndDay;
	private int mEndYear;
	private int mEndMonth;
	private Calendar mCal = Calendar.getInstance();
	private List<Integer> mYearRange=new ArrayList<Integer>();
	private List<Integer> mDayRange=new ArrayList<Integer>();
	private List<Integer> mMonthRange=new ArrayList<Integer>();
	private List<Integer> mHourRange=new ArrayList<Integer>();
	private List<Integer> mMinRange=new ArrayList<Integer>();

	private String mYearStr;
	private String mMonthStr;
	private String mDayStr;
	private String mHourStr;
	private String mMinStr;
	
	private Type mType;
	
	public void setSeparator(String separator){
		mSeparator=separator;
	}

	
	//0表示当前
	public void addLimit(int startYear,int startMonth,int startDay,int endYear){
		mStartYear=startYear;
		mStartMonth=startMonth;
		mStartDay=startDay;
		mEndYear=endYear;
		if (mStartYear==0) {
			mStartYear=mCal.get(Calendar.YEAR);
		}
		if (mEndYear==0) {
			mEndYear=mCal.get(Calendar.YEAR);
		}
		if (mEndYear-mStartYear<0) {
			throw new RuntimeException("start year is bigger than end year");
		}
		
		for (int i = mStartYear; i <mEndYear+1; i++) {
			mYearRange.add(i);
		}
		
		if (mStartDay==0) {
			mStartDay=mCal.get(Calendar.DAY_OF_MONTH);
		}
		if (mEndDay==0) {
			mEndDay= mCal.getActualMaximum(Calendar.DAY_OF_MONTH);  
		}
		if (mEndDay-mStartDay<0) {
			throw new RuntimeException("start day is bigger than end day");
		}
		
		for (int i = mStartDay; i <mEndDay+1; i++) {
			mDayRange.add(i);
		}
		
		if (mStartMonth==0) {
			mStartMonth=mCal.get(Calendar.MONTH);
		}
		if (mEndMonth==0) {
			mEndMonth=12;
		}
		if (mEndMonth-mStartMonth<0) {
			throw new RuntimeException("start month is bigger than end month");
		}
		
		for (int i = mStartMonth; i <mEndMonth+1; i++) {
			mMonthRange.add(i);
		}
		
		for (int i = 1; i < 61; i++) {
			mMinRange.add(i);
		}
		for (int i = 1; i < 25; i++) {
			mHourRange.add(i);
		}
		
		v_month.setArrayList(convert(mMonthRange));
		v_year.setArrayList(convert(mYearRange));
		v_day.setArrayList(convert(mDayRange));
		v_hour.setArrayList(convert(mHourRange) );
		v_min.setArrayList(convert(mMinRange));
		
		printList(convert(mYearRange),"mYearRange");
		printList(convert(mMonthRange),"mMonthRange");

	}
	
	private void printList(List<String> list,String tag){
		String[] strs=new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			strs[i]=list.get(i);
		}
		Log.e(TAG, "--print--"+tag+"--:"+Arrays.toString(strs));
	}
	
	private ArrayList<String> convert(List<Integer> list){
		List<String> strs=new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			strs.add(list.get(i)+"");
		}
		return (ArrayList<String>) strs;
	}
	
	public static enum Type{
		YEAR,MONTH,DAY,YEAR_MONTH,YEAR_MONTH_DAY,MONTH_DAY,TIME
	}

	public DateTimeView(Context context) {
		super(context);
		mContext=context;
		init();
	}
	
	public DateTimeView(Context context,Type type) {
		super(context);
		mContext=context;
		mType=type;
		setType(type);
		init();
	}
	
	
	public DateTimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		init();
	}
	
	public void setType(Type type){
		mType=type;
		v_year.setVisibility(GONE);
		v_month.setVisibility(GONE);
		v_day.setVisibility(GONE);
		v_hour.setVisibility(GONE);
		v_min.setVisibility(GONE);
		if (mType==Type.YEAR) {
			v_year.setVisibility(VISIBLE);
		}else if (mType==Type.MONTH) {
			v_month.setVisibility(VISIBLE);
		}else if (mType==Type.DAY) {
			v_day.setVisibility(VISIBLE);
		}else if (mType==Type.TIME) {
			v_hour.setVisibility(VISIBLE);
			v_min.setVisibility(VISIBLE);
		}else if (mType==Type.MONTH_DAY) {
			v_month.setVisibility(VISIBLE);
			v_day.setVisibility(VISIBLE);
		}else if (mType==Type.YEAR_MONTH) {
			v_month.setVisibility(VISIBLE);
			v_year.setVisibility(VISIBLE);
		}else if (mType==Type.YEAR_MONTH_DAY) {
			v_month.setVisibility(VISIBLE);
			v_year.setVisibility(VISIBLE);
			v_day.setVisibility(VISIBLE);
		}
	}

	private void init(){
		setOrientation(VERTICAL);
		v=LayoutInflater.from(mContext).inflate(R.layout.csv_date_time_view, null);
		  LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
	       v.setLayoutParams(params);
		v_year=(LoopView) v.findViewById(R.id.v_year);
		v_month=(LoopView) v.findViewById(R.id.v_month);
		v_day=(LoopView) v.findViewById(R.id.v_day);
		v_hour=(LoopView) v.findViewById(R.id.v_hour);
		v_min=(LoopView) v.findViewById(R.id.v_min);
		v_year.setNotLoop();
		v_month.setNotLoop();
		v_year.setListener(this);
		v_month.setListener(this);
		v_day.setListener(this);
		v_hour.setListener(this);
		v_min.setListener(this);
		addView(v);

	}
	
	private int year,month,day;
	
	public  void initData(int year0,int month0,int day0,int hour,int min){
		this.year=year0;
		this.month=month0;
		this.day=day0;
		Log.e(TAG, "initData："+month);

		if (year==0) {
			this.year=mCal.get(Calendar.YEAR);
		}
		v_year.setPosition(mYearRange.indexOf(year));

		if (month==0) {
			this.month=mCal.get(Calendar.MONTH)+1;
			Log.e(TAG, "initData-month==0："+this.month);

		}
		Log.e(TAG, "month位置："+mMonthRange.indexOf(month));
		v_month.setPosition(mMonthRange.indexOf(month));

		if (day==0) {
			this.day=mCal.get(Calendar.DAY_OF_MONTH);
		}
		v_day.setPosition(mDayRange.indexOf(day));

		v_hour.setPosition(mHourRange.indexOf(hour));
		v_min.setPosition(mMinRange.indexOf(min));
	}
	
	public String getDateTime(){
		if (mType==Type.YEAR) {
			if (FmValueUtil.isStrEmpty(mYearStr)) {
				if (year==0) {
					mYearStr=mCal.get(Calendar.YEAR)+mYearLabel;
				}else {
					mYearStr=year+mYearLabel;
				}
			}
			mResult=mYearStr;
		}else if (mType==Type.MONTH) {
			if (FmValueUtil.isStrEmpty(mMonthStr)) {
				if (month==0) {
					mMonthStr=mCal.get(Calendar.MONTH)+mMonthLabel;
				}else {
					mMonthStr=month+mMonthLabel;
				}
			}
			mResult=mMonthStr;
			
		}else if (mType==Type.DAY) {
			if (FmValueUtil.isStrEmpty(mDayStr)) {
				if (day==0) {
					mDayStr=mCal.get(Calendar.DAY_OF_MONTH)+mDayLabel;
				}else {
					mDayStr=day+mDayLabel;
				}
			}
			mResult=mDayStr;
		}else if (mType==Type.TIME) {
			mResult=mHourStr+mMinStr;
		}else if (mType==Type.MONTH_DAY) {
			if (FmValueUtil.isStrEmpty(mMonthStr)) {
				if (month==0) {
					mMonthStr=mCal.get(Calendar.MONTH)+mMonthLabel;
				}else {
					mMonthStr=month+mMonthLabel;
				}
			}
			if (FmValueUtil.isStrEmpty(mDayStr)) {
				if (day==0) {
					mDayStr=mSeparator+mCal.get(Calendar.DAY_OF_MONTH)+mDayLabel;
				}else {
					mDayStr=mSeparator+day+mDayLabel;
				}
			}
			mResult=mMonthStr+mDayStr;
		}else if (mType==Type.YEAR_MONTH) {
			if (FmValueUtil.isStrEmpty(mMonthStr)) {
				if (month==0) {
					Log.e(TAG, "month==0");
					mMonthStr=mSeparator+(mCal.get(Calendar.MONTH)+1)+mMonthLabel;
				}else {
					Log.e(TAG, "month！=0："+month);

					mMonthStr=mSeparator+month+mMonthLabel;
				}
			}
			if (FmValueUtil.isStrEmpty(mYearStr)) {
				if (year==0) {
					mYearStr=mCal.get(Calendar.YEAR)+mYearLabel;
				}else {
					mYearStr=year+mYearLabel;
				}
			}
			mResult=mYearStr+mMonthStr;
			Log.e(TAG, "mResult:"+mResult);
		}else if (mType==Type.YEAR_MONTH_DAY) {
			if (FmValueUtil.isStrEmpty(mYearStr)) {
				if (year==0) {
					mYearStr=mCal.get(Calendar.YEAR)+mYearLabel;
				}else {
					mYearStr=year+mYearLabel;
				}
			}
			if (FmValueUtil.isStrEmpty(mMonthStr)) {
				if (month==0) {
					mMonthStr=mSeparator+(mCal.get(Calendar.MONTH)+1)+mMonthLabel;
				}else {
					mMonthStr=mSeparator+month+mMonthLabel;
				}
			}
			if (FmValueUtil.isStrEmpty(mDayStr)) {
				if (day==0) {
					mDayStr=mSeparator+mCal.get(Calendar.DAY_OF_MONTH)+mDayLabel;
				}else {
					mDayStr=mSeparator+day+mDayLabel;
				}
			}
			mResult=mYearStr+mMonthStr+mDayStr;
		}
		Log.i(TAG, "mYearStr:"+mResult);
		return mResult;
	}

	@Override
	public void onItemSelect(LoopView v,int item) {
		Log.e(TAG, "onItemSelect-item:"+item);
		int id=v.getId();
		if (id==R.id.v_year) {
			year=mYearRange.get(item);
			mYearStr=year+mYearLabel;
			Log.i(TAG, "mYearStr:"+mYearStr);
		}else if (id==R.id.v_month) {
			month=mMonthRange.get(item);
			mMonthStr=mSeparator+month+mMonthLabel;
			Log.i(TAG, "mYearStr:"+mYearStr);
		}else if (id==R.id.v_day) {
			day=mDayRange.get(item);
			mDayStr=mSeparator+day+mDayLabel;
			Log.i(TAG, "mDayStr:"+mDayStr);
		}else if (id==R.id.v_hour) {
			mHourStr=" "+mHourRange.get(item)+mHourLabel;
			Log.i(TAG, "mHourStr:"+mHourStr);
		}else if (id==R.id.v_min) {
			mMinStr=mSeparator+mMinRange.get(item)+mMinLabel;
		}
	}

	
	
	
	
}
