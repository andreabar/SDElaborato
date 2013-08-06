package view.controllers;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import model.Query;
import model.Record;
import util.AppData;
import util.FetcherFactory;
import views.LoginPage;
import views.MainView;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;

import controllers.QueryController;
import controllers.RecordController;
import dbutil.DBHelper;

import fetcher.EuropeanaFetcher;
import fetcher.JSONFetcher;
import fetcher.MultipleFetcher;
import fetcher.VimeoFetcher;

public class ViewController implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2186091527455914268L;
	private MainView mainView;
	
	public ViewController(MainView m){
		this.setMainView(m);
		this.mainView.getLogged().setValue("Logged as : " + DBHelper.getUserName(AppData.userID));
		this.mainView.getLogoutButton().addListener(new LogoutListener(this));
		this.mainView.getSearchButton().addListener(new SearchListener(this));
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
			searches = QueryController.getSearches(AppData.userID);
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
	private ViewController viewController;
	private FetcherFactory factory = FetcherFactory.getFetcherFactory();
	
	public SearchListener(ViewController viewController){
		this.viewController = viewController;
	}

	public void buttonClick(ClickEvent event) {
	
		this.viewController.getMainView().getSearchButton().setEnabled(false);
		
		if(viewController.getMainView().getTextfield().getValue().toString().isEmpty()){
			viewController.getMainView().getTextfield().setComponentError(new UserError("required"));
			viewController.getMainView().getSearchButton().setEnabled(true);
			return;
		}
	
		JSONFetcher fetcher = null;
		
		
		if(viewController.getSourceSelected().size() > 1){
			fetcher = new MultipleFetcher();
			for(Object o : viewController.getSourceSelected())
				((MultipleFetcher)fetcher).addFetcher(factory.getFetcher(o.toString()));
			
			
		}
		else 
			fetcher = factory.getFetcher(viewController.getSourceSelected().iterator().next().toString());
			
		try {
			
			Query query = fetcher.buildQuery(viewController);
			ArrayList<Record> list = fetcher.executeQuery(query);
			
			Query updated = QueryController.saveQuery(query, AppData.userID);

//			for(Record r : list)	//FIXME togliere QueryID dai record (altrimenti non recuperabili) e recuperare record di una query con join su keyword
//				r.setQueryID(query.getId()); //FIXME aggiungere 'keyword' a record // meglio una tabella di binding result(query, record) 
												//così se un record appartiene a query con keyword diversa è comunque recuperabile
			
			List<Record> records = RecordController.saveRecords(list);
			
			QueryController.addQueryResult(records, updated);
			
			
			this.viewController.getMainView().getSearchButton().setEnabled(true);
			Object rowItem[] = new Object[]{query.getKeyword(), query.getProvider(), query.getDataType(), query.getLanguage(), query.getResults()};
			viewController.getMainView().getSearchTable().addItem(rowItem, query);
			viewController.getMainView().getSearchTable().select(query);
			viewController.getMainView().getDetailsButton().click();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			this.viewController.getMainView().getParentView().getWindow().showNotification("No result found!");
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
	}
	
	@SuppressWarnings("unchecked")
	public void valueChange(ValueChangeEvent event) {
		
		Set<Object> value = ((Set<Object>)viewController.getMainView().getGroupSelector().getValue());
		
		if(value.contains("Europeana")){
			
			viewController.getMainView().getTypeSelect().setEnabled(true);
			viewController.getMainView().getLanguageSelect().setEnabled(true);
			viewController.getMainView().getIprSelector().setEnabled(true);
			
		}
		
		 else if(viewController.getMainView().getGroupSelector().getValue().equals("Vimeo")){

			 viewController.getMainView().getTypeSelect().setValue("VIDEO");
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
	
	private ViewController viewController;
	
	public DeleteListener(ViewController viewController){
		this.viewController = viewController;
	}

	public void buttonClick(ClickEvent event) {
		Query q = (Query) this.viewController.getMainView().getSearchTable().getValue();
		try {
			QueryController.deleteSearch(q);
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

	private ViewController viewController;
	
	public TableClickListener(ViewController v){
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
	private ViewController viewController;
	
	public DetailsListener(ViewController v){
		this.viewController = v;
	}

	public void buttonClick(ClickEvent event) {
		Query selectedQuery = (Query)this.viewController.getMainView().getSearchTable().getValue();
		DetailsViewController dvc = new DetailsViewController(selectedQuery);
		this.viewController.getMainView().getParentView().addWindow(dvc.getDetailsView());
	}
	
}

class LogoutListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5508313346827627646L;
	private ViewController viewController;
	
	public LogoutListener(ViewController c){
		this.viewController = c;
	}
	
	public void buttonClick(ClickEvent event) {
		AppData.userID = -1;
		LoginController lc = new LoginController(new LoginPage());
		this.viewController.getMainView().getApplication().setMainWindow(lc.getLoginPage());
		this.viewController.getMainView().getApplication().removeWindow
		(this.viewController.getMainView().getParentView());
		
	}
	
}
