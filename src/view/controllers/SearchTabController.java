package view.controllers;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import model.Query;
import model.Record;
import util.AppData;
import util.FetcherFactory;
import view.views.LoginPage;
import view.views.SearchTab;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;

import controllers.QueryController;
import controllers.RecordController;
import dbutil.DBHelper;

import fetcher.JSONFetcher;
import fetcher.JSONFetcher.ConnectionErrorException;
import fetcher.JSONFetcher.NoResultException;
import fetcher.MultipleFetcher;

public class SearchTabController implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2186091527455914268L;
	private SearchTab mainView;
	
	public SearchTabController(SearchTab m){
		this.setMainView(m);
		this.mainView.getSearchButton().addListener(new SearchListener(this));
		this.mainView.getSearchButton().setClickShortcut(KeyCode.ENTER);
		this.mainView.getGroupSelector().addListener(new GroupSelectorListener(this));
		this.mainView.getSearchTable().addListener(new TableClickListener(this));
		this.mainView.getDetailsButton().addListener(new DetailsListener(this));
		this.mainView.getDeleteButton().addListener(new DeleteListener(this));
		loadSearchTable();
	}
	
	protected void loadSearchTable(){
		this.mainView.getSearchTable().removeAllItems();
		ArrayList<Query> searches = new ArrayList<Query>();
		try {
			searches = QueryController.getQueries(AppData.userID);
			if(!searches.isEmpty()){
				for(Query q : searches){
					Object rowItem[] = new Object[]{q.getKeyword(), q.getProvider(), q.getDataType(), q.getLanguage(), q.getLimit()};
					this.mainView.getSearchTable().addItem(rowItem, q);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

	
	public void setMainView(SearchTab mainView) {
		this.mainView = mainView;
	}
	

	public SearchTab getMainView() {
		return mainView;
	}


	@SuppressWarnings("unchecked")
	public Set<Object> getSourceSelected() {
		return ((Set<Object>)getMainView().getGroupSelector().getValue());
	}


	
	
	

}

class SearchListener implements Button.ClickListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8226238264247152044L;
	private SearchTabController viewController;
	private FetcherFactory factory = FetcherFactory.getFetcherFactory();
	
	public SearchListener(SearchTabController viewController){
		this.viewController = viewController;
	}

	public void buttonClick(ClickEvent event) {
			
		if(viewController.getMainView().getTextfield().getValue().toString().isEmpty()){
			viewController.getMainView().getTextfield().setComponentError(new UserError("required"));
			viewController.getMainView().getSearchButton().setEnabled(true);
			return;
		}
	
		JSONFetcher fetcher = initalizeFetcher();
		Query query = fetcher.buildQuery(viewController);

		try {
			
			ArrayList<Record> list = fetcher.executeQuery(query);
			query.setLimit(list.size());
			Query updated = QueryController.saveQuery(query, AppData.userID);
			List<Record> records = RecordController.saveRecords(list);
			QueryController.addQueryResult(records, updated);

			DBHelper.getConnection().commit();

			
			updateQueryTable(query);
			viewController.getMainView().getDetailsButton().click();

			
		} catch (NoResultException e) {
			this.viewController.getMainView().getParentView().getWindow().showNotification(e.getMessage());
			query.setLimit(0);
			QueryController.saveQuery(query, AppData.userID);
			updateQueryTable(query);
			
			
		} catch (Exception e) {
			this.viewController.getMainView().getParentView().getWindow().showNotification(new ConnectionErrorException().getMessage());
		}
		
		
		this.viewController.getMainView().getSearchButton().setEnabled(true);

	}

	private void updateQueryTable(Query query) {
		this.viewController.getMainView().getSearchButton().setEnabled(true);
		Object rowItem[] = new Object[]{query.getKeyword(), query.getProvider(), query.getDataType(), query.getLanguage(), query.getLimit()};
		viewController.getMainView().getSearchTable().addItem(rowItem, query);
		viewController.getMainView().getSearchTable().select(query);
	}

	private JSONFetcher initalizeFetcher() {
		JSONFetcher fetcher = null;
		
		
		if(viewController.getSourceSelected().size() > 1){
			fetcher = new MultipleFetcher();

			TreeSet<Object> ordered = new TreeSet<>(viewController.getSourceSelected());
	
			for(Object o : ordered)
				((MultipleFetcher)fetcher).addFetcher(factory.getFetcher(o.toString()));
			
			
		}
		else 
			fetcher = factory.getFetcher(viewController.getSourceSelected().iterator().next().toString());
		return fetcher;
	}
	
	
}

class GroupSelectorListener implements ValueChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2988676404511145837L;

	private SearchTabController viewController;
	
	public GroupSelectorListener(SearchTabController viewController){
		this.viewController = viewController;
	}
	
	@SuppressWarnings("unchecked")
	public void valueChange(ValueChangeEvent event) {
		
		Set<Object> value = ((Set<Object>)viewController.getMainView().getGroupSelector().getValue());
		
		if(value.contains(AppData.EUROPEANA)){
			
			viewController.getMainView().getTypeSelect().setEnabled(true);
			viewController.getMainView().getLanguageSelect().setEnabled(true);
			viewController.getMainView().getIprSelector().setEnabled(true);
			
		}
		
		 else if(viewController.getMainView().getGroupSelector().getValue().equals(AppData.VIMEO)){

			 viewController.getMainView().getTypeSelect().setValue(AppData.VIDEO);
			viewController.getMainView().getTypeSelect().setEnabled(false);
			viewController.getMainView().getLanguageSelect().setEnabled(false);
			viewController.getMainView().getIprSelector().setEnabled(false);
		 }	
		viewController.getMainView().getSearchButton().setEnabled(true);
	}
	
}

class DeleteListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3956959737914558552L;
	
	private SearchTabController viewController;
	
	public DeleteListener(SearchTabController viewController){
		this.viewController = viewController;
	}

	public void buttonClick(ClickEvent event) {
		Query q = (Query) this.viewController.getMainView().getSearchTable().getValue();
		try {
			QueryController.deleteSearch(q);
			DBHelper.getConnection().commit();
			this.viewController.loadSearchTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}

class TableClickListener implements ValueChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2459096720898773538L;

	private SearchTabController viewController;
	
	public TableClickListener(SearchTabController v){
		this.viewController = v;
	}
	
	public void valueChange(ValueChangeEvent event) {
		if(null != this.viewController.getMainView().getSearchTable().getValue()){
			this.viewController.getMainView().getDetailsButton().setEnabled(true);
			this.viewController.getMainView().getDeleteButton().setEnabled(true);
		} else {
			this.viewController.getMainView().getDetailsButton().setEnabled(false);
			this.viewController.getMainView().getDeleteButton().setEnabled(false);
		}
	}
	
}

class DetailsListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -445081953325867218L;
	private SearchTabController viewController;
	
	public DetailsListener(SearchTabController v){
		this.viewController = v;
	}

	public void buttonClick(ClickEvent event) {
		Query selectedQuery = (Query)this.viewController.getMainView().getSearchTable().getValue();
		DetailsViewController dvc = new DetailsViewController(selectedQuery);
		this.viewController.getMainView().getApplication().getMainWindow().addWindow(dvc.getDetailsView());
	}
	
}


