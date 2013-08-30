package view.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import controllers.QueryController;
import controllers.TaskController;

import model.Query;
import model.Record;

import util.AppData;
import view.views.DetailsView;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class DetailsViewController {

	private DetailsView detailsView;
	private Query query;
	
	public DetailsViewController(Query q){
		this.setDetailsView(new DetailsView());
		query = q;
		
		this.detailsView.getRecordsTable().setCaption("Query: "  +
				" on " + query.getKeyword() + ". Total results: " + query.getLimit());
		try {
			loadDetailsTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.detailsView.getKeepAll().addListener(new KeepListener(this));
		this.detailsView.getSeeOnline().addListener(new SeeOnlineListener(this));
		this.detailsView.getVerify().addListener(new VerifyListener(this));
	}
	
	public void loadDetailsTable() throws SQLException{

		for(Record r : QueryController.getRecords(query)){
			CheckBox check = new CheckBox(null, false);
			check.setImmediate(true);
			Object rowItem[] = new Object[]{r.getTitle(), r.getProvider(), r.getLanguage(), r.getType(), r.getRights(), check};
			this.getDetailsView().getRecordsTable().addItem(rowItem, r);
		}
	}

	public DetailsView getDetailsView() {
		return detailsView;
	}

	public void setDetailsView(DetailsView detailsView) {
		this.detailsView = detailsView;
	}

	public Query getQuery() {
		return query;
	}

	
}

class SeeOnlineListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6980888234309707450L;
	
	private DetailsViewController dvc;
	
	public SeeOnlineListener(DetailsViewController dvc){
		this.dvc = dvc;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Record selected = ((Record)dvc.getDetailsView().getRecordsTable().getValue());
		try {
	
			dvc.getDetailsView().open(new ExternalResource(selected.getShownAt()), "_blank");
		} catch (Exception e) {
			dvc.getDetailsView().getApplication().getMainWindow().showNotification("Select just one row", Window.Notification.TYPE_ERROR_MESSAGE);
		}
				
	}
	
}


class VerifyListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4860121522603504620L;
	
	private DetailsViewController dvc;
	
	public VerifyListener(DetailsViewController dvc){
		this.dvc = dvc;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		ArrayList<Record> toKeep = new ArrayList<Record>();
		for(Object row : dvc.getDetailsView().getRecordsTable().getItemIds()){
			
			if(((CheckBox)dvc.getDetailsView().getRecordsTable().getItem(row).getItemProperty("Keep").getValue()).booleanValue())
				toKeep.add((Record)row);
			
		}
			
		if(toKeep.isEmpty())
			return;
		
		try {
			
			TaskController.addTasks(toKeep, AppData.userID, dvc.getQuery().getId());
			dvc.getDetailsView().getApplication().getMainWindow().showNotification
			("Your request is being processed", Window.Notification.TYPE_HUMANIZED_MESSAGE);
			
			dvc.getDetailsView().getApplication().getMainWindow().removeWindow(dvc.getDetailsView());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			dvc.getDetailsView().getApplication().getMainWindow().showNotification("Server Error", Window.Notification.TYPE_ERROR_MESSAGE);
			return;
		}
		
	}
	
}

class KeepListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7829188733290672147L;
	
	private DetailsViewController dvc;
	
	public KeepListener(DetailsViewController dvc){
		this.dvc = dvc;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		@SuppressWarnings("unchecked")
		Collection<Record> rowIds =  (Collection<Record>) dvc.getDetailsView().getRecordsTable().getItemIds();
		
		if(dvc.getDetailsView().getKeepAll().getCaption().equals("Keep All")){
			for(Record r : rowIds){
				CheckBox check = (CheckBox) dvc.getDetailsView().getRecordsTable().getItem(r).getItemProperty("Keep").getValue();
				check.setValue(true);
			}
			dvc.getDetailsView().getKeepAll().setCaption("Discard All Keeping");
		} else {
			for(Record r : rowIds){
				CheckBox check = (CheckBox) dvc.getDetailsView().getRecordsTable().getItem(r).getItemProperty("Keep").getValue();
				check.setValue(false);
			}
			dvc.getDetailsView().getKeepAll().setCaption("Keep All");
		}
	}
	
}
