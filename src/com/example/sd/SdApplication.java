package com.example.sd;

import java.io.File;
import util.PropertiesReader;
import view.controllers.LoginController;
import view.views.LoginPage;
import view.views.MainWindow;

import com.vaadin.Application;

import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.themes.Runo;


import dbutil.DBHelper;

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

		this.setTheme(Runo.themeName());

		WebApplicationContext context = (WebApplicationContext)getContext();
		File properties = new File ( context.getHttpSession().getServletContext().getRealPath("/WEB-INF/db.properties"));

		PropertiesReader.initProperties(properties);
		DBHelper.connectToDB();
		
		MainWindow mainWindow = new MainWindow();
		LoginController lc = new LoginController(new LoginPage());
		mainWindow.setContent(lc.getLoginPage());
		this.setMainWindow(mainWindow);
			

		
	}

	

}

