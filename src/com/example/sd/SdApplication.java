package com.example.sd;

import java.io.File;
import java.sql.SQLException;

import util.Languages;
import view.controllers.LoginController;
import view.views.LoginPage;

import com.vaadin.Application;

import com.vaadin.terminal.gwt.server.WebApplicationContext;
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

		WebApplicationContext context = (WebApplicationContext)getContext();
		File properties = new File ( context.getHttpSession().getServletContext().getRealPath("/WEB-INF/db.properties"));

		
		DBHelper.connectToDB(properties);
		
		LoginController lc = new LoginController(new LoginPage());

		this.setTheme(Runo.themeName());
		this.setMainWindow(lc.getLoginPage());


		
	}

	

}

