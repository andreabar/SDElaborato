package view.views;

import refresher.Refresher;

import com.vaadin.ui.Window;

public class MainWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7008709578000661192L;
	
	private Refresher refresher;

	
	public MainWindow(){
		super("Multimedia Data Crawler");
		this.setScrollable(true);
		this.setSizeFull();
		refresher = new Refresher();
		this.addComponent(refresher);
	}


	public Refresher getRefresher() {
		return refresher;
	}


	public void setRefresher(Refresher refresher) {
		this.refresher = refresher;
	}
	
	
	

}
