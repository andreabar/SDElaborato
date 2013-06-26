package model;

import java.util.ArrayList;

import controllers.RecordController;

public class Record {
	
	private String europeanaID;
	private String type;
	private int id;
	
	private String title;
	private String language;
	private ArrayList<String> webResources;

	
	
	private Integer completeness;
	private ArrayList<String> dataProviders;
	
	private ArrayList<String> europeanaCollectionName;
	private String guid;
	private String link;
	private ArrayList<String> providers;
	private ArrayList<String> rights;
	private ArrayList<String> dcCreator;
	private String edmConceptLabel;
	private String edmPreview;
	private String edmTimespanLabel;
	private Integer europeanaCompleteness;
	private String year;
	
	public Record(){
		
	}
	
//	public String getShownLink() {
//
//		if(shownLink == null)
//			try {
//				shownLink = new EuropeanaFetcher().getRecordLink(this);
//				return shownLink;
//
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		
//		return shownLink;
//	}


	public Integer getCompleteness() {
		return completeness;
	}
	public void setCompleteness(Integer completeness) {
		this.completeness = completeness;
	}
	public ArrayList<String> getDataProvider() {
		return dataProviders;
	}
	public void setDataProvider(ArrayList<String> dataProvider) {
		this.dataProviders = dataProvider;
	}
	public ArrayList<String> getEuropeanaCollectionName() {
		return europeanaCollectionName;
	}
	public void setEuropeanaCollectionName(ArrayList<String> europeanaCollectionName) {
		this.europeanaCollectionName = europeanaCollectionName;
	}
	public String getEuropeanaId() {
		return europeanaID;
	}
	public void setId(String id) {
		this.europeanaID = id;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getJSONLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	public ArrayList<String> getProvider() {
		return providers;
	}
	public void setProvider(ArrayList<String> provider) {
		this.providers = provider;
	}
	public ArrayList<String> getRights() {
		return rights;
	}
	public void setRights(ArrayList<String> rights) {
		this.rights = rights;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<String> getDcCreator() {
		return dcCreator;
	}
	public void setDcCreator(ArrayList<String> dcCreator) {
		this.dcCreator = dcCreator;
	}
	public String getEdmConceptLabel() {
		return edmConceptLabel;
	}
	public void setEdmConceptLabel(String edmConceptLabel) {
		this.edmConceptLabel = edmConceptLabel;
	}
	public String getEdmPreview() {
		return edmPreview;
	}
	public void setEdmPreview(String edmPreview) {
		this.edmPreview = edmPreview;
	}
	public String getEdmTimespanLabel() {
		return edmTimespanLabel;
	}
	public void setEdmTimespanLabel(String edmTimespanLabel) {
		this.edmTimespanLabel = edmTimespanLabel;
	}
	public Integer getEuropeanaCompleteness() {
		return europeanaCompleteness;
	}
	public void setEuropeanaCompleteness(Integer europeanaCompleteness) {
		this.europeanaCompleteness = europeanaCompleteness;
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

	public ArrayList<String> getWebResources() {

		if(webResources == null)
			webResources = RecordController.getWebResources(this);
		return webResources;
		
	}

	public void setWebResources(ArrayList<String> webResources) {
		this.webResources = webResources;
	}

	public void setID(int i) {
		id = i;
	}
	
	public int getID(){
		return id;
	}
}
