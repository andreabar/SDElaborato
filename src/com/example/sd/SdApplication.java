package com.example.sd;

import util.Languages;
import views.LoginPage;
import com.vaadin.Application;

import com.vaadin.ui.themes.Runo;

import dbutil.DBHelper;
import dbutil.IprType;

/**
 * Main application class.
 */
public class SdApplication extends Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5514907833234524517L;

	@Override
	public void init() {
		
		DBHelper.connectToDB();
		Languages.buildMap();
		IprType.build();

		this.setTheme(Runo.themeName());
		this.setMainWindow(new LoginPage());
	}

}

