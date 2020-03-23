package com.huateng.fm.ui.common.themeparse;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
import com.huateng.fm.ui.common.themeparse.bean.FmTheme;
import com.huateng.fm.ui.common.themeparse.bean.FmThemeGroup;

public class XmlContentHandler extends DefaultHandler {
	private final String TAG=getClass().getSimpleName();
	private FmThemeGroup mThemeGroup;
	private List<FmTheme> mThemes;
	private FmTheme mTheme;
	private final String THEME_GROUP_STRING = "ThemeGroup";
	private final String THEME_STRING = "Theme";
	private final String THEME_NAME_STRING = "name";
	private final String THEME_DEFAULT_STRING = "defaultTheme";

	private final String PROMARYCOLOR_STRING = "PrimaryColor";
	private final String STATEDCOLOR_STRING = "StatedColor";
	private final String SPECIALCOLOR_STRING = "SpecialColor";
	private String mTempStr;
	private String mTempDefaultThemeName;

	public List<FmTheme> getThemes() {
		return mThemes;
	}
	
	public FmThemeGroup getThemeGroup(){
		return mThemeGroup;
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if (THEME_GROUP_STRING.equals(localName)) {
			mThemeGroup = new FmThemeGroup();
			mThemes = new ArrayList<FmTheme>();
			if (attributes!=null) {
				String string=attributes.getValue(0);
				Log.i(TAG, "attributes:"+string);
				mTempDefaultThemeName=string;
				mThemeGroup.setDefaultTheme(mTempDefaultThemeName);

			}
		

		}
		if (THEME_STRING.equals(localName)) {
			mTheme = new FmTheme();
			if (attributes!=null) {
				String string=attributes.getValue(0);
				Log.i(TAG, "attributes:"+string);
				mTheme.setName(string);	

			}
		}
		mTempStr = localName;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String valueStr = new String(ch, start, length);
		if (THEME_GROUP_STRING.equals(mTempStr)) {
			
		}
		if (THEME_STRING.equals(mTempStr)) {
		}
		
		if (PROMARYCOLOR_STRING.equals(mTempStr)) {
			mTheme.setPrimaryColor(valueStr);
		}
		if (STATEDCOLOR_STRING.equals(mTempStr)) {
			mTheme.setStatedColor(valueStr);
		}
		if (SPECIALCOLOR_STRING.equals(mTempStr)) {
			mTheme.setSpecialColor(valueStr);
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (THEME_GROUP_STRING.equals(localName) && mThemeGroup != null) {
			mThemeGroup.setThemes(mThemes);
		}
		if (THEME_STRING.equals(localName)) {
			mThemes.add(mTheme);
		}
		
		mTempStr = null;
	}

}
