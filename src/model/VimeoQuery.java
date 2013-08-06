package model;

public class VimeoQuery extends Query {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4028550003367033935L;
	int pages;
	public static final int MAXIMUM_RPP = 50;

	
	public VimeoQuery(String input) {

		super(input);
		setDataType("VIDEO");
		setLanguage("unknown");
		pages = 1;

	}
	
	
	public void setPages(int i){
		pages = i;
		
	}
	
	public void setLimit(int i){
		
		super.setLimit(i);
		
		if(i <= 50){
			setPages(1);
		}
		else{
			setPages((int) Math.ceil((double)i/(double)50));
			System.out.println("PAGES: " + pages);//TODO: per non multipli di 50
		}
		
	}


	public int getPages() {
		return pages;
	}
	
}
