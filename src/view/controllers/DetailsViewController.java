package view.controllers;

import java.sql.SQLException;

import controllers.QueryController;

import model.Query;
import model.Record;

import views.DetailsView;

public class DetailsViewController {

	private DetailsView detailsView;
	private Query query;
	
	public DetailsViewController(Query q, int userID){
		this.setDetailsView(new DetailsView(userID));
		query = q;
		
		this.detailsView.getRecordsTable().setCaption("Query: "  +
				" on " + query.getKeyword());
		try {
			loadDetailsTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadDetailsTable() throws SQLException{

		for(Record r : QueryController.getRecords(query)){
			
			Object rowItem[] = new Object[]{r.getTitle(), r.getLanguage(), r.getType(), r.getRights(), new com.vaadin.ui.CheckBox()};
			this.getDetailsView().getRecordsTable().addItem(rowItem, r);
		}
	}

	public DetailsView getDetailsView() {
		return detailsView;
	}

	public void setDetailsView(DetailsView detailsView) {
		this.detailsView = detailsView;
	}

	
}
