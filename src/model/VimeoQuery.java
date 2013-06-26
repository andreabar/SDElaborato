package model;

public class VimeoQuery extends Query {

	
	int rpp;
	int pages;
	
	public VimeoQuery(String input) {

		super(input);
		
	}
	
	public void setResultsPerPage(int i){
		rpp = i;
		
	}
	
	public void setPages(int i){
		pages = i;
		
	}
	
	public void setLimit(int i){
		
		if(i <= 50){
			setPages(1);
			setResultsPerPage(i);
		}
		else
			setPages(i/50);
		
	}
	
}
