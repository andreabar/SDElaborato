package controllers;

import java.sql.SQLException;

import com.google.gwt.user.client.ui.CheckBox;

import model.Query;
import model.Record;

import views.DetailsView;

public class DetailsViewController {

	private DetailsView detailsView;
	private Query query;
	
	public DetailsViewController(Query q){
		this.setDetailsView(new DetailsView());
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
