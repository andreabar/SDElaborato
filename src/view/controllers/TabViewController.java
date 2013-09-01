package view.controllers;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

import dbutil.DBHelper;

import util.AppData;
import view.views.LoginPage;
import view.views.TabSheetView;

public class TabViewController {

	private TabSheetView tabSheetView;

	public TabViewController() {
		setTabSheetView(new TabSheetView());
		tabSheetView.getTabSheet().addListener(new AddRefreshListener(this));
		tabSheetView.getLogged().setValue("Logged as : " + DBHelper.getUserName(AppData.userID));
		tabSheetView.getLogoutButton().addListener(new LogoutListener(this));
		
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

	
		
		if (source.getSelectedTab().getCaption().equals("My Results")) {
			tbc.getTabSheetView().getMainViewController().getMainView()
					.getSearchButton().removeClickShortcut();
			tbc.getTabSheetView().getResultViewController().loadResultTable();
			tbc.getTabSheetView().getResultViewController()
					.loadDownloadedFileTable();
				tbc.getTabSheetView().getResultViewController().enableClear();
//			tbc.getTabSheetView().getResultViewController().getRefresher().setRefreshTime(PropertiesReader.getRefreshingTime()*1000);
		} else {
			
//			tbc.getTabSheetView().getResultViewController().getRefresher().setRefreshTime(0); //disable the refreshing
			
			tbc.getTabSheetView().getMainViewController().getMainView()
					.getSearchButton().setClickShortcut(KeyCode.ENTER);
		}

	}

}

class LogoutListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5508313346827627646L;
	private TabViewController viewController;
	
	public LogoutListener(TabViewController c){
		this.viewController = c;
	}
	
	public void buttonClick(ClickEvent event) {
		AppData.userID = -1;
		LoginController lc = new LoginController(new LoginPage());
		this.viewController.getTabSheetView().getApplication().getMainWindow().setContent(lc.getLoginPage());
		
	}
	
}


