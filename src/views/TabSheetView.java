package views;

import view.controllers.ResultViewController;
import view.controllers.ViewController;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;

public class TabSheetView extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2610695989741893779L;
	private TabSheet tabSheet;

	
	
	public TabSheetView(){
		super("Multimedia Data Crawler");
		initViewComponents();
	}
	
	private void initViewComponents(){
		this.setWidth("100%");
		this.setHeight("100%");
		this.setScrollable(true);
		
		this.tabSheet = new TabSheet();
		
		ViewController mvc = new ViewController(new MainView(this));
		this.tabSheet.addTab(mvc.getMainView(), "Search");
		
		ResultViewController rsv = new ResultViewController(new ResultView());
		this.tabSheet.addTab(rsv.getResultView(), "My Result");
		
		this.addComponent(tabSheet);
	}
	
	public TabSheet getTabSheet() {
		return tabSheet;
	}
	public void setTabSheet(TabSheet tabSheet) {
		this.tabSheet = tabSheet;
	}
	

}
