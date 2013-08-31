package view.controllers;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

import util.PropertiesReader;
import view.views.TabSheetView;

public class TabViewController {

	private TabSheetView tabSheetView;

	public TabViewController() {
		setTabSheetView(new TabSheetView());
		tabSheetView.getTabSheet().addListener(new AddRefreshListener(this));
		
		
	}

	public TabSheetView getTabSheetView() {
		return tabSheetView;
	}

	public void setTabSheetView(TabSheetView tabSheetView) {
		this.tabSheetView = tabSheetView;
	}

}

class AddRefreshListener implements SelectedTabChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8650102474379135499L;
	private TabViewController tbc;

	public AddRefreshListener(TabViewController tbc) {
		this.tbc = tbc;
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		final TabSheet source = (TabSheet) event.getSource();

	
		
//		if (source.getSelectedTab().getCaption().equals("My Results")) {
//			tbc.getTabSheetView().getMainViewController().getMainView()
//					.getSearchButton().removeClickShortcut();
//			tbc.getTabSheetView().getResultViewController().loadResultTable();
//			tbc.getTabSheetView().getResultViewController()
//					.loadDownloadedFileTable();
//				tbc.getTabSheetView().getResultViewController().enableClear();
//			tbc.getTabSheetView().getResultViewController().getRefresher().setRefreshInterval(PropertiesReader.getRefreshingTime()*1000);
//		} else {
//			
//			tbc.getTabSheetView().getResultViewController().getRefresher().setRefreshInterval(0); //disable the refreshing
//			
//			tbc.getTabSheetView().getMainViewController().getMainView()
//					.getSearchButton().setClickShortcut(KeyCode.ENTER);
//		}

	}

}



