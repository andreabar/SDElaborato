package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract  class Record {

	private String uniqueUrl;
	private String type;
	private int id;
	private String title;
	private String language;
	private List<String> webResources;
	private String year;
	private String rights;
	private String provider;
	private String dataProvider, dataProviderDescr;
	private String portalLink;
	
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public abstract void loadMoreInfo() throws Exception;
	public abstract String getShownAt() throws Exception;

	
	public Record(ResultSet set){
		
		try {
			setID(set.getInt("id"));
			setType(set.getString("type"));
			setLanguage(set.getString("language"));
			setTitle(set.getString("title"));
			setUniqueUrl(set.getString("url"));
			setRights(set.getString("ipr_type"));
			setProvider(set.getString("provider"));
			setDataProvider(set.getString("data_provider"));
			setDataProviderDescr(set.getString("data_provider_descr"));
			setPortalLink(set.getString("portal_link"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public Record() {
		
		id = -1;
	}

	public String getUniqueUrl() {
		return uniqueUrl;
	}
	public void setUniqueUrl(String id) {
		this.uniqueUrl = id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}

	public List<String> getWebResources() throws Exception {

		if(webResources == null)
				loadMoreInfo();
		return webResources;
		
	}

	public void setWebResources(List<String> list) {
		this.webResources = list;
	}

	public void setID(int i) {
		id = i;
	}
	
	public int getID(){
		return id;
	}


	public String getRights() {
		return rights;
	}
	public void setRights(String r) {

			rights = r;
	}
	public String getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(String dataProvider) {
		this.dataProvider = dataProvider;
	}
	public String getDataProviderDescr() {
		return dataProviderDescr;
	}
	public void setDataProviderDescr(String dataProviderDescr) {
		this.dataProviderDescr = dataProviderDescr;
	}
	public String getPortalLink() {
		return portalLink;
	}
	public void setPortalLink(String portalLink) {
		this.portalLink = portalLink;
	}
}
