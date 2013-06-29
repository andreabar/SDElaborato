package model;


public class EuropenaQuery extends Query {

	private String freeAccessIPR = "http://www.europeana.eu/rights/rr-f/";
	private String rightReservedIPR = "http://www.europeana.eu/rights/rr-r/";

	public EuropenaQuery(String input) {
		super(input);

	}

	public void setPublicIpr(boolean freeAccess) {

		if (freeAccess)
			setIprType(freeAccessIPR);
		else
			setIprType(rightReservedIPR);

	}

}