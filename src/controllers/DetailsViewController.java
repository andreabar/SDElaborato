package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import views.DetailsView;

public class DetailsViewController {

	private DetailsView detailsView;
	private Date dateQuery;
	private String keywordQuery;
	
	public DetailsViewController(Date d, String k){
		this.setDetailsView(new DetailsView());
		this.setDateQuery(d);
		this.setKeywordQuery(k);
		this.detailsView.getRecordsTable().setCaption("Query: " + getDateQuery().toString() +
				" on " + getKeywordQuery());
		try {
			loadDetailsTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadDetailsTable() throws SQLException{
		ResultSet result = DBHelper.getDetails(getDateQuery(), getKeywordQuery());
		int i = 0;
		while(result.next()){
			i++;
			String title = result.getString("title");
			String type = result.getString("type");
			String language = result.getString("language");
			String url = result.getString("url");

			Object rowItem[] = new Object[]{title, language, type, url};
			this.getDetailsView().getRecordsTable().addItem(rowItem, i);
		}
	}

	public DetailsView getDetailsView() {
		return detailsView;
	}

	public void setDetailsView(DetailsView detailsView) {
		this.detailsView = detailsView;
	}

	public Date getDateQuery() {
		return dateQuery;
	}

	public void setDateQuery(Date dateQuery) {
		this.dateQuery = dateQuery;
	}

	public String getKeywordQuery() {
		return keywordQuery;
	}

	public void setKeywordQuery(String keywordQuery) {
		this.keywordQuery = keywordQuery;
	}
	

}
