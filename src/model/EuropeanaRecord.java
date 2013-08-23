package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import util.AppData;

import controllers.RecordController;

public class EuropeanaRecord extends Record {

	public EuropeanaRecord() {
		setProvider(AppData.EUROPEANA);
	
	}
	
	public EuropeanaRecord(ResultSet set) {

		super(set);
	}
	
	@Override
	public void loadMoreInfo() throws Exception { 

		try {
			setWebResources(RecordController.getWebResources(this));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public String getShownAt() throws Exception {

		if(getWebResources().isEmpty())
			loadMoreInfo();
		
		return getWebResources().get(0);
	}

	
	
	
}
