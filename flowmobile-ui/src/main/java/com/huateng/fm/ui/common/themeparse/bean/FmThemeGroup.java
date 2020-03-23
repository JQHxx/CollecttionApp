package com.huateng.fm.ui.common.themeparse.bean;

import java.util.ArrayList;
import java.util.List;

public class FmThemeGroup {
	
	private String defaultTheme;
	
	private List<FmTheme> themes=new ArrayList<FmTheme>();

	public String getDefaultTheme() {
		return defaultTheme;
	}

	public void setDefaultTheme(String defaultTheme) {
		this.defaultTheme = defaultTheme;
	}

	public List<FmTheme> getThemes() {
		return themes;
	}

	public void setThemes(List<FmTheme> themes) {
		this.themes.clear();
		this.themes.addAll(themes);
	}
	
	
	
}
