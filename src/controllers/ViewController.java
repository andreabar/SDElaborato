package controllers;

import java.net.MalformedURLException;
import java.util.ArrayList;

import model.EuropenaQuery;
import model.Query;
import model.Record;
import model.VimeoQuery;
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
		
		//TODO: creare dei selettori per i filtri, in particolare, per adesso, # di risultati
		//lingua dei contenuti (mettine solo alcune di prova per ora, la conversione vedila in
		//Europena API, ad esempio Italiano è it), una checkbox per risultati free access, e 
		// una scelta sul tipo di data (VIDEO, TEXT, IMAGE). Per ora usiamo questi, poi li aggiungiamo
		// all'evenienza (anche a seconda di cosa fare con Youtube).
		
		JSONFetcher fetcher = new EuropeanaFetcher();
		ArrayList<Record> list;
		try {
			EuropenaQuery query = new EuropenaQuery(viewController.getMainView().getTextfield().getValue().toString());
			query.setLimit(2);
			query.setDataType("video");
			query.setPublicIpr(true);
		
			list = fetcher.executeQuery(query);
			
			for(Record r : list){
				System.out.println("Title: " + r.getTitle());
				System.out.println("Type: " + r.getType());
				System.out.println("Link: " + r.getEuropeanaId());
				System.out.println("Resources : " + r.getWebResources());
			}
			
			
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
}
