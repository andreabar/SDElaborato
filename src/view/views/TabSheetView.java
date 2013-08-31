package view.views;

import view.controllers.ResultViewController;
import view.controllers.SearchTabController;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

public class TabSheetView extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2610695989741893779L;
	private TabSheet tabSheet;
	private SearchTabController mainViewController;
	private ResultViewController resultViewController;

	private Button logoutButton;
	private Label logged;

	
	
	public TabSheetView(){
		super("Multimedia Data Crawler");
		initViewComponents();
	}
	
	private void initViewComponents(){
		this.setWidth("100%");
		this.setHeight("100%");
		this.setScrollable(true);
		
		GridLayout gl = new GridLayout(5, 1);
		gl.setSpacing(true);
		gl.setWidth("100%");
		
		this.logoutButton = new Button("Logout");
		this.logoutButton.setStyleName(BaseTheme.BUTTON_LINK);

		this.logged = new Label();
		
		this.tabSheet = new TabSheet();
		
		this.mainViewController = new SearchTabController(new SearchTab(this));
		this.tabSheet.addTab(mainViewController.getMainView(), mainViewController.getMainView().getCaption());
		
		this.resultViewController = new ResultViewController(new ResultTab(this));
		this.tabSheet.addTab(resultViewController.getResultView(), resultViewController.getResultView().getCaption());
		
		gl.addComponent(logged, 3, 0);
		gl.addComponent(logoutButton, 4, 0);
		gl.setComponentAlignment(logoutButton, Alignment.MIDDLE_RIGHT);
		this.addComponent(gl);
		
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

	public Label getLogged() {
		return logged;
	}

	public void setLogged(Label logged) {
		this.logged = logged;
	}

	public Button getLogoutButton() {
		return logoutButton;
	}

	public void setLogoutButton(Button logoutButton) {
		this.logoutButton = logoutButton;
	}
	

}
