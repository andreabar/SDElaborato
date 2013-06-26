package com.example.sd;

import java.net.MalformedURLException;

import views.MainView;

import com.vaadin.Application;

import com.vaadin.ui.themes.Runo;

import controllers.DBHelper;
import controllers.ViewController;
import controllers.VimeoFetcher;

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
		
		this.setTheme(Runo.themeName());
		MainView mainView = new MainView();
		ViewController viewController = new ViewController(mainView);
		this.setMainWindow(viewController.getMainView());
	}

}

