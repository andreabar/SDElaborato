package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import controllers.RecordController;
import fetcher.EuropeanaFetcher;

public class EuropeanaRecord extends Record {

	public EuropeanaRecord() {
		setProvider(EuropeanaFetcher.PROVIDER);
	
	}
	
	public EuropeanaRecord(ResultSet set) {

		super(set);
	}
	
	@Override
	public void loadMoreInfo() throws Exception { //FIXME: moved to BackgroundApp, so just save the .json URL as resource

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
