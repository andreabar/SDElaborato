package view.controllers;

import views.TabSheetView;

public class TabViewController {
	
	private TabSheetView tabSheetView;

	
	public TabViewController(){
		setTabSheetView(new TabSheetView());
	}

	public TabSheetView getTabSheetView() {
		return tabSheetView;
	}

	public void setTabSheetView(TabSheetView tabSheetView) {
		this.tabSheetView = tabSheetView;
	}


}
