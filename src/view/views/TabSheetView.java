package view.views;

import refresher.Refresher;
import view.controllers.ResultViewController;
import view.controllers.SearchTabController;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;

public class TabSheetView extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2610695989741893779L;
	private TabSheet tabSheet;
	private SearchTabController mainViewController;
	private ResultViewController resultViewController;


	
	
	public TabSheetView(){
		super("Multimedia Data Crawler");
		initViewComponents();
	}
	
	private void initViewComponents(){
		this.setWidth("100%");
		this.setHeight("100%");
		this.setScrollable(true);
		
		this.tabSheet = new TabSheet();
		
		this.mainViewController = new SearchTabController(new SearchTab(this));
		this.tabSheet.addTab(mainViewController.getMainView(), mainViewController.getMainView().getCaption());
		
		this.resultViewController = new ResultViewController(new ResultTab(this));
		this.tabSheet.addTab(resultViewController.getResultView(), resultViewController.getResultView().getCaption());
		
		
		this.addComponent(tabSheet);
	}
	
	public TabSheet getTabSheet() {
		return tabSheet;
	}
	public void setTabSheet(TabSheet tabSheet) {
		this.tabSheet = tabSheet;
	}

	public SearchTabController getMainViewController() {
		return mainViewController;
	}

	public void setMainViewController(SearchTabController mainViewController) {
		this.mainViewController = mainViewController;
	}

	public ResultViewController getResultViewController() {
		return resultViewController;
	}

	public void setResultViewController(ResultViewController resultViewController) {
		this.resultViewController = resultViewController;
	}
	

}
