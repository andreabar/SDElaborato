package model;

public class VimeoQuery extends Query {

	
	int rpp;
	int pages;
	
	public VimeoQuery(String input) {

		super(input);
		pages = 1;

	}
	
	public void setResultsPerPage(int i){
		rpp = i;
		
	}
	
	public void setPages(int i){
		pages = i;
		
	}
	
	public void setLimit(int i){
		
		super.setLimit(i);
		if(i <= 50){
			setPages(1);
			setResultsPerPage(i);
		}
		else{
			setPages((int) Math.ceil(i/50)); //TODO: per non multipli di 50
			setResultsPerPage(50);
		}
		
	}

	public int getRpp() {
		return rpp;
	}

	public int getPages() {
		return pages;
	}
	
}
