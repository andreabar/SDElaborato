package controllers;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import model.EuropenaQuery;
import model.Query;
import model.Record;
import model.Search;
import model.VimeoQuery;
import views.MainView;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;

public class ViewController {


	private MainView mainView;
	private Integer sourceSelected;
	
	public ViewController(MainView m){
		this.setMainView(m);
		this.mainView.getSearchButton().addListener(new SearchListener(this));
		this.mainView.getGroupSelector().addListener(new GroupSelectorListener(this));
		loadSearchTable();
	}
	
	private void loadSearchTable(){
		ArrayList<Search> searches = new ArrayList<Search>();
		try {
			searches = DBHelper.getSearches();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(!searches.isEmpty()){
			int i = 0;
			for(Search s : searches){
				i++;
				Object rowItem[] = new Object[]{s.getDate(), s.getKeyword()};
				this.mainView.getSearchTable().addItem(rowItem, i);
			}
		}
	}

	
	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}
	

	public MainView getMainView() {
		return mainView;
	}


	public Integer getSourceSelected() {
		return sourceSelected;
	}


	public void setSourceSelected(Integer sourceSelected) {
		this.sourceSelected = sourceSelected;
	}
	
	

}

class SearchListener implements Button.ClickListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8226238264247152044L;
	private ViewController viewController;
	
	public SearchListener(ViewController viewController){
		this.viewController = viewController;
	}

	public void buttonClick(ClickEvent event) {
	
		this.viewController.getMainView().getSearchButton().setEnabled(false);
		
		if(viewController.getMainView().getTextfield().getValue().equals(new String())){
			viewController.getMainView().getTextfield().setComponentError(new UserError("required"));
			return;
		}
	
		JSONFetcher fetcher = null;
		
		if(viewController.getSourceSelected() == 0)
			fetcher = new EuropeanaFetcher();
		else
			fetcher = new VimeoFetcher();
		
		ArrayList<Record> list;
		try {
			
			
			Query query = fetcher.buildQuery(viewController.getMainView());

			list = fetcher.executeQuery(query);
			
			for(Record r : list){
				System.out.println("Title: " + r.getTitle());
				System.out.println("Type: " + r.getType());
				System.out.println("Resources : " + r.getWebResources());
			}
			
			
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		this.viewController.getMainView().getSearchButton().setEnabled(true);

	}
	
	
}

class GroupSelectorListener implements ValueChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2988676404511145837L;

	private ViewController viewController;
	
	public GroupSelectorListener(ViewController viewController){
		this.viewController = viewController;
		viewController.setSourceSelected(0);
	}
	
	public void valueChange(ValueChangeEvent event) {
		if(viewController.getMainView().getGroupSelector().getValue().equals("Europeana")){
			viewController.setSourceSelected(0);
			viewController.getMainView().getTypeSelect().setEnabled(true);
			viewController.getMainView().getLanguageSelect().setEnabled(true);
		} else if(viewController.getMainView().getGroupSelector().getValue().equals("Vimeo")){
			viewController.setSourceSelected(1);
			viewController.getMainView().getTypeSelect().setValue("VIDEO");
			viewController.getMainView().getTypeSelect().setEnabled(false);
			viewController.getMainView().getLanguageSelect().setEnabled(false);
		}
	}
	
}
