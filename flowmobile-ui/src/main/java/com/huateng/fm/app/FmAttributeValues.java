package com.huateng.fm.app;

import java.util.List;
import java.util.Observable;

import com.huateng.flowMobile.R;
import com.huateng.fm.ui.common.themeparse.XmlParseUtils;
import com.huateng.fm.ui.common.themeparse.bean.FmTheme;
import com.huateng.fm.ui.common.themeparse.bean.FmThemeGroup;
import com.huateng.fm.util.UiValuesUtil;

import android.content.Context;
import android.util.Log;

/**
 * This class holds the values of the common attributes.
 */
public class FmAttributeValues extends Observable{
	private final String TAG=getClass().getSimpleName();
	private Context context;
    public static int INVALID = -1;

    public static int THEME_COLOR;
    public static boolean hasShadow;

    public static int DEFAULT_CORNER_RADIUS_DP;
    public static int DEFAULT_BORDER_WIDTH_DP = 2;
    public static int DEFAULT_SIZE_DP = 10;
    public static int COMPOUNDBUTTON_DEFAULT_HEIGHT;
    public static int COMPOUNDBUTTON_DEFAULT_WIDTH;
    public static int RATINGBAR_DEFAULT_RADIUS;
    public static int RATINGBAR_DEFAULT_SPACE;
    public static int RATINGBAR_DEFAULT_SIZE;

    public static int CORNER_RADIUS;
    public static int SMALL_CORNER_RADIUS_DP;
    public static int TOGGLE_BUTTON_THUMB_COLOR=0xFFFFFFFF;
    public static int TOGGLE_BUTTON_BG_COLOR=0xFFEDEDED;
    public static int GRAY_BORDER_COLOR=0xFFDBDFE4;

    public static int DEFAULT_BORDER_WIDTH_PX = 2;
    public static int DEFAULT_SIZE_PX = 20;
    public static int TOAST_HORIZONTAL_PADDING;
    public static int TOAST_VERTICAL_PADDING;
    
    public static int  PRIMARY_COLOR,STATED_COLOR,SPECIAL_COLOR;
    private static FmAttributeValues instance;
    
    public static  class CornerStyle{
        public static final int NONE=0x1;
        public static final int SMALL=0x2;
        public static final int ROUND=0x3;
    }

    private static int[] mThemeColors;

    public static int ht3dp,ht2dp,ht1dp;

    private float radius = CORNER_RADIUS;
    private int size = DEFAULT_SIZE_PX;
    private int borderWidth = DEFAULT_BORDER_WIDTH_PX;
    private static List<FmTheme> themes;
    
    public FmAttributeValues(  ) {
    }
    
//    public static FmAttributeValues getInstance( ){
//    	if (instance==null) {
//			instance=new FmAttributeValues();
//		}
//    	return instance;
//    }
    
 
    
    public static void init(Context context){
		ht3dp = (int) context.getResources().getDimension(R.dimen.ht_3dp);
		ht2dp = (int) context.getResources().getDimension(R.dimen.ht_2dp);
		ht1dp= (int) context.getResources().getDimension(R.dimen.ht_1dp);
		COMPOUNDBUTTON_DEFAULT_HEIGHT=(int)context.getResources().getDimension(R.dimen.ht_widget_compoundButton_default_height);
    	COMPOUNDBUTTON_DEFAULT_WIDTH=(int)context.getResources().getDimension(R.dimen.ht_widget_compoundButton_default_width);
    	SMALL_CORNER_RADIUS_DP=(int)context.getResources().getDimension(R.dimen.ht_widget_corner_default_radius);
    	RATINGBAR_DEFAULT_RADIUS=(int)context.getResources().getDimension(R.dimen.ht_widget_ratingbar_default_radius);
    	RATINGBAR_DEFAULT_SPACE=(int)context.getResources().getDimension(R.dimen.ht_widget_ratingbar_default_space);
    	RATINGBAR_DEFAULT_SIZE=(int)context.getResources().getDimension(R.dimen.ht_widget_ratingbar_default_size);
    	TOAST_HORIZONTAL_PADDING=(int)context.getResources().getDimension(R.dimen.ht_widget_toast_horizontal_padding);
    	TOAST_VERTICAL_PADDING=(int)context.getResources().getDimension(R.dimen.ht_widget_toast_vertical_padding);

    	String aString=context.getTheme().toString();
    	Log.i("22",aString);
    	FmThemeGroup themeGroup=XmlParseUtils.getThemeGroup(context);
    	themes=themeGroup.getThemes();    	
    	Log.i("HTAttributeValues", "themes.size:"+themes.size());
        FmTheme theme=themes.get(0);
       Log.i("HTAttribute-init", "themes.size():"+themes.size());
       mThemeColors=new int[4];
       mThemeColors[0]=getIdByIdentifier(context,theme.getPrimaryColor(),"color");
       mThemeColors[1]=getIdByIdentifier(context,theme.getStatedColor(),"color");
       mThemeColors[2]=getIdByIdentifier(context,theme.getSpecialColor(),"color");
       mThemeColors[3]=getIdByIdentifier(context,theme.getSpecialColor(),"color");
       THEME_COLOR= UiValuesUtil.getColor(mThemeColors[0]);
       PRIMARY_COLOR=UiValuesUtil.getColor(mThemeColors[0]);
       Log.i("HTAttributeValues", "PRIMARY_COLOR:"+PRIMARY_COLOR);
       STATED_COLOR=UiValuesUtil.getColor(mThemeColors[1]);
       SPECIAL_COLOR=UiValuesUtil.getColor(mThemeColors[3]);

       Log.i("HTAttributeValues", "mThemeColors:"+mThemeColors.toString()+R.color.orange_primary);
    }
    
    /**
     * 切换主题
     * @param position
     */
    public void switchTheme(Context context,int position){
    	for (int i = 0; i < themes.size(); i++) {
    		if (position==i) {
    			FmTheme theme=themes.get(i);
        		int id=getIdByIdentifier(context,theme.getName(),"style");
            	   context.setTheme(id);
            	   Log.i("HTAttribute-init", "themes.size():"+themes.size());
            	   mThemeColors=new int[4];
            	   mThemeColors[0]=getIdByIdentifier(context,theme.getPrimaryColor(),"color");
            	   mThemeColors[1]=getIdByIdentifier(context,theme.getStatedColor(),"color");
            	   mThemeColors[2]=getIdByIdentifier(context,theme.getSpecialColor(),"color");
            	   mThemeColors[3]=getIdByIdentifier(context,theme.getSpecialColor(),"color");
            	   THEME_COLOR= mThemeColors[0];
            	   PRIMARY_COLOR=mThemeColors[0];
            	   STATED_COLOR=UiValuesUtil.getColor(mThemeColors[1]);
            	   SPECIAL_COLOR=UiValuesUtil.getColor(mThemeColors[3]);
            	   setChanged();
            	   notifyObservers();
               }
		}
    }
    
    
    
    private static int getIdByIdentifier(Context context,String identifyName,String typeName){
        int id =context.getResources().getIdentifier(identifyName, typeName, context.getPackageName());
        return id;
    }
    

  


    public int getColor(int colorPos) {
        return mThemeColors[colorPos];
    }

    public float getRadius() {
        return radius;
    }

    public float[] getOuterRadius() {
        return new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

}
