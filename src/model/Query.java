package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import util.AppData;

import dbutil.IprType;

public class Query implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1122930428045851922L;


	private String keyword;
	private ArrayList<String> iprType;
	private String dataType;
	private String provider;
	private int id;
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Query(int id, String provider, String dataType, String keyword,
			String language, int results, String date) {
		super();
		this.id = id;
		this.provider = provider;
		this.dataType = dataType;
		this.keyword = keyword;
		this.language = language;
		this.results = results;
		this.date = date;
		
	}
	
	public Query(String q) {

		keyword = q;
		results = -1;

	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public void setInput(String input) {
		this.keyword = input;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	private String language;
	private int results;

	
	public void setIprType(Set<Object> types) {

		iprType = new ArrayList<String>();
		if(types.contains(AppData.ANY_TYPE))
			return;
		
		for (Object o : types)
			iprType.add(IprType.getIprUrl(o.toString()));
		

	}

	public int getLimit() {
		return results;
	}

	public String getLanguage() {
		return language;
	}

	public ArrayList<String> getIprType() {
		return iprType;
	}

	public String getInput() {
		return keyword;
	}

	public void setLimit(int i) {
		results = i;
	}

	public String getDataType() {
		return dataType;
	}

	public boolean hasLanguageFilter() {
		return language != null && language != AppData.ANY_TYPE;
	}

	public boolean hasDataFilter() {
		return dataType != null && dataType != AppData.ANY_TYPE;
	}

}
