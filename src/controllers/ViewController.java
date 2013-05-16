package controllers;

import views.MainView;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;

public class ViewController {


	private MainView mainView;
	
	public ViewController(MainView m){
		this.setMainView(m);
		this.mainView.getSearchButton().addListener(new SearchListener(this));
	}

	
	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}
	

	public MainView getMainView() {
		return mainView;
	}
	
	

}

class SearchListener implements Button.ClickListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5263060985736943700L;
	private ViewController viewController;
	
	public SearchListener(ViewController viewController){
		this.viewController = viewController;
	}

	public void buttonClick(ClickEvent event) {
		Fetcher fetcher = Fetcher.getFetcher();
		String response = fetcher.searchMetaData(
				(String) viewController.getMainView().getTextfield().getValue());
		System.out.print(response);
	}
	
	
}
