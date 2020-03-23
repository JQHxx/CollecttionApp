package com.huateng.fm.ui.common.themeparse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.huateng.fm.ui.common.themeparse.bean.FmTheme;
import com.huateng.fm.ui.common.themeparse.bean.FmThemeGroup;

public class XmlParseUtils {
	
	
	public static FmThemeGroup getThemeGroup(Context mContext ){
		AssetManager am=mContext.getAssets();
		InputStream inputStream = null;     
		try {
			inputStream=am.open("theme_config.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		FmThemeGroup themeGroup=parseXml(inputStream);
		return themeGroup;
	}	
	
	 public static  FmThemeGroup parseXml(InputStream inStream) {
		 	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance(); // ��ʼ��sax������    
	        SAXParser saxParser;
	        XmlContentHandler handler = null;
			try {
				saxParser = saxParserFactory.newSAXParser();
				 handler = new XmlContentHandler();    
		        saxParser.parse(inStream, handler); 
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}    catch (IOException e) {
				e.printStackTrace();
			}   
	        return handler.getThemeGroup();    
	    }    

}
