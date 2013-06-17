package model;

public class Query {

	private String input;
	private String iprType;
	private String dataType;
	public void setInput(String input) {
		this.input = input;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	private String language;
	private int limit;
	
	public Query(String q) {
	
		input = q;
		limit = -1;
		
	}
	
	public void setIprType(String type){
		
		iprType = type;
		
	}

	public int getLimit() {
		return limit;
	}

	public String getLanguage() {
		return language;
	}

	public String getIprType() {
		return iprType;
	}

	public String getInput() {
		return input;
	}

	public void setLimit(int i) {
		limit = i;
	}

	public String getDataType() {
		return dataType;
	}
	
	
}
