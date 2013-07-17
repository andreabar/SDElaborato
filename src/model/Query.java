package model;

public class Query {

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

//	public Date getDate() {
//		return date;
//	}
//
//	public void setDate(Date date) {
//		this.date = date;
//	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getResults() {
		return results;
	}

	public void setResults(int results) {
		this.results = results;
	}

	private String keyword;
	private String iprType;
	private String dataType;
	private String provider;
//	private Date date;
	private int id;
	
	public Query(int id, String provider, String dataType, String keyword,
			String language, int results) {
		super();
		this.id = id;
		this.provider = provider;
		this.dataType = dataType;
		this.keyword = keyword;
		this.language = language;
//		this.date = date;
		this.results = results;
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
	
	public Query(String q) {
	
		keyword = q;
		results = -1;
		
	}
	
	public void setIprType(String type){
		
		iprType = type;
		
	}

	public int getLimit() {
		return results;
	}

	public String getLanguage() {
		return language;
	}

	public String getIprType() {
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
	
	
}
