package view.controllers;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import model.Query;
import model.Record;
import views.MainView;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;

import controllers.QueryController;
import controllers.RecordController;

import fetcher.EuropeanaFetcher;
import fetcher.JSONFetcher;
import fetcher.VimeoFetcher;

public class ViewController {


	private MainView mainView;
	private Integer sourceSelected;
	
	public ViewController(MainView m){
		this.setMainView(m);
		this.mainView.getSearchButton().addListener(new SearchListener(this));
		this.mainView.getGroupSelector().addListener(new GroupSelectorListener(this));
		this.mainView.getSearchTable().addListener(new TableClickListener(this));
		this.mainView.getDetailsButton().addListener(new DetailsListener(this));
		loadSearchTable();
	}
	
	protected void loadSearchTable(){
		this.mainView.getSearchTable().removeAllItems();
		ArrayList<Query> searches = new ArrayList<Query>();
		try {
			searches = QueryController.getSearches(mainView.getUserID());
			if(!searches.isEmpty()){
				for(Query q : searches){
					Object rowItem[] = new Object[]{q.getKeyword(), q.getProvider(), q.getDataType(), q.getLanguage(), q.getResults()};
					this.mainView.getSearchTable().addItem(rowItem, q);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
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
		
		if(viewController.getMainView().getTextfield().getValue().toString().isEmpty()){
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
			
			
			Query query = fetcher.buildQuery(viewController);
			list = fetcher.executeQuery(query);
			QueryController.saveQuery(query, viewController.getMainView().getUserID());
			
			for(Record r : list)
				r.setQueryID(query.getId());
			
			
			RecordController.saveRecords(list);
			

			this.viewController.getMainView().getSearchButton().setEnabled(true);
			Object rowItem[] = new Object[]{query.getKeyword(), query.getProvider(), query.getDataType(), query.getLanguage(), query.getResults()};
			viewController.getMainView().getSearchTable().addItem(rowItem, query);
			viewController.getMainView().getSearchTable().select(query);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

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

class TableClickListener implements ValueChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2459096720898773538L;

	private ViewController viewController;
	
	public TableClickListener(ViewController v){
		this.viewController = v;
	}
	
	public void valueChange(ValueChangeEvent event) {
		if(null != this.viewController.getMainView().getSearchTable().getValue()){
			this.viewController.getMainView().getDetailsButton().setEnabled(true);
		} else {
			this.viewController.getMainView().getDetailsButton().setEnabled(false);
		}
	}
	
}

class DetailsListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -445081953325867218L;
	private ViewController viewController;
	
	public DetailsListener(ViewController v){
		this.viewController = v;
	}

	public void buttonClick(ClickEvent event) {
		Query selectedQuery = (Query)this.viewController.getMainView().getSearchTable().getValue();
		DetailsViewController dvc = new DetailsViewController(selectedQuery, viewController.getMainView().getUserID());
		this.viewController.getMainView().addWindow(dvc.getDetailsView());
	}
	
}
